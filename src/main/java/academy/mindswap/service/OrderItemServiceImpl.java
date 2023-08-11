package academy.mindswap.service;

import academy.mindswap.converter.OrderConverter;
import academy.mindswap.converter.OrderItemConverter;
import academy.mindswap.dto.OrderDto;
import academy.mindswap.dto.OrderItemDto;
import academy.mindswap.dto.OrderItemUpdateDto;
import academy.mindswap.interceptor.VerifyUserAndOrder;
import academy.mindswap.model.Item;
import academy.mindswap.model.Order;
import academy.mindswap.model.OrderItem;
import academy.mindswap.repository.ItemRepository;
import academy.mindswap.repository.OrderItemRepository;
import academy.mindswap.repository.OrderRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.WebApplicationException;

import java.util.List;

@ApplicationScoped
public class OrderItemServiceImpl implements OrderItemService {
    @Inject
    OrderItemRepository orderItemRepository;
    @Inject
    OrderRepository orderRepository;
    @Inject
    ItemRepository itemRepository;
    @Inject
    OrderConverter orderConverter;
    @Inject
    OrderItemConverter orderItemConverter;

    @Override
    @VerifyUserAndOrder
    public List<OrderItemDto> getListOfOrderItem(Long userId, Long orderId) {
        Order order = orderRepository.findById(orderId);
        if (order == null || !order.getUser().getId().equals(userId)) {
            throw new WebApplicationException("Order not found", 400);
        }

        return order.getOrderItems().stream().map(orderItem -> orderItemConverter.toDto(orderItem)).toList();
    }

    @Override
    @VerifyUserAndOrder
    public OrderDto addItemToOrder(Long userId, Long orderId, OrderItemDto orderItemAddDto) {
        Order order = orderRepository.findById(orderId);
        Item item = itemRepository.findById(orderItemAddDto.getItem().getId());
        if (item == null) {
            throw new WebApplicationException("Item not found", 400);
        }

        OrderItem orderItem = orderItemRepository.findByOrderIdAndItemId(orderId, item.getId());
        int currentQuantity = 0;
        if (orderItem != null) {
            currentQuantity = orderItem.getQuantity();
        } else {
            orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setItem(item);
        }
        order.setTotal(recalculateTotal(order, item, currentQuantity, orderItemAddDto.getQuantity()));
        orderItem.setQuantity(orderItemAddDto.getQuantity());

        orderRepository.persist(order);
        orderItemRepository.persist(orderItem);


        List<OrderItem> orderItems = order.getOrderItems();
        orderItems.add(orderItem);
        order.setOrderItems(orderItems);

        return orderConverter.toDto(order);
    }

    @Override
    @VerifyUserAndOrder
    public OrderDto updateItemOnOrder(Long userId, Long orderId, Long itemId, OrderItemUpdateDto orderItemUpdateDto) {
        Order order = orderRepository.findById(orderId);
        if (order == null) {
            throw new WebApplicationException("Order not found", 400);
        }
        Item item = itemRepository.findById(itemId);
        if (item == null) {
            throw new WebApplicationException("Item not found", 400);
        }
        OrderItem orderItem = orderItemRepository.findByOrderIdAndItemId(orderId, itemId);
        if (orderItem == null) {
            throw new WebApplicationException("Item not found", 400);
        }

        order.setTotal(
                recalculateTotal(
                        order,
                        item,
                        orderItem.getQuantity(),
                        orderItemUpdateDto.getQuantity()
                ));
        orderItem.setQuantity(orderItemUpdateDto.getQuantity());
        orderItemRepository.persist(orderItem);
        orderRepository.persist(order);

        return orderConverter.toDto(order);
    }

    @Override
    @VerifyUserAndOrder
    @Transactional
    public void removeItemFromOrder(Long userId, Long orderId, Long itemId) {
        Order order = orderRepository.findById(orderId);
        if (order == null) {
            throw new WebApplicationException("Order not found", 400);
        }
        Item item = itemRepository.findById(itemId);
        if (item == null) {
            throw new WebApplicationException("Item not found", 400);
        }
        OrderItem orderItem = orderItemRepository.findByOrderIdAndItemId(orderId, itemId);
        if (orderItem == null) {
            throw new WebApplicationException("Item on order not found", 400);
        }

        orderItemRepository.delete(orderItem);
    }

    protected double recalculateTotal(Order order, Item item, int previousQuantity, int newQuantity) {
        double itemPrice = item.getPrice();
        double totalToDecrement = itemPrice * previousQuantity;
        double totalToIncrement = itemPrice * newQuantity;

        return order.getTotal() - totalToDecrement + totalToIncrement;
    }
}

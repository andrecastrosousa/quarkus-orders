package academy.mindswap.service;

import academy.mindswap.converter.OrderConverter;
import academy.mindswap.converter.OrderItemConverter;
import academy.mindswap.dto.OrderDto;
import academy.mindswap.dto.OrderItemAddDto;
import academy.mindswap.dto.OrderItemDto;
import academy.mindswap.dto.OrderItemUpdateDto;
import academy.mindswap.model.Item;
import academy.mindswap.model.Order;
import academy.mindswap.model.OrderItem;
import academy.mindswap.model.User;
import academy.mindswap.repository.ItemRepository;
import academy.mindswap.repository.OrderItemRepository;
import academy.mindswap.repository.OrderRepository;
import academy.mindswap.repository.UserRepository;
import io.quarkus.panache.common.Parameters;
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
    UserRepository userRepository;

    @Inject
    OrderConverter orderConverter;

    @Inject
    OrderItemConverter orderItemConverter;

    @Override
    public List<OrderItemDto> getListOfOrderItem(Long userId, Long orderId) {
        User user = userRepository.findById(userId);

        if(user == null) {
            throw new WebApplicationException("User not found", 400);
        }

        Order order = orderRepository.findById(orderId);
        if(order == null || !order.getUser().getId().equals(userId)) {
            throw new WebApplicationException("Order not found", 400);
        }

        return order.getOrderItems().stream().map(orderItem -> orderItemConverter.toDto(orderItem)).toList();
    }

    @Override
    public OrderDto addItemToOrder(Long userId, Long orderId, OrderItemAddDto orderItemAddDto) {
        User user = userRepository.findById(userId);

        if(user == null) {
            throw new WebApplicationException("User not found", 400);
        }

        Order order = orderRepository.findById(orderId);
        if(order == null || !order.getUser().getId().equals(userId)) {
            throw new WebApplicationException("Order not found", 400);
        }

        Item item = itemRepository.findById(orderItemAddDto.getItem().getId());
        if(item == null) {
            throw new WebApplicationException("Item not found", 400);
        }

        OrderItem orderItem = new OrderItem();
        orderItem.setOrder(order);
        orderItem.setItem(item);
        orderItem.setQuantity(orderItemAddDto.getQuantity());

        order.setTotal(order.getTotal() + (item.getPrice() * orderItemAddDto.getQuantity()));

        orderItemRepository.persist(orderItem);
        orderRepository.persist(order);

        List<OrderItem> orderItems = order.getOrderItems();
        orderItems.add(orderItem);
        order.setOrderItems(orderItems);

        return orderConverter.toDto(order);
    }

    @Override
    public OrderDto updateItemOnOrder(Long userId, Long orderId, Long itemId, OrderItemUpdateDto orderItemUpdateDto) {
        User user = userRepository.findById(userId);

        if(user == null) {
            throw new WebApplicationException("User not found", 400);
        }

        Order order = orderRepository.findById(orderId);
        if(order == null || !order.getUser().getId().equals(userId)) {
            throw new WebApplicationException("Order not found", 400);
        }
        Item item = itemRepository.findById(itemId);
        if(item == null) {
            throw new WebApplicationException("Item not found", 400);
        }
        OrderItem orderItem = orderItemRepository.findByOrderIdAndItemId(orderId, itemId);
        if(orderItem == null) {
            throw new WebApplicationException("Item not found", 400);
        }

        double itemPrice = orderItem.getItem().getPrice();
        double totalToDecrement = itemPrice * orderItem.getQuantity();

        orderItem.setQuantity(orderItemUpdateDto.getQuantity());
        order.setTotal(order.getTotal() - totalToDecrement + (itemPrice * orderItem.getQuantity()));
        orderItemRepository.persist(orderItem);
        orderRepository.persist(order);

        return orderConverter.toDto(order);
    }

    @Override
    @Transactional
    public void removeItemFromOrder(Long userId, Long orderId, Long itemId) {
        User user = userRepository.findById(userId);

        if(user == null) {
            throw new WebApplicationException("User not found", 400);
        }

        Order order = orderRepository.findById(orderId);
        if(order == null || !order.getUser().getId().equals(userId)) {
            throw new WebApplicationException("Order not found", 400);
        }
        Item item = itemRepository.findById(itemId);
        if(item == null) {
            throw new WebApplicationException("Item not found", 400);
        }
        OrderItem orderItem = orderItemRepository.findByOrderIdAndItemId(orderId, itemId);
        if(orderItem == null) {
            throw new WebApplicationException("Item on order not found", 400);
        }

        orderItemRepository.delete(orderItem);
    }
}

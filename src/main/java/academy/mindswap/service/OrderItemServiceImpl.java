package academy.mindswap.service;

import academy.mindswap.dto.OrderItemAddDto;
import academy.mindswap.model.Item;
import academy.mindswap.model.Order;
import academy.mindswap.model.OrderItem;
import academy.mindswap.repository.ItemRepository;
import academy.mindswap.repository.OrderItemRepository;
import academy.mindswap.repository.OrderRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
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

    @Override
    public List<OrderItem> getListOfOrderItem(Long userId, Long orderId) {
        Order order = orderRepository.findById(orderId);
        if(order == null) {
            throw new WebApplicationException("Order not found", 400);
        }

        return order.getOrderItems();
    }

    @Override
    public Order addItemToOrder(Long userId, Long orderId, OrderItemAddDto orderItemAddDto) {
        Order order = orderRepository.findById(orderId);
        if(order == null) {
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

        orderItemRepository.persist(orderItem);

        List<OrderItem> orderItems = order.getOrderItems();
        orderItems.add(orderItem);
        order.setOrderItems(orderItems);

        return order;
    }
}

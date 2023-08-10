package academy.mindswap.service;

import academy.mindswap.model.Order;

import java.util.List;

public interface OrderService {

    List<Order> listAll(Long userId);
    Order create(Long userId, Order order);
    Order update(Long userId, Long orderId, Order order);
    void delete(Long userId, Long orderId);
}
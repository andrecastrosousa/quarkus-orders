package academy.mindswap.service;

import academy.mindswap.dto.OrderCreateDto;
import academy.mindswap.dto.OrderDto;

import java.util.List;

public interface OrderService {

    List<OrderDto> listAll(Long userId);

    OrderDto findById(Long userId, Long orderId);

    OrderDto create(Long userId, OrderCreateDto order);

    void delete(Long userId, Long orderId);
    // OrderDto update(Long userId, Long orderId, Order order);
}
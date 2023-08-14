package academy.mindswap.service;

import academy.mindswap.dto.OrderCreateDto;
import academy.mindswap.dto.OrderDto;

import java.util.List;

public interface OrderService {

    List<OrderDto> listAll(String email);

    OrderDto findById(String email, Long orderId);

    OrderDto create(String email, OrderCreateDto order);

    void delete(String email, Long orderId);
}
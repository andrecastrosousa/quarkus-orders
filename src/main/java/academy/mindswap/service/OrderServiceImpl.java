package academy.mindswap.service;

import academy.mindswap.converter.OrderConverter;
import academy.mindswap.dto.OrderCreateDto;
import academy.mindswap.dto.OrderDto;
import academy.mindswap.model.Order;
import academy.mindswap.model.User;
import academy.mindswap.repository.OrderRepository;
import academy.mindswap.repository.UserRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;

import java.util.List;

@ApplicationScoped
public class OrderServiceImpl implements OrderService {
    @Inject
    OrderRepository orderRepository;

    @Inject
    UserRepository userRepository;

    @Inject
    OrderConverter orderConverter;

    @Override
    public List<OrderDto> listAll(Long userId) {
        User user = userRepository.findById(userId);
        if (user == null) {
            throw new WebApplicationException("User not found", 404);
        }
        return orderRepository.findByUserId(userId).stream()
                .map(order -> orderConverter.toDto(order))
                .toList();
    }

    @Override
    public OrderDto findById(Long userId, Long orderId) {
        Order orderFound = orderRepository.findById(orderId);
        if (orderFound == null || !orderFound.getUser().getId().equals(userId)) {
            throw new WebApplicationException("Order not found", 404);
        }

        return orderConverter.toDto(orderFound);
    }

    @Override
    public OrderDto create(Long userId, OrderCreateDto orderCreateDto) {
        User user = userRepository.findById(userId);
        if (user == null) {
            throw new WebApplicationException("User not found", 404);
        }

        Order order = orderConverter.toEntityFromCreateDto(orderCreateDto);
        order.setUser(user);
        orderRepository.persist(order);

        return orderConverter.toDto(order);
    }

    @Override
    public void delete(Long userId, Long orderId) {
        Order orderFound = orderRepository.findById(orderId);

        if (orderFound == null) {
            throw new WebApplicationException("Order not found", 404);
        }
        if (!orderFound.getUser().getId().equals(userId)) {
            throw new WebApplicationException("Order not found", 404);
        }

        orderRepository.delete(orderFound);
    }

    /*@Override
    public OrderDto update(Long userId, Long orderId, Order order) {
        Order orderFound = orderRepository.findById(orderId);

        if(orderFound == null || !order.getId().equals(orderId) || !orderFound.getUser().getId().equals(userId)) {
            throw new WebApplicationException("Order not found", 404);
        }

        orderFound.setTotal(order.getTotal());
        orderRepository.persist(orderFound);

        return orderConverter.toDto(orderFound);
    }*/

}

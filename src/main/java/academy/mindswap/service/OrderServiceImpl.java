package academy.mindswap.service;

import academy.mindswap.converter.OrderConverter;
import academy.mindswap.dto.OrderCreateDto;
import academy.mindswap.dto.OrderDto;
import academy.mindswap.interceptor.UserHasOrder;
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
    public List<OrderDto> listAll(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new WebApplicationException("User not found", 404);
        }
        return user.getOrders().stream()
                .map(order -> orderConverter.toDto(order))
                .toList();
    }

    @Override
    @UserHasOrder
    public OrderDto findById(String email, Long orderId) {
        return orderConverter.toDto(orderRepository.findById(orderId));
    }

    @Override
    public OrderDto create(String email, OrderCreateDto orderCreateDto) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new WebApplicationException("User not found", 404);
        }

        Order order = orderConverter.toEntityFromCreateDto(orderCreateDto);
        order.setUser(user);
        orderRepository.persist(order);

        return orderConverter.toDto(order);
    }

    @Override
    @UserHasOrder
    public void delete(String email, Long orderId) {
        orderRepository.delete(orderRepository.findById(orderId));
    }
}

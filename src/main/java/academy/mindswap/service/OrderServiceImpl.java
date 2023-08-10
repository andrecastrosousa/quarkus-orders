package academy.mindswap.service;

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

    @Override
    public List<Order> listAll(Long userId) {
        User user = userRepository.findById(userId);
        if(user == null) {
            throw new WebApplicationException("User not found", 404);
        }
        return orderRepository.findByUserId(userId);
    }

    @Override
    public Order create(Long userId, Order order) {
        User user = userRepository.findById(userId);
        if(user == null) {
            throw new WebApplicationException("User not found", 404);
        }
        order.setUser(user);

        orderRepository.persist(order);
        return order;
    }

    @Override
    public Order update(Long userId, Long orderId, Order order) {
        Order orderFound = orderRepository.findById(orderId);

        if(orderFound == null || !order.getId().equals(orderId)) {
            throw new WebApplicationException("Order not found", 404);
        }
        if(!orderFound.getUser().getId().equals(userId)) {
            throw new WebApplicationException("Order not found", 404);
        }

        orderFound.setTotal(order.getTotal());
        orderRepository.persist(orderFound);

        return orderFound;
    }

    @Override
    public void delete(Long userId, Long orderId) {
        Order orderFound = orderRepository.findById(orderId);

        if(orderFound == null) {
            throw new WebApplicationException("Order not found", 404);
        }
        if(!orderFound.getUser().getId().equals(userId)) {
            throw new WebApplicationException("Order not found", 404);
        }

        orderRepository.delete(orderFound);
    }
}

package academy.mindswap.converter;

import academy.mindswap.dto.OrderCreateDto;
import academy.mindswap.dto.OrderDto;
import academy.mindswap.model.Order;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class OrderConverter {
    @Inject
    ObjectMapper objectMapper;

    public Order fromDto(OrderDto orderDto) {
        return objectMapper.convertValue(orderDto, Order.class);
    }

    public OrderDto toDto(Order order) {
        return objectMapper.convertValue(order, OrderDto.class);
    }

    public Order toEntityFromCreateDto(OrderCreateDto orderCreateDto) {
        return objectMapper.convertValue(orderCreateDto, Order.class);
    }
}

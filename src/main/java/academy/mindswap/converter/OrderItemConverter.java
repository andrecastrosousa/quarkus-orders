package academy.mindswap.converter;

import academy.mindswap.dto.OrderItemDto;
import academy.mindswap.dto.UserCreateDto;
import academy.mindswap.dto.UserDto;
import academy.mindswap.model.OrderItem;
import academy.mindswap.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class OrderItemConverter {
    @Inject
    ObjectMapper objectMapper;

    public OrderItemDto toDto(OrderItem orderItem) {
        return objectMapper.convertValue(orderItem, OrderItemDto.class);
    }
}

package academy.mindswap.service;

import academy.mindswap.dto.OrderDto;
import academy.mindswap.dto.OrderItemAddDto;
import academy.mindswap.dto.OrderItemDto;
import academy.mindswap.dto.OrderItemUpdateDto;
import academy.mindswap.model.Item;
import academy.mindswap.model.Order;
import academy.mindswap.model.OrderItem;

import java.util.List;

public interface OrderItemService {
    List<OrderItemDto> getListOfOrderItem(Long userId, Long orderId);

    OrderDto addItemToOrder(Long userId, Long orderId, OrderItemAddDto orderItemAddDto);

    OrderDto updateItemOnOrder(Long userId, Long orderId, Long itemId, OrderItemUpdateDto OrderItemUpdateDto);
}

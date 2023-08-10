package academy.mindswap.service;

import academy.mindswap.dto.OrderItemAddDto;
import academy.mindswap.model.Item;
import academy.mindswap.model.Order;
import academy.mindswap.model.OrderItem;

import java.util.List;

public interface OrderItemService {
    List<OrderItem> getListOfOrderItem(Long userId, Long orderId);

    Order addItemToOrder(Long userId, Long orderId, OrderItemAddDto orderItemAddDto);
}

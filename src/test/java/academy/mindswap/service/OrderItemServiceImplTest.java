package academy.mindswap.service;

import academy.mindswap.converter.OrderConverter;
import academy.mindswap.converter.OrderItemConverter;
import academy.mindswap.model.Item;
import academy.mindswap.model.Order;
import academy.mindswap.repository.ItemRepository;
import academy.mindswap.repository.OrderItemRepository;
import academy.mindswap.repository.OrderRepository;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
public class OrderItemServiceImplTest {
    @Inject
    OrderItemServiceImpl orderItemService;

    @InjectMock
    OrderItemRepository orderItemRepository;
    @InjectMock
    OrderRepository orderRepository;
    @InjectMock
    ItemRepository itemRepository;
    @InjectMock
    OrderConverter orderConverter;
    @InjectMock
    OrderItemConverter orderItemConverter;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    public void recalculateTotalFromExistentItemIncrementQuantity() {
        Order order = new Order();
        order.setTotal(60);

        Item item = new Item();
        item.setPrice(20);
        item.setName("test");

        order.setTotal(orderItemService.recalculateTotal(order, item, 3, 7));

        assertEquals(140, order.getTotal());
    }

    @Test
    public void recalculateTotalFromExistentItemDecrementQuantity() {
        Order order = new Order();
        order.setTotal(60);

        Item item = new Item();
        item.setPrice(20);
        item.setName("test");

        order.setTotal(orderItemService.recalculateTotal(order, item, 3, 2));

        assertEquals(40, order.getTotal());
    }
}

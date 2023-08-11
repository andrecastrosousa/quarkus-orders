package academy.mindswap.repository;

import academy.mindswap.model.OrderItem;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class OrderItemRepository implements PanacheRepository<OrderItem> {

    public OrderItem findByOrderIdAndItemId(Long orderId, Long itemId) {
        return find(
                "order.id = :orderId and item.id = :itemId",
                Parameters.with("orderId", orderId)
                        .and("itemId", itemId)
        ).firstResultOptional().orElse(null);
    }
}

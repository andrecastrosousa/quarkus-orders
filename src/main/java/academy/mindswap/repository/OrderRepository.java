package academy.mindswap.repository;

import academy.mindswap.model.Order;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class OrderRepository implements PanacheRepository<Order> {

    public List<Order> findByUserId(Long userId) {
        return find("user.id", userId).list();
    }
}

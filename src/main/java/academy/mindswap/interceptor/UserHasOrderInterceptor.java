package academy.mindswap.interceptor;

import academy.mindswap.model.Order;
import academy.mindswap.model.User;
import academy.mindswap.repository.OrderRepository;
import academy.mindswap.repository.UserRepository;
import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;
import jakarta.ws.rs.WebApplicationException;

@UserHasOrder
@Priority(10)
@Interceptor
public class UserHasOrderInterceptor {

    @Inject
    UserRepository userRepository;

    @Inject
    OrderRepository orderRepository;

    @AroundInvoke
    public Object validation(InvocationContext ctx) throws Exception {
        String email = (String) ctx.getParameters()[0];
        Long orderId = (Long) ctx.getParameters()[1];

        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new WebApplicationException("User not found", 404);
        }

        Order order = orderRepository.findById(orderId);
        if (order == null || !order.getUser().getId().equals(user.getId())) {
            throw new WebApplicationException("Order not found", 404);
        }


        return ctx.proceed();
    }
}

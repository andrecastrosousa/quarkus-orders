package academy.mindswap.interceptor;

import jakarta.annotation.Priority;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;

import java.util.concurrent.atomic.AtomicReference;

@Logging
@Priority(10)
@Interceptor
public class LoggingInterceptor {

    static final AtomicReference<Object> LOG = new AtomicReference<Object>();

    @AroundInvoke
    Object log(InvocationContext ctx) throws Exception {
        System.out.println("LoggingInterceptor: " + ctx.getMethod());
        Object ret = ctx.proceed();
        System.out.println("LoggingInterceptor: " + ret);
        LOG.set(ret);
        return ret;
    }
}
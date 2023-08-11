package academy.mindswap.interceptor;

import jakarta.interceptor.InterceptorBinding;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({METHOD, TYPE})
@Retention(RUNTIME)
@Documented
@InterceptorBinding
public @interface VerifyUserAndOrder {
}

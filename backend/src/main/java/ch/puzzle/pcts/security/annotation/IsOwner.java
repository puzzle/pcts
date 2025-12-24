package ch.puzzle.pcts.security.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.security.access.prepost.PreAuthorize;

/**
 * Validates that the email in the request's JWT matches the email of the
 * resource owner.
 * <p>
 * For this check to function correctly, the controller method must provide a
 * user ID through one of the following methods:
 * <ul>
 * <li>A parameter named exactly <code>id</code></li>
 * <li>A parameter annotated with <code>@P("id")</code></li>
 * </ul>
 * <p>
 * If neither condition is met, <code>isOwner()</code> receives a
 * <code>null</code> argument, causing the security check to fail.
 */
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("@SecurityService.isOwner(#id)")
public @interface IsOwner {
    String id() default "id";
}

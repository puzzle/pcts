package ch.puzzle.pcts.security.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.security.access.prepost.PreAuthorize;

/**
 * Checks whether the JWT sent with the request has the same email as the
 * resource trying to be accessed.
 * <p>
 * To use this on a controller method the user id parameter needs to be either -
 * named exactly <code>id</code> - be annotated with <code>@P("id")</code> If
 * this is not the case, <code>isOwner()</code> will always be passed
 * <code>null</code>, which then fails the security check.
 */
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("@SecurityService.isOwner(#id)")
public @interface IsOwner {
    String id() default "id";
}

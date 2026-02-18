package ch.puzzle.pcts.security.annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.security.access.prepost.PreAuthorize;

/**
 * Combined check: Access allowed if user is an Admin OR the Owner of the
 * resource.
 */
@Documented
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("@SecurityService.isAdmin() or @SecurityService.isOwner(#id)")
public @interface IsAdminOrOwner {
    String id() default "id";
}

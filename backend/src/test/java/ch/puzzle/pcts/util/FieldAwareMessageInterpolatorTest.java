package ch.puzzle.pcts.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import jakarta.validation.MessageInterpolator;
import jakarta.validation.Path;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import org.hibernate.validator.messageinterpolation.HibernateMessageInterpolatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FieldAwareMessageInterpolatorTest {

    private MessageInterpolator delegate;
    private FieldAwareMessageInterpolator interpolator;
    private MessageInterpolator.Context context;
    private HibernateMessageInterpolatorContext hibernateContext;

    @BeforeEach
    void setup() {
        delegate = mock(MessageInterpolator.class);
        interpolator = new FieldAwareMessageInterpolator(delegate);

        context = mock(MessageInterpolator.Context.class);
        hibernateContext = mock(HibernateMessageInterpolatorContext.class);

        when(context.unwrap(HibernateMessageInterpolatorContext.class)).thenReturn(hibernateContext);
    }

    @Test
    void shouldInterpolateAttributeNotNullMessage() {
        // given
        String template = "{attribute.not.null}";
        String resolvedTemplate = "{class}.{field} must not be null.";

        when(delegate.interpolate(eq(template), any(), any())).thenReturn(resolvedTemplate);

        // Mock validation context
        Path path = mockPathWithLeaf("email");
        when(hibernateContext.getPropertyPath()).thenReturn(path);
        when(hibernateContext.getRootBeanType()).thenReturn((Class) User.class);
        when(context.getValidatedValue()).thenReturn(null);

        // when
        String result = interpolator.interpolate(template, context, Locale.ENGLISH);

        // then
        assertThat(result).isEqualTo("User.email must not be null.");
    }

    @Test
    void shouldInterpolateSizeBetweenMessage() {
        // given
        String template = "{attribute.size.between}";
        String resolvedTemplate = "{class}.{field} size must be between {min} and {max}, given {is}.";

        when(delegate.interpolate(eq(template), any(), any())).thenReturn(resolvedTemplate);

        // Mock validation context
        Path path = mockPathWithLeaf("name");
        when(hibernateContext.getPropertyPath()).thenReturn(path);
        when(hibernateContext.getRootBeanType()).thenReturn((Class) User.class);
        when(context.getValidatedValue()).thenReturn("A");

        // when
        String result = interpolator.interpolate(template, context, Locale.ENGLISH);

        // then
        assertThat(result).isEqualTo("User.name size must be between {min} and {max}, given A.");
    }

    private Path mockPathWithLeaf(String leafName) {
        Path path = mock(Path.class);
        Path.Node node = mock(Path.Node.class);
        when(node.getName()).thenReturn(leafName);
        Iterator<Path.Node> iterator = List.of(node).iterator();
        when(path.iterator()).thenReturn(iterator);
        return path;
    }

    static class User {
        String email;
        String name;
    }
}

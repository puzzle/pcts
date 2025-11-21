package ch.puzzle.pcts.util;

import jakarta.validation.MessageInterpolator;
import jakarta.validation.Path;
import java.util.Locale;
import org.hibernate.validator.messageinterpolation.HibernateMessageInterpolatorContext;

/**
 * A custom {@link MessageInterpolator} that wraps another interpolator (the
 * "delegate") to add support for custom placeholders in validation messages.
 *
 * <h5>1. Default Interpolation (by the Delegate)</h3> First, it allows the
 * delegate to perform its default interpolation. This resolves all standard
 * Bean Validation placeholders — these are enclosed in <code>{...}</code> and
 * map directly to the attributes of the constraint annotation.
 *
 * <h6>The delegate resolves these, including:</h6>
 * <ul>
 * <li><code>{min}</code>, <code>{max}</code> (from <code>@Size</code>)</li>
 * <li><code>{value}</code> (from <code>@Min</code>, <code>@Max</code>,
 * <code>@DecimalMin</code>, <code>@DecimalMax</code>)</li>
 * <li><code>{inclusive}</code> (from <code>@DecimalMin</code>,
 * <code>@DecimalMax</code>)</li>
 * <li><code>{regexp}</code> (from <code>@Pattern</code> and
 * <code>@Email</code>)</li>
 * <li><code>{integer}</code>, <code>{fraction}</code> (from
 * <code>@Digits</code>)</li>
 * </ul>
 *
 * <p>
 * <b>Note:</b> Only constraint annotation parameters (e.g., <code>{min}</code>,
 * <code>{max}</code>) are interpolated by the default message interpolator.
 * Expression Language (EL) variables or arbitrary expressions are not
 * automatically resolved unless additional configuration or a custom
 * interpolator is used.
 *
 * <h5>2. Custom Interpolation (by this Class)</h5> After the delegate resolves
 * all standard placeholders, this class performs a final replacement for the
 * following <b>custom</b> placeholders:
 * <ul>
 * <li><code><b>{class}</b></code>: Replaced with the simple name of the root
 * bean class being validated (e.g., "User"). Defaults to
 * "CLASS_NOT_RESOLVED".</li>
 * <li><code><b>{field}</b></code>: Replaced with the name of the specific
 * property (field) that failed validation (e.g., "username"). Defaults to
 * "FIELD_NOT_RESOLVED".</li>
 *
 * </ul>
 *
 *
 * <h5>Example Usage</h3>
 * <p>
 * Given a message template like this:
 * 
 * <pre>
 * {@code @NotNull(message = "{attribute.not.null}")}
 * </pre>
 * <p>
 * The final interpolated message might look like this:
 * 
 * <pre>
 * "Role.name must not be null."
 * </pre>
 *
 * @see MessageInterpolator
 * @see HibernateMessageInterpolatorContext
 */
public class FieldAwareMessageInterpolator implements MessageInterpolator {

    private final MessageInterpolator delegate;

    private static final String DEFAULT_FIELD_NOT_EXISTENT = "FIELD_NOT_RESOLVED";
    private static final String DEFAULT_CLASS_NOT_EXISTENT = "CLASS_NOT_RESOLVED";

    public FieldAwareMessageInterpolator(MessageInterpolator delegate) {
        this.delegate = delegate;
    }

    @Override
    public String interpolate(String messageTemplate, Context context) {
        return interpolate(messageTemplate, context, Locale.getDefault());
    }

    @Override
    public String interpolate(String messageTemplate, Context context, Locale locale) {
        // Let the default interpolator resolve standard placeholders first. In our case
        // that includes {min} and {max}.
        String resolvedTemplate = delegate.interpolate(messageTemplate, context, locale);

        String fieldName = DEFAULT_FIELD_NOT_EXISTENT;
        String className = DEFAULT_CLASS_NOT_EXISTENT;

        HibernateMessageInterpolatorContext hibernateContext = context
                .unwrap(HibernateMessageInterpolatorContext.class);

        if (hibernateContext != null) {
            fieldName = getLeafNodeName(hibernateContext.getPropertyPath());
            className = hibernateContext.getRootBeanType().getSimpleName();
        }

        return resolvedTemplate
                .replace("{key}", messageTemplate.replace(".", "_").replace("{", "").replace("}", "").toUpperCase())
                .replace("{class}", className)
                .replace("{field}", fieldName)
                .replace("{is}", String.valueOf(context.getValidatedValue()));
    }

    private String getLeafNodeName(Path path) {
        if (path == null)
            return DEFAULT_FIELD_NOT_EXISTENT;

        Path.Node leaf = null;
        for (Path.Node node : path) {
            leaf = node;
        }

        return (leaf != null) ? leaf.getName() : DEFAULT_FIELD_NOT_EXISTENT;
    }
}
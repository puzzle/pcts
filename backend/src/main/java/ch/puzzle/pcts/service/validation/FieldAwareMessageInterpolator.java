package ch.puzzle.pcts.service.validation;

import jakarta.validation.MessageInterpolator;
import jakarta.validation.Path;
import java.util.Locale;
import org.hibernate.validator.messageinterpolation.HibernateMessageInterpolatorContext;

public class FieldAwareMessageInterpolator implements MessageInterpolator {

    private final MessageInterpolator delegate;

    public FieldAwareMessageInterpolator(MessageInterpolator delegate) {
        this.delegate = delegate;

    }

    @Override
    public String interpolate(String messageTemplate, Context context) {
        return interpolate(messageTemplate, context, Locale.getDefault());
    }

    @Override
    public String interpolate(String messageTemplate, Context context, Locale locale) {
        // we first apply the default only afterwards we add the additional fields so
        // this delegate needs to be on top!
        String resolvedTemplate = delegate.interpolate(messageTemplate, context, locale);

        String className = null;
        String fieldName = "Unknown";

        HibernateMessageInterpolatorContext hibernateContext = context
                .unwrap(HibernateMessageInterpolatorContext.class);
        if (hibernateContext != null) {
            className = hibernateContext.getRootBeanType().getSimpleName();
            Path propertyPath = hibernateContext.getPropertyPath();
            Path.Node leaf = null;
            for (Path.Node node : propertyPath) {
                leaf = node;
            }
            if (leaf != null) {
                fieldName = leaf.getName();
            }
        }

        // option to add the name of the class --> might help later on we can define a
        // custom format
        // we then could parse the format in the frontend as we want and show errors any
        // way we like
        if (resolvedTemplate.contains("{class}") && className != null) {
            resolvedTemplate = resolvedTemplate.replace("{class}", className);
        }

        // sometimes it is useful to have the given value but not always so we do it
        // conditionally
        Object actualValue = context.getValidatedValue();
        if (resolvedTemplate.contains("{value}")) {
            String givenValue = actualValue != null ? actualValue.toString() : "null";
            resolvedTemplate = resolvedTemplate.replace("{value}", givenValue);
        }

        // maybe we do not want the field in some messages therefore we make it
        // conditional
        if (resolvedTemplate.contains("{field}") && fieldName != null) {
            resolvedTemplate = resolvedTemplate.replace("{field}", fieldName);
        }
        return resolvedTemplate;

    }
}
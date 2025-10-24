package ch.puzzle.pcts;

import jakarta.validation.MessageInterpolator;
import jakarta.validation.Path;
import java.util.Locale;
import org.hibernate.validator.messageinterpolation.HibernateMessageInterpolatorContext;

public record FieldAwareMessageInterpolator(MessageInterpolator delegate) implements MessageInterpolator {

    @Override
    public String interpolate(String messageTemplate, Context context) {
        return interpolate(messageTemplate, context, Locale.getDefault());
    }

    @Override
    public String interpolate(String messageTemplate, Context context, Locale locale) {
        String resolvedTemplate = delegate.interpolate(messageTemplate, context, locale);

        String className = null;
        String fieldName = null;

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

        Object actualValue = context.getValidatedValue();

        String classNameStr = (className != null) ? className : "null";
        String fieldNameStr = (fieldName != null) ? fieldName : "null";
        String valueStr = (actualValue != null) ? actualValue.toString() : "null";

        resolvedTemplate = resolvedTemplate.replace("{class}", classNameStr);
        resolvedTemplate = resolvedTemplate.replace("{field}", fieldNameStr);
        resolvedTemplate = resolvedTemplate.replace("{value}", valueStr);

        return resolvedTemplate;
    }
}
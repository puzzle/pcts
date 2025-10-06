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
        String fieldName = "daf";

        HibernateMessageInterpolatorContext hibernateContext = context
                .unwrap(HibernateMessageInterpolatorContext.class);
        if (hibernateContext != null) {
            Path propertyPath = hibernateContext.getPropertyPath();
            Path.Node leaf = null;
            for (Path.Node node : propertyPath) {
                leaf = node;
            }
            if (leaf != null) {
                fieldName = leaf.getName();
            }
        }

        // Object das = (ConstraintDescriptorImpl) context.;
        // System.out.println(das);

        // if (fieldAttr != null && !fieldAttr.toString().isBlank()) {
        // fieldName = fieldAttr.toString();
        // System.out.println("fieldname is:" + fieldName + "+".repeat(20));
        // } else {
        // System.out.println("*".repeat(20));
        // }

        String interpolated = messageTemplate.replace("{field}", fieldName);
        return delegate.interpolate(interpolated, context, locale);
    }
}

package ch.puzzle.pcts.architecture;

import com.tngtech.archunit.core.domain.JavaAnnotation;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.domain.JavaPackage;
import com.tngtech.archunit.lang.AbstractClassesTransformer;
import com.tngtech.archunit.lang.ClassesTransformer;
import java.lang.annotation.Annotation;
import java.util.Optional;
import java.util.stream.Collectors;

public class CustomTransformers {
    public static ClassesTransformer<JavaPackage> packages() {
        return new AbstractClassesTransformer<>("packages") {
            @Override
            public Iterable<JavaPackage> doTransform(JavaClasses javaClasses) {
                return javaClasses.stream().map(JavaClass::getPackage).collect(Collectors.toSet());
            }
        };
    }

    public static ClassesTransformer<JavaAnnotation<JavaClass>> annotations(Class<? extends Annotation> annotationClass) {
        return new AbstractClassesTransformer<>("annotations") {
            @Override
            public Iterable<JavaAnnotation<JavaClass>> doTransform(JavaClasses javaClasses) {
                return javaClasses
                        .stream()
                        .map(clazz -> clazz.tryGetAnnotationOfType(annotationClass.getTypeName()))
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .toList();
            }
        };
    }
}

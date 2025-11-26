package ch.puzzle.pcts.architecture;

import com.tngtech.archunit.core.domain.JavaAnnotation;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.domain.JavaPackage;
import com.tngtech.archunit.lang.AbstractClassesTransformer;
import com.tngtech.archunit.lang.ClassesTransformer;
import java.util.Collection;
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

    public static ClassesTransformer<JavaAnnotation<JavaClass>> annotations() {
        return new AbstractClassesTransformer<>("annotations") {

            @Override
            public Iterable<JavaAnnotation<JavaClass>> doTransform(JavaClasses javaClasses) {
                return javaClasses
                        .stream()
                        .map(JavaClass::getAnnotations)
                        .flatMap(Collection::stream)
                        .collect(Collectors.toList());
            }
        };
    }
}

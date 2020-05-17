package net.danielrickman.api.util;

import io.github.classgraph.ClassGraph;
import lombok.experimental.UtilityClass;

import java.lang.annotation.Annotation;
import java.util.List;

@UtilityClass
public class ClassUtil {

    public List<Class<?>> getClassesWithAnnotation(Class<? extends Annotation> annotation, String packageName) {
        return new ClassGraph()
                .enableAllInfo()
                .whitelistPackages(packageName).scan()
                .getClassesWithAnnotation(annotation.getName())
                .loadClasses();
    }
}
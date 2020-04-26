package net.danielrickman.api.util;

import io.github.classgraph.ClassGraph;

import java.lang.annotation.Annotation;
import java.util.List;

public class ClassUtil {

    public static List<Class<?>> getClassesWithAnnotation(Class<? extends Annotation> annotation, String packageName) {
     return new ClassGraph().enableAllInfo().whitelistPackages(packageName).scan().getClassesWithAnnotation(annotation.getName()).loadClasses();
    }
}

package ru.ssau.tk.java_domination_339.java_labs_2024.ui.api.enums;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import ru.ssau.tk.java_domination_339.java_labs_2024.functions.*;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class MathFunctionType {


    ;

    private final String localizedName;
    private final MathFunction function;
    private static  Map<String, MathFunction> map = new HashMap<>();
    private static List<String> func = new ArrayList<>();
    private static List<Class<?>> classes = findSimpleFunctions();
    MathFunctionType(String localizedName, MathFunction function) {
        this.localizedName = localizedName;
        this.function = function;
    }

    public String getLocalizedName() {
        return localizedName;
    }

    public MathFunction getFunction() {
        return function;
    }
    public static List<String> getFunctions() {
        return func;
    }
    static public List<Class<?>> findSimpleFunctions() {

            List<Class<?>> functions = new ArrayList<>();
            ClassPathScanningCandidateComponentProvider scanner =
                    new ClassPathScanningCandidateComponentProvider(false);

            scanner.addIncludeFilter(new AnnotationTypeFilter(SimpleFunctionAnnotation.class));

            Set<BeanDefinition> beans = scanner.findCandidateComponents("ru.ssau.tk");
            System.out.println(beans);
            for (BeanDefinition bean : beans) {
                try {
                    Class<?> cls = Class.forName(bean.getBeanClassName());
                    functions.add(cls);

                } catch (ClassNotFoundException e) {
                    // Обработка ошибки
                }
            }
            System.out.println(functions);
            // Сортировка функций по приоритету и названию
            functions.sort((a, b) -> {
                SimpleFunctionAnnotation annotA = a.getAnnotation(SimpleFunctionAnnotation.class);
                SimpleFunctionAnnotation annotB = b.getAnnotation(SimpleFunctionAnnotation.class);

                int priorityCompare = Integer.compare(annotB.priority(), annotA.priority());
                if (priorityCompare != 0) return priorityCompare;

                return annotA.name().compareTo(annotB.name());
            });
            classes = functions;
            for (Class<?> cls : classes) {
                func.add(cls.getAnnotation(SimpleFunctionAnnotation.class).name());
            }
        return classes;
    }

    public static Map<String, MathFunction> getLocalizedFunctionMap() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        if (map.isEmpty()) {
            List<Class<?>> functions = classes;
            for (Class<?> type : functions) {
                map.put(type.getAnnotation(SimpleFunctionAnnotation.class).name(),(MathFunction) type.getDeclaredConstructor().newInstance());
            }
            System.out.println(map);
        }
        return map;

    }

    public static Map<String, MathFunction> addFunctionMap(String name, MathFunction obj) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        if (map.isEmpty()) {
            map = getLocalizedFunctionMap();
        }
        map.put(name, obj);
        func.add(name);
        return map;
    }
}
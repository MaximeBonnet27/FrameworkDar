package com.wasp.util.waspComponent.views;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unused")
public class ViewModel implements IView {

    private final String name;
    private final HashMap<String, List<IView>> templates;
    private HashMap<String, Object> attibutes;
    private String content;

    public ViewModel(String name) {
        this.attibutes = new HashMap<>();
        this.name = name;
        this.templates = new HashMap<>();
    }

    public void putAttribute(String name, Object value) {
        attibutes.put(name, value);
    }

    public Object getAttribute(String key) {
        return attibutes.get(key);
    }

    public boolean containsAttributeObject(String key) {
        return attibutes.containsKey(key);
    }

    public void putAllAttribute(Map<String, Object> m) {
        attibutes.putAll(m);
    }

    @Override
    public final String getName() {
        return name;
    }

    @Override
    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String getContent() {
        return content;
    }

    @Override
    public void addTemplate(String key, IView view) {
        if (!this.templates.containsKey(key))
            this.templates.put(key, new ArrayList<>());
        this.templates.get(key).add(view);
    }

    @Override
    public HashMap<String, List<IView>> getTemplates() {
        return this.templates;
    }

    @Override
    public String evaluate() {
        StringBuilder result = new StringBuilder();
        StringBuilder expression = new StringBuilder();
        boolean dollar = false;
        boolean chevron = false;
        for (int i = 0; i < content.length(); i++) {
            char c = content.charAt(i);
            if (dollar && chevron) {
                if (c == '}') {
                    dollar = false;
                    chevron = false;
                    try {
                        System.out.println(expression);
                        result.append(valueOfExpression(expression.toString()));
                    } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                        e.printStackTrace();
                        result.append("${" + expression + "}");
                    }
                    expression = new StringBuilder();
                } else {
                    expression.append(c);
                }
            } else {
                switch (c) {
                    case '\\':
                        i++;
                        result.append(content.charAt(i));
                        break;
                    case '$':
                        if (dollar)
                            result.append('$');
                        dollar = true;
                        break;
                    case '{':
                        if (dollar) {
                            chevron = true;
                        } else {
                            result.append(c);
                        }
                        break;
                    default:
                        result.append(c);
                }
            }
        }
        return result.toString();
    }

    private String valueOfExpression(String expression) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        int endIndex = expression.indexOf('.');
        endIndex = endIndex == -1 ? expression.length() : endIndex;
        if (attibutes.containsKey(expression.substring(0, endIndex))) {
            return attributeEvaluation(expression);
        }
        return templateEvaluation(expression);
    }

    private String attributeEvaluation(String expression) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String[] fragment = expression.split("\\.");
        Object object = getAttribute(fragment[0]);
        if (fragment.length > 1) {
            for (int i = 1; i < fragment.length; i++) {

                String getter = "get" + upperFirstLetter(fragment[i]);
                Method method;
                try {
                    method = object.getClass().getDeclaredMethod(getter);
                } catch (NoSuchMethodException e) {
                    method = object.getClass().getDeclaredMethod(fragment[i]);
                }
                object = method.invoke(object);
            }
        }
        return object.toString();
    }

    private String templateEvaluation(String expression) {
        List<IView> views = getTemplates().get(expression);
        if (views != null) {
            StringBuilder stringBuilder = new StringBuilder();
            for (IView v : views) {
                stringBuilder.append(v.evaluate());
            }
            return stringBuilder.toString();
        }
        return "";
    }

    private String upperFirstLetter(String value) {
        if (value.length() <= 1)
            return value.toUpperCase();
        else {
            return Character.toUpperCase(value.charAt(0)) + value.substring(1);
        }
    }

    @Override
    public String toString() {
        return evaluate();
    }
}

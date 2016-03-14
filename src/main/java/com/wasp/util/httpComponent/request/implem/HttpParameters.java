package com.wasp.util.httpComponent.request.implem;

import org.apache.log4j.Logger;

import java.lang.reflect.*;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class HttpParameters extends HashMap<String, String> {
    private static Logger logger = Logger.getLogger(HttpParameters.class);

    public HttpParameters() {
        super();
    }

    public HttpParameters(String value) {
        super();
        if (value != null) {
            String[] args = value.split("&");
            for (String s : args) {
                String[] kv = s.split("=");
                if(kv.length==2)
                    put(kv[0], normalize(kv[1]));
            }
        }
    }

    private String normalize(String string) {
        StringBuilder builder = new StringBuilder();
        char[] chars = string.toCharArray();
        for(int i=0; i<chars.length;i++){
            if(chars[i]=='+'){
                builder.append(" ");
            }else {
                if (chars[i] == '\\')
                    i++;
                builder.append(chars[i]);
            }
        }
        return builder.toString();
    }


    public <T> T toClass(Class<T> clazz) throws IllegalAccessException, InstantiationException {
        T instance = clazz.newInstance();
        java.lang.reflect.Method[] methods = clazz.getMethods();
        Set<Entry<String, String>> entries = entrySet();
        for (Entry<String, String> entry : entries) {
            List<Method> setters = Arrays.asList(methods)
                    .stream()
                    .filter(m -> m.getName().equals("set" + upperFirstLetter(entry.getKey())) && m.getParameterCount() == 1)
                    .collect(Collectors.toList());

            boolean setted = false;

            for (int i = 0; i < setters.size() && !setted; i++) {
                Method method = setters.get(i);
                Class<?> parametreClass = method.getParameterTypes()[0];
                if (parametreClass.isPrimitive()) {
                    switch (parametreClass.getName()) {
                        //TODO to complete
                        case "int":
                            try {
                                method.invoke(instance, Integer.parseInt(entry.getValue()));
                                setted = true;
                            } catch (InvocationTargetException e) {
                                logger.debug(e.getMessage());
                            }
                            break;
                        case "float":
                            try {
                                method.invoke(instance, Float.parseFloat(entry.getValue()));
                                setted = true;
                            } catch (InvocationTargetException e) {
                                logger.debug(e.getMessage());
                            }
                            break;
                    }
                } else {
                    switch (parametreClass.getName()) {
                        //TODO to complete
                        case "java.lang.String":
                            try {
                                method.invoke(instance, entry.getValue());
                                setted = true;
                            } catch (InvocationTargetException e) {
                                logger.debug(e.getMessage());
                            }
                            break;
                        case "java.lang.Integer":
                            try {
                                method.invoke(instance, Integer.parseInt(entry.getValue()));
                                setted = true;
                            } catch (InvocationTargetException e) {
                                logger.debug(e.getMessage());
                            }
                            break;
                        case "java.lang.Float":
                            try {
                                method.invoke(instance, Float.parseFloat(entry.getValue()));
                                setted = true;
                            } catch (InvocationTargetException e) {
                                logger.debug(e.getMessage());
                            }
                            break;
                    }
                }
            }
        }
        return instance;
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
        StringBuilder result = new StringBuilder();
        entrySet().forEach(entry -> result
                .append(entry.getKey())
                .append("=").append(entry.getValue())
                .append("&"));

        return result.toString().replaceFirst("&$", "");
    }
}

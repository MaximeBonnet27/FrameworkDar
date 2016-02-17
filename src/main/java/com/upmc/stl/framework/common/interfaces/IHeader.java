package com.upmc.stl.framework.common.interfaces;

import java.util.List;
import java.util.Set;

public interface IHeader<T> {
    void addItem(T item, String value);
    void addItems(T item, List<String> values);
    Set<String> getValues(T item);
}

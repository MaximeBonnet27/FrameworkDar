package com.upmc.stl.framework.common.interfaces;

import java.util.List;

public interface IHeader<T> {
    void addItem(T item, String value);
    void addItems(T item, List<String> values);
}

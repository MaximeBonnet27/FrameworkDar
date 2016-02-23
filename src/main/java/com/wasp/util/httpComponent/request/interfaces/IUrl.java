package com.wasp.util.httpComponent.request.interfaces;

import java.util.Map;

public interface IUrl {
    String getResource();
    Integer getIntegerValue(String key);
    String getValue(String key);
    Map<String,String> getArguments();
}

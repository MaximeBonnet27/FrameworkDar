package com.wasp.util.httpComponent.request.interfaces;

import java.util.Map;

public interface IUrl {
    String getContext();
    String getResource();
    Integer getIntegerValue(String key);
    Float getFloatValue(String key);
    String getValue(String key);
    Map<String,String> getArguments();
}

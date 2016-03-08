package com.wasp.util.waspComponent.views;

import java.util.HashMap;
import java.util.List;

@SuppressWarnings("unused")
public interface IView {
    String getName();
    void setContent(String content);
    String getContent();
    void addTemplate(String key,IView view);
    HashMap<String,List<IView>> getTemplates();
    String evaluate();
}

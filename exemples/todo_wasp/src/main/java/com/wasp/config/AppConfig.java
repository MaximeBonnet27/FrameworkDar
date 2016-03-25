package com.wasp.config;

import com.wasp.configuration.wasp.ViewMapping;
import com.wasp.configuration.wasp.WaspConfig;
import com.wasp.controller.ProfilController;
import com.wasp.controller.WebSiteController;

public class AppConfig extends WaspConfig{
    @Override
    public Class[] getAnnotatedControllers() {
        return new Class[]{ProfilController.class, WebSiteController.class};
    }

    @Override
    public ViewMapping getViewMapping() {
        return new ViewMapping("views/",".html");
    }
}

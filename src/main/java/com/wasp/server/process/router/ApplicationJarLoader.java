package com.wasp.server.process.router;

import com.wasp.AppUtils;
import com.wasp.configuration.wasp.ViewMapping;
import com.wasp.configuration.wasp.Wasp;
import com.wasp.server.process.router.exceptions.MappingException;
import com.wasp.util.waspComponent.views.IView;
import org.apache.log4j.Logger;
import org.xeustechnologies.jcl.JarClassLoader;
import org.xeustechnologies.jcl.JclObjectFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

public class ApplicationJarLoader {
    private static Logger logger = Logger.getLogger(ApplicationJarLoader.class);

    private Wasp applicationConfiguration;

    private JarClassLoader jcl;

    public ApplicationJarLoader(String jarLocation) {
        logger.info("loading jar " + jarLocation);
        this.jcl = new JarClassLoader();
        try {
            this.jcl.add(new FileInputStream(jarLocation));
            URL url = new URL("jar:file:" + jarLocation + "!/wasp.json");
            this.applicationConfiguration = new AppUtils().fromJSON(url, Wasp.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Wasp getApplicationConfiguration() {
        return applicationConfiguration;
    }

    public JarClassLoader getJcl() {
        return jcl;
    }

    public Class<?> loadClass(String className) {
        return newInstance(className).getClass();
    }

    public Object newInstance(String className) {
        return JclObjectFactory.getInstance().create(getJcl(), className);
    }

    public void initContentView(IView view) throws MappingException {
        ViewMapping viewMapping = applicationConfiguration.getViewMapping();
        String name = view.getName();
        if (viewMapping != null)
            name = viewMapping.getPrefix() + name + viewMapping.getSuffix();
        final String finalName = "/WASP-INF/" + name;
        view.setContent(getResourceContent(finalName));

        List<IView> collect = view.getTemplates().values().stream().flatMap(Collection::stream).collect(Collectors.toList());
        for(IView v:collect){
            initContentView(v);
        }

    }

    public String getResourceContent(String path) throws MappingException {
        if (path.matches("^/WASP-INF/.+")) {
            path = path.replaceFirst("^/", "");
            final String finalPath = path;
            try {
                return new String(jcl.getLoadedResources()
                    .entrySet()
                    .stream()
                    .filter(e -> e.getKey().equals(finalPath))
                    .findFirst()
                    .get()
                    .getValue());
            } catch (NoSuchElementException e) {
                throw new MappingException("resource " + finalPath + " does not existe");
            }
        } else {
            return null;
        }
    }
}

package com.wasp.server.process.router;

import com.wasp.AppUtils;
import com.wasp.schemas.wasp.RequestMappingType;
import com.wasp.schemas.wasp.WaspType;
import com.wasp.schemas.wasp_conf.ApplicationType;
import com.wasp.schemas.wasp_conf.WaspConfigType;
import com.wasp.server.process.interfaces.IProcess;
import com.wasp.util.httpComponent.request.interfaces.IHttpRequest;
import com.wasp.util.httpComponent.response.enums.EStatus;
import com.wasp.util.httpComponent.response.implem.HttpResponseBuilder;
import com.wasp.util.httpComponent.response.interfaces.IHttpResponse;
import org.xeustechnologies.jcl.JarClassLoader;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

import static com.wasp.server.process.router.RequestMapping.callback;

public class Router implements IProcess {
    private HashMap<String,JarClassLoader> jarLoaders;
    private HashMap<String, WaspType> applications;

    public Router(WaspConfigType configuration) {
        this.jarLoaders = new HashMap<>();
        this.applications = new HashMap<>();
        List<ApplicationType> application = configuration.getWasps().getApplication();
        application.forEach(app ->
                applications.put(app.getContext(), loadApplication(app.getContext(), app.getLocation())));
    }

    private WaspType loadApplication(String context, String location) {
        JarClassLoader jcl = new JarClassLoader();
        jarLoaders.put(context,jcl);
        try {
            jcl.add(new FileInputStream(location));
            URL url = new URL("jar:file:" + location + "!/wasp.xml");
            InputStream stream = url.openStream();
            return new AppUtils().loadXML(stream, WaspType.class);
        } catch (IOException | ParserConfigurationException | JAXBException | SAXException e) {
            e.printStackTrace();
        }
        return null;
    }


   /* public Router(String configFile) {
   JarClassLoader jcl = new JarClassLoader();
        try {
            File file = new File("/Users/jordhanleoture/Documents/workspaceIntelli/PointProject/target/PointProject-1.0-SNAPSHOT.jar");
            jcl.add(new FileInputStream(file));
            Object o = JclObjectFactory.getInstance().create(jcl, "stl.upmc.com.App");
            Annotation[] annotations = Router.class.getAnnotations();
            for (int i=0;i<annotations.length;i++){
                System.out.println(annotations[i]);
            }
            applications = new HashMap<>();
            applications.put("/monapp", (IProcess)o);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

    @Override
    public IHttpResponse run(IHttpRequest request) {
        String[] split = request.getMethod().getUrl().getResource().split("/");
        String context = split.length > 1 ? "/" + split[1] : "/";

        WaspType wasp = applications.get(context);
        if (wasp != null) {
            RequestMappingType requestMappingType=RequestMapping.findRequestMappindType(wasp.getRequestMapping(), request);
                if (requestMappingType!=null) {
                    return callback(jarLoaders.get(context),requestMappingType,request);
                }
        }
        //TODO default 404 error content
        return new HttpResponseBuilder().status(EStatus.NOT_FOUND).build();
    }


}


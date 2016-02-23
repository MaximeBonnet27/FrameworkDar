package com.wasp;

import com.wasp.schemas.wasp_conf.WaspConfigType;
import com.wasp.server.GeneriqueHttpServer;
import com.wasp.server.process.router.Router;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        if(args.length!=1) {
            System.out.println("usage: java -jar wasp.jar /path/to/wasp-conf.xml");
            return;
        }

        try {
            WaspConfigType configuration=new AppUtils().loadXML(new FileInputStream(args[0]), WaspConfigType.class);
            System.out.println(configuration);
            GeneriqueHttpServer httpServer = new GeneriqueHttpServer(configuration.getPort(),new Router(configuration));
            new Thread(httpServer::launchServer).start();

            Scanner scanner = new Scanner(System.in);
            while (!scanner.nextLine().equals("quit")) ;
            httpServer.close();
            scanner.close();
        } catch (IOException | ParserConfigurationException | JAXBException | SAXException e) {
            e.printStackTrace();
        }
    }

}

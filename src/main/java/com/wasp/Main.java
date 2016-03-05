package com.wasp;

import com.wasp.configuration.wasp_conf.WaspServer;
//import com.wasp.schemas.wasp_conf.WaspConfigType;
import com.wasp.server.GenericHttpServer;
import com.wasp.server.process.router.Router;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    private static Logger logger=Logger.getLogger(Main.class);

    public static void main(String[] args) {
        if(args.length!=1) {
            System.out.println("usage: java -jar wasp.jar /path/to/wasp-conf.json");
            return;
        }

        try {
            WaspServer configuration=new AppUtils().fromJSON(new File(args[0]), WaspServer.class);
            logger.info(configuration);
            GenericHttpServer httpServer = new GenericHttpServer(configuration.getPort(),new Router(configuration));
            new Thread(httpServer::launchServer).start();

            Scanner scanner = new Scanner(System.in);
            while (!scanner.nextLine().equals("quit")) ;
            httpServer.close();
            scanner.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

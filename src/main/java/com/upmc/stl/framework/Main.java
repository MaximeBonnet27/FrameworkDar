package com.upmc.stl.framework;

import com.upmc.stl.framework.server.HttpServerEcho;

import java.io.IOException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        try {
            HttpServerEcho httpServer = new HttpServerEcho(1234);
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

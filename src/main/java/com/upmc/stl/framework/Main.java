package com.upmc.stl.framework;

import com.upmc.stl.framework.server.ServerEcho;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        ServerEcho server = new ServerEcho(1234);
        new Thread(server::launchServer).start();
        Scanner scanner = new Scanner(System.in);
        while(!scanner.nextLine().equals("quit"));
        server.stop();
        scanner.close();
    }

}

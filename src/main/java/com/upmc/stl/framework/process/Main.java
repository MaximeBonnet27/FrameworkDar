package com.upmc.stl.framework.process;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Server server = new Server(1234);
        new Thread(server::launchServer).start();
        Scanner scanner = new Scanner(System.in);
        while(!scanner.nextLine().equals("quit"));
        server.stop();
        scanner.close();
    }

}

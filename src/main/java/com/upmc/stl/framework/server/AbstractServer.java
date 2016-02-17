package com.upmc.stl.framework.server;

import com.upmc.stl.framework.common.enums.EProtocol;
import com.upmc.stl.framework.process.interfaces.IProcess;
import com.upmc.stl.framework.request.enums.EMethodType;
import com.upmc.stl.framework.request.enums.ERequestHeaderItem;
import com.upmc.stl.framework.request.implem.Request;
import com.upmc.stl.framework.request.interfaces.IMethod;
import com.upmc.stl.framework.request.interfaces.IRequest;
import com.upmc.stl.framework.response.interfaces.IResponse;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.Scanner;

public class AbstractServer {
    private ServerSocket serverSocket;
    private int port;
    private IProcess process;

    public AbstractServer(int port,IProcess process) {
        this.port = port;
        this.process=process;
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void launchServer() {
        try {
            while (true) {
                Socket connectionSocket = serverSocket.accept();
                StringBuilder sb = new StringBuilder();
                BufferedReader in =
                        new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
                String line;
                while (!(line = in.readLine()).isEmpty()) {
                    // TODO: 17/02/16 Log4j
                    sb.append(line).append("\n");
                }
                // TODO: 17/02/16 vérification avan parser
                IRequest request = parse(sb.toString());
                IResponse response = process.run(request);

                BufferedOutputStream bos = new BufferedOutputStream(connectionSocket.getOutputStream());
                bos.write(response.toString().getBytes());
                bos.flush();
                bos.close();
            }
        } catch (IOException e) {
            //ServerSocket is closed, that way we break out of the loop
            // TODO: 17/02/16 log4j
        }
    }

    public void stop() {
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // TODO: 17/02/16 To refacto
    public IRequest parse(String command) {
        IRequest request = new Request();

        Scanner scanner = new Scanner(command);
        String line;
        line = scanner.nextLine();
        String[] firstLineTokens = line.split(" ");

        IMethod method = request.getMethod();
        method.setMethodType(EMethodType.getMethod(firstLineTokens[0]));
        method.setURL(firstLineTokens[1]);
        method.setProtocol(EProtocol.getProtocol(firstLineTokens[2]));

        while (scanner.hasNextLine()
                && !(line=scanner.nextLine()).isEmpty()){

            String[] tokens = line.split(":");

            for(int i = 0; i < tokens.length; ++i){
                tokens[i] = tokens[i].trim();
            }

            ERequestHeaderItem item = ERequestHeaderItem.getItem(tokens[0]);
            String values = tokens[1];
            request.getHeader().addItems(item, Arrays.asList(values.split(",")));
        }

        StringBuilder stringBuilder = new StringBuilder();

        while (scanner.hasNextLine()) {
            line = scanner.nextLine();

            stringBuilder
                    .append(line)
                    .append("\r\n");
        }

        request.setContent(stringBuilder.toString());
        return request;
    }
}

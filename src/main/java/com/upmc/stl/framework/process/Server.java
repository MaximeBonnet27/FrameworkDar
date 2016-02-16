package com.upmc.stl.framework.process;

import com.upmc.stl.framework.common.enums.EProtocol;
import com.upmc.stl.framework.response.enums.EResponseHeaderItem;
import com.upmc.stl.framework.response.enums.EStatus;
import com.upmc.stl.framework.response.implem.ResponseBuilder;
import com.upmc.stl.framework.response.interfaces.IResponse;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

// TODO: 16/02/16 Refactoring
// TODO: 16/02/16 Annotation Semaine 2 !
public class Server {

    private ServerSocket serverSocket;
    private int port;

    public Server(int port){
        this.port = port;
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void launchServer() {
        StringBuilder sb = new StringBuilder();
        try {
            while (true) {
                Socket connectionSocket = serverSocket.accept();
                BufferedReader inFromClient =
                        new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
                String line;
                line = inFromClient.readLine();
                System.out.println(line);
                sb.append(line).append("\n");
                process(sb.toString(),connectionSocket);
            }
        } catch (IOException e) {
            //ServerSocket is closed, that way we break out of the loop
        }
    }

    private void process(String commandReceived, Socket socket) {
        System.out.println(commandReceived);
        System.out.flush();
        try {
            BufferedOutputStream bos = new BufferedOutputStream(socket.getOutputStream());

            String message = "Hello !";

            IResponse r = new ResponseBuilder()
                    .protocol(EProtocol.HTTP_1_1)
                    .status(EStatus.OK)
                    .header(EResponseHeaderItem.CONTENT_TYPE, "text/plain")
                    .header(EResponseHeaderItem.CONTENT_LENGTH, message.length()+"")
                    .content(message)
                    .build();

            bos.write(r.toString().getBytes());
            bos.flush();
            bos.close();
        }
        catch (IOException e){
            e.printStackTrace();
        }


    }

    public void stop(){
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}

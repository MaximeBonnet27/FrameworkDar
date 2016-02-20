package com.upmc.stl.framework.server;

import com.upmc.stl.framework.common.enums.HttpProtocolVersions;
import com.upmc.stl.framework.process.interfaces.IProcess;
import com.upmc.stl.framework.request.exceptions.MethodeTypeException;
import com.upmc.stl.framework.request.interfaces.IHttpRequest;
import com.upmc.stl.framework.response.enums.EStatus;
import com.upmc.stl.framework.response.implem.HttpResponseBuilder;
import com.upmc.stl.framework.response.interfaces.IHttpResponse;
import org.apache.log4j.Logger;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class AbstractHttpServer extends ServerSocket {
    private static final Logger logger = Logger.getLogger(AbstractHttpServer.class);
    private IProcess process;
    private boolean askClose;

    private AbstractHttpServer(int port) throws IOException {
        super(port);
    }

    public AbstractHttpServer(int port, IProcess process) throws IOException {
        this(port);
        this.process = process;
        this.askClose = false;
    }

    @Override
    public void close() throws IOException {
        this.askClose = true;
        super.close();
    }

    public void launchServer() {
        logger.info("Server listening on localhost:" + getLocalPort() + " ...");
        try {
            while (true) {
                Socket connectionSocket = accept();
                new Thread(() -> serviceClient(connectionSocket)).start();
            }
        } catch (IOException e) {
            if (!askClose)
                logger.error(e.getMessage());
        }
    }

    private void serviceClient(Socket clientSocket) {
        IHttpRequest request;
        IHttpResponse response;

        //pour éviter un blocage dans le serveur
        try {
            clientSocket.setSoTimeout(10000);
        } catch (SocketException e) {
            logger.error(e.getMessage());
            try {
                clientSocket.close();
            } catch (IOException e1) {
                logger.error(e1.getMessage());
            }
            return;
        }

        Client client = new Client(clientSocket);

        try {
            request = client.getHttpRequest();
            logger.info("receive :\n" + request);
            response = process.run(request);
        } catch (IOException | MethodeTypeException e1) { // BAD REQUEST
            logger.error(e1.getMessage());
            response = new HttpResponseBuilder().protocol(HttpProtocolVersions.HTTP_1_1)
                    .status(EStatus.BAD_REQUEST)
                    .content(e1.getMessage())
                    .build();
        }

        client.sendHttpResponse(response);
        client.shutdown();
    }

}

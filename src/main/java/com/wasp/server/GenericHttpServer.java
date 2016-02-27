package com.wasp.server;

import com.wasp.util.httpComponent.common.enums.HttpProtocolVersions;
import com.wasp.server.process.interfaces.IProcess;
import com.wasp.util.httpComponent.request.exceptions.MethodeTypeException;
import com.wasp.util.httpComponent.request.interfaces.IHttpRequest;
import com.wasp.util.httpComponent.response.enums.EStatus;
import com.wasp.util.httpComponent.response.implem.HttpResponseBuilder;
import com.wasp.util.httpComponent.response.interfaces.IHttpResponse;
import org.apache.log4j.Logger;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class GenericHttpServer extends ServerSocket {
    private static final Logger logger = Logger.getLogger(GenericHttpServer.class);
    private IProcess process;
    private boolean askClose;

    private GenericHttpServer(int port) throws IOException {
        super(port);
    }

    public GenericHttpServer(int port, IProcess process) throws IOException {
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

        HttpClient httpClient = new HttpClient(clientSocket);

        try {
            request = httpClient.getHttpRequest();
            logger.info("receive :\n" + request);
            response = process.run(request);
        } catch (IOException | MethodeTypeException e) { // BAD REQUEST
            logger.error(e.getMessage());
            response = new HttpResponseBuilder().protocol(HttpProtocolVersions.HTTP_1_1)
                    .status(EStatus.BAD_REQUEST)
                    .content(e.getMessage())
                    .build();
        }

        httpClient.sendHttpResponse(response);
        httpClient.shutdown();
    }

}

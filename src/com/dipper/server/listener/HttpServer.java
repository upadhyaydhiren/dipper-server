/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dipper.server.listener;

import java.net.ServerSocket;
import java.net.Socket;

/**
 *This is Http Server
 * @author Dhiren
 */
public class HttpServer extends Thread {

    private ServerSocket serverSocket;
    private final int port;
    private boolean running = false;

    public HttpServer(int port) {
        this.port = port;
    }

    /**
     * This is server start method
     */
    public void startServer() {
        try {
            serverSocket = new ServerSocket(port);
            this.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This is server stop method
     */
    public void stopServer() {
        running = false;
        this.interrupt();
    }

    @Override
    public void run() {
        running = true;
        while (running) {
            try {
                Socket socket = serverSocket.accept();
                APIListener aPIListener = new APIListener(socket);
                aPIListener.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}

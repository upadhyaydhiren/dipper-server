/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dipper.server.listener;

/**
 *This is tester class
 * @author Dhiren
 */
public class APITester {

    public static void main(String[] args) {
        try {

            HttpServer httpServer = new HttpServer(8080);
            httpServer.startServer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

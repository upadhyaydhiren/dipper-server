/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dipper.server.listener;

import com.dipper.server.requesthandler.HttpRequest;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * This is api Listener class
 *
 * @author Dhiren
 * @version
 */
public class APIListener extends Thread {

    private final Socket socket;

    public APIListener(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            BufferedWriter writer;
            try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
                writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                StringBuilder payload = new StringBuilder();
                while (bufferedReader.ready()) {
                    payload.append((char) bufferedReader.read());
                }
                String outPut = QueryListner.queryProcessing(payload.toString());
                writer.write(outPut);
                writer.flush();
                bufferedReader.close();
                writer.close();
            }
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This is static query Listener class
     */
    static class QueryListner {

        static Map<Long, HttpRequest> connectionPooling = new HashMap<>();

        /**
         * This is query processing method
         *
         * @param readData it takes @link{String} request input
         * @return Response String
         */
        public static String queryProcessing(String readData) {
            String requestType = readData.split("\n")[0];
            if (requestType.toLowerCase().contains("get")) {
                String queryString = requestType.split(" ")[1].toLowerCase();
                if (queryString.contains("?")) {
                    String queryParam = queryString.split("\\?")[1];
                    if (queryParam.contains("&") && queryParam.contains("=")) {
                        String queryParam1 = queryParam.split("&")[0].split("=")[1];
                        String queryParam2 = queryParam.split("&")[1].split("=")[1];
                        HttpRequest httpRequest = new HttpRequest(Long.valueOf(queryParam1), Long.valueOf(queryParam2));
                        connectionPooling.put(Long.valueOf(queryParam1), httpRequest);
                        return "{\"status\":\"ok\"}";
                    } else {
                        return "{\"msg\":\"Invalid query param\"}";
                    }
                } else if (!queryString.contains("?") && queryString.equals("/api/serverstatus")) {
                    String responseString = "{";
                    if (connectionPooling.size() > 1) {
                        responseString = connectionPooling.keySet().stream().filter((connectionId) -> (connectionPooling.get(connectionId).getTimeOut() > System.currentTimeMillis())).map((connectionId) -> "\"" + connectionId + "\":\"" + ((connectionPooling.get(connectionId).getTimeOut() - System.currentTimeMillis()) / 1000) + "\",").reduce(responseString, String::concat);
                    } else {
                        responseString = connectionPooling.keySet().stream().filter((connectionId) -> (connectionPooling.get(connectionId).getTimeOut() > System.currentTimeMillis())).map((connectionId) -> "\"" + connectionId + "\":\"" + ((connectionPooling.get(connectionId).getTimeOut() - System.currentTimeMillis()) / 1000) + "\"").reduce(responseString, String::concat);
                    }
                    if (connectionPooling.size() > 1) {
                        responseString = responseString.substring(0, responseString.length() - 2);
                        responseString+="\"";
                    }
                    responseString += "}";
                    return responseString;
                } else {
                    return "{\"msg\":\"Invalid query\"}";
                }
            }
            if (requestType.toLowerCase().contains("post")) {
                String queryString = requestType.split(" ")[1].toLowerCase();
                if (queryString.contains("?")) {
                    String queryParam = queryString.split("\\?")[1];
                    if (queryParam.contains("=")) {
                        String queryParam1 = queryParam.split("=")[1];
                        if (connectionPooling.containsKey(Long.valueOf(queryParam1))) {
                            connectionPooling.remove(Long.valueOf(queryParam1));
                            return "{\"status\":\"killed\"}";
                        } else {
                            return "{\"status\":\"invalid\",\"connection Id\":\"" + Long.valueOf(queryParam1) + "\"}";
                        }
                    } else {
                        return "{\"msg\":\"Invalid query param\"}";
                    }
                } else {
                    return "{\"msg\":\"Invalid request\"}";
                }
            } else {
                return "{\"msg\":\"Invalid request method\"}";
            }
        }
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dipper.server.requesthandler;

/**
 *This is Http Request class
 * @author Dhiren
 */
public class HttpRequest {

    private Long connectionId;
    private Long createdConnectiontime;
    private Long timeOut;

    public HttpRequest() {
    }

    public HttpRequest(Long connectionId, Long timeOut) {
        this.connectionId = connectionId;
        this.createdConnectiontime = System.currentTimeMillis();
        this.timeOut = (System.currentTimeMillis() + (timeOut * 1000));
    }

    public Long getConnectionId() {
        return connectionId;
    }

    public void setConnectionId(Long connectionId) {
        this.connectionId = connectionId;
    }

    public Long getCreatedConnectiontime() {
        return createdConnectiontime;
    }

    public void setCreatedConnectiontime(Long createdConnectiontime) {
        this.createdConnectiontime = createdConnectiontime;
    }

    public Long getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(Long timeOut) {
        this.timeOut = (System.currentTimeMillis() + (timeOut * 1000));
    }

}

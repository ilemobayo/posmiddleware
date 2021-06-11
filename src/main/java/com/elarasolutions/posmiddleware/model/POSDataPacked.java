package com.elarasolutions.posmiddleware.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class POSDataPacked {

    @SerializedName("host")
    @Expose
    private String host;

    @SerializedName("port")
    @Expose
    private int port;

    @SerializedName("ssl")
    @Expose
    private boolean ssl;

    @SerializedName("msg")
    @Expose
    private String msg;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public boolean isSsl() {
        return ssl;
    }

    public void setSsl(boolean ssl) {
        this.ssl = ssl;
    }
}
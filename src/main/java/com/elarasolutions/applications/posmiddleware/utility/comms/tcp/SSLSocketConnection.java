package com.elarasolutions.applications.posmiddleware.utility.comms.tcp;


import com.elarasolutions.applications.posmiddleware.utility.comms.tcp.exceptions.ConnectException;

public interface SSLSocketConnection {

    void init(String host, int port, int timeout, String cer); //throws Exception;

    void connect() throws ConnectException;

    void send(byte[] var1) throws ConnectException;

    byte[] receive() throws ConnectException;

    byte[] receiveWithHeader() throws Exception;

    void disconnect() throws ConnectException;
}

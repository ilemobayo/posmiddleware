package com.elarasolutions.posmiddleware.utility.comms.tcp;

import com.elarasolutions.posmiddleware.utility.comms.tcp.exceptions.ConnectException;

public interface SocketConnection {

    void init(String host, int port, int timeout) ;//throws Exception;

    void connect() throws ConnectException;

    void send(byte[] var1) throws ConnectException;

    byte[] receive() throws ConnectException;

    byte[] receiveWithHeader() throws Exception;

    void disconnect() throws ConnectException;

}

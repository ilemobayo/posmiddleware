package com.elarasolutions.applications.posmiddleware.utility.comms.tcp;


import com.elarasolutions.applications.posmiddleware.utility.comms.CommsListener;

public interface TcpComms {

    byte[] dataCommuBlocking(byte[] senddata) throws Exception;
    void dataCommu(byte[] senddata, CommsListener commsListener);


    public String getHost();

    public void setHost(String host);

    public int getPort();

    public void setPort(int port);

    public int getTimeout();

    public void setTimeout(int timeout);

    public boolean isIfSSL();

    public void setIfSSL(boolean ifSSL);

    public String getCer();

    public void setCer(String cer);

}

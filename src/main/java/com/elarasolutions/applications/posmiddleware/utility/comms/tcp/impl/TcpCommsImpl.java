package com.elarasolutions.applications.posmiddleware.utility.comms.tcp.impl;

import com.elarasolutions.applications.posmiddleware.utility.comms.CommsListener;
import com.elarasolutions.applications.posmiddleware.utility.comms.tcp.SSLSocketConnection;
import com.elarasolutions.applications.posmiddleware.utility.comms.tcp.SocketConnection;
import com.elarasolutions.applications.posmiddleware.utility.comms.tcp.TcpComms;
import com.elarasolutions.applications.posmiddleware.utility.comms.tcp.exceptions.ConnectException;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

public class TcpCommsImpl implements TcpComms {

    private static TcpCommsImpl instance = null;
    private CommsListener commsListener = null;
    private SocketConnection socket = null;
    private SSLSocketConnection sslSocket = null;
    private Thread threadComm = null;
    private byte[] resp = null;
    String host;
    int port;
    int timeout;
    private boolean ifSSL = false;
    private String cer = null;
    private boolean ifContinueRecv = false;
    private int finish = 3;
    private int dataLength = 2;

    public TcpCommsImpl(String host, int port, int timeout, boolean ifSSL, String cer) {
        this.host = host;
        this.port = port;
        this.timeout = timeout;
        this.ifSSL = ifSSL;
        this.cer = cer;
    }

    public synchronized byte[] dataCommuBlocking(byte[] senddata) throws Exception{
        if((this.host == null || this.host.isEmpty()) && this.port == 0 && this.timeout == 0) {
        } else {
            if(!this.ifSSL) {
                this.socket = new SocketConnectionImpl();
                try {
                    this.notifyStatus(0, null);
                    this.socket.init(host, port, timeout);
                } catch (Exception var6) {
                    var6.printStackTrace();
                    throw var6;
                }
                this.dataCommuSocketBlocking(senddata);
                return resp;
            } else {
                sslSocket = new SSLSocketConnectionImpl();
                try {
                    this.notifyStatus(0, null);
                    sslSocket.init(host, port, timeout, cer);
                } catch (Exception var5) {
                    var5.printStackTrace();
                    throw var5;
                }
                this.dataCommuSocketSSLBlocking(senddata);
                return resp;
            }
        }
        return null;
    }


    public synchronized void dataCommu(byte[] senddata, CommsListener commsListener) {
        if(commsListener == null && (this.host == null || this.host.isEmpty()) && this.port == 0 && this.timeout == 0) {
        } else {
            this.commsListener = commsListener;
            if(this.threadComm != null) {
                this.threadComm.isAlive();
            }
            if(!this.ifSSL) {
                this.socket = new SocketConnectionImpl();
                try {
                    this.notifyStatus(0, null);
                    this.socket.init(host, port, timeout);
                } catch (Exception var6) {
                    var6.printStackTrace();
                    this.notifyErrorCode(2, var6.getMessage());
                }
                this.dataCommuSocket(senddata);
            } else {
                sslSocket = new SSLSocketConnectionImpl();
                try {
                    this.notifyStatus(0, null);
                    sslSocket.init(host, port, timeout, cer);
                } catch (Exception var5) {
                    var5.printStackTrace();
                    this.notifyErrorCode(2, var5.getMessage());
                }
                this.dataCommuSocketSSL(senddata);
            }

        }
    }

    private void dataCommuSocketBlocking(final byte[] senddata) throws Exception{
//        Log.e("TCPComms", "" + GlobalData.getInstance().getEnv() + "\t SSL: "+this.isIfSSL());
        try {
            notifyStatus(1, null);
            socket.connect();
            notifyStatus(2, null);
            socket.send(senddata);
            notifyStatus(3, null);
//            if (true)
                recv();
//            else
//                recvWithHeader();
        } catch (ConnectException var10) {
            var10.printStackTrace();
            throw var10;
        } finally {
            try {
                socket.disconnect();
            } catch (ConnectException var9) {
                var9.printStackTrace();
                throw var9;
            }
        }
    }

    private void dataCommuSocket(final byte[] senddata) {
//        Log.e("TCPComms", "" + GlobalData.getInstance().getEnv() + "\t SSL: "+this.isIfSSL());
        this.threadComm = new Thread(new Runnable() {
            public void run() {
                try {
                    notifyStatus(1, null);
                    socket.connect();
                    notifyStatus(2, null);
                    socket.send(senddata);
                    notifyStatus(3, null);
//                    if (true)
                        recv();
//                    else
//                        recvWithHeader();
                } catch (ConnectException var10) {
                    var10.printStackTrace();
                    notifyErrorCode(var10.getErrorCode(), var10.getMessage());
                } catch (Exception var10) {
                    var10.printStackTrace();
                    notifyErrorCode(3, var10.getMessage());
                } finally {
                    try {
                        socket.disconnect();
                    } catch (ConnectException var9) {
                        var9.printStackTrace();
                        notifyErrorCode(var9.getErrorCode(), var9.getMessage());
                    }
                }
            }
        });
        this.threadComm.start();
    }

    private void recv() throws ConnectException {
        System.out.println("TcpComms: Response without header returned");
        this.resp = this.socket.receive();
        this.notifyStatus(4, this.resp);
        if(this.ifContinueRecv) {
            this.ifContinueRecv = false;
            this.recv();
        }

    }

    private void recvWithHeader() throws Exception {
        System.out.println("TcpComms: Response with header returned");
        byte[] body = this.socket.receiveWithHeader();
        resp = body;
        notifyStatus(4, body);
    }

    private void dataCommuSocketSSLBlocking(final byte[] senddata) throws Exception{
//        Log.e("TCPComms", "" + GlobalData.getInstance().getEnv() + "\t SSL: "+this.isIfSSL());
        try {
            sslSocket.connect();
            sslSocket.send(senddata);
//            if (true)
                recvSSL();
//            else
//                recvSSLWithHeader();
        } catch (ConnectException var10) {
            var10.printStackTrace();
            this.commsListener.OnError(1, "Timeout");
            throw var10;
        } finally {
            try {
                sslSocket.disconnect();
            } catch (ConnectException var9) {
                var9.printStackTrace();
                throw var9;
            }
        }
    }


    private void dataCommuSocketSSL(final byte[] senddata) {
//        Log.e("TCPComms", "" + GlobalData.getInstance().getEnv() + "\t SSL: "+this.isIfSSL());
        this.threadComm = new Thread(() -> {
            try {
                online().doOnSuccess(online -> {
                    if (online) {
                        new Thread(() -> {
                            try {
                                notifyStatus(1, null);
                                sslSocket.connect();
                                notifyStatus(2, null);
                                sslSocket.send(senddata);
                                notifyStatus(3, null);
                                ifContinueRecv = true;
                                finish = 3;
//                                if (true)
                                    recvSSL();
//                                else
//                                    recvSSLWithHeader();
                            } catch (ConnectException var10) {
                                var10.printStackTrace();
                                TcpCommsImpl.this.notifyErrorCode(var10.getErrorCode(), var10.getMessage());
                            } catch (Exception e) {
                                e.printStackTrace();
                                TcpCommsImpl.this.notifyErrorCode(2, e.getMessage());
                            }
                        }).start();
                    } else {
                        TcpCommsImpl.this.notifyErrorCode(2, "Poor Internet Connection");
                    }
                })
                        .doOnError(e -> {
                            throw new Exception(e);
                        }).subscribe();
            } catch (Exception var10) {
                var10.printStackTrace();
                TcpCommsImpl.this.notifyErrorCode(2, var10.getMessage());
            } finally {
                try {
                    sslSocket.disconnect();
                } catch (ConnectException var9) {
                    var9.printStackTrace();
                    notifyErrorCode(var9.getErrorCode(), var9.getMessage());
                }
            }
        });
        this.threadComm.start();
    }

    private void recvSSL() throws ConnectException {
        System.out.println("TcpComms: Response without header returned");
        resp = this.sslSocket.receive();
        notifyStatus(4, this.resp);
        if(this.ifContinueRecv) {
            this.ifContinueRecv = false;
            recvSSL();
        }
    }

    private void recvSSLWithHeader() throws Exception {
        System.out.println("TcpComms: Response with header returned");
        byte[] body = this.sslSocket.receiveWithHeader();
        resp = body;
        notifyStatus(4, body);
    }

    private synchronized void notifyStatus(int code, byte[] data) {
        if(this.commsListener != null) {
            this.commsListener.OnStatus(code, data);
        }

    }

    private synchronized void notifyErrorCode(int code, String msg) {
        if(this.commsListener != null) {
            this.commsListener.OnError(code, msg);
        }

    }

    public void forceStopThread() {
        if(this.threadComm != null && this.threadComm.isAlive()) {
            this.threadComm.stop();
            this.threadComm = null;
        }

    }

    public void continueRecv() {
        this.ifContinueRecv = true;
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

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public boolean isIfSSL() {
        return ifSSL;
    }

    public void setIfSSL(boolean ifSSL) {
        this.ifSSL = ifSSL;
    }

    public String getCer() {
        return cer;
    }

    public void setCer(String cer) {
        this.cer = cer;
    }

    private Single<Boolean> online() {
        return Single.fromCallable(this::callable)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread());
    }

    private boolean callable() {
        try {
            int timeout = 1500;
            Socket socket = new Socket();
            InetSocketAddress socketAddress = new InetSocketAddress("8.8.8.8", 53);

            socket.connect(socketAddress, timeout);
            socket.close();
            System.out.println("Connectivity: Connected");
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Connectivity: Not Connected");
            return false;
        }
    }
}

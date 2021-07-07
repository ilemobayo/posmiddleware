package com.elarasolutions.applications.posmiddleware.utility.comms.tcp.impl;

import com.elarasolutions.applications.posmiddleware.utility.comms.tcp.SSLSocketConnection;
import com.elarasolutions.applications.posmiddleware.utility.comms.tcp.exceptions.ConnectException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.Timer;
import java.util.TimerTask;


public class SSLSocketConnectionImpl implements SSLSocketConnection {

    private static final byte HEAD_BYTES = 2;
    private SSLSocket socket;
    private String ip;
    private int port;
    private int timeout;
    private String cer;
    private Timer timer = null;

    public SSLSocketConnectionImpl() {
    }

    public void init(String host, int port, int timeout, String cer) {
        //this.mParams = new CommuParams();
        //LogUtil.si(SSLSocketConnection.class, "Gets the data initialization communication parameter from XML.");

        //this.mParams = params;
        //LogUtil.si(SSLSocketConnection.class, "The communication parameters are initialized directly from the parameter structure.");
        //LogUtil.si(SSLSocketConnection.class, "setCommuParams : type = " + params.getType() + " ip = " + params.getIp() + " port = " + params.getPort() + " timeout = " + params.getTimeout());
        this.ip = host;
        this.port = port;
        this.timeout = timeout;
        this.cer = cer;
        if(this.cer != null && this.cer.startsWith("null")) {
            this.cer = null;
        }

    }

    public void connect() throws ConnectException {
        //LogUtil.si(SSLSocketConnection.class, "-----connect-----");

        try {
            SSLSocketFactory e = null;
            if(this.cer == null) {
                //LogUtil.si(SSLSocketConnection.class, "The certificate is null.");
                e = SSLContextBuild.trustAllHttpsCertificates().getSocketFactory();
            } else {
                //LogUtil.si(SSLSocketConnection.class, "The certificate is" + this.cer);
                SSLContext sslContext = null;
                if(this.cer.contains(".bks")) {
                    //LogUtil.si(SSLSocketConnection.class, "TLS");
                    sslContext = SSLContextBuild.getTLSContext(cer);
                } else {
                    //LogUtil.si(SSLSocketConnection.class, "SSL");
                    sslContext = SSLContextBuild.getSSLContext(cer);
                }

                if(sslContext == null) {
                    throw new ConnectException(1, "Connection error,The certificate is not set.");
                }

                e = sslContext.getSocketFactory();
            }

            this.socket = (SSLSocket)e.createSocket();
            //DebugHelper.d("SocketConnection", "------ <connect> [" + this.ip + " " + this.port + "] timeout(" + this.timeout + "s)------");
            this.socket.connect(new InetSocketAddress(this.ip, this.port), this.timeout * 1000);
            //DebugHelper.d("SocketConnection", "------ <connect> [" + this.ip + " " + this.port + "] success");
        } catch (IOException var3) {
            throw new ConnectException(1, "Connection error.");
        } catch (Exception var4) {
            var4.printStackTrace();
            throw new ConnectException(1, "Connection error.");
        }
    }

    public void send(byte[] data) throws ConnectException {
        //LogUtil.si(SSLSocketConnection.class, "-----send-----");
        try {
            OutputStream e = this.socket.getOutputStream();
            byte[] out;
            out = new byte[2 + data.length];
            System.arraycopy(new byte[]{(byte)(data.length >> 8 & 255), (byte)(data.length & 255)}, 0, out, 0, 2);
            System.arraycopy(data, 0, out, 2, data.length);
            this.startTimer(e);
            e.write(out);
            this.stopTimer();
            //DebugHelper.d("SocketConnection", "------ <send> " + out.length + "(bytes) ------");
            //DebugHelper.dumpHex("<send>", data);
        } catch (IOException var4) {
            throw new ConnectException(2, "Send an error.");
        }
    }

//    public byte[] receive() throws ConnectException {
//        //LogUtil.si(SSLSocketConnection.class, "-----receive-----");
//        try {
//            this.socket.setSoTimeout(this.timeout * 1000);
//            InputStream e = this.socket.getInputStream();
//            byte[] buffer;
//            long beginTime;
//            int len;
//            int total;
//            buffer = new byte[150];
//            beginTime = System.currentTimeMillis();
//
//            while(e.read(buffer, 0, 2) <= 0) {
//                if(this.isTimeout(beginTime, this.timeout * 1000)) {
//                    throw new ConnectException(4, "Receiving timeout.");
//                }
//
//                try {
//                    Thread.sleep(100L);
//                } catch (InterruptedException var8) {
//                    throw new ConnectException(5, "Receiving interrupt.");
//                }
//            }
//
//            //DebugHelper.dumpHex("<recv packet header>", buffer);
//            len = buffer[0] << 8 & '\uff00' | buffer[1] & 255;
//            //DebugHelper.d("SocketConnection", "------ <recv> " + len + "(bytes) to receive ------");
//            buffer = new byte[len];
//            total = e.read(buffer, 0, len);
//            //DebugHelper.d("SocketConnection", "------ <recv> receive " + total + "(bytes) ------");
//            //DebugHelper.dumpHex("<recv>", buffer);
//            return buffer;
//        } catch (IOException var10) {
//            throw new ConnectException(3, "Receiving IO error.");
//        }
//    }


    public byte[] receiveWithHeader() throws Exception {
        byte body[] = null;
        byte header[] = null;
        int i;

        DataInputStream receiverStream = null;
        this.socket.setSoTimeout(this.timeout * 1000);
        //InputStream e = this.socket.getInputStream();

        // read text from the socket
        try {
            while (true) {
                try {
                    header = new byte[2];
                    receiverStream = new DataInputStream(this.socket.getInputStream());
                    receiverStream.readFully(header);
                    int len = ((((int) header[0]) & 0xFF) << 8) | (((int) header[1]) & 0xFF);
                    body = new byte[len];
                    receiverStream.readFully(body);
                    /*System.out.println("HostConnection :: writeToAndReadFromSocketSSL () :: Remote Port::" + remotePort);
                    System.out.println("HostConnection :: writeToAndReadFromSocketSSL () :: Response Length::" + len);
                    System.out.println("HostConnection :: writeToAndReadFromSocketSSL () :: Response body::" + new String(body).toString());*/
                    if (body != null && body.length == len) {
                        break;
                    } else {
                        body = null;
                    }
                } catch (EOFException eof) {
                    eof.printStackTrace();
                    //receive();
                    break;
                } catch (IOException io) {
                    io.printStackTrace();
                }
            }
        } finally {
            try {
                receiverStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return body;
    }

    public byte[] receive() throws ConnectException {
        //LogUtil.si(SSLSocketConnection.class, "-----receive-----");
        try {
            this.socket.setSoTimeout(this.timeout * 1000);
            InputStream e = this.socket.getInputStream();
            byte[] buffer;
            long beginTime;
            int len;
            int total;
            buffer = new byte[150];
            beginTime = System.currentTimeMillis();

            while(e.read(buffer, 0, 2) <= 0) {
                if(this.isTimeout(beginTime, this.timeout * 1000)) {
                    throw new ConnectException(4, "Receiving timeout.");
                }

                try {
                    Thread.sleep(100L);
                } catch (InterruptedException var8) {
                    throw new ConnectException(5, "Receiving interrupt.");
                }
            }

            //DebugHelper.dumpHex("<recv packet header>", buffer);
            len = buffer[0] << 8 & '\uff00' | buffer[1] & 255;
            //DebugHelper.d("SocketConnection", "------ <recv> " + len + "(bytes) to receive ------");
            buffer = new byte[len];
            total = e.read(buffer, 0, len);
            //DebugHelper.d("SocketConnection", "------ <recv> receive " + total + "(bytes) ------");
            //DebugHelper.dumpHex("<recv>", buffer);
            return buffer;
        } catch (IOException var10) {
            throw new ConnectException(3, "Receiving IO error.");
        }
    }

    public void disconnect() throws ConnectException {
        //LogUtil.si(SSLSocketConnection.class, "-----disconnect-----");

        try {
            if(this.socket.isConnected()) {
                //DebugHelper.d("SocketConnection", "准备close套接字");
                this.socket.close();
            }

            //DebugHelper.d("SocketConnection", "关闭套接字成功");
            this.socket = null;
            //DebugHelper.d("SocketConnection", "------ <disconnect> [" + this.ip + " " + this.port + "] ------");
        } catch (Exception var2) {
            throw new ConnectException(6, "Closing the link error.");
        }
    }

    private boolean isTimeout(long beginTime, int timeout) {
        return System.currentTimeMillis() - beginTime > (long)timeout;
    }

    private void startTimer(final OutputStream os) {
        //LogUtil.si(SSLSocketConnection.class, "-----startTimer-----");
        if(this.timer == null) {
            this.timer = new Timer();
            //LogUtil.si(SSLSocketConnection.class, "-----timer非null-----");
            this.timer.schedule(new TimerTask() {
                public void run() {
                    try {
                        os.close();
                    } catch (IOException var2) {
                        var2.printStackTrace();
                    }

                    SSLSocketConnectionImpl.this.stopTimer();
                }
            }, 5000L);
        }

    }

    private void stopTimer() {
        //LogUtil.si(SSLSocketConnection.class, "-----stopTimer-----");
        if(this.timer != null) {
            //LogUtil.si(SSLSocketConnection.class, "-----The timer not null-----");
            this.timer.cancel();
            this.timer = null;
        }

    }
}

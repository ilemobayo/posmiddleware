package com.elarasolutions.posmiddleware.utility.comms.tcp.impl;

import com.elarasolutions.posmiddleware.utility.comms.tcp.exceptions.ConnectException;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.net.ssl.SSLSocketFactory;

public class Client
{ 
    // initialize socket and input output streams 
    private Socket socket            = null;
    private DataInputStream input   = null;
    private DataOutputStream out     = null;

    public Client(){}
  
    // constructor to put ip address and port 
    public void startConnection()
    { 
        // establish a connection 
        try
        {
            SSLSocketFactory e = SSLContextBuild.trustAllHttpsCertificates().getSocketFactory();
            this.socket = e.createSocket();
            this.socket.connect(new InetSocketAddress("196.6.103.18", 5002), 6 * 1000);
            System.out.println("Connected"); 
  
//            // takes input from terminal
//            this.input  = new DataInputStream(System.in);
//
//            // sends output to the socket
//            this.out    = new DataOutputStream(this.socket.getOutputStream());
        } 
        catch(UnknownHostException u)
        { 
            System.out.println(u); 
        } 
        catch(IOException i)
        { 
            System.out.println(i); 
        } catch (Exception e) {
            e.printStackTrace();
        }

        // string to read message from input 
//        String line = "";
//
//        // keep reading until "Over" is input
//        while (!line.equals("Over"))
//        {
//            try
//            {
//                line = this.input.readLine();
//                this.out.writeUTF(line);
//            }
//            catch(IOException i)
//            {
//                System.out.println(i);
//            }
//        }
  
        // close the connection 
//        try
//        {
//            this.input.close();
//            this.out.close();
//            this.socket.close();
//        }
//        catch(IOException i)
//        {
//            System.out.println(i);
//        }
    }

    public void send(byte[] data) throws ConnectException {
        //LogUtil.si(SSLSocketConnection.class, "-----send-----");

        try {
            OutputStream e = this.socket.getOutputStream();
            byte[] out;
            out = new byte[2 + data.length];
            System.arraycopy(new byte[]{(byte)(data.length >> 8 & 255), (byte)(data.length & 255)}, 0, out, 0, 2);
            System.arraycopy(data, 0, out, 2, data.length);
            e.write(out);

            receive();
        } catch (IOException var4) {
            throw new ConnectException(2, "Send an error.");
        }
    }

    public byte[] receive() throws ConnectException {
        //LogUtil.si(SSLSocketConnection.class, "-----receive-----");
        try {
            this.socket.setSoTimeout(6 * 1000);
            InputStream e = this.socket.getInputStream();
            byte[] buffer;
            long beginTime;
            int len;
            int total;
            buffer = new byte[150];
            beginTime = System.currentTimeMillis();

            while(e.read(buffer, 0, 2) <= 0) {
                if(this.isTimeout(beginTime, 6 * 1000)) {
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
//            System.out.println("SocketConnection ------ <recv> " + len + "(bytes) to receive ------");
            buffer = new byte[len];
            total = e.read(buffer, 0, len);
//            System.out.println("SocketConnection ------ <recv> receive " + total + "(bytes) ------");
            //DebugHelper.dumpHex("<recv>", buffer);
            return buffer;
        } catch (IOException var10) {
            throw new ConnectException(3, "Receiving IO error.");
        }
    }

    private boolean isTimeout(long beginTime, int timeout) {
        return System.currentTimeMillis() - beginTime > (long)timeout;
    }
}
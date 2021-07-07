package com.elarasolutions.applications.posmiddleware.utility.comms;

import com.elarasolutions.applications.posmiddleware.utility.Utils;
import com.solab.iso8583.IsoMessage;

import javax.net.ssl.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;

public class Transaction {

    public static IsoMessage sendRequest(String host, int port, boolean ssl, byte[] data) throws Exception{

        DataInputStream dataInputStream = null;
        DataOutputStream dataOutputStream = null;

        if(ssl){
            System.setProperty("javax.net.debug", "none");

            TrustManager tm = new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }
                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }
                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            };
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, new TrustManager[] { tm }, null);
            SSLSocketFactory ssf = sc.getSocketFactory();
            SSLSocket socket = (SSLSocket) ssf.createSocket(host, port);
            socket.startHandshake();
            socket.setSoTimeout(60*1000);
            dataInputStream = new DataInputStream(socket.getInputStream());
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
        }else{
            Socket socket = new Socket(host, port);
            socket.setSoTimeout(60*1000);
            socket.setKeepAlive(true);
            socket.setReuseAddress(true);
            dataInputStream = new DataInputStream(socket.getInputStream());
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
        }

        int reqLen = data.length;
        byte[] bLenght = new byte[2];
        bLenght[1] = (byte) reqLen; // (lenght & 0xFF);
        bLenght[0] = (byte) (reqLen >> 8); // ((lenght >> 8) & 0xFF);
        byte[] toSend = concat(bLenght, data);

        dataOutputStream.write(toSend, 0, toSend.length);
        dataOutputStream.flush();

        byte[] header = new byte[2];
        dataInputStream.readFully(header, 0, header.length);
        int respLen = (int) (((((int)header[0]) & 0xFF) << 8) | (((int)header[1]) & 0xFF));
        byte[] resp = new byte[respLen];
        dataInputStream.readFully(resp, 0, respLen);

        System.out.println("Response form server: " + Arrays.toString(resp));
        IsoMessage isoMessageResponse = new Utils().decode(resp);
        System.out.println("Response: \n" + isoMessageResponse.debugString());
        return isoMessageResponse;
    }

    private static byte [] concat(byte[] A, byte[] B) {
        int aLen = A.length;
        int bLen = B.length;
        byte[] C= new byte[aLen+bLen];
        System.arraycopy(A, 0, C, 0, aLen);
        System.arraycopy(B, 0, C, aLen, bLen);
        return C;
    }
}

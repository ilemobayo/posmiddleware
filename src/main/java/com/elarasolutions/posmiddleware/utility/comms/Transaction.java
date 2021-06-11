package com.elarasolutions.posmiddleware.utility.comms;

import com.elarasolutions.posmiddleware.utility.Utils;
import com.solab.iso8583.IsoMessage;

import javax.net.ssl.*;
import java.io.*;
import java.net.Socket;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;

public class Transaction {

    public static IsoMessage sendRequest(String host, int port, boolean ssl, byte[] data) throws Exception{

        DataInputStream dataInputStream = null;
        DataOutputStream dataOutputStream = null;

        if(ssl){
            /*
            String certfile = "/Users/lekanomotayo/projects/phoenix/comodo.crt";
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            FileInputStream fis = new FileInputStream(certfile);
            DataInputStream dis = new DataInputStream(fis);
            byte[] bytes = new byte[dis.available()];
            dis.readFully(bytes);
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
            InputStream certstream = bais;
            Certificate certs =  cf.generateCertificate(certstream);
            String keystoreFilename = "/Users/lekanomotayo/projects/phoenix/keystore.jks";
            String keystorePwd = "password";
            createKeystoreWithCert(keystoreFilename, keystorePwd, "nibss", certs);
            //keystoreFilename = "/Library/Java/JavaVirtualMachines/jdk1.8.0_121.jdk/Contents/Home/jre/lib/security/cacerts";
            //keystorePwd = "changeit";
            System.setProperty("javax.net.ssl.trustStore", keystoreFilename);
            System.setProperty("javax.net.ssl.trustStorePassword", keystorePwd);
            System.setProperty("javax.net.ssl.keyStore", keystoreFilename);
            System.setProperty("javax.net.ssl.keyStorePassword", keystorePwd);
            System.setProperty("jdk.tls.disabledAlgorithms", "SSLv3, DHE");
            */

            System.setProperty("javax.net.debug", "all");

            /*
            X509TrustManager tm = new X509TrustManager() {
                public void checkClientTrusted(X509Certificate[] xcs, String string) throws CertificateException {
                }
                public void checkServerTrusted(X509Certificate[] xcs, String string) throws CertificateException {
                }
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            };
            SSLContext ctx = SSLContext.getInstance("TLS");
            ctx.init(null, new TrustManager[]{tm}, null);
            */
            /*
            SSLSocketFactory factory= (SSLSocketFactory) SSLSocketFactory.getDefault();
            SSLSocket socket = (SSLSocket) factory.createSocket(host,port);
            dataInputStream = new DataInputStream(socket.getInputStream());
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
            */

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

        //int lenght = rawMsg.length;
        int reqLen = data.length;byte[] bLenght = new byte[2];
        bLenght[1] = (byte) reqLen; // (lenght & 0xFF);
        bLenght[0] = (byte) (reqLen >> 8); // ((lenght >> 8) & 0xFF);
        byte[] toSend = concat(bLenght, data);
        /*
        int headerreq = reqLen >> 8;
        dataOutputStream.write(headerreq);
        dataOutputStream.write(reqLen);
        */

        //dataOutputStream.write(toSend);
        dataOutputStream.write(toSend, 0, toSend.length);
        dataOutputStream.flush();

        byte[] header = new byte[2];
        dataInputStream.readFully(header, 0, header.length);
        int respLen = (int) (((((int)header[0]) & 0xFF) << 8) | (((int)header[1]) & 0xFF));
        byte[] resp = new byte[respLen];
        dataInputStream.readFully(resp, 0, respLen);


        /*
        byte [] buffer  = new byte[1024];
        int count = 0;
        while( dataInputStream.available() > 0)
        {
            buffer [count++] = dataInputStream.readByte();
            if(count>= buffer.length-1){
                resize(buffer);
            }
        }
        byte [] returnbuffer =  new byte[count-2];
        System.arraycopy(buffer,2,returnbuffer,0,count-2);
        int available = dataInputStream.available();
        if (available > 0) {
            int read = dataInputStream.read(buffer);
        }
        */

        System.out.println("Response form server: " + Arrays.toString(resp));
        IsoMessage isoMessageResponse = new Utils().decode(resp);
        System.out.println("Response: \n" + isoMessageResponse.debugString());
        return isoMessageResponse;
    }

    private static void createKeystoreWithCert(String filename, String pwd, String alias, Certificate certs) {

        try{
            KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
            char[] password = pwd.toCharArray();
            //ks.load(null, password);
            ks.load(null, null);
            FileOutputStream fos = new FileOutputStream(filename);
            ks.store(fos, password);
            fos.close();

            ks.setCertificateEntry(alias, certs);
            FileOutputStream out = new FileOutputStream(filename);
            ks.store(out, password);
            out.close();
        }
        catch(Exception ex){

        }
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

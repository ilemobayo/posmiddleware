package com.elarasolutions.posmiddleware.utility;

import com.elarasolutions.posmiddleware.model.POSData;
import com.elarasolutions.posmiddleware.model.POSDataPacked;
import com.elarasolutions.posmiddleware.utility.comms.Transaction;
import com.elarasolutions.posmiddleware.utility.comms.tcp.TcpComms;
import com.elarasolutions.posmiddleware.utility.comms.tcp.impl.TcpCommsImpl;
import com.solab.iso8583.IsoMessage;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class ISODataUtils {

    private byte[] createRequest(POSData data) throws Exception{
        IsoMessage message = new Utils().createIsoRequest(data);
        return message.writeData();
    }

    public POSData sendIsoRequest(POSData data) throws Exception {
        String host = "arca-pos.qa.arca-payments.network";
        int port = 11000;
        //NIBSS
//        host = "196.6.103.72";
//        port = 5042;
        // nc -vz arca-pos.qa.arca-payments.network 11000
        // nc -vz 196.6.103.72 5042
        byte[] dataBytes = createRequest(data);
        //sendPingRequest(host);
        TcpComms comms = new TcpCommsImpl(host, port, 60, false, null);
        byte[] receiveData = comms.dataCommuBlocking(dataBytes);
        IsoMessage isoResponseMessage;
        isoResponseMessage = new Utils().decode(receiveData);
        return buildISOJsonData(isoResponseMessage);
    }

    public POSData sendIsoRequest(POSDataPacked data) throws Exception {
        String host = "arca-pos.qa.arca-payments.network";
        int port = 11000;
        //NIBSS
//        host = "196.6.103.72";
//        port = 5042;
        byte[] dataBytes = data.getMsg().getBytes();
        IsoMessage isoResponseMessage;
        try {
            //sendPingRequest(host);
            isoResponseMessage = Transaction.sendRequest(host, port, true, dataBytes);
            return buildISOJsonData(isoResponseMessage);
        } catch (Exception ex){
            ex.printStackTrace();
            return new POSData();
        }
    }

    // Sends ping request to a provided IP address
    public static void sendPingRequest(String ipAddress)
            throws UnknownHostException, IOException
    {
        InetAddress geek = InetAddress.getByName(ipAddress);
        System.out.println("Sending Ping Request to " + ipAddress);
        if (geek.isReachable(5000))
            System.out.println("Host is reachable");
        else
            System.out.println("Sorry ! We can't reach to this host");
    }

    private POSData buildISOJsonData(IsoMessage message){
        POSData posData = new POSData();
        for (int i = 0; i <= 128; i++) {
            if (message.hasField(i)) {
                if (i == 2) {
                    posData.setField2(message.getField(i).toString());
                } else if(i == 3) {
                    posData.setField3(message.getField(i).toString());
                } else if(i == 4) {
                    posData.setField4(message.getField(i).toString());
                } else if(i == 7) {
                    posData.setField7(message.getField(i).toString());
                } else if(i == 11) {
                    posData.setField11(message.getField(i).toString());
                } else if(i == 12) {
                    posData.setField12(message.getField(i).toString());
                } else if(i == 13) {
                    posData.setField13(message.getField(i).toString());
                } else if(i == 14) {
                    posData.setField14(message.getField(i).toString());
                } else if(i == 18) {
                    posData.setField18(message.getField(i).toString());
                } else if(i == 22) {
                    posData.setField22(message.getField(i).toString());
                } else if(i == 23) {
                    posData.setField23(message.getField(i).toString());
                } else if(i == 25) {
                    posData.setField25(message.getField(i).toString());
                } else if(i == 26) {
                    posData.setField26(message.getField(i).toString());
                } else if(i == 28) {
                    posData.setField28(message.getField(i).toString());
                } else if(i == 32) {
                    posData.setField32(message.getField(i).toString());
                } else if(i == 33) {
                    posData.setField33(message.getField(i).toString());
                } else if(i == 35) {
                    posData.setField35(message.getField(i).toString());
                } else if(i == 37) {
                    posData.setField37(message.getField(i).toString());
                } else if(i == 39) {
                    posData.setField39(message.getField(i).toString());
                } else if(i == 40) {
                    posData.setField40(message.getField(i).toString());
                } else if(i == 41) {
                    posData.setField41(message.getField(i).toString());
                } else if(i == 42) {
                    posData.setField42(message.getField(i).toString());
                } else if(i == 43) {
                    posData.setField43(message.getField(i).toString());
                } else if(i == 49) {
                    posData.setField49(message.getField(i).toString());
                } else if(i == 53) {
                    posData.setField53(message.getField(i).toString());
                } else if(i == 55) {
                    posData.setField55(message.getField(i).toString());
                } else if(i == 63) {
                    posData.setField63(message.getField(i).toString());
                } else if(i == 64) {
                    posData.setField64(message.getField(i).toString());
                } else if(i == 123) {
                    posData.setField123(message.getField(i).toString());
                } else if(i == 128) {
                    posData.setField128(message.getField(i).toString());
                }
            }
        }
        return posData;
    }

}

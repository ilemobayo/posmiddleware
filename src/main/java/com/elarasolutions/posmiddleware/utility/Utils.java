package com.elarasolutions.posmiddleware.utility;

import com.elarasolutions.posmiddleware.PosmiddlewareApplication;
import com.elarasolutions.posmiddleware.model.POSData;
import com.solab.iso8583.IsoMessage;
import com.solab.iso8583.MessageFactory;
import com.solab.iso8583.parse.ConfigParser;
import com.solab.iso8583.util.HexCodec;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class Utils {

    MessageFactory<IsoMessage> messageFactory;

    public Utils(){
        try {
            messageFactory = getIsoMessageFactory();
            System.out.println("Utils: messageFactory initialization success");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Utils: messageFactory initialization failed");
        }
    }

    private String loadPackagerFile() throws IOException {
        File resource = new ClassPathResource("packager/NCS_PACKAGER.xml").getFile();
        return new String(Files.readAllBytes(resource.toPath()));
    }

    public static InputStream getFile(String filename)throws IOException{
        return PosmiddlewareApplication.class.getClassLoader().getResource(filename).openStream();
    }

    private MessageFactory<IsoMessage> getIsoMessageFactory() throws IOException {
        StringReader stringReader = new StringReader(loadPackagerFile());
        MessageFactory<IsoMessage> messageFactory;
        messageFactory = ConfigParser.createFromReader(stringReader);
        messageFactory.setUseBinaryBitmap(false); //NIBSS usebinarybitmap = false
        messageFactory.setUseBinaryMessages(false);
        messageFactory.setForceSecondaryBitmap(false);
        messageFactory.setForceStringEncoding(true);
        //messageFactory.setCharacterEncoding(StandardCharsets.US_ASCII.name());
        //messageFactory.setEtx(-1);
        messageFactory.setIgnoreLastMissingField(false);
        messageFactory.setCharacterEncoding(StandardCharsets.UTF_8.name());
        return messageFactory;
    }

    /**
     * Convert POSData object into ISO8583 data
     * @param data POSData object
     * @return IsoMessage Object
     * @throws Exception
     */
    public IsoMessage createIsoRequest(POSData data) throws Exception {
        int type = Integer.parseInt(data.getMsgType(), 16);
        IsoMessage message = messageFactory.newMessage(type);
        IsoMessage templ = messageFactory.getMessageTemplate(type);
        if (!StringUtil.isEmpty(data.getField2())) {
            message.setValue(2, data.getField2(), templ.getField(2).getType(), data.getField2().length());
        }
        if (!StringUtil.isEmpty(data.getField3())) {
            message.setValue(3, data.getField3(), templ.getField(3).getType(), templ.getField(3).getLength());
        }
        if (!StringUtil.isEmpty(data.getField4())) {
            message.setValue(4, data.getField4(), templ.getField(4).getType(), templ.getField(4).getLength());
        }
        if (!StringUtil.isEmpty(data.getField7())) {
            message.setValue(7, data.getField7(), templ.getField(7).getType(), templ.getField(7).getLength());
        }
        if (!StringUtil.isEmpty(data.getField11())) {
            message.setValue(11, data.getField11(), templ.getField(11).getType(), templ.getField(11).getLength());
        }
        if (!StringUtil.isEmpty(data.getField12())) {
            message.setValue(12, data.getField12(), templ.getField(12).getType(), templ.getField(12).getLength());
        }
        if (!StringUtil.isEmpty(data.getField11())) {
            message.setValue(13, data.getField13(), templ.getField(13).getType(), templ.getField(13).getLength());
        }
        if (!StringUtil.isEmpty(data.getField14())) {
            message.setValue(14, data.getField14(), templ.getField(14).getType(), templ.getField(14).getLength());
        }
        if (!StringUtil.isEmpty(data.getField18())) {
            message.setValue(18, data.getField18(), templ.getField(18).getType(), templ.getField(18).getLength());
        }
        if (!StringUtil.isEmpty(data.getField22())) {
            message.setValue(22, data.getField22(), templ.getField(22).getType(), templ.getField(22).getLength());
        }
        if (!StringUtil.isEmpty(data.getField23())) {
            message.setValue(23, data.getField23(), templ.getField(23).getType(), templ.getField(23).getLength());
        }
        if (!StringUtil.isEmpty(data.getField25())) {
            message.setValue(25, data.getField25(), templ.getField(25).getType(), templ.getField(25).getLength());
        }
        if (!StringUtil.isEmpty(data.getField26())) {
            message.setValue(26, data.getField26(), templ.getField(26).getType(), templ.getField(26).getLength());
        }
        if (!StringUtil.isEmpty(data.getField28())) {
            message.setValue(28, data.getField28(), templ.getField(28).getType(), templ.getField(28).getLength());
        }
        if (!StringUtil.isEmpty(data.getField32())) {
            message.setValue(32, data.getField32(), templ.getField(32).getType(), data.getField32().length());
        }
        //message.setValue(33, fwdInstId, templ.getField(33).getType(), fwdInstId.getLength());
        if (!StringUtil.isEmpty(data.getField35())) {
            message.setValue(35, data.getField35(), templ.getField(35).getType(), data.getField35().length());
        }
        if (!StringUtil.isEmpty(data.getField37())) {
            message.setValue(37, data.getField37(), templ.getField(37).getType(), templ.getField(37).getLength());
        }
        // Set for Refund
        if (!StringUtil.isEmpty(data.getField38())) {
            message.setValue(38, data.getField38(), templ.getField(38).getType(), templ.getField(38).getLength());
        } else {
            message.removeFields(38);
            System.out.println("Msg Type: F38 is empty");
        }
        if (!StringUtil.isEmpty(data.getField39())) {
            message.setValue(39, data.getField39(), templ.getField(39).getType(), templ.getField(39).getLength());
        }
        if (!StringUtil.isEmpty(data.getField40())) {
            message.setValue(40, data.getField40(), templ.getField(40).getType(), templ.getField(40).getLength());
        }
        if (!StringUtil.isEmpty(data.getField41())) {
            message.setValue(41, data.getField41(), templ.getField(41).getType(), templ.getField(41).getLength());
        }
        if (!StringUtil.isEmpty(data.getField42())) {
            message.setValue(42, data.getField42(), templ.getField(42).getType(), templ.getField(42).getLength());
        }
        if (!StringUtil.isEmpty(data.getField43())) {
            message.setValue(43, data.getField43(), templ.getField(43).getType(), templ.getField(43).getLength());
        }
        if (!StringUtil.isEmpty(data.getField49())) {
            message.setValue(49, data.getField49(), templ.getField(49).getType(), templ.getField(49).getLength());
        }
        if (!StringUtil.isEmpty(data.getField52())) {
            message.setValue(52, data.getField52(), templ.getField(52).getType(), templ.getField(52).getLength());
        } else {
            message.removeFields(52);
            System.out.println("Msg Type: F52 is empty");
        }
        if (!StringUtil.isEmpty(data.getField53())) {
            message.setValue(53, data.getField53(), templ.getField(53).getType(), templ.getField(53).getLength());
        } else {
            message.removeFields(53);
            System.out.println("Msg Type: F53 is empty");
        }
        if (!StringUtil.isEmpty(data.getField54())) {
            message.setValue(54, data.getField54(), templ.getField(54).getType(), templ.getField(54).getLength());
        } else {
            message.removeFields(54);
            System.out.println("Msg Type: F54 is empty");
        }
        if (!StringUtil.isEmpty(data.getField55())) {
            message.setValue(55, data.getField55(), templ.getField(55).getType(), data.getField55().length());
        }
        if (!StringUtil.isEmpty(data.getField56())) {
            message.setValue(56, data.getField56(), templ.getField(56).getType(), templ.getField(56).getLength());
        } else {
            message.removeFields(56);
            System.out.println("Msg Type: F56 is empty");
        }
        if (!StringUtil.isEmpty(data.getField59())) {
            message.setValue(59, data.getField59(), templ.getField(59).getType(), templ.getField(59).getLength());
        }
        if (!StringUtil.isEmpty(data.getField60())) {
            message.setValue(60, data.getField60(), templ.getField(60).getType(), templ.getField(60).getLength());
        } else {
            message.removeFields(60);
            System.out.println("Msg Type: F60 is empty");
        }
        if (!StringUtil.isEmpty(data.getField62())) {
            message.setValue(62, data.getField62(), templ.getField(62).getType(), templ.getField(62).getLength());
        } else {
            message.removeFields(62);
            System.out.println("Msg Type: F62 is empty");
        }
        if (!StringUtil.isEmpty(data.getField63())) {
            message.setValue(63, data.getField63(), templ.getField(63).getType(), templ.getField(63).getLength());
        } else {
            message.removeFields(63);
            System.out.println("Msg Type: F63 is empty");
        }
        if (!StringUtil.isEmpty(data.getField64())) {
            message.setValue(64, data.getField64(), templ.getField(64).getType(), templ.getField(64).getLength());
        } else {
            message.removeFields(64);
            System.out.println("Msg Type: F64 is empty");
        }
        if (!StringUtil.isEmpty(data.getField90())) {
            message.setValue(90, data.getField90(), templ.getField(90).getType(), templ.getField(90).getLength());
        }
        if (!StringUtil.isEmpty(data.getField95())) {
            message.setValue(95, data.getField95(), templ.getField(95).getType(), templ.getField(95).getLength());
        }
        if (!StringUtil.isEmpty(data.getField100())) {
            message.setValue(100, data.getField100(), templ.getField(100).getType(), templ.getField(100).getLength());
        } else {
            message.removeFields(100);
            System.out.println("Msg Type: F100 is empty");
        }
        if (!StringUtil.isEmpty(data.getField123())) {
            message.setValue(123, data.getField123(), templ.getField(123).getType(), templ.getField(123).getLength());
        }
        if (!StringUtil.isEmpty(data.getField124())) {
            message.setValue(124, data.getField124(), templ.getField(124).getType(), templ.getField(124).getLength());
        } else {
            message.removeFields(124);
            System.out.println("Msg Type: F124 is empty");
        }
        if (!StringUtil.isEmpty(data.getField128())) {
            message.setValue(128, data.getField128(), templ.getField(128).getType(), templ.getField(128).getLength());
        }
        System.out.println("Debugged Message: "+message.debugString());
        dumpIsoMessages(message, true);
        return message;
    }

    /**
     * Helps decode byte array of data into IsoMessage
     * @param data byte array representation of Iso8583
     * @return IsoMessage
     */
    public IsoMessage decode(byte[] data) {
        byte[] testbyte = data;
        List arrList = new ArrayList<>();
        int j = 0;
        for(byte b:testbyte){
            System.out.println("[" + j++ + ", " + b + "]");
        }
        try{
            IsoMessage isoMessage = messageFactory.parseMessage(data, 0);
            dumpIsoMessages(isoMessage, false);
            return isoMessage;
        } catch(Exception ex){
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * Helps in debugging both request and response
     * @param message an IsoMessage 8583 Data
     * @param isRequest set if the message is a request or response
     */
    private static void dumpIsoMessages(IsoMessage message, boolean isRequest) {
        StringBuilder isoString = new StringBuilder();
        isoString.append("============================================\n");
        isoString.append("      ISO DATA DUMP\n");
        isoString.append("============================================\n");
        try {
            for (int i = 0; i <= 128; i++) {
                if (message.hasField(i)) {
                    isoString.append("Field " + i + "  => " + message.getField(i) + "\n");
                }
            }
            isoString.append("============================================\n");
            System.out.println(isoString.toString());
        } catch (Exception ex) {
            isoString.append("============================================\n");
            ex.printStackTrace();
        }
    }
}
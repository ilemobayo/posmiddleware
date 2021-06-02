package com.elarasolutions.posmiddleware.utility;

import com.elarasolutions.posmiddleware.model.POSData;
import com.solab.iso8583.IsoMessage;
import com.solab.iso8583.MessageFactory;
import com.solab.iso8583.parse.ConfigParser;
import com.solab.iso8583.util.HexCodec;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class Utils {

    MessageFactory<IsoMessage> messageFactory;

    public Utils(){
        try {
            messageFactory = getIsoMessageFactory();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String loadPackagerFile() throws IOException {
        File resource = new ClassPathResource("packager/NCS_PACKAGER.xml").getFile();
        String text = new String(Files.readAllBytes(resource.toPath()));
        System.out.println(text);
        return text;
    }

    private MessageFactory<IsoMessage> getIsoMessageFactory() throws IOException {
        StringReader stringReader = new StringReader(loadPackagerFile());
        MessageFactory<IsoMessage> messageFactory;
        messageFactory = ConfigParser.createFromReader(stringReader);
        messageFactory.setUseBinaryBitmap(false);
        messageFactory.setCharacterEncoding(StandardCharsets.UTF_8.name());
        return messageFactory;
    }

    public IsoMessage createIsoRequest(POSData data) throws Exception {
        System.out.println(data.getMsgType());
        int type = Integer.parseInt(data.getMsgType(), 16);
        System.out.println(type);
        System.out.println(data.getField55());
        IsoMessage message = messageFactory.newMessage(type);
        IsoMessage templ = messageFactory.getMessageTemplate(type);
        message.setValue(2, data.getField2(), templ.getField(2).getType(), data.getField2().length());
        message.setValue(3, data.getField3(), templ.getField(3).getType(), templ.getField(3).getLength());
        message.setValue(4, data.getField4(), templ.getField(4).getType(), templ.getField(4).getLength());
        message.setValue(7, data.getField7(), templ.getField(7).getType(), templ.getField(7).getLength());
        message.setValue(11, data.getField11(), templ.getField(11).getType(), templ.getField(11).getLength());
        message.setValue(12, data.getField12(), templ.getField(12).getType(), templ.getField(12).getLength());
        message.setValue(13, data.getField13(), templ.getField(13).getType(), templ.getField(13).getLength());
        message.setValue(14, data.getField14(), templ.getField(14).getType(), templ.getField(14).getLength());
        message.setValue(18, data.getField18(), templ.getField(18).getType(), templ.getField(18).getLength());
        message.setValue(22, data.getField22(), templ.getField(22).getType(), templ.getField(22).getLength());
        if (!StringUtil.isEmpty(data.getField23())) {
            message.setValue(23, data.getField23(), templ.getField(23).getType(), templ.getField(23).getLength());
        }
        message.setValue(25, data.getField25(), templ.getField(25).getType(), templ.getField(25).getLength());
        message.setValue(26, data.getField26(), templ.getField(26).getType(), templ.getField(26).getLength());
        message.setValue(28, data.getField28(), templ.getField(28).getType(), templ.getField(28).getLength());
        message.setValue(32, data.getField32(), templ.getField(32).getType(), data.getField32().length());
        //message.setValue(33, fwdInstId, templ.getField(33).getType(), fwdInstId.getLength());
        if (!StringUtil.isEmpty(data.getField35())) {
            message.setValue(35, data.getField35(), templ.getField(35).getType(), data.getField35().length());
        }
        message.setValue(37, data.getField37(), templ.getField(37).getType(), templ.getField(37).getLength());
        // Set for Refund
        if (!StringUtil.isEmpty(data.getField38())) {
            message.setValue(38, data.getField38(), templ.getField(38).getType(), templ.getField(38).getLength());
        }
        if (!StringUtil.isEmpty(data.getField40())) {
            message.setValue(40, data.getField40(), templ.getField(40).getType(), templ.getField(40).getLength());
        }
        message.setValue(41, data.getField41(), templ.getField(41).getType(), templ.getField(41).getLength());
        message.setValue(42, data.getField42(), templ.getField(42).getType(), templ.getField(42).getLength());
        message.setValue(43, data.getField43(), templ.getField(43).getType(), templ.getField(43).getLength());
        message.setValue(49, data.getField49(), templ.getField(49).getType(), templ.getField(49).getLength());
        if (!StringUtil.isEmpty(data.getField52())) {
            message.setValue(52, data.getField52(), templ.getField(52).getType(), templ.getField(52).getLength());
        }
        // Set for Cashback
        if (!StringUtil.isEmpty(data.getField54())) {
            message.setValue(54, data.getField54(), templ.getField(54).getType(), templ.getField(54).getLength());
        }
        if (!StringUtil.isEmpty(data.getField55())) {
            message.setValue(55, data.getField55(), templ.getField(55).getType(), data.getField55().length());
        }
        message.setValue(59, data.getField59(), templ.getField(59).getType(), templ.getField(59).getLength());
        if (!StringUtil.isEmpty(data.getField90())) {
            message.setValue(90, data.getField90(), templ.getField(90).getType(), templ.getField(90).getLength());
        }
        if (!StringUtil.isEmpty(data.getField95())) {
            message.setValue(95, data.getField95(), templ.getField(95).getType(), templ.getField(95).getLength());
        }
        message.setValue(123, data.getField123(), templ.getField(123).getType(), templ.getField(123).getLength());
        message.setValue(128, data.getField128(), templ.getField(128).getType(), templ.getField(128).getLength());

        dumpIsoMessages(message, true);
        return message;
    }

    public IsoMessage decode(byte[] data) {
        try{
            IsoMessage isoMessage = messageFactory.parseMessage(data, 0);
            dumpIsoMessages(isoMessage, false);
            return isoMessage;
        } catch(Exception ex){
            ex.printStackTrace();
        }
        return null;
    }

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
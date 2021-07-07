package com.elarasolutions.applications.posmiddleware.controller;

import com.elarasolutions.applications.posmiddleware.model.POSData;
import com.elarasolutions.applications.posmiddleware.model.POSDataPacked;
import com.elarasolutions.applications.posmiddleware.model.POSDataPackedResponse;
import com.elarasolutions.applications.posmiddleware.model.SecureDataTest;
import com.elarasolutions.applications.posmiddleware.utility.ISODataUtils;
import com.elarasolutions.applications.posmiddleware.utility.Utils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.PublicKey;

@CrossOrigin(maxAge = 3600)
@RestController
public class TransactionController {

    @PostMapping("/transaction")
    POSData packedISO(@RequestBody POSDataPacked data) {
        try {
            return new ISODataUtils().sendIsoRequest(data);
        } catch (Exception e) {
            e.printStackTrace();
            return new POSData();
        }
    }

    @PostMapping("/transaction/new")
    POSDataPackedResponse packedISONew(@RequestBody POSDataPacked data) {
        try {
            return new ISODataUtils().sendIsoRequestNew(data);
        } catch (Exception e) {
            e.printStackTrace();
            return new POSDataPackedResponse();
        }
    }

    @PostMapping("/transaction/p")
    POSData jsonISO(@RequestBody POSData data) {
        try {
            return new ISODataUtils().sendIsoRequest(data);
        } catch (Exception e) {
            e.printStackTrace();
            return new POSData();
        }
    }

    @PostMapping("/transaction/secure/e")
    SecureDataTest secureDataTestE(@RequestBody SecureDataTest secureDataTest) {
        try {
            System.out.println(secureDataTest.toString());
            SecureDataTest secureDataTest1 = new SecureDataTest();
//            PublicKey publicKey = Utils.strToPublicKey(secureDataTest.getKey());
//            String msg = Utils.encryptedRSA(secureDataTest.getMsg(), publicKey);
//            String msg = Utils.generateHMACSHA256Signature(secureDataTest.getKey(), secureDataTest.getMsg());
            String msg = Utils.encryptTextUsingAES(secureDataTest.getMsg(), secureDataTest.getKeyAes());
            String eAESKey = Utils.encryptAESKey(secureDataTest.getKeyAes(), secureDataTest.getKey());
            secureDataTest1.setKeyAes(eAESKey);
            secureDataTest1.setKey(secureDataTest.getKey());
            secureDataTest1.setMsg(msg);
            return secureDataTest1;
        } catch (Exception e) {
            e.printStackTrace();
            return new SecureDataTest();
        }
    }

    @PostMapping("/transaction/secure/d")
    SecureDataTest secureDataTestD(@RequestBody SecureDataTest secureDataTest) {
        try {
            SecureDataTest secureDataTest1 = new SecureDataTest();
//            PublicKey publicKey = Utils.strToPublicKey(secureDataTest.getKey());
//            String msg = Utils.decryptRSA(secureDataTest.getMsg(), publicKey);
            String msg = Utils.decryptDataRSAForArcaPay(secureDataTest.getMsg(), secureDataTest.getKey());
            secureDataTest1.setKey(secureDataTest.getKey());
            secureDataTest1.setMsg(msg);
            return secureDataTest1;
        } catch (Exception e) {
            e.printStackTrace();
            return new SecureDataTest();
        }
    }

}

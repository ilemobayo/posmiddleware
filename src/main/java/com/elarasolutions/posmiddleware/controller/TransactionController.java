package com.elarasolutions.posmiddleware.controller;

import com.elarasolutions.posmiddleware.model.POSData;
import com.elarasolutions.posmiddleware.utility.ISODataUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(maxAge = 3600)
@RestController
public class TransactionController {

    @PostMapping("/transaction")
    POSData newPatient(@RequestBody POSData data) {
        try {
            return new ISODataUtils().sendIsoRequest(data);
        } catch (Exception e) {
            e.printStackTrace();
            return new POSData();
        }
    }

}

package com.elarasolutions.posmiddleware.utility.comms;

public interface CommsListener {

    void OnStatus(int var1, byte[] var2);

    void OnError(int var1, String var2);
}

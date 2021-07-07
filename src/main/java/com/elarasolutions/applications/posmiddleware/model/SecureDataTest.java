package com.elarasolutions.applications.posmiddleware.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SecureDataTest {

    @SerializedName("keyAes")
    @Expose
    private String keyAes;

    @SerializedName("key")
    @Expose
    private String key;

    @SerializedName("msg")
    @Expose
    private String msg;

    public String getKeyAes() {
        return keyAes;
    }

    public void setKeyAes(String keyAes) {
        this.keyAes = keyAes;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "SecureDataTest{" +
                "aesKey='" + keyAes + '\'' +
                ", key='" + key + '\'' +
                ", msg='" + msg + '\'' +
                '}';
    }
}

package com.elarasolutions.applications.posmiddleware.utility;

public class StringUtil {

    public static boolean isEmpty(String msg){
        boolean is;
        is = (msg == null || msg.isEmpty());
        return is;
    }

    public static boolean isNull(CharSequence str) {
        return str == null || str.length() == 0;
    }

    public static String leftPadding(char fill, int totalLength, String str) {
        StringBuffer buffer = new StringBuffer();
        for (int i = str.length(); i < totalLength; i++) {
            buffer.append(fill);
        }
        buffer.append(str);
        return buffer.toString();
    }

    public static String leftPadding(String fill, int totalLength, String str) {
        StringBuffer buffer = new StringBuffer();
        for (int i = str.length(); i < totalLength; i++) {
            buffer.append(fill);
        }
        buffer.append(str);
        return buffer.toString();
    }

    public static String leftAppend(String fill, int appendLength, String str) {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < appendLength; i++) {
            buffer.append(fill);
        }
        buffer.append(str);
        return buffer.toString();
    }

    public static String rightAppend(String fill, int appendLength, String str) {
        StringBuilder buffer = new StringBuilder(str);
        for (int i = 0; i < appendLength; i++) {
            buffer.append(fill);
        }
        return buffer.toString();
    }

    public static String rightPadding(String fill, int totalLength, String str) {
        StringBuilder buffer = new StringBuilder(str);
        while (str.length() < totalLength) {
            buffer.append(fill);
        }
        return buffer.toString();
    }

    private static int getByteLength(char a) {
        String tmp = Integer.toHexString(a);
        return tmp.length() >> 1;
    }

    public static String leftPad(String str, int len, char pad) {
        if(str == null)
            return null;
        StringBuilder sb = new StringBuilder();
        while (sb.length() + str.length() < len) {
            sb.append(pad);
        }
        sb.append(str);
        String paddedString = sb.toString();
        return paddedString;
    }

    public static String rightPad(String str, int len, char pad) {

        if(str == null)
            return null;
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        while (sb.length() < len) {
            sb.append(pad);
        }
        String paddedString = sb.toString();
        return paddedString;
    }

    public static String maskString(String string){
        try {
            StringBuilder sb = new StringBuilder();
            for(int i = 0; i < string.length(); i++) {
                sb.append("*");
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "********";
        }
    }

    public static String maskPan(String pan){
        try {
            String firstPart = pan.substring(0, 6);
            int len = pan.length();
            String lastPart = pan.substring(len - 4, len);
            int middlePartLength = len - 6;
            String middleLastPart = leftPad("", middlePartLength, '*');
            return firstPart + middleLastPart.substring(0, middleLastPart.length() - 4) + lastPart;
        } catch (Exception e) {
            e.printStackTrace();
            return "0000********0000";
        }
    }
}

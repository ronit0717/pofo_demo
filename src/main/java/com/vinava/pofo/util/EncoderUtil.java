package com.vinava.pofo.util;

public class EncoderUtil {

    public static String getPofoEncodedString(String inputString) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < inputString.length(); i++) {
            char c = inputString.charAt(i);
            if (Character.isDigit(c)) {
                sb.append(getEncodedDigit(Character.toString(c)));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    private static String getEncodedDigit(String c) {
        int i = Integer.parseInt(c);
        if (i >= 5) {
            i = i - 5;
        } else {
            i = i + 5;
        }
        return Integer.toString(i);
    }

}

package com.khanh.expensemanagement.util;

public class DataUtil {

    public static String fncNS(String prmValue) {
        if (prmValue == null || prmValue.trim().isEmpty()) {
            return " ";
        }
        return prmValue;
    }
}

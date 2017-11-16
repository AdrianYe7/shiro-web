package com.xyz.utils;

public final class StringUtil {

    private StringUtil() {}

    public static String replaceCharAt(int index, String oldString, String newString) {
        StringBuilder sb = new StringBuilder(oldString);
        return sb.replace(index,index+1, newString).toString();
    }

    public static String upperFirstChar(String oldString) {
        return replaceCharAt(0, oldString, oldString.substring(0, 1).toUpperCase());
    }
}

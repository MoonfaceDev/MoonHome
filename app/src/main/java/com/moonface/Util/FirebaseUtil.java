package com.moonface.Util;

public class FirebaseUtil {
    public static boolean isValid(String path){
        return (path.contains(".") || path.contains("#") || path.contains("$") || path.contains("[") || path.contains("]"));
    }
}

package com.niuxuewei.lucius.core.utils;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class UserUtils {

    public static String avatar(String email) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        String emailMd5 = DatatypeConverter.printHexBinary(messageDigest.digest(email.getBytes())).toLowerCase();
        return "https://secure.gravatar.com/avatar/" + emailMd5 + "?s=400&default=identicon&rating=g";
    }

}

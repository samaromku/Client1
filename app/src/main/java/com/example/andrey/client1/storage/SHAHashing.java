package com.example.andrey.client1.storage;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHAHashing {
    public String hashPwd(String pwd) throws NoSuchAlgorithmException {
        String password = pwd;
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(password.getBytes());
        byte byteData[] = md.digest();
        //convert the byte to hex format method 1
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < byteData.length; i++) {
            sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }
        System.out.println("Hex format : " + sb.toString());
        return sb.toString();
    }
}

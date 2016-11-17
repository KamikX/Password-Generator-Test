package com.sarriel.pass.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Base64;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordGeneratorService extends Service {

    // Binder given to clients
    private final IBinder mBinder = new PasswordGeneratorBinder();


    @Override
    public IBinder onBind(Intent intent) {
       return  mBinder;
    }


    public class PasswordGeneratorBinder extends Binder {
        public PasswordGeneratorService getService() {
            return PasswordGeneratorService.this;
        }
    }

    /**
     * Generate password by using SHA-256 hashing method on concatenated alias and secret.
     * @param alias
     * @param secret
     * @return
     * @throws PasswordGenException
     */
    public  String generatePassword(String alias, String secret) throws PasswordGenException {
        String seed = alias.concat(secret);
        String password = null;
        try {
            password = (Base64.encodeToString(getHash(seed).getBytes(), 0)).substring(17,49);
        } catch (Exception e) {
            throw (new PasswordGenException("Error generating password"));
        }

        return password;
    }

    /**
     * Get hexadecimal string of hash generated for given string.
     * @param string
     * @return
     */
    private  String getHash(String string) {
        MessageDigest digest=null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        }
        digest.reset();
        return bin2hex(digest.digest(string.getBytes()));
    }

    /**
     * Convert byte data to hexadecimal string.
     * @param data
     * @return
     */
    private  String bin2hex(byte[] data) {
        return String.format("%0" + (data.length*2) + "x", new BigInteger(1, data));
    }





}

package com.shlomitubul.secretsManager.easyCrypto;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;

public class HashHelper {
    private static final String ALGO = "PBKDF2WithHmacSHA1";
    private static final int ITERATION = 65536;
    private static final int KEY_LENGTH = 128;

    public  static  byte[] generateSalt(){
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return  salt;
    }

    public  static byte[] generateHash(String password,byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, ITERATION, KEY_LENGTH);
        SecretKeyFactory factory = SecretKeyFactory.getInstance(ALGO);
        return factory.generateSecret(spec).getEncoded();
    }

    public  static boolean verifyPassword(String txtPassword,byte[] hashedPassword,byte[] salt) throws InvalidKeySpecException, NoSuchAlgorithmException {
        byte[] passwordToVerify = generateHash(txtPassword,salt);
        return  Arrays.equals(passwordToVerify,hashedPassword);
    }

}

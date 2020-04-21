package com.shlomitubul.secretsManager.easyCrypto;



import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class KeyStoreManager {
    private final String KEY_STORE_NOT_LOAD_EXCEPTION_MESSAGE = "key store must be initialize before use !";

    private boolean keyStoreLoaded = false;
    private KeyStore ks;

    public boolean isKeyStoreLoaded() {
        return keyStoreLoaded;
    }

    public KeyStoreManager() throws KeyStoreException {
        ks = KeyStore.getInstance(KeyStore.getDefaultType());
    }

    public boolean setKey(String alias,SecretKey secretKey, String password) throws Exception {
            if(!keyStoreLoaded)
                throw new Exception(KEY_STORE_NOT_LOAD_EXCEPTION_MESSAGE);

            try {
                KeyStore.SecretKeyEntry secret
                        = new KeyStore.SecretKeyEntry(secretKey);
                KeyStore.ProtectionParameter passwordParameter
                        = new KeyStore.PasswordProtection(password.toCharArray());
                ks.setEntry(alias, secret, passwordParameter);
                return true;
            }
            catch (Exception e){
                e.printStackTrace();

            }
        return false;
    }


    public SecretKey getKey(String alias,String password) throws
            Exception {
        if(!keyStoreLoaded)
            throw new Exception(KEY_STORE_NOT_LOAD_EXCEPTION_MESSAGE);

        return  (SecretKey) ks.getKey(alias, password.toCharArray());
    }

    /**
     * load the keystore and create one if not exists
     * @param name
     * @param password
     * @param path
     * @return
     * @throws CertificateException
     * @throws NoSuchAlgorithmException
     * @throws IOException
     * @throws KeyStoreException
     */
    public boolean loadKeyStore(String name, String password, String path)
            throws
            CertificateException,
            NoSuchAlgorithmException,
            IOException,
            KeyStoreException {
        try {
            if (!keyStoreExist(path, name)) {
                ks.load(null, password.toCharArray());
            }
            else {
                ks.load(new FileInputStream(path + "/" + name), password.toCharArray());
            }
            ks.store(new FileOutputStream(path + "/" + name), password.toCharArray());
            keyStoreLoaded = true;
        }
        catch (Exception e) {
                e.printStackTrace();
                keyStoreLoaded = false;
        }
        return keyStoreLoaded;
    }

    private  boolean keyStoreExist(String ksFolderPath,String name){
       return Files.exists(Paths.get(ksFolderPath + "/" + name));
    }
}

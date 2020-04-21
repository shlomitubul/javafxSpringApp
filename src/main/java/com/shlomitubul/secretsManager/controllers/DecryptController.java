package com.shlomitubul.secretsManager.controllers;

import com.shlomitubul.secretsManager.dbModel.EncryptionSession;
import com.shlomitubul.secretsManager.easyCrypto.CryptoManager;
import com.shlomitubul.secretsManager.easyCrypto.KeyStoreManager;
import com.shlomitubul.secretsManager.services.DatabaseService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import org.springframework.stereotype.Component;


import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;

import java.security.KeyStoreException;

import java.util.Base64;

@Component
public class DecryptController extends  FxmlController{

    @FXML private TextArea plainText;
    @FXML private TextArea cipherText;
    @FXML private TextField keyAlias;
    @FXML private PasswordField keyPassword;

    public DecryptController(DatabaseService db, CryptoManager cryptoManager, KeyStoreManager keyStoreManager) throws KeyStoreException {
        super(db, cryptoManager, keyStoreManager);
    }


    public void onDecryptClick() throws Exception {
        if(!handleInputConstraint())
            return;
        byte[] encrptedBytes =  Base64.getDecoder().decode(cipherText.getText().getBytes());
        SecretKey key = keyStoreManager.getKey(keyAlias.getText(),keyPassword.getText());
        EncryptionSession encInfo = db.getEncryptionSession(keyAlias.getText());
        GCMParameterSpec gcmParameterSpec = cryptoManager.createGCMParameter(encInfo.getIv());

        byte[] decryptedBytes = this.cryptoManager.aesDecrypt(encrptedBytes,key,gcmParameterSpec,cryptoManager.getAadData());

        plainText.setText(new String(decryptedBytes));
    }


    private boolean handleInputConstraint() {
        if (keyAlias.getText().isEmpty()) {
            showAlert("key alias can't be empty", Alert.AlertType.ERROR);
            return false;
        } else if (cipherText.getText().isEmpty()) {
            showAlert("encrypted text can't be empty", Alert.AlertType.ERROR);
            return false;
        }
        else if(keyPassword.getText().isEmpty()){
            showAlert("password can't be empty", Alert.AlertType.ERROR);
            return false;
        }
        return true;
    }
}

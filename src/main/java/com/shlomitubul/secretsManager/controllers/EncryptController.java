package com.shlomitubul.secretsManager.controllers;

import com.shlomitubul.secretsManager.dbModel.EncryptionSession;
import com.shlomitubul.secretsManager.easyCrypto.CryptoManager;
import com.shlomitubul.secretsManager.easyCrypto.KeyStoreManager;
import com.shlomitubul.secretsManager.services.DatabaseService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import java.security.KeyStoreException;
import java.util.Base64;

@Component
public class EncryptController extends FxmlController {

    public final String TEXT_ERROR_MSG = "text length need to at least 3 characters.";
    public final String KEY_ALIAS_EMPTY_ERROR_MSG = "key alias shouldn't be empty.";
    public final String KEY_PASSWORD_TOO_SHORT_ERROR_MSG = "key password need to be at least 8 characters.";
    public final String ENC_ERR_MSG = "we got some fatal errors... try again later.";
    public final String ENC_OK_MSG = "encryption done just remember your key alias and password to decrypt back your data.";





    @FXML private TextArea plainText;
    @FXML private  TextArea encryptedText;
    @FXML private TextField keyAlias;
    @FXML private PasswordField keyPassword;


    public TextArea getPlainText() {
        return plainText;
    }

    public TextArea getEncryptedText() {
        return encryptedText;
    }

    public TextField getKeyAlias() {
        return keyAlias;
    }

    public PasswordField getKeyPassword() {
        return keyPassword;
    }

    public EncryptController(DatabaseService db, CryptoManager cryptoManager, KeyStoreManager keyStoreManager) throws KeyStoreException {
        super(db, cryptoManager, keyStoreManager);
    }

    public void onEncryptClick() throws Exception {
        if (!handleInputConstraint()) return;

        SecretKey key = cryptoManager.generateSecretKey();
        byte[] iv = cryptoManager.generateInitializationVector();
        GCMParameterSpec gcmParameterSpec = cryptoManager.createGCMParameter(iv);
        byte[] addData = cryptoManager.getAadData();

        byte[] cipherBytes =  cryptoManager.aesEncrypt(plainText.getText().getBytes(),key,gcmParameterSpec,addData);


        encryptedText.setText(Base64.getEncoder().encodeToString(cipherBytes));

        boolean sessionSaved =  db.createEncryptionSession(new EncryptionSession(keyAlias.getText(),iv));
        keyStoreManager.setKey(keyAlias.getText(),key,keyPassword.getText());

        if(!sessionSaved)
        {
            showAlert(ENC_ERR_MSG, Alert.AlertType.ERROR);
            return;
        }
        showAlert(ENC_OK_MSG,"Encryption success", Alert.AlertType.INFORMATION);
        clearFields(keyAlias,keyPassword);
    }

    private boolean handleInputConstraint() {
        if (keyPassword.getText().length() < 8) {
            showAlert(KEY_PASSWORD_TOO_SHORT_ERROR_MSG, Alert.AlertType.ERROR);
            return false;
        } else if (keyAlias.getText().isEmpty()) {
            showAlert(KEY_ALIAS_EMPTY_ERROR_MSG, Alert.AlertType.ERROR);
            return false;
        } else if (plainText.getText().length() < 3) {
            showAlert(TEXT_ERROR_MSG, Alert.AlertType.ERROR);
            return false;
        }
        return true;
    }

}

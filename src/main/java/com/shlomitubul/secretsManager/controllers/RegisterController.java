package com.shlomitubul.secretsManager.controllers;

import com.shlomitubul.secretsManager.dbModel.KeyStoreInfo;
import com.shlomitubul.secretsManager.easyCrypto.FileHelper;
import com.shlomitubul.secretsManager.easyCrypto.KeyStoreManager;
import com.shlomitubul.secretsManager.services.AuthService;
import com.shlomitubul.secretsManager.services.DatabaseService;
import com.shlomitubul.secretsManager.view.FxmlView;
import com.shlomitubul.secretsManager.view.StageManager;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;

import static javafx.scene.control.Alert.AlertType.ERROR;

@Component
public class RegisterController {

    @FXML private TextField keystoreName;
    @FXML private PasswordField keystorePassword;
    @FXML private Button btn_ok;
    @FXML private TextField txt_username;
    @FXML private PasswordField pass_password;

    private AuthService authService;
    private StageManager stageManager;
    private KeyStoreManager keyStoreManager;
    private  DatabaseService databaseService;

    @Autowired @Lazy
    public RegisterController(AuthService authService, StageManager stageManager, KeyStoreManager keyStoreManager, DatabaseService databaseService) {
        this.authService = authService;
        this.stageManager = stageManager;
        this.keyStoreManager = keyStoreManager;
        this.databaseService = databaseService;
    }

    public void initialize(){
        btn_ok.disableProperty().bind(Bindings.or(txt_username.lengthProperty().lessThan(6),
                pass_password.lengthProperty().lessThan(8)));
    }

    public void register() throws InvalidKeySpecException, NoSuchAlgorithmException, SQLException, CertificateException, KeyStoreException, IOException {
        Alert loginAlert = new Alert(Alert.AlertType.NONE);

        boolean isRegisterSuccess = authService.createUser(txt_username.getText(),pass_password.getText());
        boolean isKeystoreCreated =
                databaseService.saveKeystoreInfo(new KeyStoreInfo(databaseService.getUser(txt_username.getText()).userID,keystoreName.getText(), FileHelper.KEYSTORES_DIR)) &&
                keyStoreManager.loadKeyStore(keystoreName.getText(),keystorePassword.getText(),FileHelper.KEYSTORES_DIR);

        if (!isRegisterSuccess || !isKeystoreCreated) {
            loginAlert.setAlertType(ERROR);
            loginAlert.setContentText("ops... something went wrong");
            loginAlert.show();
            return;
        }
        stageManager.switchScene(FxmlView.LOGIN);
    }

}

package com.shlomitubul.secretsManager.controllers;

import com.shlomitubul.secretsManager.dbModel.KeyStoreInfo;
import com.shlomitubul.secretsManager.easyCrypto.KeyStoreManager;
import com.shlomitubul.secretsManager.services.AuthService;
import com.shlomitubul.secretsManager.services.DatabaseService;
import com.shlomitubul.secretsManager.view.FxmlView;
import com.shlomitubul.secretsManager.view.StageManager;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.xml.crypto.Data;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;

import static javafx.scene.control.Alert.AlertType.ERROR;


@Component
public class LoginController extends FxmlController {

    @FXML private Button btn_login;
    @FXML private TextField txt_username;
    @FXML private PasswordField pass_password;

    private AuthService authService;
    private StageManager stageManager;
    private KeyStoreManager keyStoreManager;
    private DatabaseService db;

    @Autowired @Lazy
    public LoginController(AuthService authService, StageManager stageManager, KeyStoreManager keyStoreManager, DatabaseService db) throws KeyStoreException {
        super(db,null,keyStoreManager);
        this.authService = authService;
        this.stageManager = stageManager;
        this.keyStoreManager = keyStoreManager;
        this.db = db;
    }

    @Override
    public void initialize(){
        super.initialize();
           btn_login.disableProperty()
                   .bind(Bindings.or(txt_username.lengthProperty().lessThan(6),
                  pass_password.lengthProperty().lessThan(8)));
    }

    public void login() throws InvalidKeySpecException, NoSuchAlgorithmException, SQLException {
        boolean isLoginSuccessfully = authService.signIn(txt_username.getText(),pass_password.getText());
        if(!isLoginSuccessfully){
            showAlert("username or password are incorrect","login error", ERROR);
            return;
        }

        if(!loadKeystore())
            showAlert("keystore password are incorrect","keystore load error", ERROR);

        stageManager.switchScene(FxmlView.MAIN);
    }

    public boolean loadKeystore(){
        try{
            if(keyStoreManager.isKeyStoreLoaded())
                return true;

            String ksPassword = showTextDialog("keystorge","keystorge password required","password:");
            KeyStoreInfo ksInfo = db.getKeystoreInfo(authService.getCurrentUser().userID);
            return keyStoreManager.loadKeyStore(ksInfo.name,ksPassword, ksInfo.path);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        return false;
    }


    public void goToRegisterPage(){
        stageManager.switchScene(FxmlView.REGISTER);
    }
}

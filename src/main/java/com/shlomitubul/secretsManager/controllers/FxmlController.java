package com.shlomitubul.secretsManager.controllers;

import com.shlomitubul.secretsManager.easyCrypto.CryptoManager;
import com.shlomitubul.secretsManager.easyCrypto.KeyStoreManager;
import com.shlomitubul.secretsManager.services.DatabaseService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextInputControl;
import javafx.scene.control.TextInputDialog;
import org.springframework.beans.factory.annotation.Autowired;

import java.security.KeyStoreException;
import java.sql.SQLException;
import java.util.Optional;


public abstract class FxmlController {


    private Alert messageAlert;

    protected DatabaseService db;
    protected CryptoManager cryptoManager;
    protected KeyStoreManager keyStoreManager;

    @Autowired
    public FxmlController(DatabaseService db, CryptoManager cryptoManager, KeyStoreManager keyStoreManager) {
        this.db = db;
        this.cryptoManager =  cryptoManager;
        this.keyStoreManager = keyStoreManager;
    }

    @FXML
    public void initialize()  {
        messageAlert =  new Alert(Alert.AlertType.NONE);
    }


    public void clearFields(TextInputControl... fields){
        for (TextInputControl field : fields)
        {
            field.clear();
        }
    }

    public void showAlert(String text, Alert.AlertType type)  {
        messageAlert.setContentText(text);
        messageAlert.setAlertType(type);
        messageAlert.show();
    }

    public void showAlert(String text,String title, Alert.AlertType type)  {
        messageAlert.setContentText(text);
        messageAlert.setAlertType(type);
        messageAlert.setHeaderText(title);
        messageAlert.show();
    }


    public String showTextDialog(String title,String header,String content){
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(title);
        dialog.setHeaderText(header);
        dialog.setContentText(content);

        Optional<String> result = dialog.showAndWait();

        if (result.get().length() < 8){
            dialog.showAndWait();
        }
        return  result.get();
    }

}

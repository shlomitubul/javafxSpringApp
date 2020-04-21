package com.shlomitubul.secretsManager.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TabPaneManger  {

    private final EncryptController encryptController;
    private final DecryptController decryptController;

    @FXML  private Tab tabConsole;
    @FXML  private Tab tabLogger;

    @Autowired
    public TabPaneManger(EncryptController encryptController,  DecryptController decryptController) {
        this.encryptController = encryptController;
        this.decryptController = decryptController;
    }


    public EncryptController getEncryptController() {
        return encryptController;
    }

    public DecryptController getDecryptController() {
        return decryptController;
    }
}
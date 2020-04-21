package com.shlomitubul.secretsManager.view;

import java.util.ResourceBundle;

public enum FxmlView {

    MAIN {
        @Override
        String getTitle() {
            return getStringFromResourceBundle("main.title");
        }

        @Override
        String getFxmlFile() {
            return "/fxml/Main.fxml";
        }
    },
    LOGIN {
        @Override
        String getTitle() {
            return getStringFromResourceBundle("login.title");
        }

        @Override
        String getFxmlFile() {
            return "/fxml/Login.fxml";
        }
    },
    REGISTER{
        @Override
        String getTitle() {
            return getStringFromResourceBundle("register.title");
        }

        @Override
        String getFxmlFile() {
            return "/fxml/Register.fxml";
        }
    };
    
    abstract String getTitle();
    abstract String getFxmlFile();
    
    String getStringFromResourceBundle(String key){
        return ResourceBundle.getBundle("Bundle").getString(key);
    }

}

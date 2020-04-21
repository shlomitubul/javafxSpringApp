package com.shlomitubul.secretsManager.dbModel;

public class KeyStoreInfo {
    public String name;
    public String path;
    public int userId;

    public KeyStoreInfo(int userId,String name, String path) {
        this.userId = userId;
        this.name = name;
        this.path = path;
    }
}

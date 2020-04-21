package com.shlomitubul.secretsManager.dbModel;

public class User {
    public int userID;
    public String username;
    public byte[] passwordHash;
    public byte[] passwordSalt;

    public User(String username, byte[] passwordHash, byte[] passwordSalt) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.passwordSalt = passwordSalt;
    }

    public User(int userID,String username, byte[] passwordHash, byte[] passwordSalt) {
        this.userID = userID;
        this.username = username;
        this.passwordHash = passwordHash;
        this.passwordSalt = passwordSalt;
    }

    public User(){

    }
}

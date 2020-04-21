package com.shlomitubul.secretsManager.services;

import com.shlomitubul.secretsManager.dbModel.User;
import com.shlomitubul.secretsManager.easyCrypto.HashHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;

@Service
public class AuthService {

    private User currentUser;

    @Autowired
    private DatabaseService dbService;

    public User getCurrentUser() {
        return currentUser;
    }

    public boolean signIn(String username, String password) throws SQLException, InvalidKeySpecException, NoSuchAlgorithmException {
        User user = dbService.getUser(username);

        if(user == null)
            return false;

        boolean isSignInSuccess = HashHelper.verifyPassword(password,user.passwordHash,user.passwordSalt);

        if(isSignInSuccess)
            currentUser = user;

        return isSignInSuccess;
    }

    public boolean createUser(String username,String password) throws InvalidKeySpecException, NoSuchAlgorithmException, SQLException {
        byte[] salt = HashHelper.generateSalt();
        HashHelper.generateHash(password,salt);
        byte[] hash = HashHelper.generateHash(password,salt);
        return dbService.createUser(new User(username,hash,salt));
    }
}

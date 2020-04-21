package com.shlomitubul.secretsManager.services;

import com.shlomitubul.secretsManager.dbModel.EncryptionSession;
import com.shlomitubul.secretsManager.dbModel.KeyStoreInfo;
import com.shlomitubul.secretsManager.dbModel.User;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;

@Service
public class DatabaseService {

    private Connection connection;

    public DatabaseService(){
        connection = getConnection();
    }

    public Connection getConnection(){
        if(connection != null)
            return connection;

        try{
            Class.forName("org.sqlite.JDBC");
            return DriverManager.getConnection("jdbc:sqlite:PasswordManagerDB.db");
        }catch (Exception ex){
            ex.printStackTrace();
            return null;
        }
    }

    public boolean isDbConnected() throws SQLException {
           return !connection.isClosed();
    }

    public User getUser(String username) throws SQLException {
        PreparedStatement preparedStatement = null;
        ResultSet result = null;
        String query = "select * from user where username = ?";

        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,username);
            result = preparedStatement.executeQuery();

            if(!result.next())
                return null;

            User user = new User();
            user.username = result.getString("Username");
            user.userID = result.getInt("id");
            user.passwordHash = result.getBytes("PasswordHash");
            user.passwordSalt = result.getBytes("PasswordSalt");
            return user;

        }catch (Exception ex){
                ex.printStackTrace();
                return  null;
        }
        finally {
            preparedStatement.close();
            result.close();
        }

    }

    public boolean createUser(User user) throws SQLException {
        if(getUser(user.username) != null)
            return false;

        PreparedStatement preparedStatement = null;
        int result;
        String query = "INSERT INTO User (Username, PasswordHash, PasswordSalt) VALUES (?, ?, ?);";

        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,user.username);
            preparedStatement.setBytes(2, user.passwordHash);
            preparedStatement.setBytes(3,user.passwordSalt);
            result = preparedStatement.executeUpdate();

            return result > 0;

        }catch (Exception ex){
            ex.printStackTrace();
            return  false;
        }
        finally {
            preparedStatement.close();
        }
    }

    public boolean createEncryptionSession(EncryptionSession session) throws SQLException {
        if(session.getName().isEmpty() ||  !isValidDate(session.getDate()) || session.getIv() == null)
            return false;

        PreparedStatement preparedStatement = null;
        int result;
        String query = "INSERT INTO EncryptionSessions (Name, Iv, EncDate) VALUES (?, ?, ?);";

        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,session.getName());
            preparedStatement.setBytes(2, session.getIv());
            preparedStatement.setString(3,session.getDate());
            result = preparedStatement.executeUpdate();

            return result > 0;

        }catch (Exception ex){
            ex.printStackTrace();
            return  false;
        }
        finally {
            preparedStatement.close();
        }
    }

    public EncryptionSession getEncryptionSession(String alias) throws SQLException {
        PreparedStatement preparedStatement = null;
        ResultSet result = null;

        String query = "select * from EncryptionSessions where Name = ?";

        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,alias);
            result = preparedStatement.executeQuery();

            if(!result.next())
                return null;

            return new EncryptionSession(result.getString("Name"),result.getBytes("Iv"),result.getString("EncDate"));

        }catch (Exception ex){
            ex.printStackTrace();
            return null;
        }
        finally {
            preparedStatement.close();
            result.close();
        }
    }

    public boolean saveKeystoreInfo(KeyStoreInfo keyStoreInfo) throws SQLException {
        if(keyStoreInfo.name.isEmpty())
            return false;

        PreparedStatement preparedStatement = null;
        int result;
        String query = "INSERT INTO KeystoreInfo (name,path,user_id) VALUES (?, ?,?);";

        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,keyStoreInfo.name);
            preparedStatement.setString(2,keyStoreInfo.path);
            preparedStatement.setInt(3,keyStoreInfo.userId);
            result = preparedStatement.executeUpdate();

            return result > 0;

        }catch (Exception ex){
            ex.printStackTrace();
            return  false;
        }
        finally {
            preparedStatement.close();
        }
    }

    public KeyStoreInfo getKeystoreInfo(int userId) throws SQLException {
        PreparedStatement preparedStatement = null;
        ResultSet result = null;

        String query = "select * from KeystoreInfo where user_id = ?";

        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, userId);
            result = preparedStatement.executeQuery();

            if(!result.next())
                return null;

            return new KeyStoreInfo(result.getInt("user_id"),result.getString("name"),result.getString("path"));

        }catch (Exception ex){
            ex.printStackTrace();
            return null;
        }
        finally {
            preparedStatement.close();
            result.close();
        }
    }
    private static boolean isValidDate(String inDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        dateFormat.setLenient(false);
        try {
            dateFormat.parse(inDate.trim());
        } catch (ParseException pe) {
            return false;
        }
        return true;
    }
}

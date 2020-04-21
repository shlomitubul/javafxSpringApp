package com.shlomitubul.secretsManager.easyCrypto;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileHelper {
    public static final String BASE_PATH = System.getProperty("user.home");
    public static final String APP_BASE_DIR = BASE_PATH + "/PasswordManager";
    public static final String IVS_DIR = APP_BASE_DIR + "/Ivs";
    public static final String KEYSTORES_DIR = APP_BASE_DIR + "/Jks";
    public static final String ENCRYPTED_DATA_DIR = APP_BASE_DIR + "/Enc";

    static {
        ensureDirectoriesExists(APP_BASE_DIR,IVS_DIR,KEYSTORES_DIR,ENCRYPTED_DATA_DIR);
    }


    public static void ensureDirectoriesExists(String... pathToEnsure){
        for (int i = 0; i< pathToEnsure.length;i++){
            File dir = new File(pathToEnsure[i]);
            if(!dir.exists())
                dir.mkdir();
        }
    }

    public static boolean saveFile(byte[] file, String path,String fileName) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(path + "/"+ fileName)) {
            fos.write(file);
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }
    public static byte[] loadFile(String path){
        byte[] data = null;
        try {
            Path fileLocation = Paths.get(path);
            data = Files.readAllBytes(fileLocation);
        } catch (Exception e){
            e.printStackTrace();
            return  null;
        }
        return data;
    }

    public static boolean isDirExists(String path){
        return Files.exists(Paths.get(path));
    }

}

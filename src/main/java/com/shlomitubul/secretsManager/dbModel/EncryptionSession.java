package com.shlomitubul.secretsManager.dbModel;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class EncryptionSession {

    private String name;
    private String date;
    private byte[] iv;
    private LocalDateTime myDateObj;
    private DateTimeFormatter myFormatObj;

    /**
     * manually set the date
     * @param name
     * @param iv
     * @param date date format required:  dd-MM-yyyy HH:mm:ss
     */
    public EncryptionSession(String name, byte[] iv, String date ) {
        this.name = name;
        this.iv = iv;
        this.date = date;

    }

    /**
     * auto set the date
     * date wel be set according to this format: dd-MM-yyyy HH:mm:ss
     * @param name
     * @param iv
     */
    public EncryptionSession(String name, byte[] iv ) {
        this.name = name;
        this.iv = iv;

        initDate();
    }

    public String getName() {
        return name;
    }
    public byte[] getIv() {
        return iv;
    }
    public String getDate() {
        return date;
    }


    private void initDate() {
        myDateObj = LocalDateTime.now();
        myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        this.date = myDateObj.format(myFormatObj);
    }
}

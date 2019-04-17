package c.main.aortaandroid;

import android.app.Application;
import android.app.Dialog;

import java.security.Key;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import javax.crypto.spec.SecretKeySpec;

public class GlobaleVariabelen extends Application {
    //aanpassen aan eigen IP adres
    private String ipServer = "192.168.0.185";
    private int id;
    private String loggedInUser;
    private String[] filiaalNamen;
    private String[] addresLijst;
    private String[] idLijst;
    private int filiaalid;
    private boolean taakUitgevoerd = false;
    String[] taken;
    private int huidigFiliaalId;
    private Dialog dialog;


    private String type;
    private String fromRoom;
    private String toRoom;
    private int taskId;


    public Dialog getDialog() {
        return dialog;
    }

    public void setDialog(Dialog dialog) {
        this.dialog = dialog;
    }

    public String getToRoom() {
        return toRoom;
    }

    public void setToRoom(String toType) {
        this.toRoom = toType;
    }

    public void setFromRoom(String fromType) {
        this.fromRoom = fromType;
    }
    public String getFromRoom(){
        return fromRoom;
    }

    public void setType(String type) {
        this.type = type;
    }
    public String getType(){
        return type;
    }

    public void setTaskId(int id){
        taskId=id;
    }
    public int getTaskId(){
        return taskId;
    }


    private static final String KEY_DATA = "dkz45KZADH@#!!EF684pm";
    public static Key JWT_KEY = new SecretKeySpec(KEY_DATA.getBytes(), 0, 16, "AES");



    public boolean isTaakUitgevoerd() {
        return taakUitgevoerd;
    }

    public void setFiliaalid(int i){
        this.filiaalid=i;
    }
    public int getFiliaalid(){
        return this.filiaalid;
    }

    public void setTaakUitgevoerd(boolean taakUitgevoerd) {
        this.taakUitgevoerd = taakUitgevoerd;
    }


    public String[] getAddresLijst() {
        return addresLijst;
    }

    public void setAddresLijst(String[] addresLijst) {
        this.addresLijst = addresLijst;
    }

    public String[] getIdLijst() {
        for(int i=0;i<idLijst.length;i++) {
            System.out.println("id"+idLijst[i]);


        }
        return idLijst;
    }

    public String[] getTaken() {
        return taken;
    }

    public void setTaken(String[] taken) {
        this.taken = taken;
    }

    public void setIdLijst(String[] idLijst) {
        this.idLijst = idLijst;
    }

    public String[] getFiliaalNamen() {
        return filiaalNamen;
    }

    public void setFiliaalNamen(String[] filiaalNamen) {
        this.filiaalNamen = filiaalNamen;
    }

    public String getLoggedInUser() {
        return loggedInUser;
    }

    public void setLoggedInUser(String loggedInUser) {
        this.loggedInUser = loggedInUser;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getHuidigFiliaalId() {
        return huidigFiliaalId;
    }

    public void setHuidigFiliaalId(int huidigFiliaalId) {
        this.huidigFiliaalId = huidigFiliaalId;
    }

    public Key getJWTKey() {
        return JWT_KEY;
    }

    public String getIpServer(){
        return ipServer;
    }

    public void setIpServer(String ipServer){
        this.ipServer = ipServer;
    }



}

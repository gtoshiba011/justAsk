package manager;
import java.util.*;
import manager.SurveyManager;
import manager.QuestionManager;
import manager.AccountManager;
import QR.*;

public class Manager {

    // member varable
    private int _joinEventID;
    private Vector _myEventList;
    private AccountManager _accountManager;

    //constructor
    public Manager() {
        //TODO
    }

    // define function
    public long sendLaunchEventMessage(){
        //TODO
        return 0;
    }

    public long convertQRcode(QR QRcode){
        //TODO
        return 0;
    }

    public boolean joinEvent(long eventID){
        //TODO
        return true;
    }

    public boolean joinEvent(QR QRcode){
        //TODO
        return true;
    }

    public boolean showEventList(){
        //TODO
        return true;
    }

    public boolean showEventInfo(){
        //TODO
        return true;
    }

    public void findLocalData(){
        //TODO
    }

    public boolean synchronizeEvent(){
        //TODO
        return true;
    }

    public boolean addToEventList(long eventID){
        //TODO
        return true;
    }

    public boolean saveEventToAPI(long eventID){
        //TODO
        return true;
    }

    public boolean scanQRCode(){
        //TODO
        return true;
    }

    public boolean closeEvent(){
        //TODO
        return true;
    }

    // main
    public static void main(String[] args){
        //TODO
    }
}

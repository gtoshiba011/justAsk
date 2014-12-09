package event;
import QR.QR;
import speech.SpeechInfo;
import manager.*;

public class Event {

    // member varibale
    private long _ID;
    private SpeechInfo _speechInfo;
    private QR _qr;
    private String _webCode;
    private String _URL;
    private QuestionManager _quesManager;
    private SurveyManager _survManager;
    private boolean _status;

    //constructor
    public Event() {
        //TODO
    }

    // function
    public boolean updateSpeechInfo(){
        //TODO
        return true;
    }
    public boolean modifySpeechInfo(){
        //TODO
        return true;
    }
    public boolean exit(){
        //TODO
        return true;
    }
    // main
    public static void main(String[] args){
        //TODO
    }
}

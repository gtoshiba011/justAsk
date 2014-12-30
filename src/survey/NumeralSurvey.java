package survey;

import java.util.Vector;

public class NumeralSurvey extends Survey{

    private int _numberOfChoice;
    private Vector _choice;// type : int

    //constructor
    public NumeralSurvey(String topic) {
        super(topic);
        _surveyType = 2;
    }

    public boolean receiveAndParseResult(){
        //TODO
        return false;
    }

    public boolean showResult(){
        //TODO
        return false;
    }

    public boolean replyResult(int answer){
        //TODO
    	//whether send to server
    	_numberOfChoice = answer;
        return false;
    }
}
package survey;

import java.util.Vector;

public class NumeralSurvey extends Survey{

    private int _numberOfChoice;

    //constructor
    public NumeralSurvey(int ID, int status, String topic) {
        super(ID, status, topic);
    	_surveyType = NUMERAL;
    }
    
    public boolean replyResult(int answer){
        //TODO
    	//whether send to server
    	_numberOfChoice = answer;
        return false;
    }
}
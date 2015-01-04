package survey;

import java.util.Vector;

public class EssaySurvey extends Survey{

	private String _answer;
    //private Vector _result;// type : String

    //constructor
    public EssaySurvey(int ID, int status, String topic) {
        super(ID, status, topic);
    	_surveyType = ESSAY;
    }

    public boolean replyResult(String answer){
    	_answer = answer;
        return true;
    }
    
    // speaker
    /*public boolean receiveAndParseResult(){
        //TODO
        return false;
    }
    public boolean showResult(){
        //TODO
        return false;
    }*/
}
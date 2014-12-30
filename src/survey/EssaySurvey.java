package survey;

import java.util.Vector;

public class EssaySurvey extends Survey{

	private String _answer;
    private Vector _result;// type : String

    //constructor
    public EssaySurvey(String topic) {
        super(topic);
        //TODO
    }

    public boolean receiveAndParseResult(){
        //TODO
        return false;
    }

    public boolean showResult(){
        //TODO
        return false;
    }

    public boolean replyResult(String answer){
        //TODO
    	//whether send to server
    	_answer = answer;
        return true;
    }
}
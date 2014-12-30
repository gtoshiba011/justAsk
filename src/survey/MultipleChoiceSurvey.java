package survey;

import java.util.Vector;

public class MultipleChoiceSurvey extends Survey{

    private int _numberOfChoice;
    private Vector _choice;// type : int

    //constructor
    public MultipleChoiceSurvey(String topic) {
        super(topic);
        _surveyType = 3;
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

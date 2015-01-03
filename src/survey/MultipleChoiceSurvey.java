package survey;

import java.util.Vector;

import org.json.JSONArray;

public class MultipleChoiceSurvey extends Survey{

    private int _numberOfChoice;
    //private JSONArray _choiceArr;// type : int

    //constructor
    public MultipleChoiceSurvey(int ID, int status, String topic, JSONArray choiceJSONArray) {
        super(ID);
    	_status = status;
    	_surveyTopic = topic;
        _surveyType = MULTIPLE;
        _choiceArr = choiceJSONArray;
    }
    public boolean replyResult(int answer){
        //TODO
    	_numberOfChoice = answer;
        return false;
    }

}

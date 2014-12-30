package survey;
import java.util.Vector;

public class Survey {

    private int _ID;
    private int _status;
    protected int _surveyType;
    /* define type ***
    undefined 	0
    TrueFalse 	1
    Numeral		2
    Multiple 	3
    Essay		4
    *** end define type */
    private String _surveyTopic;
    
    //constructor
    public Survey(String topic) {
    	//TODO
    	//send request to server to get event ID
    	_ID = 0;
    	//TODO
    	//define status; whether can answer or not
    	_status = 0;
    	_surveyType = 0;
    	_surveyTopic = topic;
    }

    public boolean updateSurveyInfo(String topic){
        _surveyTopic = topic;
        return true;
    }

    public int getSurveyID(){
        return _ID;
    }
    
    public String getSurveyTopic() {
    	return _surveyTopic;
    }

    public boolean receiveAndParseResult(){
        //TODO
        return false;
    }

    public boolean showResult(){
        //TODO
    	//activity
        return false;
    }

    public boolean replyResult(){
        //TODO
    	//send result to server
        return false;
    }
    
    public boolean changeStatus(int status){
    	_status = status;
    	return true;
    }
}

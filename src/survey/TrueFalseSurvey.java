package survey;

public class TrueFalseSurvey extends Survey{

	private boolean _boolOfChoice;

    //constructor
    //constructor
    public TrueFalseSurvey(int ID, int status, String topic) {
        super(ID);
    	_status = status;
    	_surveyTopic = topic;
    	_surveyType = TRUEFALSE;
    }

    public boolean replyResult(boolean answer){
    	_boolOfChoice = answer;
        return true;
    }
}

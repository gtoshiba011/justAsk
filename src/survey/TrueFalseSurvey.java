package survey;

public class TrueFalseSurvey extends Survey{

	private boolean _boolOfChoice;
    private int _numberOfTrue;
    private int _numberOfFalse;

    //constructor
    public TrueFalseSurvey(String topic) {
        super(topic);
        _surveyType = 1;
        _numberOfTrue = 0;
        _numberOfFalse = 0;
    }

    public boolean receiveAndParseResult(){
        //TODO
    	//parse received message from server
        return false;
    }

    public boolean showResult(){
        //TODO
        return false;
    }

    public boolean replyResult(boolean answer){
        //TODO
    	//whether send to server
    	_boolOfChoice = answer;
        return false;
    }
}

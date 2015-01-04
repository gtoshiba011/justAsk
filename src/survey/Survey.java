package survey;
import java.util.Vector;

import org.json.JSONArray;

public class Survey {

	// public constants
	public static final int TRUEFALSE = 1;
	public static final int MULTIPLE = 2;
	public static final int NUMERAL = 3;
	public static final int ESSAY = 4;
	
	public static final int INITIAL = 1;
	public static final int START = 2;
	public static final int STOP = 3;
	
	protected int _ID;
    protected int _status;	//1: initial, 2:start, 3:stop 
    protected int _surveyType;
    /* define type ***
    undefined 	0
    TrueFalse 	1
	Multiple	2
    Numeral 	3
    Essay		4
    *** end define type */
    protected JSONArray _choiceArr;// type : int
    protected String _surveyTopic;
    protected boolean _isAnswer;
    
    //constructor
    public Survey(int ID, int status, String topic) {
    	_ID = ID;
    	_status = status;
    	_surveyTopic = topic;
    	_isAnswer = false;
    }
    public boolean changeStatus(int status){
    	_status = status;
    	return true;
    }
    public boolean updateSurveyInfo(String topic){
        _surveyTopic = topic;
        return true;
    }
    // get private member
    public int getStatus(){
    	return _status;
    }
    public String getSurveyTopic(){
    	return _surveyTopic;
    }
    public int getSurveyType(){
    	return _surveyType;
    }
    public int getID(){
    	return _ID;
    }
    public JSONArray getChoiceArray(){
    	return _choiceArr;
    }
    public boolean setIsAnswer(){
    	_isAnswer = true;
    	return true;
    }
    public boolean getIsAnswer(){
    	return _isAnswer;
    }
    /*public boolean receiveAndParseResult(){
        //TODO
        return false;
    }*/
}

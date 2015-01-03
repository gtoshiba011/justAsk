package survey;
import java.util.Vector;

import org.json.JSONArray;

public class Survey {

	// public constants
	public static final int TRUEFALSE = 1;
	public static final int MULTIPLE = 2;
	public static final int NUMERAL = 3;
	public static final int ESSAY = 4;

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
    
    //constructor
    public Survey(int ID) {
    	_ID = ID;
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
    public JSONArray getChoiceArray(){
    	return _choiceArr;
    }
    
    /*public boolean receiveAndParseResult(){
        //TODO
        return false;
    }*/
}

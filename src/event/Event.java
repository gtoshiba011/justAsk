package event;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import speech.SpeechInfo;
import android.util.Log;
import manager.*;

public class Event {

    // member variable
    private int _eventID;
    private SpeechInfo _speechInfo;
    
    private QuestionManager _quesManager;
    private SurveyManager _survManager;
    private boolean _isClosed;
    
    //constructor
    public Event(int eventID) {
    	_eventID = eventID;
    	_speechInfo = new SpeechInfo(null, null, null);
    	_isClosed = false;
    	_quesManager = new QuestionManager();
    	_survManager = new SurveyManager();
    }
    public Event(int eventID, String name, String email, String topic) {
    	_eventID = eventID;
    	_speechInfo = new SpeechInfo(name, email, topic);
    	_isClosed = false;
    	_quesManager = new QuestionManager();
    	_survManager = new SurveyManager();
    }
    public boolean modifyEventInfo(String name, String email, String topic){
    	_speechInfo.modifyName(name);
    	_speechInfo.modifyEmail(email);
    	_speechInfo.modifyTopic(topic);
        return true;
    }
    public boolean modifyStatus(boolean isClosed){
    	_isClosed = isClosed;
    	return true;
    }
    public SpeechInfo getSpeechInfo(){
    	return _speechInfo;
    }
    public QuestionManager getQuestionManager(){
    	return _quesManager;
    }
    public SurveyManager getSurveyManager(){
    	return _survManager;
    }
    public int getID(){
    	return _eventID;
    }
    public boolean getStatus(){
    	return _isClosed;
    }
    public boolean exit(){
        modifyStatus(true);
        return true;
    }
    // Question
    public boolean updateQuestionInfo(JSONArray questionJSONArray){
    	int questionSize = questionJSONArray.length();
    	int count = 0;
		JSONObject questionObject = null;
		int ID = count;
		String topic = "";
		boolean isSolved = false;
		int popu = 0;
    	while(count != questionSize){
    		try {
				questionObject = questionJSONArray.getJSONObject(count);
				ID = count;
				topic = questionObject.getString("Question_Topic");			
				String str = questionObject.getString("Status");
				if(str.equals("un-solved"))
					isSolved = false;
				else if(str.equals("solved"))
					isSolved = true;
				else
					Log.e("updateQuestionInfo", "else ERROR");
				popu = questionObject.getInt("Question_Popular");
			} catch (JSONException e) {
				e.printStackTrace();
				Log.e("updateQuestionInfo", "ERROR");
				return false;
			}
    		Log.i("updateQuestionInfo", "ID: " + Integer.toString(ID) + " topic: " + topic + " isSolved: " + Boolean.toString(isSolved) + " popu: " + Integer.toString(popu) );
        	_quesManager.createQuestion(ID, topic, isSolved, popu);
    		// update count
        	count++;
    	}
    	return true;
    }
    public boolean incrPopu(int QID){
    	return _quesManager.increasePopu(QID);
    }
    public boolean decrPopu(int QID){
    	return _quesManager.decreasePopu(QID);
    }
    public boolean createQuestion(int QID, String topic){
    	return _quesManager.createQuestion(QID, topic, false, 0);
    }
    public boolean changeQuestionStatus(int QID, boolean status){
    	return _quesManager.changeStutus(QID, status);
    }
    // Survey
    public boolean updateSurveyInfo(JSONArray surveyJSONArray){
    	int surveySize = surveyJSONArray.length();
    	int count = 0;
		JSONObject surveyObject = null;
		int ID = count;
		int status = 0;
		String topic = "";
		int type = 0;
		JSONArray choiceJSONArray= null;
    	while ( surveySize != count){
			try {
				surveyObject = surveyJSONArray.getJSONObject(count);
				ID = count;
	    		String str = surveyObject.getString("Status");
	    		if(str.equals("initial"))
	    			status = 1;
	    		else if(str.equals("start"))
	    			status = 2;
	    		else if(str.equals("stop"))
	    			status = 3;
	    		else
					Log.e("updateSurveyInfo", "else ERROR");
	    		topic =  surveyObject.getString("Survey_Topic");
	    		type = surveyObject.getInt("Survey_Type");
	    		if(type == 2)
	    			choiceJSONArray =  surveyObject.getJSONArray("Choice");
			} catch (JSONException e) {
				e.printStackTrace();
				Log.e("updateSurveyInfo", "ERROR");
				return false;
			}
    		_survManager.createSurvey(ID, status, topic, type, choiceJSONArray);
    		// update count
    		count++;
        }
        return true;
    }
    public boolean createSurvey(int ID, String topic, int surveyType){
    	return _survManager.createSurvey(ID, 1, topic, surveyType, null);
    }
    public boolean createSurvey(int ID, String topic, int surveyType, JSONArray choiceJSONArray){
    	return _survManager.createSurvey(ID, 1, topic, surveyType, choiceJSONArray);
    }
    public boolean changeSurveyStatus(int SID, int status){
    	return _survManager.changeSurveyStatus(SID, status);
    }
    
}

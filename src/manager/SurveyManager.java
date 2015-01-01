package manager;

import java.util.Hashtable;

import org.json.JSONArray;

import android.util.Log;
import survey.*;

public class SurveyManager {

    private Hashtable<Integer, Survey> _mySurveyTable;// type : Survey*

    //constructor
    public SurveyManager() {
    	_mySurveyTable = new Hashtable<Integer, Survey>();
    }    
    private Survey getSurvey(int ID){
    	return _mySurveyTable.get(ID);
    }
    public boolean createSurvey(int ID, int status, String topic, int surveyType, JSONArray choiceJSONArray){
    	Survey survey;
    	switch(surveyType){
    	case Survey.TRUEFALSE:
    		survey = new TrueFalseSurvey(ID, status, topic);
    		break;
    	case Survey.MULTIPLE:
    		survey = new MultipleChoiceSurvey(ID, status, topic, choiceJSONArray);
    		break;
    	case Survey.NUMERAL:
    		survey = new NumeralSurvey(ID, status, topic);
    		break;
    	case Survey.ESSAY:
    		survey = new EssaySurvey(ID, status, topic);
    		break;
    	default:
    		Log.e("SurveyManager", "createSurvey(): error type");
    		return false;    			
    	}
    	addToSurveyList(ID, survey);
    	return true;
    } 
    public void addToSurveyList(int surveyID, Survey survey){
    	_mySurveyTable.put(surveyID,  survey);
    }
    public boolean deleteFromSurveyList(int surveyID){
    	if( !_mySurveyTable.containsKey(surveyID)){
    		Log.e("SruveyManager", "aadToSurveyList(): find no survey ID" + Integer.toString(surveyID) );
    		return false;
    	}
    	_mySurveyTable.remove(surveyID);
    	return true;
    }
    public boolean changeSurveyStatus(int SID, int status){
    	if( getSurvey(SID).getStatus() == status)
    		return true;
    	if(status == 1){ //initial
    		//TODO
    		return getSurvey(SID).changeStatus(status);
    	}
    	else if(status == 2){ //start
    		//TODO
    		return getSurvey(SID).changeStatus(status);
    	}
    	else if(status == 3){ //stop
    		//close survey
    		return closeSurvey(SID);
    	}
    	else{
    		Log.e("SurveyManager::changeSurveyStatus()", "else ERROR");
    		return false;
    	}
    }  
    public boolean closeSurvey(int SID){
    	deleteFromSurveyList(SID);
    	return true;
    }
    
    public void sortSurveyList(){
        //TODO
    	//how to sort list
    }
}
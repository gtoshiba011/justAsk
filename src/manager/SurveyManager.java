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
    public Hashtable<Integer, Survey> getServeyTable(){
    	return _mySurveyTable;
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
    		Log.e("SurveyManager::createSurvey()", "case, error type");
    		return false;    			
    	}
    	addToSurveyList(ID, survey);
    	return true;
    } 
    public boolean addToSurveyList(int surveyID, Survey survey){
    	if( _mySurveyTable.containsKey(surveyID)){
    		Log.e("SruveyManager::addToSurveyList()", "already has survey ID " + surveyID );
    		return false;
    	}
    	_mySurveyTable.put(surveyID,  survey);
    	return true;
    }
    public boolean deleteFromSurveyList(int surveyID){
    	if( !_mySurveyTable.containsKey(surveyID)){
    		Log.e("SruveyManager::deleteFromSurveyList()", "find no survey ID " + surveyID );
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
}
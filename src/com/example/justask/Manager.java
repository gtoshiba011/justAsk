package com.example.justask;

import java.util.Hashtable;

import org.json.JSONArray;

import QR.QR;
import event.Event;
import android.app.Application;
import android.util.Log;

public class Manager extends Application{
	
    // member variable
	// 0: not join event
	// 1: join as speaker
	// 2: join as audience
	//private int _identity;
    private int _joinEventID;
    private Hashtable<Integer, Event> _myEventTable;
    //private AccountManager _accountManager;

    //constructor
    public Manager() {
        //TODO
    	//_identity = 2;
    	_joinEventID = 0;
    	_myEventTable = new Hashtable<Integer, Event>();
    	//_accountManager = new AccountManager();
    }
    public Event getEvent(int eventID){
    	if(_myEventTable.containsKey(eventID))
    		return _myEventTable.get(eventID);
    	else 
    		return null;
    }
    public boolean addToEventTable(int eventID, Event event){
    	_myEventTable.put(eventID, event);
        return true;
    }
    public void setEventID(int eventID){
    	_joinEventID = eventID;
    }
    public int getJoinEventID(){
    	return _joinEventID;
    }
    //Server to client mission
    // M1
    public void modifiedEventInfo(int eventID, String name, String email, String topic){
    	getEvent(eventID).modifyEventInfo(name, email, topic);
    }    
    // M2
    public boolean createSurvey(int eventID, int SID, int type, String topic){
    	return getEvent(eventID).createSurvey(SID, topic, type);
    }
    public boolean createSurvey(int eventID, int SID, int type, String topic, JSONArray choiceJSONArray){
    	return getEvent(eventID).createSurvey(SID, topic, type, choiceJSONArray);
    }
    // M3 no-used
    // M4
    public boolean createQuestion(int eventID, int QID, String topic){
    	return getEvent(eventID).createQuestion(QID, topic);
    }
    // M5
    public boolean incrPopu(int eventID, int QID){
    	return getEvent(eventID).incrPopu(QID);
    }
    // M6
    public boolean decrPopu(int eventID, int QID){
    	return getEvent(eventID).decrPopu(QID);
    }
    // M7
    public boolean chagneQuestionStatus(int eventID, int QID, String status){
    	boolean isSolved = (status == "solved") ? true : false;
    	return getEvent(eventID).changeQuestionStatus(QID, isSolved);
    }
    // M8
    public boolean changeSurveyStatus(int eventID, int SID, String status){
    	int st = 0;
    	if(status == "initial")
    		st = 1;
    	else if(status == "start")
    		st = 2;
    	else if(status == "stop")
    		st = 3;
    	return getEvent(eventID).changeSurveyStatus(SID, st);
    }
    // M9
    public boolean closeEvent(int eventID){
    	if(getEvent(eventID).getStatus() == false){
    		Log.e("Manager", "clsoeEvent");
    		return false;
    	}
    	else{
        	_joinEventID = 0;
    		return getEvent(eventID).exit();
    	}
    }
    // M10
    public boolean createEvent(int eventID){
    	if(_myEventTable.containsKey(eventID)){
    		Log.e("Manager","createEvent");
    		return false;
    	}
    	Event event = new Event(eventID);
    	_joinEventID = eventID;
    	addToEventTable(eventID, event);
    	return true;
    }
    // M11
    public boolean updateEventInfo(int eventID, String name, String email, String topic, JSONArray surveyJSONArray, JSONArray questionJSONArray){	
    	Event event = getEvent(eventID);
    	event.modifyEventInfo(name, email, topic);
        return ( event.updateSurveyInfo(surveyJSONArray)&&event.updateQuestionInfo(questionJSONArray) );
    }


	
    //activity
    public boolean showEventInfo(){
        //TODO
        return true;
    }


    
   
    /*
    // Speaker
    public void setIdentiy(int identity){
    	_identity = identity;
    }
    // History
    public boolean showEventTable(){
        //TODO
        return false;
    }
    public boolean updateEvent(){
    	//TODO
    	return false;
    }    
    //account
    public boolean saveEventToAPI(long eventID){
        //TODO
        return false;
    }
    //login
    public boolean synchronizeEvent(){
        //TODO
        return false;
    }
    // No use
    public boolean scanQRCode(){
        //TODO
        return true;
    }
    public long convertQRcode(QR QRcode){
        //TODO
        return 0;
    }
    public void createEvent(int eventID, String name, String email, String topic) {
    	Event event = new Event(eventID, name, email, topic);
    	_joinEventID = eventID;
    	addToEventTable(eventID, event);
    }*/
    
}

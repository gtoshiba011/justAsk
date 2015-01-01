package manager;

import question.Question;

import java.util.*;

import android.util.Log;

public class QuestionManager {

    // member variable
	private Hashtable<Integer, Question> _allTable;
    private Hashtable<Integer, Question> _unSolvedTable;
    private Hashtable<Integer, Question> _solvedTable;

    //constructor
    public QuestionManager() {
    	_allTable = new Hashtable<Integer, Question>();
    	_unSolvedTable = new Hashtable<Integer, Question>();
    	_solvedTable = new Hashtable<Integer, Question>();
    }
    public boolean createQuestion(int ID, String title, boolean isSolved, int popu){
    	Question question = new Question(ID, title, isSolved, popu);
    	addQuestionToList(ID,question);
    	if(isSolved)
    		addQuestionToSolvedList(ID);
    	else
    		addQuestionToUnsolvedList(ID);
    	return true;
    }
    public Question getQuestion(int quesID){
    	return _allTable.get(quesID);
    }
    public Hashtable<Integer, Question> getAllTable(){
    	return _allTable;
    }
    public Hashtable<Integer, Question> getUnSolvedTable(){
    	return _unSolvedTable;
    }
    public Hashtable<Integer, Question> getSolvedTable(){
    	return _solvedTable;
    }
    public boolean changeStutus(int quesID, boolean isSolved){
    	if( getQuestion(quesID).isSolved() == isSolved)
    		return true;
    	else{
    		getQuestion(quesID).setStatus(isSolved);
    		if(isSolved)
    			return ( addQuestionToSolvedList(quesID) && deleteQuestionFromUnsolvedList(quesID) );
    		else
    			return ( addQuestionToUnsolvedList(quesID) && deleteQuestionFromSolvedList(quesID) );
    	}
    }
    public boolean increasePopu(int QID){
    	return getQuestion(QID).increasePopu();
    }
    public boolean decreasePopu(int QID){
    	return getQuestion(QID).decreasePopu();
    }
    public boolean addQuestionToList(int ID, Question question){
    	if(!_allTable.containsKey(question.getQuestionID())){
    		_allTable.put(ID, question);
    		return true;
    	}
    	else{
    		Log.e("QuestionManager", "addQuestionToList()");
        	return false;
    	}	
    }
    public boolean deleteQuestionFromList(int quesID){
    	if(_allTable.containsKey(quesID)){
    		_allTable.remove(quesID);
    		return true;
    	}
    	else{
    		Log.e("QuestionManager", "deleteQuestionFromList()");
        	return false;
    	}
    }
    public boolean addQuestionToUnsolvedList(int quesID){
    	Question question = getQuestion(quesID);
    	if(!_unSolvedTable.containsKey(quesID)){
    		_unSolvedTable.put(quesID, question);
    		return true;
    	}
    	else{
    		Log.e("QuestionManager", "addQuestionToUnsolvedList()");
        	return false;
    	}	
    }
    public boolean deleteQuestionFromUnsolvedList(int quesID){
    	if(_unSolvedTable.containsKey(quesID)){
    		_unSolvedTable.remove(quesID);
    		return true;
    	}
    	else{
    		Log.e("QuestionManager", "deleteQuestionFromUnsolvedList()");
        	return false;
    	}
    }
    public boolean addQuestionToSolvedList(int quesID){
    	Question question = getQuestion(quesID);
    	if(!_solvedTable.containsKey(quesID)){
    		_solvedTable.put(quesID, question);
    		return true;
    	}
    	else{
    		Log.e("QuestionManager", "addQuestionToSolvedList(): no this ID "
    				+ Integer.toString(quesID));
        	return false;
    	}
    }
    public boolean deleteQuestionFromSolvedList(int quesID){
    	if(_solvedTable.containsKey(quesID)){
    		_solvedTable.remove(quesID);
    		return true;
    	}
    	else{
    		Log.e("QuestionManager", "deleteQuestionFromSolvedList(): no this ID "
    				+ Integer.toString(quesID));
        	return false;
    	}
    } 
    
    
	//activity
    public void showQuestionList(){
        //TODO
    }
    public boolean updateQuestionList(){
        //TODO
    	//connect to server and update all table
        return true;
    }
    //sort unsolved question by 'like' amount
    public boolean sortQuestion(){
        //TODO
    	//is this function necessary?
        return false;
    }   
}

package manager;

import question.Question;

import java.util.*;
import java.util.Map.Entry;

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
    		Log.e("QuestionManager::addQuestionToList()", "already has question ID " + ID);
        	return false;
    	}	
    }
    public boolean deleteQuestionFromList(int ID){
    	if(_allTable.containsKey(ID)){
    		_allTable.remove(ID);
    		return true;
    	}
    	else{
    		Log.e("QuestionManager::deleteQuestionFromList()", "doesn't have question ID " + ID);
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
    		Log.e("QuestionManager::addQuestionToUnsolvedList()", "already has question ID " + quesID);
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
    		Log.e("QuestionManager::deleteQuestionFromUnsolvedList()", "doesn't have question ID " + quesID);
        	return false;
    	}
    }
    public boolean deleteQuestionFromSolvedList(int quesID){
    	if(_solvedTable.containsKey(quesID)){
    		_solvedTable.remove(quesID);
    		return true;
    	}
    	else{
    		Log.e("QuestionManager::deleteQuestionFromSolvedList()", "doesn't have question ID " + quesID);

        	return false;
    	}
    }    
    //sort unsolved question by 'like' amount
    //NO-USE NOW
    public boolean sortQuestion(){
        //TODO
    	Hashtable<Integer, Integer> tempTable = new Hashtable<Integer, Integer>();
    	Enumeration<Integer> enumkey = _unSolvedTable.keys();
    	// add new Hashtable<Integer, Integer>;
    	while(enumkey.hasMoreElements()){
    		int key = enumkey.nextElement();
    		tempTable.put(key, _unSolvedTable.get(key).getPopu());
    	}
        ArrayList<Map.Entry<Integer, Integer>> tempList = new ArrayList<Map.Entry<Integer, Integer>>(tempTable.entrySet());
        Collections.sort(tempList, new Comparator<Map.Entry<?, Integer>>(){

        	public int compare(Map.Entry<?, Integer> o1, Map.Entry<?, Integer> o2) {
        		return o1.getValue().compareTo(o2.getValue());
        	}});
        Hashtable<Integer, Question> sortedTable = new Hashtable<Integer, Question>();
        for(Iterator<Entry<Integer, Integer>> it = tempList.iterator(); it.hasNext();){
        	Map.Entry<Integer, Integer> entry = (Map.Entry<Integer, Integer>)it.next();
        	Log.i("QuestionManager::sortQuestion()", "key: " + Integer.toString(entry.getKey()) + "value: " + Integer.toString(entry.getValue()));
        	sortedTable.put((Integer) entry.getKey(), _unSolvedTable.get(entry.getKey()));
        }
        _unSolvedTable = sortedTable;
        return true;
    }
}

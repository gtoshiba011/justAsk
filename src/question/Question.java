package question;

import android.util.Log;

public class Question {

    // member variable
    private int _ID;
    private String _topic;
    private boolean _isSolved;	// true: solved; false: unsolved
    private int _popu;
    private boolean _like;
    
    //constructor
    public Question(int ID, String topic, boolean isSolved, int popu) {
    	_ID = ID;
        _topic = topic;
        _isSolved = isSolved;
        _popu = popu;
        _like = false;
    }
    // function
    // get private member variable
    public int getQuestionID(){
        return _ID;
    }
    public String getQuestionTopic(){
        return _topic;
    }
    public boolean isSolved(){
        return _isSolved;
    }
    public int getPopu(){
        return _popu;
    }
    public boolean isLiked(){
    	return _like;
    }
    // change private member
    public boolean increasePopu(){
    	_popu++;
    	return true;
    }
    public boolean decreasePopu(){
    	_popu--;
    	return true;
    }
    public boolean setPopu(int popu){
    	_popu = popu;
    	return true;
    }
    public boolean setStatus(boolean status){
    	_isSolved = status;
        return true;
    }
    public boolean setLike(boolean like){
    	_like = like;
    	return true;
    }
}

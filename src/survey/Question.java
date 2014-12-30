package question;

import android.util.Log;

public class Question {

    // member variable
    private int _ID;
    private String _title;
    private boolean _isSolved;	// true: solved; false: unsolved
    private int _popu;

    //constructor
    public Question(int ID, String title, boolean isSolved, int popu) {
    	//TODO
    	//send request to server to get event ID
    	_ID = 0;
        _title = title;
        _isSolved = isSolved;
        _popu = popu;
    }
    
    // function
    // get private member variable
    public int getQuestionID(){
        return _ID;
    }
    public String getQuestionTitle(){
        return _title;
    }
    public boolean isSolved(){
        return _isSolved;
    }
    public int getPopu(){
        return _popu;
    }
    
    // change private member
    public boolean increasePopu(int incr){
    	if(incr < 0){
    		Log.e("Question", "function increasePopu() value error");
    		return false;
    	}
    	else{
    		_popu += incr;
    		return true;
    	}
    }
    public boolean decreasePopu(int decr){
    	if(decr < 0){
    		Log.e("Question", "function decreasePopu() value error");
    		return false;
    	}
    	else{
    		_popu -= decr;
    		if( _popu < 0){
    			Log.e("Question", "_popu < 0");
    			_popu = 0;
    			return false;
    		}
    		return true;
    	}
    }
    public boolean setStatus(boolean status){
    	_isSolved = status;
        return true;
    }
}

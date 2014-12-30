package question;

import android.util.Log;

public class Question {

    // member variable
    private int _ID;
    private String _title;
    private boolean _status;	// true: solved; false: unsolved
    private int _popu;
    private boolean _like;

    //constructor 
    public Question(int id, String title) {
        _ID = id;
        _title = title;
        _status = false;
        _popu = 0;
        _like = false;
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
        return _status;
    }
    public int getPopu(){
        return _popu;
    }
    public boolean isLiked(){
    	return _like;
    }
    
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
    
    public boolean setPopu(int popu){
    	_popu = popu;
    	return true;
    }
    
    public boolean setStatus(boolean status){
    	_status = status;
        return true;
    }
    
    public boolean setLike(boolean like){
    	_like = like;
    	return true;
    }

    // main
    public static void main(String[] args){
        //TODO
    }
}

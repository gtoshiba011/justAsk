package speech;
public class SpeechInfo {

    // member varibale
    private String _speakerName;
    private String _speakerEmail;
    private String _speakerTopic;

    //constructor
    public SpeechInfo(String speakerName, String speakerEmail, String speakerTopic) {
        _speakerName = speakerName;
        _speakerEmail = speakerEmail;
        _speakerTopic = speakerTopic;       
    }

    // function
    public boolean modifyName(String name){
    	_speakerName = name;
        return true;
    }
    public boolean modifyEmail(String email){
        _speakerEmail = email;
        return true;
    }
    public boolean modifyTopic(String topic){
        _speakerTopic = topic;
        return true;
    }
    
    // get member
    public String getName(){
    	return _speakerName;
    }
    public String getEmail(){
    	return _speakerEmail;
    }
    public String getTopic(){
    	return _speakerTopic;
    }
}

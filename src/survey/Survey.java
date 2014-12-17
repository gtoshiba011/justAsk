package survey;
import java.util.Vector;

public class Survey {

    private int _ID;
    private int _status;
    private int _surveyType;
    private String _surveyTopic;

    //constructor
    public Survey(int id, int type, String topic ) {
        //TODO
    }

    public boolean updateSurveyInfo(String topic){
        //TODO
        return false;
    }

    public int getSurveyID(){
        return _ID;
    }

    public boolean receiveAndParseResult(){
        //TODO
        return false;
    }

    public boolean showResult(){
        //TODO
        return false;
    }

    public boolean replyResult(){
        //TODO
        return false;
    }
}

class TrueFalseSurvey extends Survey{

    private int _numberOfTrue;
    private int _numberOfFalse;
    private Vector Topic;// type : string

    //constructor
    public TrueFalseSurvey(int id, int type, String topic) {
        super(id, type, topic);
        //TODO
    }

    public boolean receiveAndParseResult(){
        //TODO
        return false;
    }

    public boolean showResult(){
        //TODO
        return false;
    }

    public boolean replyResult(){
        //TODO
        return false;
    }
}

class NumeralSurvey extends Survey{

    private int _numberOfChoice;
    private Vector _choice;// type : int

    //constructor
    public NumeralSurvey(int id, int type, String topic) {
        super(id, type, topic);
        //TODO
    }

    public boolean receiveAndParseResult(){
        //TODO
        return false;
    }

    public boolean showResult(){
        //TODO
        return false;
    }

    public boolean replyResult(){
        //TODO
        return false;
    }
}

class MultipleChoiceSurvey extends Survey{

    private int _numberOfChoice;
    private Vector _choice;// type : int

    //constructor
    public MultipleChoiceSurvey(int id, int type, String topic) {
        super(id, type, topic);
        //TODO
    }

    public boolean receiveAndParseResult(){
        //TODO
        return false;
    }

    public boolean showResult(){
        //TODO
        return false;
    }

    public boolean replyResult(){
        //TODO
        return false;
    }
}

class EssaySurvey extends Survey{

    private Vector _result;// type : String

    //constructor
    public EssaySurvey(int id, int type, String topic) {
        super(id, type, topic);
        //TODO
    }

    public boolean receiveAndParseResult(){
        //TODO
        return false;
    }

    public boolean showResult(){
        //TODO
        return false;
    }

    public boolean replyResult(){
        //TODO
        return false;
    }
}

package History;

public class History {
	private int _id;
	private String _eventName;
	private String _presenter;
	
	
	public History()
	{
		this._eventName=null;
		this._presenter=null;
		this._id=0;
	}
	public History( int id, String eventName, String presenter) {
		super();
		this._id = id;
		this._eventName = eventName;
		this._presenter = presenter;
	}
	
	public int getId() {
		return _id;
	}
	public String getEventName() {
		return _eventName;
	}
	public String getPresenter() {
		return _presenter;
	}

	
}

package com.example.justask;

// import socket dictionary
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;
import org.json.JSONObject;

import question.Question;
import survey.Survey;
import android.app.Fragment;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
//global manager
//json object
//import android.view.Menu;
//import android.view.MenuInflater;

public class MainActivity extends SherlockFragmentActivity {

	private DrawerLayout mDrawerLayout;
	private LinearLayout mDrawer;
	private ActionBarDrawerToggle mDrawerToggle;
	
	private NonSwipeableViewPager viewPager;
	private TabsPagerAdapter mAdapter;
	private ActionBar actionBar;
	
	//private ExpandableListView elv = (ExpandableListView)findViewById(R.id.mExpandableListView);
	private ExpandableListView elv;
	
	// socket object
	private static WebSocketClient mWebSocketClient;
	// application
	Manager manager;
	
	// Buttons
	private Button btnEditSave, btnWebCode;
	private Button addQuestionBut;

	// Tabs titles
	private String[] tabsTitles = {"Profile", "Questions", "Survey"};
	private int[] tabDrawable = {R.drawable.ic_profile, R.drawable.ic_question, R.drawable.ic_survey};
	
	// Question list
	//protected QuestionDbHelper qdb;
	List<Map<String, Question>> qlist;
	List<Map<String, String>> group;
	List<List<Map<String, Question>>> child;
	ExpandableAdapter adapter;
	
	List<Map<String, String>> groups;
	Map<String, String> group_unSloved;
	Map<String, String> group_sloved;

	// Survey list
	//protected SurveyDbHelper sdb;
	List<Survey> slist;
	MyAdapter adapt;
	List<Survey> surveyList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);		
		
		//manager
		manager = (Manager) getApplicationContext();
		Log.i("MainActivity::manager", Integer.toString(manager.getJoinEventID()));
		
		// socket connection
		connectWebSocket();
		
		// Drawer
		mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
		mDrawer = (LinearLayout)findViewById(R.id.drawer);
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
		mDrawer.setBackgroundColor(Color.parseColor("#f02F6877"));

		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setIcon(android.R.color.transparent);
		
		//Event Tab Layout Initialization
		final TabHost tabHost=(TabHost)findViewById(android.R.id.tabhost);
		tabHost.setup();

		mDrawerToggle = new ActionBarDrawerToggle(
				this,
				mDrawerLayout,
				R.drawable.ic_drawer,
				R.string.drawer_open,
				R.string.drawer_close) {
			
			public void onDrawerClosed(View view) {
				getSupportActionBar().setTitle( tabsTitles[tabHost.getCurrentTab()] );
				super.onDrawerClosed(view);
			}
			
			public void onDrawerOpened(View drawerView) {
				// Set the title on the action when drawer open
				getSupportActionBar().setTitle("JustAsk");
				super.onDrawerOpened(drawerView);
			}
		};
		
		mDrawerLayout.setDrawerListener(mDrawerToggle);
		
		for (int i = 0; i < tabsTitles.length; i++) {
			TabHost.TabSpec spec=tabHost.newTabSpec( tabsTitles[i] );
			spec.setContent(R.id.fakeTabContent);
			Drawable dwb = getResources().getDrawable( tabDrawable[i] );
			if( i==0 )
				dwb.mutate().setColorFilter( 0xffffffff, Mode.MULTIPLY);
			else
				dwb.mutate().setColorFilter( 0xffaaaaaa, Mode.MULTIPLY);
			spec.setIndicator( null, dwb );
			tabHost.addTab(spec);
			//tabHost.getTabWidget().getChildAt(i).setVisibility(View.GONE);
			tabHost.getTabWidget().getChildAt(i).setBackgroundColor(Color.parseColor("#f02F6877"));
			TextView t = (TextView)tabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title);
			t.setTextColor(Color.parseColor("#FFFFFF"));
		}
		//tabHost.getTabWidget().setStripEnabled( true );
		getSupportActionBar().setTitle( tabsTitles[0] );
		View v = (View) tabHost.getParent();
		v.setBackgroundColor(Color.parseColor("#ff2F6877"));
		
		// Set question list titles
		/*group = new ArrayList<Map<String, String>>();
		Map<String, String> unsolved = new HashMap<String, String>();
		unsolved.put("group", "Unsolved");
		group.add(unsolved);
		Map<String, String> solved = new HashMap<String, String>();
		solved.put("group", "Solved");
		group.add(solved);*/
		
		// Question Tab
		groups = new ArrayList<Map<String, String>>();
		group_unSloved = new HashMap<String, String>();
		group_sloved = new HashMap<String, String>();
		group_unSloved.put("group", "Un-Solved Question");
		group_sloved.put("group", "Solved Question");
		groups.add(group_unSloved);
		groups.add(group_sloved);
		
		//addQuestionBut = (Button)findViewById()
		
		// Allocate address to question list
		/*qlist = new ArrayList<Map<String,Question>>();
		child = new ArrayList<List<Map<String,Question>>>();*/

		// Allocate address to survey list
		slist = new ArrayList<Survey>();
		
		viewPager = (NonSwipeableViewPager) findViewById(R.id.pager);
		actionBar = getSupportActionBar();
		mAdapter = new TabsPagerAdapter(getSupportFragmentManager(), tabsTitles.length);
		viewPager.setAdapter(mAdapter);
		tabHost.setOnTabChangedListener(new OnTabChangeListener() {

			@Override
			public void onTabChanged(String tabId) {
				//ActionBar actionBar = getActionBar();
				for (int i = 0; i < tabsTitles.length; i++) {
					ImageView iv = (ImageView)tabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.icon);
					Drawable dw = getResources().getDrawable( tabDrawable[i] );
					dw.mutate().setColorFilter( 0xffaaaaaa, Mode.MULTIPLY);	
					if (tabId.equals(tabsTitles[i])) {
						getSupportActionBar().setTitle( tabsTitles[i] );
						dw.mutate().setColorFilter( 0xffffffff, Mode.MULTIPLY);		
						viewPager.setCurrentItem(i, false);
						if( i == 0){
							updateInfo();
						}
						else if( i == 1 ){							
							updateQuestionlist(true, true);
						}
						else if( i == 2 ){
							updateSurveylist();
						}
					}
					iv.setImageDrawable(dw);
				}
			}
		});

		viewPager.setOnPageChangeListener(new NonSwipeableViewPager.OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {
				tabHost.setCurrentTab(position);
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
			}
		});
		
		getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#2F6877")));
		
		// join an event again
		joinEvent(manager.getJoinEventID());
		
		// update information
		//updateInfo();
		//updateQuestionlist(elv.isGroupExpanded(0), elv.isGroupExpanded(1));
		//updateSurveylist();
	}
	
	@Override
    protected void onStart() {
        super.onStart();
        //updateInfo();
        Log.d("MainActivity:onStart","update info in textview finished");
    }
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getSupportMenuInflater();
        inflater.inflate(R.menu.main_page_drawer, menu);
        return super.onCreateOptionsMenu(menu);
    }
	
	public void updateInfo(){
		TextView event_id_text =  (TextView) findViewById(R.id.EventID);
		TextView name_text =  (TextView) findViewById(R.id.txvPresenter);
        TextView mail_text =  (TextView) findViewById(R.id.txvEmail);
        TextView topic_text =  (TextView) findViewById(R.id.txvTopic);
		event_id_text.setText(Integer.toString(manager.getJoinEventID()));
		name_text.setText(manager.getEvent(manager.getJoinEventID()).getSpeechInfo().getName());
        mail_text.setText(manager.getEvent(manager.getJoinEventID()).getSpeechInfo().getEmail());
        topic_text.setText(manager.getEvent(manager.getJoinEventID()).getSpeechInfo().getTopic());
	}

	public void updateSurveylist(){
		surveyList = new ArrayList<Survey>();
		Hashtable<Integer, Survey> surveyHash = manager.getEvent(manager.getJoinEventID()).getSurveyManager().getServeyTable();
		Enumeration<Integer> enumKey = surveyHash.keys();
		while(enumKey.hasMoreElements()){
			Integer key = enumKey.nextElement();
			surveyList.add(surveyHash.get(key));
		}
		adapt = new MyAdapter(MainActivity.this, R.layout.survey_item_view, surveyList);
		ListView listTask = (ListView) findViewById(R.id.listView1);
		listTask.setAdapter(adapt);
	}
	
	public void updateQuestionlist( boolean group0, boolean group1){

		/*child = new ArrayList<List<Map<String,Question>>>();
		List<Map<String, Question>> unsolved, solved;
		unsolved = new ArrayList<Map<String, Question>>();
		solved = new ArrayList<Map<String, Question>>();
		
		Iterator<Map<String, Question>> it = qlist.iterator();
		while( it.hasNext() ){
			Map<String, Question> temp = it.next();
			Question questionNow = temp.get("child");
			if( questionNow.isSolved() )
				solved.add(temp);
			else
				unsolved.add(temp);
		}
		child.add(unsolved);
		child.add(solved);*/
		
		//group_unSloved's children
		List<Map<String, Question>> child_unSloved = new ArrayList<Map<String, Question>>();
		Hashtable<Integer, Question> unSolvedTable = manager.getEvent(manager.getJoinEventID()).getQuestionManager().getUnSolvedTable();
		Enumeration<Integer> enumKey = unSolvedTable.keys();
		while(enumKey.hasMoreElements()){
			Map<String, Question> data = new HashMap<String, Question>();
			Integer key = enumKey.nextElement();
			Question question = unSolvedTable.get(key);
			data.put("child", question);
			child_unSloved.add(data);
		}
		
		//group_sloved's children
		List<Map<String, Question>> child_sloved = new ArrayList<Map<String, Question>>();
		Hashtable<Integer, Question> solvedTable = manager.getEvent(manager.getJoinEventID()).getQuestionManager().getSolvedTable();
		enumKey = solvedTable.keys();
		while(enumKey.hasMoreElements()){
			Map<String, Question> data = new HashMap<String, Question>();
			Integer key = enumKey.nextElement();
			Question question = solvedTable.get(key);
			data.put("child", question);
			child_sloved.add(data);
		}
		
		// add child_unSolved, child_solved to childs
		ArrayList<List<Map<String,Question>>> childs = new ArrayList<List<Map<String,Question>>>();
		childs.add(child_unSloved);
		childs.add(child_sloved);
				
		adapter = new ExpandableAdapter(MainActivity.this, groups, childs);

		elv = (ExpandableListView)findViewById(R.id.mExpandableListView);
		elv.setAdapter(adapter);
		if( group0 ) elv.expandGroup(0);
		if( group1 ) elv.expandGroup(1);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			if (mDrawerLayout.isDrawerOpen(mDrawer)) {
				mDrawerLayout.closeDrawer(mDrawer);
			} else {
				mDrawerLayout.openDrawer(mDrawer);
			}
		}
		//close activity after pressing action_home buttom in action bar
		else if(item.getItemId() == R.id.action_home) {
			mWebSocketClient.close();
			MainActivity.this.finish(); //close Activity
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		mDrawerToggle.syncState();
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggles
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

    // For buttons clicked events function
    public void EditProfile(View v){
    	// Buttons
    	btnEditSave = (Button)findViewById(R.id.btnEditSave);
    	btnWebCode = (Button)findViewById(R.id.btnWebCode);
    	 
    	if( (Integer)btnEditSave.getTag() == 0 ){
    		btnEditSave.setText("Save");
    		btnWebCode.setText("Cancel");
    		btnEditSave.setTag(1);
    		btnWebCode.setTag(1);
    	}
    	else {
    		btnEditSave.setText("Edit");
    		btnWebCode.setText("Web Code");
    		btnEditSave.setTag(0);
    		btnWebCode.setTag(0);
    	}
    }
    
    public void WebCode(View v){
    	// Buttons
    	btnEditSave = (Button)findViewById(R.id.btnEditSave);
    	btnWebCode = (Button)findViewById(R.id.btnWebCode);
    	
    	if( (Integer)btnEditSave.getTag() == 1 ){
    		btnEditSave.setText("Edit");
    		btnWebCode.setText("Web Code");
    		btnEditSave.setTag(0);
    		btnWebCode.setTag(0);
    	}
    	else{
    		
    	}
    }
    
    
    // Question List Manager
	public void addQuestionNow(View v) {
		
		EditText t = (EditText) findViewById(R.id.edtQuestion);
		String s = t.getText().toString();
		if (s.equalsIgnoreCase("")) {
			Toast.makeText(this, "enter the question description first!!",Toast.LENGTH_LONG);
		} else {
			int eventID = manager.getJoinEventID();
			askQuestion(eventID, s);
			t.setText("");
			/*Question question = new Question(0, s, false, 0);
			Log.d("question list", "data added");
			t.setText("");;
			Map<String, Question> newQuestion = new HashMap<String, Question>();
			newQuestion.put("child", question);
			qlist.add(newQuestion);
			child.get(0).add(newQuestion);
			adapter.notifyDataSetChanged();*/
		}
	}

	/*
	public void addSurveyNow(View v) {
		
		EditText t = (EditText) findViewById(R.id.edtSurvey);
		String s = t.getText().toString();
		if (s.equalsIgnoreCase("")) {
			Toast.makeText(this, "enter the question description first!!",Toast.LENGTH_LONG);
		} else {
			Survey survey = new Survey(0);
			//sdb.addSurvey(survey);
			Log.d("survey list", "data added");
			t.setText("");
			slist.add(survey);
			adapt.notifyDataSetChanged();
		}
	}
	
	//NO-USED NOW
	public void SolveQuestion(View view) {
		View v = (View) view.getParent();
		CheckBox cb = (CheckBox) v.findViewById(R.id.chkStatus);
		ToggleButton tb = (ToggleButton) v.findViewById(R.id.btnLike);
		
		Question changeQuestion = (Question) tb.getTag();
		changeQuestion.setStatus(cb.isChecked());
		
		ExpandableListView elv = (ExpandableListView)findViewById(R.id.mExpandableListView);
		updateQuestionlist( elv.isGroupExpanded(0), elv.isGroupExpanded(1) );
		adapter.notifyDataSetChanged();
	}*/
	
	public void onClickLike(View view) {
		View v = (View) view.getParent();
		ToggleButton tb = (ToggleButton) v.findViewById(R.id.btnLike);
		Question changeQuestion = (Question) tb.getTag();
		changeQuestion.setLike(tb.isChecked());
		int eventID = manager.getJoinEventID();
		int QID = changeQuestion.getQuestionID();
		if( tb.isChecked() ){
			//manager.getEvent(eventID).incrPopu(QID);
			tb.setBackgroundResource(R.drawable.button_like_clicked);
			increasePopu(eventID, QID);
		}
		else{
			//manager.getEvent(eventID).incrPopu(QID);
			tb.setBackgroundResource(R.drawable.button_like_unclicked);
			decreasePopu(eventID, QID);
		}
		//tb.setText( String.valueOf(changeQuestion.getPopu()));
	}
	
	public void sendSurveyResult(View view) {
		Survey survey = (Survey)view.getTag();
		surveyList.remove(survey);
		adapt.notifyDataSetChanged();
	}

	// Adapter for question list
	class ExpandableAdapter extends BaseExpandableListAdapter
	{
		private Context context;
		List<Map<String, String>> groups;
		List<List<Map<String, Question>>> childs;
		
		public ExpandableAdapter(Context context, List<Map<String, String>> groups, List<List<Map<String, Question>>> childs)
		{
			this.groups = groups;
			this.childs = childs;
			this.context = context;
		}

		public Object getChild(int groupPosition, int childPosition)
		{
			return childs.get(groupPosition).get(childPosition);
		}

		public long getChildId(int groupPosition, int childPosition)
		{
			return childPosition;
		}

		//獲取二級清單的View物件
		public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView,
				ViewGroup parent)
		{
			//Log.d("ExpandableAdapter", "getChileView()");
			//@SuppressWarnings("unchecked")
			Question question = ((Map<String, Question>) getChild(groupPosition, childPosition)).get("child");
			LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			//獲取二級清單對應的佈局檔, 並將其各元素設置相應的屬性
			LinearLayout linearLayout = (LinearLayout) layoutInflater.inflate(R.layout.question_item_view, null);
			TextView tv = (TextView) linearLayout.findViewById(R.id.txvQuestion);
			tv.setText(question.getQuestionTopic());

			ToggleButton btn = (ToggleButton)linearLayout.findViewById(R.id.btnLike);
			if( question.isLiked() ){
				btn.setChecked( true );
				btn.setBackgroundResource(R.drawable.button_like_clicked);
			}
			else{
				btn.setChecked( false );
				btn.setBackgroundResource(R.drawable.button_like_unclicked);
			}
			//Log.i("MainActivity::Tab", "Q ID: " + String.valueOf(question.getQuestionID()) + " popu: " + String.valueOf(question.getPopu()));
			
			btn.setText( String.valueOf(question.getPopu()) );
			btn.setTag(question);
			
			//CheckBox cb = (CheckBox) linearLayout.findViewById(R.id.chkStatus);
			
			if( question.isSolved() ) {
				//cb.setChecked(true);
				linearLayout.setBackgroundResource(R.drawable.question_solved_shape);
				btn.setBackgroundResource(R.drawable.button_like_nonclickable);
				btn.setClickable(false);
			}
			//else btn.setClickable(true);

			return linearLayout;
		}

		public int getChildrenCount(int groupPosition)
		{
			return childs.get(groupPosition).size();
		}

		public Object getGroup(int groupPosition)
		{
			return groups.get(groupPosition);
		}

		public int getGroupCount()
		{
			return groups.size();
		}

		public long getGroupId(int groupPosition)
		{
			return groupPosition;
		}

		//獲取一級清單View物件
		public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent)
		{
			String text = groups.get(groupPosition).get("group");
			LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			//獲取一級清單佈局檔,設置相應元素屬性
			LinearLayout linearLayout = (LinearLayout) layoutInflater.inflate(R.layout.question_group, null);
			TextView textView = (TextView)linearLayout.findViewById(R.id.group_tv);
			textView.setText(text);

			return linearLayout;
		}

		public boolean hasStableIds()
		{
			return false;
		}

		public boolean isChildSelectable(int groupPosition, int childPosition)
		{
			return false;
		}

	}
	
	// Adapter for survey list
	private class MyAdapter extends ArrayAdapter<Survey> {

		Context context;
		List<Survey> surveyList = new ArrayList<Survey>();
		int layoutResourceId;

		public MyAdapter(Context context, int layoutResourceId,
				List<Survey> objects) {			
			super(context, layoutResourceId, objects);
			this.layoutResourceId = layoutResourceId;
			this.surveyList = objects;
			this.context = context;
		}

		/**
		 * This method will DEFINe what the view inside the list view will
		 * finally look like Here we are going to code that the checkbox state
		 * is the status of task and check box text is the task name
		 */
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Button btn = null;
			TextView txv = null;

			Survey current = surveyList.get(position);
			//if (convertView == null) {
				LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				switch( current.getSurveyType() ){
					case Survey.TRUEFALSE:
						convertView = inflater.inflate(R.layout.survey_truefalse_view, parent, false);
						break;
					case Survey.MULTIPLE:
						convertView = inflater.inflate(R.layout.survey_multiple_view, parent, false);
						RadioGroup group;
						group = (RadioGroup)convertView.findViewById(R.id.radioGroup1);
						for(int i = 0 ; i < current.getChoiceArray().length() ; i++){
					         RadioButton radio = new RadioButton(MainActivity.this);
					         try {
								radio.setText(current.getChoiceArray().getString(i));
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
					         group.addView(radio);
					    }
						break;
					case Survey.NUMERAL:
						convertView = inflater.inflate(R.layout.survey_numeral_view, parent, false);
						break;
					case Survey.ESSAY:
						convertView = inflater.inflate(R.layout.survey_essay_view, parent, false);
						break;
					default:
						break;
				}
				txv = (TextView) convertView.findViewById(R.id.txvSurvey);
				btn = (Button) convertView.findViewById(R.id.btnSend);
				convertView.setTag(R.id.first_tag, txv);
				convertView.setTag(R.id.second_tag, btn);				
			//} 
			//else {
			//	txv = (TextView) convertView.getTag(R.id.first_tag);
			//	btn = (Button) convertView.getTag(R.id.second_tag);
			//}

			txv.setText(current.getSurveyTopic());
			txv.setTag(current);
			btn.setTag(current);

			return convertView;
		}

	}
	
	// *** socket communication start ***
    private void connectWebSocket() {
    	Log.i("MainActivity::connectWebSocket()", "Connect web socket...");
        URI uri;
        try {
        	Log.i("webSocket", "start new uri");
            uri = new URI("ws://140.112.230.230:7272");
            Log.i("webSocket", "new uri success!");
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        }
        Log.i("MainActivity::connectWebSocket()", "Connect success!");
        mWebSocketClient = new WebSocketClient(uri) {
            @Override
            public void onOpen(ServerHandshake serverHandshake) {
                Log.i("Websocket", "Opened");
                //mWebSocketClient.send("Hello from " + Build.MANUFACTURER + " " + Build.MODEL);
            }

            @Override 
            public void onMessage(String s) {
                final String message = s;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("webSocket", "onMessage: " + message);
                        // decode JSON Object
                        JSONObject object = null;
                        int event_mission;
                        try {
							object = new JSONObject(message);
							event_mission = (Integer)object.get("Event_Mission");
							switch(event_mission){
								case 0:	//Request Reply
									Log.i("MainActivity::case0", object.toString());
									break;
								case 1: //update event information
									manager.modifiedEventInfo(object.getInt("Event_ID"), object.getString("Name"), object.getString("Email"), object.getString("Topic"));
									updateInfo();
									Log.i("MainActivity::case1", "finisih update event info");
									break;
								case 2: //create survey
									if(object.getInt("Survey_Type") == 2)
										manager.createSurvey(object.getInt("Event_ID"), object.getInt("Survey_ID"), object.getInt("Survey_Type"), object.getString("Survey_Topic"), object.getJSONArray("Choice"));
									else
										manager.createSurvey(object.getInt("Event_ID"), object.getInt("Survey_ID"), object.getInt("Survey_Type"), object.getString("Survey_Topic"));
									updateSurveylist();
									Log.i("MainActivity::case2", "finisih create survey");
									break;
								case 4: //create question
									manager.createQuestion(object.getInt("Event_ID"), object.getInt("Question_ID"), object.getString("Question_Topic"));
									updateQuestionlist(elv.isGroupExpanded(0), elv.isGroupExpanded(1));
									Log.i("MainActivity::case4", "finisih create question");
									break;
								case 5: //increase question popularity
									manager.incrPopu(object.getInt("Event_ID"), object.getInt("Question_ID"));
									updateQuestionlist(elv.isGroupExpanded(0), elv.isGroupExpanded(1));
									Log.i("MainActivity::case5", "finisih increase question popularity");
									break;
								case 6: //decrease question popularity
									manager.decrPopu(object.getInt("Event_ID"), object.getInt("Question_ID"));
									updateQuestionlist(elv.isGroupExpanded(0), elv.isGroupExpanded(1));
									Log.i("MainActivity::case6", "finisih decrease question popularity");
									break;
								case 7: //change question status
									manager.chagneQuestionStatus(object.getInt("Event_ID"), object.getInt("Question_ID"), object.getString("Status"));
									updateQuestionlist(elv.isGroupExpanded(0), elv.isGroupExpanded(1));
									Log.i("MainActivity::case7", "finisih change question status");
									break;
								case 8: //change survey status
									//TODO
									manager.changeSurveyStatus(object.getInt("Event_ID"), object.getInt("Survey_ID"), object.getString("Status"));
									updateSurveylist();
									Log.i("MainActivity::case8", "finisih change survey status");
									break;
								case 9: //close the event, can't ask question, survey...etc
									//TODO
									manager.closeEvent(object.getInt("Event_ID"));
									Log.i("MainActivity::case9", "finisih close the event");
									break;
								default:
									Log.e("MainActivity::case default", "Wrong case " + Integer.toString(event_mission));
									break;
							}
						} catch (JSONException e) {
							e.printStackTrace();
							Log.e("MainPageDrawer::JSONObject", "JSONObject error");
						}
                    }
                });
            }

            @Override
            public void onClose(int i, String s, boolean b) {
                Log.i("Websocket", "Closed " + s);
            }

            @Override
            public void onError(Exception e) {
                Log.i("Websocket", "Error " + e.getMessage());
            }
        };
        mWebSocketClient.connect();
    }
    public void sendMessage(String sendMessage) {
        mWebSocketClient.send(sendMessage);
        Log.i("MainActivity::sendMessage()", "success send message: " + sendMessage);
    }
    // end of socket communication
    
    // *** mission function ***    
    // for audience 
    //mission 0
    public boolean joinEvent(int eventID){
    	JSONObject object = new JSONObject();
    	try{
    		object.put("Identity", 1);
    		object.put("Event_Mission", 0);
    		object.put("Event_ID", eventID);
    	} catch(JSONException e){
    		Log.e("MainActivity::joinEvent()", e.toString());
    	}
		sendMessage(object.toString());
    	Log.i("MainActivity::joinEvent()", "finish join event");
        return true;
    }
    //mission 1
    public boolean replySurvey(int eventID, int surveyID, String answer){
    	JSONObject object = new JSONObject();
    	try{
    		object.put("Identity", 1);
    		object.put("Event_Mission", 1);
    		object.put("Event_ID", eventID);
    		object.put("Survey_ID", surveyID);
    		object.put("Answer", answer);
    	} catch(JSONException e){
    		Log.e("MainActivity::replySurvey()", e.toString());
    	}
    	sendMessage(object.toString());
    	Log.i("MainActivity::replySurvey()", "finish reply survey");
        return true;
    }
    //mission 2
    public boolean askQuestion(int eventID, String topic){
    	JSONObject object = new JSONObject();
    	try{
    		object.put("Identity", 1);
    		object.put("Event_Mission", 2);
    		object.put("Event_ID", eventID);
    		object.put("Question_Topic", topic);
    	} catch(JSONException e){
    		Log.e("MainActivity::askQuestion()", e.toString());
    	}
    	sendMessage(object.toString());
    	Log.i("MainActivity::askQuestion()", "finish ask question");
        return true;
    }
    //mission 3
    public boolean increasePopu(int eventID, int questionID){
    	JSONObject object = new JSONObject();
    	try{
    		object.put("Identity", 1);
    		object.put("Event_Mission", 3);
    		object.put("Event_ID", eventID);
    		object.put("Question_ID", questionID);
    	} catch(JSONException e){
    		Log.e("MainActivity::increasePopu()", e.toString());
    	}
    	sendMessage(object.toString());
    	Log.i("MainActivity::increasePopu()", "finish increase Popu");
        return true;
    }
    //mission 4
    public boolean decreasePopu(int eventID, int questionID){
    	JSONObject object = new JSONObject();
    	try{
    		object.put("Identity", 1);
    		object.put("Event_Mission", 4);
    		object.put("Event_ID", eventID);
    		object.put("Question_ID", questionID);
    	} catch(JSONException e){
    		Log.e("MainActivity::decreasePopu()", e.toString());
    	}
    	sendMessage(object.toString());
    	Log.i("MainActivity::decreasePopu()", "finish decrease Popu");
        return true;
    }
    /*// for speaker
    //mission 0
    public boolean createEvent(){
    	JSONObject object = new JSONObject();
    	try{
    		object.put("Identity", 0);
    		object.put("Event_Mission", 0);
    	} catch(JSONException e){
    		Log.e("createEvent()", e.toString());
    	}
    	return false;
    }
    //mission 1
    public boolean modifySpeechInfo(SpeechInfo info){
        //TODO
    	int eventID = 0;
    	JSONObject object = new JSONObject();
    	try{
    		object.put("Identity", 0);
    		object.put("Event_Mission", 1);
    		object.put("Event_ID", eventID);
    		object.put("Name", info.getName());
    		object.put("Email", info.getEmail());
    		object.put("Topic", info.getTopic());
    	} catch(JSONException e){
    		Log.e("modifySpeechInfo()", e.toString());
    	}
    	return false;
    }
    //mission 2
    public boolean createSurvey(int surveyType, String topic){
        //TODO
    	int eventID = 0;
    	JSONObject object = new JSONObject();
    	try{
    		object.put("Identity", 0);
    		object.put("Event_Mission", 2);
    		object.put("Event_ID", eventID);
    		object.put("Survey_type", surveyType);
    		object.put("Survey_Topic", topic);
    	} catch(JSONException e){
    		Log.e("createSurvey()", e.toString());
    	}
    	return false;
    }
    //mission 3
    public boolean startSurvey(){
    	//TODO
    	int eventID = 0;
    	int surveyID = 0;
    	JSONObject object = new JSONObject();
    	try{
    		object.put("Identity", 0);
    		object.put("Event_Mission", 3);
    		object.put("Event_ID", eventID);
    		object.put("Survey_ID", surveyID);
    	} catch(JSONException e){
    		Log.e("startSurvey()", e.toString());
    	}
    	return false;
    }
    //mission 4
    public boolean closeSurvey(){
    	//TODO
    	int eventID = 0;
    	int surveyID = 0;
    	JSONObject object = new JSONObject();
    	try{
    		object.put("Identity", 0);
    		object.put("Event_Mission", 4);
    		object.put("Event_ID", eventID);
    		object.put("Survey_ID", surveyID);
    	} catch(JSONException e){
    		Log.e("showSurveyResult()", e.toString());
    	}
    	return false;
    }
    //mission 5
    public boolean showSurveyResult(){
    	//TODO
    	int eventID = 0;
    	int surveyID = 0;
    	JSONObject object = new JSONObject();
    	try{
    		object.put("Identity", 0);
    		object.put("Event_Mission", 5);
    		object.put("Event_ID", eventID);
    		object.put("Survey_ID", surveyID);
    	} catch(JSONException e){
    		Log.e("showSurveyResult()", e.toString());
    	}
    	return false;
    }
    //mission 6
    public boolean solveQuestion(){
    	//TODO
    	int eventID = 0;
    	int questionID = 0;
    	JSONObject object = new JSONObject();
    	try{
    		object.put("Identity", 0);
    		object.put("Event_Mission", 6);
    		object.put("Event_ID", eventID);
    		object.put("Question_ID", questionID);
    	} catch(JSONException e){
    		Log.e("solveQuestion()", e.toString());
    	}
    	return false;
    }
    //mission 7
    public boolean unSolveQuestion(){
    	//TODO
    	int eventID = 0;
    	int questionID = 0;
    	JSONObject object = new JSONObject();
    	try{
    		object.put("Identity", 0);
    		object.put("Event_Mission", 7);
    		object.put("Event_ID", eventID);
    		object.put("Question_ID", questionID);
    	} catch(JSONException e){
    		Log.e("UNsolveQuestion()", e.toString());
    	}
    	return false;
    }
    */
}

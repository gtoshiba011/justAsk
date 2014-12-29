package com.example.justask;

// import socket dictionary
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import question.Question;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;

public class MainActivity extends SherlockFragmentActivity {

	private DrawerLayout mDrawerLayout;
	private LinearLayout mDrawer;
	private ActionBarDrawerToggle mDrawerToggle;
	
	private NonSwipeableViewPager viewPager;
	private TabsPagerAdapter mAdapter;
	private ActionBar actionBar;
	
	// socket obj
	private WebSocketClient mWebSocketClient;
	
	// Buttons
	private Button btnEditSave, btnWebCode;

	// Tabs titles
	private String[] tabsTitles = {"Profile", "Questions", "Survey"};
	
	// Question list
	protected QuestionDbHelper qdb;
	List<List<Map<String,Question>>> qlist;
	ExpandableAdapter adapter;
	
	// Survey list
	protected SurveyDbHelper sdb;
	List<Question> slist;
	MyAdapter adapt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// Drawer
		mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
		mDrawer = (LinearLayout)findViewById(R.id.drawer);
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		mDrawerToggle = new ActionBarDrawerToggle(
				this,
				mDrawerLayout,
				R.drawable.ic_drawer,
				R.string.drawer_open,
				R.string.drawer_close) {
			
			public void onDrawerClosed(View view) {
				super.onDrawerClosed(view);
			}
			
			public void onDrawerOpened(View drawerView) {
				// Set the title on the action when drawer open
				getSupportActionBar().setTitle("JustAsk");
				super.onDrawerOpened(drawerView);
			}
		};
		
		mDrawerLayout.setDrawerListener(mDrawerToggle);


		//Event Tab Layout Initialization
		final TabHost tabHost=(TabHost)findViewById(android.R.id.tabhost);
		tabHost.setup();
		
		for (int i = 0; i < tabsTitles.length; i++) {
			TabHost.TabSpec spec=tabHost.newTabSpec( tabsTitles[i] );
			spec.setContent(R.id.fakeTabContent);
			spec.setIndicator( tabsTitles[i] );
			tabHost.addTab(spec);
			//tabHost.getTabWidget().getChildAt(i).setVisibility(View.GONE);
			tabHost.getTabWidget().getChildAt(i).setBackgroundColor(Color.parseColor("#f02F6877"));
			TextView t = (TextView)tabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title);
			t.setTextColor(Color.parseColor("#FFFFFF"));
		}
		//tabHost.getTabWidget().setStripEnabled( true );
		View v = (View) tabHost.getParent();
		v.setBackgroundColor(Color.parseColor("#ff2F6877"));
		
		viewPager = (NonSwipeableViewPager) findViewById(R.id.pager);
		actionBar = getSupportActionBar();
		mAdapter = new TabsPagerAdapter(getSupportFragmentManager(), tabsTitles.length);
		viewPager.setAdapter(mAdapter);
		tabHost.setOnTabChangedListener(new OnTabChangeListener() {
			//*
			@Override
			public void onTabChanged(String tabId) {
				for (int i = 0; i < tabsTitles.length; i++) {
					if (tabId.equals(tabsTitles[i])) {
						viewPager.setCurrentItem(i, false);
						if( i == 1 ){
							initialQuestionlist();
						}
						else if( i == 2 ){
							initialSurveylist();
						}
						break;
					}
				}
			}
			/*/
			@Override
			public void onTabChanged(String tabId) {
				if( tabId.equals(mainpageTitle) )
					viewPager.setCurrentItem(0);
			}
			*/
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
		
		// socket connection
		connectWebSocket();
		//String message = "{\"type\": \"hello_world\"}";
		//sendMessage(message);
	}

	public void initialSurveylist(){
		sdb = new SurveyDbHelper(MainActivity.this);
		slist = sdb.getAllSurveys();
		adapt = new MyAdapter(MainActivity.this, R.layout.survey_item_view, slist);
		ListView listTask = (ListView) findViewById(R.id.listView1);
		listTask.setAdapter(adapt);
	}
	
	public void initialQuestionlist(){
		
		// Set question list title
		List<Map<String, String>> groups = new ArrayList<Map<String, String>>();
		Map<String, String> unsolved = new HashMap<String, String>();
		unsolved.put("group", "Unsolved");
		groups.add(unsolved);
		Map<String, String> solved = new HashMap<String, String>();
		solved.put("group", "Solved");
		groups.add(solved);
		
		qdb = new QuestionDbHelper(MainActivity.this);
		qlist = qdb.getAllQuestions();
		adapter = new ExpandableAdapter(MainActivity.this, groups, qlist);
		ExpandableListView elv = (ExpandableListView)findViewById(R.id.mExpandableListView);
		elv.setAdapter(adapter);
		
		//qdb = new QuestionDbHelper(MainActivity.this);
		//slist = qdb.getAllQuestions();
		//adapt = new MyAdapter(MainActivity.this, R.layout.list_inner_view, slist);
		//ListView listTask = (ListView) findViewById(R.id.listView1);
		//listTask.setAdapter(adapt);
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
			Question question = new Question(0, s);
			qdb.addQuestion(question);
			Log.d("question list", "data added");
			t.setText("");
			//adapter.add(question);
			Map<String, Question> newQuestion = new HashMap<String, Question>();
			newQuestion.put("child", question);
			qlist.get(0).add(newQuestion);
			adapter.notifyDataSetChanged();
		}
		
	}

	public void addSurveyNow(View v) {
		
		EditText t = (EditText) findViewById(R.id.edtSurvey);
		String s = t.getText().toString();
		if (s.equalsIgnoreCase("")) {
			Toast.makeText(this, "enter the question description first!!",Toast.LENGTH_LONG);
		} else {
			Question question = new Question(0, s);
			sdb.addSurvey(question);
			Log.d("survey list", "data added");
			t.setText("");
			adapt.add(question);
			adapt.notifyDataSetChanged();
		}
	}
	
	public void SolveQuestion(View view) {
		
	}
	
	public void onClickLike(View view) {
		View v = (View) view.getParent();
		ToggleButton tb = (ToggleButton) v.findViewById(R.id.btnLike);
		Question changeQuestion = (Question) tb.getTag();
		if( tb.isChecked() ){
			changeQuestion.increasePopu(1);
			tb.setBackgroundResource(R.drawable.button_like_clicked);
		}
		else{
			changeQuestion.decreasePopu(1);
			tb.setBackgroundResource(R.drawable.button_like_unclicked);
		}
		tb.setText( String.valueOf(changeQuestion.getPopu()) );
		qdb.updateQuestionStatus(changeQuestion, tb.isChecked());
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
			//@SuppressWarnings("unchecked")
			Question question = ((Map<String, Question>) getChild(groupPosition, childPosition)).get("child");
			LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			//獲取二級清單對應的佈局檔, 並將其各元素設置相應的屬性
			LinearLayout linearLayout = (LinearLayout) layoutInflater.inflate(R.layout.question_item_view, null);
			TextView tv = (TextView) linearLayout.findViewById(R.id.txvQuestion);
			tv.setText(question.getQuestionTitle());
			ToggleButton btn = (ToggleButton)linearLayout.findViewById(R.id.btnLike);
			if( qdb.getQuestionLike(question) ){
				btn.setChecked( true );
				btn.setBackgroundResource(R.drawable.button_like_clicked);
			}
			else{
				btn.setChecked( false );
				btn.setBackgroundResource(R.drawable.button_like_unclicked);
			}
			btn.setText( String.valueOf(question.getPopu()) );
			btn.setTag(question);

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
	private class MyAdapter extends ArrayAdapter<Question> {

		Context context;
		List<Question> questionList = new ArrayList<Question>();
		int layoutResourceId;

		public MyAdapter(Context context, int layoutResourceId,
				List<Question> objects) {			
			super(context, layoutResourceId, objects);
			this.layoutResourceId = layoutResourceId;
			this.questionList = objects;
			this.context = context;
		}

		/**
		 * This method will DEFINe what the view inside the list view will
		 * finally look like Here we are going to code that the checkbox state
		 * is the status of task and check box text is the task name
		 */
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			CheckBox chk = null;
			Button btn = null;
			TextView txv = null;
			
			if (convertView == null) {
				LayoutInflater inflater = (LayoutInflater) context
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.survey_item_view,
						parent, false);
				chk = (CheckBox) convertView.findViewById(R.id.chkStatus);
				btn = (Button) convertView.findViewById(R.id.btnSend);
				convertView.setTag(R.id.first_tag, chk);
				convertView.setTag(R.id.second_tag, btn);

				chk.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						CheckBox cb = (CheckBox) v;
						Question changeQuestion = (Question) cb.getTag();
						changeQuestion.setStatus(cb.isChecked());
						
						View questionBlcok = (View)cb.getParent();
						if( cb.isChecked() ){
							//questionBlcok.setBackgroundResource(R.drawable.question_solved_shape);
							//Button send = (Button)questionBlcok.findViewById(R.id.btnSend);
							//send.setBackgroundResource(R.drawable.button_like_nonclickable);
							//like.setEnabled(false);
						}
						else{
							//cb.setChecked(true);
						}
						
						sdb.updateSurveyStatus(changeQuestion);
					}
				});
				
				btn.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						Button tb = (Button) v;
						Question changeQuestion = (Question) tb.getTag();
						/*
						if( tb.isChecked() ){
							changeQuestion.increasePopu(1);
							tb.setBackgroundResource(R.drawable.button_like_clicked);
						}
						else{
							changeQuestion.decreasePopu(1);
							tb.setBackgroundResource(R.drawable.button_like_unclicked);
						}
						*/
						//tb.setText( String.valueOf(changeQuestion.getPopu()) );
						sdb.updateSurveyStatus(changeQuestion);
					}
				});
				
			} else {
				chk = (CheckBox) convertView.getTag(R.id.first_tag);
				btn = (Button) convertView.getTag(R.id.second_tag);
			}
			
			Question current = questionList.get(position);
			chk.setText(current.getQuestionTitle());
			chk.setChecked(current.isSolved());
			chk.setTag(current);
			
			//btn.setText( String.valueOf( current.getPopu() ) );
			//btn.setChecked( false );
			btn.setTag(current);
			
			Log.d("listener", String.valueOf(current.getQuestionID()));
			return convertView;
		}

	}
	
	
	// socket communication start
    private void connectWebSocket() {
        URI uri;
        try {
            uri = new URI("140.112.230.230:10012");
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        }

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
                        //TextView textView = (TextView)findViewById(R.id.messages);
                        //textView.setText(textView.getText() + "\n" + message);
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
        //EditText editText = (EditText)findViewById(R.id.message);
        //editText.setText("");
    }
    // end of socket communication
}

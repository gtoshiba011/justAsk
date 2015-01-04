package com.example.justask;

//import socket dictionary
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;
import org.json.JSONObject;

import History.History;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
//json object
//Application
//import android.view.Menu;
//import android.view.MenuInflater;
//import android.view.MenuItem;
//json object
//Application
import com.example.justask.MainPageDrawer.DrawerItemFragment;

//import net.sourceforge.zbar.android.CameraTest.*;

public class EventHistory extends SherlockFragmentActivity implements DialogInterface.OnClickListener{
	private DrawerLayout mDrawerLayout;
	private LinearLayout mDrawer;
	private ActionBarDrawerToggle mDrawerToggle;
	
	private NonSwipeableViewPager viewPager;
	private TabsPagerAdapter mAdapter;
	private ActionBar actionBar;
    
	// socket obj
	private static WebSocketClient mWebSocketClient;
	
	// application
	private static Manager manager;
	
	// Event history database
	protected HistoryDbHelper db;
	List<History> list;
	HistoryAdapter adapt;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_history_drawer);

		// socket connection
		//connectWebSocket();
		manager = (Manager) getApplicationContext();
		
		// Drawer
		mDrawerLayout = (DrawerLayout)findViewById(R.id.history_drawer_layout);
		mDrawer = (LinearLayout)findViewById(R.id.history_drawer);
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
		mDrawer.setBackgroundColor(Color.parseColor("#f02F6877"));

		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setIcon(android.R.color.transparent);
		getSupportActionBar().setTitle("Event History");

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
		mDrawerToggle = new ActionBarDrawerToggle(
				this,
				mDrawerLayout,
				R.drawable.ic_drawer,
				R.string.drawer_open,
				R.string.drawer_close) {
			
			public void onDrawerClosed(View view) {
				getSupportActionBar().setTitle( "Event History" );
				super.onDrawerClosed(view);
			}
			
			public void onDrawerOpened(View drawerView) {
				// Set the title on the action when drawer open
				getSupportActionBar().setTitle("Event History");
				super.onDrawerOpened(drawerView);
			}
		};
		
		mDrawerLayout.setDrawerListener(mDrawerToggle);

        //if (savedInstanceState == null) {
            //selectItem(0);
            Fragment fragment = new DrawerItemFragment();
            Bundle args = new Bundle();
            args.putInt(DrawerItemFragment.ITEM_NUMBER, 0);
            fragment.setArguments(args);

            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
        //}
        
        ActionBar ab = getActionBar(); 
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#2F6877"));     
        getActionBar().setBackgroundDrawable(colorDrawable);
        
        db = new HistoryDbHelper(this);
		list = db.getAllHistorys();
		adapt = new HistoryAdapter(this, R.layout.history_item_view, list);
    }
    
    // Called function when event in history list is clicked
    public void HistoryClick(View view){
    	History history = (History)view.findViewById(R.id.txvHisTopic).getTag();
    	joinEvent(history.getId());
    	Toast toast = Toast.makeText(EventHistory.this,"Join now...Please wait", Toast.LENGTH_LONG);
		toast.show();
    	EventHistory.this.finish();
    }
    
    private class HistoryAdapter extends ArrayAdapter<History> {

		Context context;
		List<History> historyList = new ArrayList<History>();
		int layoutResourceId;

		public HistoryAdapter(Context context, int layoutResourceId,
				List<History> objects) {
			super(context, layoutResourceId, objects);
			this.layoutResourceId = layoutResourceId;
			this.historyList = objects;
			this.context = context;
		}

		/**
		 * This method will DEFINe what the view inside the list view will
		 * finally look like Here we are going to code that the checkbox state
		 * is the status of task and check box text is the task name
		 */
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			TextView txvName = null;
			TextView txvPerson = null;
			if (convertView == null) {
				LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.history_item_view, parent, false);
				txvName = (TextView) convertView.findViewById(R.id.txvHisTopic);
				txvPerson = (TextView) convertView.findViewById(R.id.txvHisPresenter);
				convertView.setTag(R.id.first_tag, txvName);
				convertView.setTag(R.id.second_tag, txvPerson);
			} else {
				txvName = (TextView) convertView.getTag(R.id.first_tag);
				txvPerson = (TextView) convertView.getTag(R.id.second_tag);
			}
			
			History current = historyList.get(position);
			txvName.setText(current.getEventName());
			txvPerson.setText(current.getPresenter());
			txvName.setTag(current);
			txvPerson.setTag(current);
			//Log.d("listener", String.valueOf(current.getId()));
			return convertView;
		}

	}
    
    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first

        // socket connection
     	connectWebSocket();
    }
    @Override
    public void onStop() {
        super.onStop();  // Always call the superclass method first

        EventHistory.this.finish();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getSupportMenuInflater();
        inflater.inflate(R.menu.event_drawer, menu);
        return super.onCreateOptionsMenu(menu);
    }
    
	// *** socket communication start ***
    private void connectWebSocket() {
    	Log.i("EventHistory::connectWebSocket()", "Connect web socket...");
        URI uri;
        try {
        	Log.i("webSocket", "start new uri");
            uri = new URI("ws://140.112.230.230:7272");
            Log.i("webSocket", "new uri success!");
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        }
        Log.i("EventHistory::connectWebSocket()", "Connect success!");
        mWebSocketClient = new WebSocketClient(uri) {
            @Override
            public void onOpen(ServerHandshake serverHandshake) {
                Log.i("Websocket", "Opened");
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
							event_mission = object.getInt("Event_Mission");
							switch(event_mission){
								case 0:	//Request Reply
									if(object.getBoolean("Success") == false){
										Toast toast = Toast.makeText(EventHistory.this,"Invalid Event ID", Toast.LENGTH_LONG);
										toast.show();
									}
									Log.i("EventHistory::case0", object.toString());
									break;
								case 10:// create event;
									manager.createEvent(object.getInt("Event_ID"));
									Log.i("EventHistory::case10", "finisih create event");
									break;
								case 11:// when join event and update the event
									manager.updateEventInfo(object.getInt("Event_ID"), object.getString("Name"), object.getString("Email"), object.getString("Topic"), object.getJSONArray("SurveyList"), object.getJSONArray("QuestionList"));
									Log.i("EventHistory::case11", "finisih update event");
									Intent intent = new Intent();
									intent.setClass(EventHistory.this, MainActivity.class);
									mWebSocketClient.close();
									startActivity(intent);
									break;
								default:
									Log.e("EventHistory::case default", "Wrong case " + Integer.toString(event_mission));
									break;
							}
						} catch (JSONException e) {
							e.printStackTrace();
							Log.e("EventHistory::JSONObject", "JSONObject error");
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
    public static void sendMessage(String sendMessage) {
        mWebSocketClient.send(sendMessage);
        Log.i("EventHistory::sendMessage()", "success send message: " + sendMessage);
    }
    // *** end of socket communication ***
    /* comment this line to prevent setting buttom on action bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }
    */

    /* Called whenever we call invalidateOptionsMenu() */
    /*
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        //menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }
    */
    @Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId() == R.id.action_clear) {
			new AlertDialog.Builder(this)
				.setMessage("History list will be CLEARED!\n" +
							"This step cannot be undone!")
				.setIcon(R.drawable.ic_evil)
				.setTitle("Are you sure?")
				.setNegativeButton("No", this)
				.setPositiveButton("Yes", this)
				.show();
		}
		else if (item.getItemId() == android.R.id.home) {
			if (mDrawerLayout.isDrawerOpen(mDrawer)) {
				mDrawerLayout.closeDrawer(mDrawer);
			} else {
				mDrawerLayout.openDrawer(mDrawer);
			}
		}

		return super.onOptionsItemSelected(item);
	}
    
    @Override
    public void onClick(DialogInterface dialog, int id) {
    	if( id == DialogInterface.BUTTON_POSITIVE) {
    		db.clear();
			list.clear();
			adapt.notifyDataSetChanged();
    	}
    }

    /* The click listner for ListView in the navigation drawer */
    /*
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }*/
    
    private void selectItem(int position) {
        Fragment fragment = new DrawerItemFragment();
        Bundle args = new Bundle();
        args.putInt(DrawerItemFragment.ITEM_NUMBER, position);
        fragment.setArguments(args);

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

        // update selected item and title, then close the drawer
        //mDrawerList.setItemChecked(position, true);
        //setTitle(mDrawerTitles[position]);
        //mDrawerLayout.closeDrawer();
    }
    
    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }
	
    /**
     * Fragment that appears in the "content_frame", shows a planet
     */
    public class DrawerItemFragment extends Fragment {
        public static final String ITEM_NUMBER = "item_number";
        //Manager manager;

        //public DrawerItemFragment(Manager _manager) {
        public DrawerItemFragment() {
        	//manager = _manager;
            // Empty constructor required for fragment subclasses
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            final View rootView = inflater.inflate(R.layout.event_history, container, false);
            
            ListView listTask = (ListView) rootView.findViewById(R.id.eventList);
    		listTask.setAdapter(adapt);
            
            int i = getArguments().getInt(ITEM_NUMBER);
            String planet = getResources().getStringArray(R.array.drawer_item_array)[i];

            //int imageId = getResources().getIdentifier(planet.toLowerCase(Locale.getDefault()),
            //                "drawable", getActivity().getPackageName());
            //((ImageView) rootView.findViewById(R.id.image)).setImageResource(imageId);
            getActivity().setTitle(getResources().getStringArray(R.array.drawer_item_array)[0]);
            
            return rootView;
        }
    }
    // *** mission function ***    
    // for audience 
    //mission 0
    public static boolean joinEvent(int eventID){
    	JSONObject object = new JSONObject();
    	JSONObject decode_object;
    	try{
    		object.put("Identity", 1);
    		object.put("Event_Mission", 0);
    		object.put("Event_ID", eventID);
    		sendMessage(object.toString());
    	} catch(JSONException e){
    		e.printStackTrace();
    		Log.e("EventHistory::JSONObject", e.toString());
    	}
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
    }*/
}

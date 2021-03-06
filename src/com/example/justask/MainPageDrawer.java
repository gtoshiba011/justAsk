package com.example.justask;

//import socket dictionary
import java.net.URI;
import java.net.URISyntaxException;

import net.sourceforge.zbar.android.CameraTest.CameraTestActivity;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
//json object
//Application

import net.sourceforge.zbar.android.CameraTest.*;

public class MainPageDrawer extends Activity {
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private String[] mDrawerTitles;
    
	// socket obj
	private static WebSocketClient mWebSocketClient;
	// application
	private static Manager manager;
	//final Manager manager = (Manager) getApplicationContext();
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page_drawer);

		// socket connection
		//connectWebSocket();
		manager = (Manager) getApplicationContext();
		
        mTitle = mDrawerTitle = getTitle();
        mDrawerTitles = getResources().getStringArray(R.array.drawer_item_array);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.mainpage_drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mDrawerList.setBackgroundColor(Color.parseColor("#f02F6877"));

        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        // set up the drawer's list view with items and click listener
        mDrawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item, mDrawerTitles));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        // enable ActionBar app icon to behave as action to toggle nav drawer
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
                ) {
            public void onDrawerClosed(View view) {
            	super.onDrawerClosed(view);
                getActionBar().setTitle(mTitle);
                //invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
            	super.onDrawerOpened(drawerView);
                getActionBar().setTitle(mDrawerTitle);
                //invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {
            selectItem(0);
        }
        
        ActionBar ab = getActionBar(); 
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#2F6877"));     
        ab.setBackgroundDrawable(colorDrawable);
    }
    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first

        // socket connection
     	//connectWebSocket();
    }
	// *** socket communication start ***
    private void connectWebSocket() {
    	Log.i("MainPageDrawer::connectWebSocket()", "Connect web socket...");
        URI uri;
        try {
        	Log.i("MainPageDrawer::WebSocket", "start new uri");
            uri = new URI("ws://140.112.230.230:7272");
            Log.i("MainPageDrawer:WebSocket", "new uri success!");
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        }
        Log.i("MainPageDrawer::connectWebSocket()", "Connect success!");
        mWebSocketClient = new WebSocketClient(uri) {
            @Override
            public void onOpen(ServerHandshake serverHandshake) {
                Log.i("MainPageDrawer:Websocket", "Opened");
            }

            @Override 
            public void onMessage(String s) {
                final String message = s;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("MainPageDrawer:WebSocket", "onMessage: " + message);
                        
                        // decode JSON Object
                        JSONObject object = null;
                        int event_mission;
                        try {
							object = new JSONObject(message);
							event_mission = object.getInt("Event_Mission");
							switch(event_mission){
								case 0:	//Request Reply
									if(object.getBoolean("Success") == false){
										Toast toast = Toast.makeText(MainPageDrawer.this,"Invalid Event Id", Toast.LENGTH_LONG);
										toast.show();
									}
									Log.i("MainPageDrawer::case0", object.toString());
									break;
								case 10:// create event;
									manager.createEvent(object.getInt("Event_ID"));
									Log.i("MainPageDrawer::case10", "finisih create event");
									break;
								case 11:// when join event and update the event
									manager.updateEventInfo(object.getInt("Event_ID"), object.getString("Name"), object.getString("Email"), object.getString("Topic"), object.getJSONArray("SurveyList"), object.getJSONArray("QuestionList"));
									Log.i("MainPageDrawer::case11", "finisih update event");
									Intent intent = new Intent();
									intent.setClass(MainPageDrawer.this, MainActivity.class);
									mWebSocketClient.close();
									startActivity(intent);
									break;
								default:
									Log.e("MainPageDrawer::case default", "Wrong case " + Integer.toString(event_mission));
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
                Log.i("MainPageDrawer:Websocket", "Closed " + s);
            }

            @Override
            public void onError(Exception e) {
                Log.i("MainPageDrawer:Websocket", "Error " + e.getMessage());
            }
        };
        mWebSocketClient.connect();
    }
    public static void sendMessage(String sendMessage) {
        mWebSocketClient.send(sendMessage);
        Log.i("MainPageDrawer::sendMessage()", "success send message: " + sendMessage);
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
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        //menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
         // The action bar home/up action should open or close the drawer.
         // ActionBarDrawerToggle will take care of this.
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action buttons
        /*
        switch(item.getItemId()) {
        case R.id.action_websearch:
            // create intent to perform web search for this planet
            Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
            intent.putExtra(SearchManager.QUERY, getActionBar().getTitle());
            // catch event that there's no activity to handle intent
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            } else {
                Toast.makeText(this, R.string.app_not_available, Toast.LENGTH_LONG).show();
            }
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }*/
        return super.onOptionsItemSelected(item);
    }

    /* The click listner for ListView in the navigation drawer */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    private void selectItem(int position) {
        // update the main content by replacing fragments
        //Fragment fragment = new DrawerItemFragment(manager);
        Fragment fragment = new DrawerItemFragment();
        Bundle args = new Bundle();
        args.putInt(DrawerItemFragment.ITEM_NUMBER, position);
        fragment.setArguments(args);

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

        // update selected item and title, then close the drawer
        mDrawerList.setItemChecked(position, true);
        //setTitle(mDrawerTitles[position]);
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
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
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	switch(resultCode){
    	case 1://QR flag
    		String result = data.getExtras().getString("QREventCode");
    		if(result.length()!=6)
			{
				Toast toast = Toast.makeText(MainPageDrawer.this,"Invalid Event Id", Toast.LENGTH_LONG);
				toast.show();
				break;
			}
    		EditText editText = (EditText) findViewById(R.id.edtEventCode);
    		editText.setText(result);
    		break;
    	}
    }
    
    // test Internet connection
    private boolean checkNetworkConnected() {
    	boolean result = false;
    	ConnectivityManager CM = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
    	if (CM == null) {
    		result = false;
    	}
    	else {
    		NetworkInfo info = CM.getActiveNetworkInfo(); 
    		if (info != null && info.isConnected()) {
    			 if (!info.isAvailable()) {
    				 result = false;
    			 }
    			 else {
    				 result = true;
    			 }
    		}
    	}
    	return result;
    }
    
	// Launch an event
	public void launch(View v){
		Intent it = new Intent(this, MainActivity.class);
		startActivity( it );
	}
	public void launch(){
		Toast toast = Toast.makeText(MainPageDrawer.this,"Connecting...Please wait", Toast.LENGTH_LONG);
		toast.show();
		Intent it = new Intent(this, MainActivity.class);
		startActivity( it );
	}
	// When the "Scan QR code" button is pushed
	public void ScanQR(View v){
		Intent it = new Intent(this, CameraTestActivity.class);
		startActivityForResult(it, 1);
	}
	
	// When the "Event History" button is pushed
	public void event(View v){
		//mWebSocketClient.close();
		Intent it = new Intent(this, EventHistory.class);
		startActivity( it );
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
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            final View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            int i = getArguments().getInt(ITEM_NUMBER);
            String planet = getResources().getStringArray(R.array.drawer_item_array)[i];

            //int imageId = getResources().getIdentifier(planet.toLowerCase(Locale.getDefault()),
            //                "drawable", getActivity().getPackageName());
            //((ImageView) rootView.findViewById(R.id.image)).setImageResource(imageId);
            getActivity().setTitle(getResources().getStringArray(R.array.drawer_item_array)[0]);
            
        	// When enter event code finished
        	final EditText editText_code = (EditText) rootView.findViewById(R.id.edtEventCode);
        	editText_code.addTextChangedListener(new TextWatcher() {
        		@Override
        		public void onTextChanged(CharSequence s, int start, int before, int count) {
        			if(editText_code.getText().length()==6)
        			{
        				String mString = editText_code.getText().toString();
        				//joinEvent(Integer.valueOf(mString));
        				//Log.d("joinEvent",mString);
        				editText_code.setText("");
        				manager.setEventID(Integer.valueOf(mString));
        				if(checkNetworkConnected() == true)
        					launch();
        				else{
        					Toast toast = Toast.makeText(MainPageDrawer.this,"No Internet Connection!!", Toast.LENGTH_LONG);
							toast.show();
        				}
        			}
        		}

        		@Override
        		public void beforeTextChanged(CharSequence s, int start, int count,
        				int after) {
        			// TODO Auto-generated method stub
        			
        		}

        		@Override
        		public void afterTextChanged(Editable s) {
        			// TODO Auto-generated method stub
        			
        		}
        	});
            
            return rootView;
        }
    }
    // *** mission function ***    
    // for audience 
    //mission 0
    public static boolean joinEvent(int eventID){
    	JSONObject object = new JSONObject();
    	try{
    		object.put("Identity", 1);
    		object.put("Event_Mission", 0);
    		object.put("Event_ID", eventID);
    		sendMessage(object.toString());
    	} catch(JSONException e){
    		e.printStackTrace();
    		Log.e("MainPageDrawer::JSONObject", e.toString());
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

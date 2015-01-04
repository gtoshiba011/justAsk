package com.example.justask;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class ProfileFragment extends Fragment{
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	
    	View rootView = inflater.inflate(R.layout.profile_fragment_layout, container, false);
    	
		// Buttons
    	Button btnEditSave, btnWebCode;
    	btnEditSave = (Button) rootView.findViewById(R.id.btnEditSave);
    	btnWebCode = (Button) rootView.findViewById(R.id.btnWebCode);
    	btnEditSave.setTag(0);
    	btnWebCode.setTag(0);
    	btnEditSave.setVisibility(View.GONE);
    	btnWebCode.setVisibility(View.GONE);
    	/*
    	MainActivity act = (MainActivity)getActivity();
    	TextView event_id_text =  (TextView) rootView.findViewById(R.id.EventID);
		TextView name_text =  (TextView) rootView.findViewById(R.id.txvPresenter);
        TextView mail_text =  (TextView) rootView.findViewById(R.id.txvEmail);
        TextView topic_text =  (TextView) rootView.findViewById(R.id.txvTopic);
		event_id_text.setText(Integer.toString(act.manager.getJoinEventID()));
		name_text.setText(act.manager.getEvent(act.manager.getJoinEventID()).getSpeechInfo().getName());
        mail_text.setText(act.manager.getEvent(act.manager.getJoinEventID()).getSpeechInfo().getEmail());
        topic_text.setText(act.manager.getEvent(act.manager.getJoinEventID()).getSpeechInfo().getTopic());
    	*/
    	return rootView;
    }
   
}

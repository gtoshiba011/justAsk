package com.example.justask;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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
    	
    	return rootView;
    }
   
}

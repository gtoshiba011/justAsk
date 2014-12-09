package com.example.justask;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ProfileForSpeaker extends Fragment {

	  private String value = "";

	  @Override
	  public void onAttach(Activity activity) {
	    super.onAttach(activity);

	    MainActivity mainActivity = (MainActivity)activity;
	    value = mainActivity.getProfileData();
	  }

	  @Override
	  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

	    return inflater.inflate(R.layout.speaker_profile, container, false);
	  }
	  @Override
	  public void onActivityCreated(Bundle savedInstanceState) {
	    super.onActivityCreated(savedInstanceState);
	    TextView txtResult = (TextView) this.getView().findViewById(R.id.textView1);
	    txtResult.setText(value);
	  }

	}
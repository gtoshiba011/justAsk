package com.example.justask;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;

public class EventManagerForSpeaker extends FragmentActivity {
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.speaker_event_manager);

    FragmentTabHost tabHost = (FragmentTabHost)findViewById(android.R.id.tabhost);
    tabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
    tabHost.addTab(tabHost.newTabSpec("Profile").setIndicator("Profile"), ProfileForSpeaker.class, null);
    tabHost.addTab(tabHost.newTabSpec("Questions").setIndicator("Questions"), QuestionsForSpeaker.class, null);
    tabHost.addTab(tabHost.newTabSpec("Survey").setIndicator("Survey"), SurveyForSpeaker.class, null);

  }

  /**************************
   * 
   * µπ§l≠∂≈“©I•s•Œ
   * 
   **************************/
  public String getProfileData(){
	  return "Profile Page";
  }
  public String getQuestionsData(){
	  return "Question Page";
  }
  public String getSurveyData(){
	  return "Survey Page";
  }
}
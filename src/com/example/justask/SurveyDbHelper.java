package com.example.justask;

import java.util.ArrayList;
import java.util.List;

import question.Question;
import survey.Survey;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SurveyDbHelper extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "surveyManager";

	// tasks table name
	private static final String TABLE_SURVEYS = "surveys";

	// tasks Table Columns names
	private static final String KEY_ID = "id";
	private static final String KEY_SURVEYNAME = "surveyName";
	private static final String KEY_STATUS = "status";
	private static final String KEY_TYPE = "type";

	public SurveyDbHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_SURVEYS + " ( "
				+ KEY_ID 			+ " INTEGER PRIMARY KEY AUTOINCREMENT, " 
				+ KEY_SURVEYNAME	+ " TEXT, " 
				+ KEY_TYPE			+ " INTEGER, "
				+ KEY_STATUS 		+ " INTEGER)";
		db.execSQL(sql);
	
		//db.close();
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_SURVEYS);
		// Create tables again
		onCreate(db);
	}

	// Adding new task
	public void addSurvey(Survey survey) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		//values.put(KEY_SURVEYNAME, survey.getSurvey Title()); // task name
		//values.put(KEY_STATUS, survey.isSolved());

		// Inserting Row
		db.insert(TABLE_SURVEYS, null, values);
		db.close(); // Closing database connection
	}

	public List<Survey> getAllSurveys() {
		List<Survey> surveyList = new ArrayList<Survey>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_SURVEYS;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				Survey survey = new Survey(	//cursor.getInt(0),
											//cursor.getInt(1),
											cursor.getString(2)  );
				//survey.setStatus( cursor.getInt(2)!=0 );
				// Adding contact to list
				surveyList.add(survey);
			} while (cursor.moveToNext());
		}

		// return task list
		return surveyList;
	}

	public void updateSurveyStatus(Survey survey) {
		// updating row
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		//values.put(KEY_SURVEYNAME, survey.getSurveyTitle());
		//values.put(KEY_STATUS, survey.isSolved());
		//db.update(TABLE_SURVEYS, values, KEY_ID + " = ?",new String[] {String.valueOf(survey.getSurveyID())});
		//db.close();
	}
	

	

}

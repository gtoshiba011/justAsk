package com.example.justask;

import java.util.ArrayList;
import java.util.List;

import History.History;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class HistoryDbHelper extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "historyManager";

	// tasks table name
	private static final String TABLE_HISTORYS = "historys";

	// tasks Table Columns names
	private static final String KEY_ID = "id";
	private static final String KEY_EVENTNAME = "eventName";
	private static final String KEY_PRESENTER = "presenter";

	public HistoryDbHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_HISTORYS + " ( "
				+ KEY_ID 			+ " INTEGER PRIMARY KEY AUTOINCREMENT, " 
				+ KEY_EVENTNAME		+ " TEXT, " 
				+ KEY_PRESENTER		+ " TEXT)";
		db.execSQL(sql);
		//db.close();
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_HISTORYS);
		// Create tables again
		onCreate(db);
	}

	// Adding new task
	public void addHistory(int id, String name, String presenter) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_ID, id);
		values.put(KEY_EVENTNAME, name); 
		values.put(KEY_PRESENTER, presenter);

		// Inserting Row
		db.insert(TABLE_HISTORYS, null, values);
		db.close(); // Closing database connection
	}

	public List<History> getAllHistorys() {
		List<History> eventList = new ArrayList<History>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_HISTORYS;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				History history = new History( 	cursor.getInt(0),
												cursor.getString(1),
												cursor.getString(2)  );
				// Adding contact to list
				eventList.add(history);
			} while (cursor.moveToNext());
		}

		return eventList;
	}
	/*
	public void updateSurveyStatus(Survey survey) {
		// updating row
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		//values.put(KEY_SURVEYNAME, survey.getSurveyTitle());
		//values.put(KEY_STATUS, survey.isSolved());
		//db.update(TABLE_SURVEYS, values, KEY_ID + " = ?",new String[] {String.valueOf(survey.getSurveyID())});
		//db.close();
	}
*/

	

}

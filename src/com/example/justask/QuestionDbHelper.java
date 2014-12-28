package com.example.justask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import question.Question;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class QuestionDbHelper extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "questionManager";

	// tasks table name
	private static final String TABLE_QUESTIONS = "questions";

	// tasks Table Columns names
	private static final String KEY_ID = "id";
	private static final String KEY_QUESTIONNAME = "questionName";
	private static final String KEY_STATUS = "status";
	private static final String KEY_POPULARITY = "popularity";
	private static final String KEY_LIKE = "like";

	public QuestionDbHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_QUESTIONS + " ( "
				+ KEY_ID 			+ " INTEGER PRIMARY KEY AUTOINCREMENT, " 
				+ KEY_QUESTIONNAME	+ " TEXT, " 
				+ KEY_STATUS 		+ " INTEGER, "
				+ KEY_POPULARITY	+ " INTEGER, "
				+ KEY_LIKE			+ " INTEGER)";
		db.execSQL(sql);
	
		//db.close();
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUESTIONS);
		// Create tables again
		onCreate(db);
	}

	// Adding new task
	public void addQuestion(Question question) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_QUESTIONNAME, question.getQuestionTitle()); // task name
		values.put(KEY_STATUS, question.isSolved());
		values.put(KEY_POPULARITY, question.getPopu());
		values.put(KEY_LIKE, 0);

		// Inserting Row
		db.insert(TABLE_QUESTIONS, null, values);
		db.close(); // Closing database connection
	}

	public List<List<Map<String, Question>>> getAllQuestions() {
		List<Map<String, Question>> unsolved = new ArrayList<Map<String, Question>>();
		List<Map<String, Question>> solved = new ArrayList<Map<String, Question>>();		
		
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_QUESTIONS;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				Question question = new Question(	cursor.getInt(0),
													cursor.getString(1)  );
				question.setStatus( cursor.getInt(2)!=0 );
				question.setPopu( cursor.getInt(3) );
				
				Map<String, Question> questionData = new HashMap<String, Question>();
				questionData.put("child", question);
				
				// Adding contact to list
				if( question.isSolved() ){
					solved.add(questionData);
				}
				else{
					unsolved.add(questionData);
				}

			} while (cursor.moveToNext());
			//Log.d("End", String.valueOf(unsolved.size()) + ' ' + String.valueOf(solved.size()) );
		}

		// return task list
		List<List<Map<String, Question>>> questionList = new ArrayList<List<Map<String, Question>>>();
		questionList.add(unsolved);
		questionList.add(solved);
		return questionList;
	}

	public void updateQuestionStatus(Question question, boolean like) {
		// updating row
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_QUESTIONNAME, question.getQuestionTitle());
		values.put(KEY_STATUS, question.isSolved());
		values.put(KEY_POPULARITY, question.getPopu());
		values.put(KEY_LIKE, like);
		db.update(TABLE_QUESTIONS, values, KEY_ID + " = ?",new String[] {String.valueOf(question.getQuestionID())});
		//db.close();
	}
	

	

}

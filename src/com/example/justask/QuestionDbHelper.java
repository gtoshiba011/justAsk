package com.example.justask;

import java.util.ArrayList;
import java.util.List;

import question.Question;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class QuestionDbHelper extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "questionManager";

	// tasks table name
	private static final String TABLE_QUESTIONS = "questions";

	// tasks Table Columns names
	private static final String KEY_ID = "id";
	private static final String KEY_TASKNAME = "taskName";
	private static final String KEY_STATUS = "status";

	public QuestionDbHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_QUESTIONS + " ( "
				+ KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_TASKNAME
				+ " TEXT, " + KEY_STATUS + " INTEGER)";
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
		values.put(KEY_TASKNAME, question.getQuestionTitle()); // task name
		// status of task- can be 0 for not done and 1 for done
		values.put(KEY_STATUS, question.isSolved());

		// Inserting Row
		db.insert(TABLE_QUESTIONS, null, values);
		db.close(); // Closing database connection
	}

	public List<Question> getAllQuestions() {
		List<Question> questionList = new ArrayList<Question>();
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
				// Adding contact to list
				questionList.add(question);
			} while (cursor.moveToNext());
		}

		// return task list
		return questionList;
	}

	public void updateQuestion(Question question) {
		// updating row
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_TASKNAME, question.getQuestionTitle());
		values.put(KEY_STATUS, question.isSolved());
		db.update(TABLE_QUESTIONS, values, KEY_ID + " = ?",new String[] {String.valueOf(question.getQuestionID())});
		//db.close();
	}
	

	

}

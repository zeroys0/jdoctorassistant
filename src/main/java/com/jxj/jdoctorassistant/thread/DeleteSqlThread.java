package com.jxj.jdoctorassistant.thread;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.jxj.jdoctorassistant.model.SearchWord;
import com.jxj.jdoctorassistant.util.DatabaseHelper;

public class DeleteSqlThread extends Thread {

	private String searchWord;
	private DatabaseHelper databasehelper;
	private Context context;
	private SQLiteDatabase db;

	public DeleteSqlThread(String searchWord, Context context) {
		this.context = context;
		this.searchWord = searchWord;
	}

	@Override
	public void run() {
		databasehelper = new DatabaseHelper(context);
		db = databasehelper.getWritableDatabase();
		if(searchWord.length()>0){
			db.execSQL("DELETE FROM "+DatabaseHelper.TABLE_NAME+" WHERE "+ SearchWord.SEARCHWORD+"="
				+searchWord);
		}else {
			db.delete(DatabaseHelper.TABLE_NAME, null, null);
		}
	}
}

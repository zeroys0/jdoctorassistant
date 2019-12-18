package com.jxj.jdoctorassistant.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.jxj.jdoctorassistant.model.SearchWord;

public class DatabaseHelper extends SQLiteOpenHelper {

	private final static String DATABASE_NAME = "jD_data.db";
	private final static int DATABASE_VERSION = 1;
	public final static String TABLE_NAME = "SEARCHTABLE";

	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		 db.execSQL("CREATE TABLE SEARCHTABLE('"+ SearchWord.SEARCHWORD+"' VARCHAR,'"+SearchWord.DATE+"' VARCHAR,'"+SearchWord.COUNT+"'INTEGER)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		String sql = "DROP TABLE IF EXISTS " + TABLE_NAME;
    	db.execSQL(sql);
    	onCreate(db);
	}


}

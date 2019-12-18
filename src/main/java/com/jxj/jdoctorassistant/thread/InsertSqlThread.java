package com.jxj.jdoctorassistant.thread;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jxj.jdoctorassistant.model.SearchWord;
import com.jxj.jdoctorassistant.util.DatabaseHelper;

public class InsertSqlThread extends Thread {

	public SearchWord userbean;
	private DatabaseHelper databasehelper;
	private Context context;
	private SQLiteDatabase db;
	private int id;
	private boolean add=true;

	public InsertSqlThread(SearchWord userbean, Context context) {
		this.context = context;
		this.userbean = userbean;
	}

	@Override
	public void run() {
		databasehelper = new DatabaseHelper(context);
		db = databasehelper.getWritableDatabase();
		String sql = "INSERT INTO "+DatabaseHelper.TABLE_NAME+" VALUES (?,?,?)";
		String querysql = "select * from "+DatabaseHelper.TABLE_NAME;
		Cursor cursor = db.rawQuery(querysql, null);
		if (cursor.getCount() == 0) {
			db.execSQL(sql,
					new Object[] { userbean.getSearchWord(), userbean.getDate(),userbean.getCount()});

		} else {
			while (cursor.moveToNext()) {
				if (cursor.getString(0).equals(userbean.getSearchWord())) {
					add = false;
//					String sql_update="UPDATE "+DatabaseHelper.TABLE_NAME+" SET "+SearchWord.SEARCHWORD+" = '"+userbean.getSearchWord()+
//							"', "+SearchWord.DATE+" = '"+userbean.getDate()+
//							"', "+SearchWord.COUNT+" = "+userbean.getCount()+
//							" WHERE "+SearchWord.SEARCHWORD+" = "+userbean.getSearchWord();
//					db.execSQL(sql_update);
				}

			}
			if (add) {
				db.execSQL(sql,
						new Object[] { userbean.getSearchWord(), userbean.getDate(),userbean.getCount()});
			}
		}

	}

}

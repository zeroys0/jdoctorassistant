package com.jxj.jdoctorassistant.thread;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jxj.jdoctorassistant.model.SearchWord;
import com.jxj.jdoctorassistant.util.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class QuerySqlThread extends Thread {

	private DatabaseHelper databasehelper;
	private Context context;
	private List<SearchWord> list = new ArrayList<>();
	private SQLiteDatabase db;

	public QuerySqlThread(Context context) {
		this.context = context;
	}

	@Override
	public void run() {
		databasehelper = new DatabaseHelper(context);
		db = databasehelper.getWritableDatabase();
		String sql = "select * from "+DatabaseHelper.TABLE_NAME;
		Cursor cursor = db.rawQuery(sql, null);

		if (cursor.getCount() != 0) {
			while (cursor.moveToNext()) {
				SearchWord userbean = new SearchWord();
				userbean.setSearchWord(cursor.getString(0));
				userbean.setDate(cursor.getString(1));
				userbean.setCount(cursor.getInt(2));
				list.add(userbean);
			}
		}

	}

	public List<SearchWord> getList() {
		return list;
	}

}

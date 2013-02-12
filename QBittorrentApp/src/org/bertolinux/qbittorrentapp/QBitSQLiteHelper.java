package org.bertolinux.qbittorrentapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class QBitSQLiteHelper extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "qBitConn.db";
	private static final String DATABASE_CREATE =
			"create table qBitConn(" +
					"id integer primary key autoincrement," +
					"hostname	text not null," +
					"login		text not null," +
					"password 	text not null"  +
			")";	
	
	@SuppressWarnings("unused")
	private SQLiteDatabase database;
	
	public QBitSQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null,1);
		database = this.getWritableDatabase();
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		// TODO Auto-generated method stub
		database.execSQL(DATABASE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub

	}
}

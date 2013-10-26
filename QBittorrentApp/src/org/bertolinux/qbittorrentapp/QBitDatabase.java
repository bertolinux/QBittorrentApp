package org.bertolinux.qbittorrentapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class QBitDatabase {
    SQLiteDatabase mDb;
    DbHelper mDbHelper;
    Context mContext;
    private static final String DB_NAME="QBitDatabase.db";
    private static final int DB_VERSION=1;
    private boolean exists = false;
    
    public QBitDatabase(Context ctx) {
        mContext=ctx;
        mDbHelper=new DbHelper(ctx, DB_NAME, null, DB_VERSION);  
        mDb=mDbHelper.getWritableDatabase();    
    }
    
    public void reset() {
    	mDb.delete(QBitConnectionData.KTABLE, null, null);
    }
    
    public QBitConnectionData getConnectionData() {
    	Cursor cursor = mDb.query(QBitConnectionData.KTABLE, null,null,null,null,null,null);
	    if (cursor.getCount() == 0)
	    	return null;
	    cursor.moveToFirst();
	    return new QBitConnectionData(cursor);
    }
    
    public boolean exists() {
	    return exists;
    }
    
    public void close() {
        mDb.close();
    }
    
    public void insertConnection(String hostname, String port, String username, String password) {
        ContentValues contentview = new ContentValues();
        contentview.put(QBitConnectionData.KHOSTNAME, hostname);
        contentview.put(QBitConnectionData.KPORT, port);
        contentview.put(QBitConnectionData.KUSERNAME, username);
        contentview.put(QBitConnectionData.KPASSWORD, password);
        mDb.insert(QBitConnectionData.KTABLE, null, contentview);
    }
   
    public Cursor fetchConnection() { 
        return mDb.query(QBitConnectionData.KTABLE, null,null,null,null,null,null);              
    }

    private static final String TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
        + QBitConnectionData.KTABLE 	+ " ("
        + QBitConnectionData.KID		+ " integer primary key autoincrement, "
        + QBitConnectionData.KHOSTNAME 	+ " text not null, "
        + QBitConnectionData.KPORT 		+ " integer not null, "
        + QBitConnectionData.KUSERNAME 	+ " text not null, "
        + QBitConnectionData.KPASSWORD 	+ " text not null);";

    private class DbHelper extends SQLiteOpenHelper { 
	    public DbHelper(Context context, String name, CursorFactory factory,int version) {
	        super(context, name, factory, version);
	    }
	
	    @Override
	    public void onCreate(SQLiteDatabase _db) {
	            _db.execSQL(TABLE_CREATE);
	    }
	
	    @Override
	    public void onUpgrade(SQLiteDatabase _db, int oldVersion, int newVersion) {
	
	    }
    }         
}
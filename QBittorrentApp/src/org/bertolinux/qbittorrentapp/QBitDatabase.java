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
    
    private String hostname;
    private String port;
    private String username;
    private String password;
    
    public QBitDatabase(Context ctx) {
        mContext=ctx;
        mDbHelper=new DbHelper(ctx, DB_NAME, null, DB_VERSION);  
        mDb=mDbHelper.getWritableDatabase();        
        fetch();	    
    }
    
    public void reset() {
    	mDb.delete(connectionMetaData.TABLE, null, null);
    }
    
    private void fetch() {
    	Cursor c = mDb.query(connectionMetaData.TABLE, null,null,null,null,null,null);
	    if (c.getCount() == 0) {
	    	exists = false;
	    	return;
	    }
	    c.moveToFirst();
	    hostname 	= c.getString(c.getColumnIndex(QBitDatabase.connectionMetaData.HOSTNAME_KEY));	
	    port 		= c.getString(c.getColumnIndex(QBitDatabase.connectionMetaData.PORT_KEY));	
	    username 	= c.getString(c.getColumnIndex(QBitDatabase.connectionMetaData.USERNAME_KEY));	
	    password 	= c.getString(c.getColumnIndex(QBitDatabase.connectionMetaData.PASSWORD_KEY));	
	    exists = true;
    }
    
    public boolean exists() {
	    return exists;
    }
    
    public String getHostname() {
    	return this.hostname;
    }
    
    public String getPort() {
    	return this.port;
    	
    }
    
    public String getUsername() {
    	return this.username;
    }
    
    public String getPassword() {
    	return this.password;
    }
    
    public void close() {
        mDb.close();
    }
    
    public void insertConnection(String hostname, String port, String username, String password) {
            ContentValues cv=new ContentValues();
            cv.put(connectionMetaData.HOSTNAME_KEY, hostname);
            cv.put(connectionMetaData.PORT_KEY, port);
            cv.put(connectionMetaData.USERNAME_KEY, username);
            cv.put(connectionMetaData.PASSWORD_KEY, password);
            mDb.insert(connectionMetaData.TABLE, null, cv);
            fetch();
    }
   
    public Cursor fetchConnection() { 
            return mDb.query(connectionMetaData.TABLE, null,null,null,null,null,null);              
    }

    static class connectionMetaData {
            static final String TABLE = "connectionMetaData";
            static final String ID = "_id";
            static final String HOSTNAME_KEY = "hostname";
            static final String PORT_KEY = "port";
            static final String USERNAME_KEY = "username";
            static final String PASSWORD_KEY = "password";
    }

    private static final String TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
                    + connectionMetaData.TABLE 			+ " ("
                    + connectionMetaData.ID				+ " integer primary key autoincrement, "
                    + connectionMetaData.HOSTNAME_KEY 	+ " text not null, "
                    + connectionMetaData.PORT_KEY 		+ " integer not null, "
                    + connectionMetaData.USERNAME_KEY 	+ " text not null, "
                    + connectionMetaData.PASSWORD_KEY 	+ " text not null);";

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
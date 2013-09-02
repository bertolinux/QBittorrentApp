package org.bertolinux.qbittorrentapp;

import android.database.Cursor;

public class QBitConnectionData {
    static final String KTABLE = "connectionMetaData";
    static final String KID = "_id";
    static final String KHOSTNAME = "hostname";
    static final String KPORT = "port";
    static final String KUSERNAME = "username";
    static final String KPASSWORD = "password";
	
	private String hostname;
	private String port;
	private String username;
	private String password; 
	public QBitConnectionData(Cursor cursor) {
		hostname 	= cursor.getString(cursor.getColumnIndex(KHOSTNAME));	
	    port 		= cursor.getString(cursor.getColumnIndex(KPORT));	
	    username 	= cursor.getString(cursor.getColumnIndex(KUSERNAME));	
	    password 	= cursor.getString(cursor.getColumnIndex(KPASSWORD));
	}

	public String getUrl() {
		return "http://" + hostname + ":" + port;	
	}
	
	public String getUsername() {
		return username;
	}
	
	public String getPassword() {
		return password;
	}
}

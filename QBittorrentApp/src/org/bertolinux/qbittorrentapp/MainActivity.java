package org.bertolinux.qbittorrentapp;

import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity {
	private QBitDatabase database;
	private QBitConnectionData connectionData;
	private MainActivity myActivity = this;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        database = new QBitDatabase(this);
        init();
    }
    
    private void init() {
    	connectionData = database.getConnectionData();
    	if (connectionData == null)
    		askConnectionData();
    	else 
    		showTorrents();
    }
    
    public QBitConnectionData getConnectionData() {
    	return connectionData;
    }
    
    private void askConnectionData() {
    	final Dialog ask = new Dialog(this);
    	ask.setContentView(R.layout.askconnection);
    	ask.setTitle("New Connection Data");
    	
    	Button save = (Button) ask.findViewById(R.id.save);
		
    	save.setOnClickListener(new OnClickListener() {
	    	public void onClick(View v1) {
	    		v1.setClickable(false);
	    		String hostname = (String) ((TextView) ask.findViewById(R.id.hostname)).getText().toString();
	    		String port 	= (String) ((TextView) ask.findViewById(R.id.port)).getText().toString();
	    		String username = (String) ((TextView) ask.findViewById(R.id.username)).getText().toString();
	    		String password = (String) ((TextView) ask.findViewById(R.id.password)).getText().toString();
	    		database.insertConnection(hostname, port, username, password);
	    		ask.dismiss();
	    		init();
	    	}
    	});
		ask.show();
    }
    
    public void refreshTorrentList() {
    	new GetTorrentList(this).execute(connectionData);   		
    }
    
    private void showTorrents() {        
    	refreshTorrentList();
    	
    	QBitTimer qbittimer = new QBitTimer();
        Timer timer = new Timer();
        timer.schedule(qbittimer, 10000, 10000);        	
    	
        Button refresh 	= (Button) findViewById(R.id.refresh);
        refresh.setOnClickListener(new OnClickListener() {
	    	public void onClick(View v1) {
	    		refreshTorrentList();
	    	}
    	});
        
        Button reset 	= (Button) findViewById(R.id.reset);
        reset.setOnClickListener(new OnClickListener() {
	    	public void onClick(View v1) {
	    		AlertDialog.Builder confirm = new AlertDialog.Builder(myActivity);
	    		confirm.setTitle(R.string.confirmation);
	    		confirm.setMessage(getResources().getString(R.string.confirm) + " " + getResources().getString(R.string.reset));
	    		confirm.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						database.reset();
			    		init();
					}
				  });
				confirm.setNegativeButton("No",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						dialog.cancel();
					}
				});
	    		Dialog dialog = confirm.create();
	    		dialog.show();
	    	}
    	});
    }   
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    @SuppressLint("NewApi")
	public void manageTorrentList(String json) throws Exception {
        JSONArray jsonArray = new JSONArray(json);
        List<TDownload> list = new LinkedList<TDownload>();
        for (int i=0; i < jsonArray.length(); i++)
    		list.add(new TDownload(jsonArray.getJSONObject(i)));
    	ListView listview = (ListView) findViewById(R.id.listView1);
        QBitAdapter adapter = new QBitAdapter(this, R.layout.row, list);
        listview.setAdapter(adapter);
    }
    
    class QBitTimer extends TimerTask {
  	  public void run() {
  		refreshTorrentList();
  	  }
  	} 
    
}

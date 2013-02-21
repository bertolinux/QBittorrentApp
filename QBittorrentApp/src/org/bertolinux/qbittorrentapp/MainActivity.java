package org.bertolinux.qbittorrentapp;

import java.util.LinkedList;
import java.util.List;
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
	String url;
    String username;
    String password; 
    QBitDatabase db;
    MainActivity myActivity = this;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }
    
    private void init() {
    	db = new QBitDatabase(this);
    	if (!db.exists())
    		askConnectionData();
    	else {
    		url = "http://" + db.getHostname() + ":" + db.getPort();
    		username = db.getUsername();
    		password = db.getPassword();
    		showTorrents();
    	}
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
	            db.insertConnection(hostname, port, username, password);
	    		ask.dismiss();
	    		init();
	    	}
    	});
		ask.show();
    }
    
    private void showTorrents() {        
    	new GetTorrentList(this).execute();   
		
        Button refresh 	= (Button) findViewById(R.id.refresh);
        refresh.setOnClickListener(new OnClickListener() {
	    	public void onClick(View v1) {
	            new GetTorrentList(MainActivity.this).execute();   
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
			    		//db.reset();
			    		//init();
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

    public String getUrl() {
    	return url;
    }
    
    public String getUsername() {
    	return username;
    }
    
    public String getPassword() {
    	return password;
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
}

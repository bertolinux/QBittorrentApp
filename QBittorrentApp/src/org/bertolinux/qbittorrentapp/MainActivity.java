package org.bertolinux.qbittorrentapp;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONObject;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
//import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
 
public class MainActivity extends Activity {
	private static final int FILE_SELECT_CODE = 0;
	private static final int PICK_CONTACT_REQUEST = 0;
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
    
    private void initIntent() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT); 
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            startActivityForResult(
                    Intent.createChooser(intent, "Select txt file"),
                    FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
        	
        }
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
    	new GetTotals(this).execute(connectionData);   		
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
        
        Button uploadButton = (Button) findViewById(R.id.UploadIdButton); 
        uploadButton.setOnClickListener(new OnClickListener() {
	    	public void onClick(View v1) {
	    		initIntent();
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
	public void manageTorrentList(String json) {
    	try {
	    	JSONArray jsonArray = new JSONArray(json);
	        List<TDownload> list = new LinkedList<TDownload>();
	        for (int i=0; i < jsonArray.length(); i++)
	    		list.add(new TDownload(jsonArray.getJSONObject(i)));
	    	ListView listview = (ListView) findViewById(R.id.listView1);
	        QBitAdapter adapter = new QBitAdapter(this, R.layout.row, list);
	        listview.setAdapter(adapter);
    	} catch (Exception e) {
			e.printStackTrace();
		} 
    }
    
	public void setTotals(String json) {
    	try {
    		JSONObject jsonObject = new JSONObject(json);
    		String[] fields = { "DlInfos","UpInfos"}; 
        	try {
    	    	String download = jsonObject.getString(fields[0]);
    	    	String upload = jsonObject.getString(fields[1]);
    	        TextView totDl = (TextView) findViewById(R.id.TotDl);
    	        TextView totUl = (TextView) findViewById(R.id.TotUl);
    	        totDl.setText(download);
    	        totUl.setText(upload);
        	} catch (Exception e) {};
	        
    	} catch (Exception e) {
			e.printStackTrace();
		} 
    }
	
    class QBitTimer extends TimerTask {
  	  public void run() { 
  		refreshTorrentList();
  	  }
  	} 
    
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_CONTACT_REQUEST) {
            if (resultCode == RESULT_OK) {
            	File file = new File(data.getData().getPath());
            	Upload myupload = new Upload(this);    	
            	myupload.setUpload(file);
            	myupload.execute(connectionData);
            }
        }
    }
}

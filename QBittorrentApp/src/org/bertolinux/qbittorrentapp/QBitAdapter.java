package org.bertolinux.qbittorrentapp;

import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

class QBitAdapter extends ArrayAdapter<TDownload> {
	protected MainActivity myactivity;
	
	public QBitAdapter(Context context, int textViewResourceId, List<TDownload> list) {
		super(context, textViewResourceId, list);
		myactivity = (MainActivity) context;
	}
	
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext()
             .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.row, null);
        final TextView name = (TextView)convertView.findViewById(R.id.textview_name);
        TextView size = (TextView)convertView.findViewById(R.id.textview_size);
        TextView progress = (TextView)convertView.findViewById(R.id.textview_progress);
        ProgressBar progressbar = (ProgressBar)convertView.findViewById(R.id.progressbar);
        TextView dlspeed = (TextView)convertView.findViewById(R.id.textview_dlspeed);
        TextView eta = (TextView)convertView.findViewById(R.id.textview_eta);
        TDownload c = getItem(position);
        name.setText(c.getName());
        String percentual = String.valueOf(Math.round(Double.valueOf(c.getProgress()).doubleValue()*100)); 
        progress.setText(percentual+"%");
        progressbar.setProgress(Integer.valueOf(percentual));
        size.setText(c.getSize());
        dlspeed.setText(c.getDlspeed());
        eta.setText(c.getEta());
        final String hash = c.getHash();
        final String state = c.getState();        
        
        switch (Integer.valueOf(percentual)) {
        case 100:
        	convertView.setBackgroundColor(Color.rgb(6,150	,0));
        	break;
        case 0:
        	convertView.setBackgroundColor(Color.rgb(200,100,100));
        	break;
        default:
        	convertView.setBackgroundColor(Color.YELLOW);
        	break;
        }
        
        if (state.contains("paused")) {
        	dlspeed.setText("STOP");
        	convertView.setBackgroundColor(Color.LTGRAY);
        }
        	
        convertView.setOnClickListener(new OnClickListener() {
	    	public void onClick(View v) {
	    		final Dialog dialog = new Dialog(getContext());
	    		dialog.setContentView(R.layout.dialog);
	    		dialog.setTitle("Torrent Operation");
	    		
	    		TextView dialogtextview1 = (TextView) dialog.findViewById(R.id.dialogtextview1);
	    		dialogtextview1.setText(name.getText());
	    		
	    		Button Stop = (Button) dialog.findViewById(R.id.Stop);
	    		Button Start = (Button) dialog.findViewById(R.id.Start);
	    		Button Delete = (Button) dialog.findViewById(R.id.Delete);
	    		Button DeleteFromDisk = (Button) dialog.findViewById(R.id.DeleteFromDisk);
	    		
	    		Stop.setOnClickListener(new OnClickListener() {
	    	    	public void onClick(View v1) {
	    	    		v1.setClickable(false);
	    	            new SendCommand(myactivity,"pause",hash, dialog).execute(myactivity.getConnectionData());
	    	    	}
	        	});
	    		Start.setOnClickListener(new OnClickListener() {
	    	    	public void onClick(View v1) {
	    	    		v1.setClickable(false);
	    	            new SendCommand(myactivity,"resume",hash, dialog).execute(myactivity.getConnectionData());
	    	    	}
	        	});
	    		Delete.setOnClickListener(new OnClickListener() {
	    	    	public void onClick(View v1) {
	    	    		v1.setClickable(false);
	    	            new SendCommand(myactivity,"delete",hash, dialog).execute(myactivity.getConnectionData());
	    	            new SendCommand(myactivity,"delete",hash, dialog, true).execute(myactivity.getConnectionData());
	    	    	}
	        	});
	    		DeleteFromDisk.setOnClickListener(new OnClickListener() {
	    	    	public void onClick(View v1) {
	    	    		v1.setClickable(false);
	    	            new SendCommand(myactivity,"deletePerm",hash, dialog).execute(myactivity.getConnectionData() );
	    	            new SendCommand(myactivity,"deletePerm",hash, dialog, true).execute(myactivity.getConnectionData() );
	    	    	}
	        	});
	    		dialog.show();
	    	}
    	});
        return convertView;
    }
}
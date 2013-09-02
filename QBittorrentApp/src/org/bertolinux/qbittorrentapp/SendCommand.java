package org.bertolinux.qbittorrentapp;

import android.app.Dialog;

public class SendCommand extends QBitConnection {
	private Dialog dialog;
	
	public SendCommand(MainActivity activity, String command, String hash, Dialog dialog) {
		super(activity);
		setSpecificUrl("/command/"+command);
		setHash(hash);
		this.dialog = dialog;
	}
	
	protected void onPostExecute(String result) {
		try {
			dialog.dismiss();
			myactivity.refreshTorrentList();
		} catch (Exception e) {
			e.printStackTrace();
		} 
  }

}

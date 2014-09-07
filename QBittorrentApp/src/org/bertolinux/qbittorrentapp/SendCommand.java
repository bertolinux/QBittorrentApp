package org.bertolinux.qbittorrentapp;

import android.app.Dialog;

public class SendCommand extends QBitConnection implements QBitManagerConnection {
	private Dialog dialog;
	
	public SendCommand(MainActivity activity, String command, String hash, Dialog dialog) {
		super(activity);
		setSpecificUrl("/command/"+command);
		setHash(hash);
		this.dialog = dialog;
	}
	
	public SendCommand(MainActivity activity, String command, String hash, Dialog dialog, boolean newVersion) {
		this(activity, command, hash, dialog);
		setNewVersion();
	}
	
	public void onPostExecute(String result) {
		dialog.dismiss();
		myactivity.refreshTorrentList();
  }

}

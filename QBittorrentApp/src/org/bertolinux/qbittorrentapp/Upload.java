package org.bertolinux.qbittorrentapp;

import java.io.File;

public class Upload extends QBitConnection implements QBitManagerConnection {
	public Upload(MainActivity activity) {
		super(activity);
		setSpecificUrl("/command/upload");
	}
	
	public void setUpload(File upload) {
		this.upload = upload;
	}

	@Override
	public void onPostExecute(String result) {
		myactivity.refreshTorrentList();
	}
}

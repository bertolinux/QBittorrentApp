package org.bertolinux.qbittorrentapp;

public class GetTorrentList extends QBitConnection implements QBitManagerConnection {
	public GetTorrentList(MainActivity activity) {
		super(activity);
		setSpecificUrl("/json/events");
	}
	
	public void onPostExecute(String result) {
		myactivity.manageTorrentList(result);
  }

}

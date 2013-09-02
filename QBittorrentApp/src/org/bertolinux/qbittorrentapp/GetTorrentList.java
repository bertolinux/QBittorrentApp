package org.bertolinux.qbittorrentapp;

public class GetTorrentList extends QBitConnection {
	public GetTorrentList(MainActivity activity) {
		super(activity);
		setSpecificUrl("/json/events");
	}
	
	protected void onPostExecute(String result) {
		try {
			myactivity.manageTorrentList(result);
		} catch (Exception e) {
			e.printStackTrace();
		} 
  }

}

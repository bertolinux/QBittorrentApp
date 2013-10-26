package org.bertolinux.qbittorrentapp;

public class GetTotals extends QBitConnection implements QBitManagerConnection {
	public GetTotals(MainActivity activity) {
		super(activity);
		setSpecificUrl("/json/transferInfo");
//		{"DlInfos":"D: 0.0 B/s - T: 1.8 GiB","UpInfos":"U: 0.0 B/s - T: 301.7 MiB"}
	}
	
	public void onPostExecute(String result) {
		myactivity.setTotals(result);
  }

}

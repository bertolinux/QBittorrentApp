package org.bertolinux.qbittorrentapp;

import org.json.JSONObject;

class TDownload {
	private String name;
	private String size;
	private String progress;
	private String dlspeed;
	private String eta;
	private String hash;
	private String state;
	
	public TDownload(JSONObject jsonObject) {
		String[] fields = { "name","size","progress","dlspeed","eta","hash","state" }; 
    	try {
	    	this.name = jsonObject.getString(fields[0]);
	        this.size = jsonObject.getString(fields[1]);
	        this.progress = jsonObject.getString(fields[2]);
	        this.dlspeed = jsonObject.getString(fields[3]);
	        this.eta = jsonObject.getString(fields[4]);
	        this.hash = jsonObject.getString(fields[5]);
	        this.state = jsonObject.getString(fields[6]);
    	} catch (Exception e) {};
	}

	public String getName() {
		return this.name;
	}
	
	public String getSize() {
		return this.size;
	}
	
	public String getProgress() {
		return this.progress;
	}
	
	public String getDlspeed() {
		return this.dlspeed;
	}
	
	public String getEta() {
		return this.eta;
	}

	public String getHash() {
		return this.hash;
	}
	
	public String getState() {
		return this.state;
	}
}
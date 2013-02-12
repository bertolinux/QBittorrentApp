package org.bertolinux.qbittorrentapp;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import android.os.AsyncTask;

public class GetTorrentList extends AsyncTask<Void, Integer, String> {
	private MainActivity myactivity;
	
	public GetTorrentList(MainActivity activity) {
		myactivity = activity;
	}
	
	@Override
	protected String doInBackground(Void... arg0) {
		String url = myactivity.getUrl()+"/json/events";
	    String username = myactivity.getUsername();
	    String password = myactivity.getPassword();
		String json = "";
		
		DefaultHttpClient httpClient = new DefaultHttpClient();
        try {
            URL urlObj = new URL(url);
	        HttpHost host = new HttpHost(urlObj.getHost(), urlObj.getPort(), urlObj.getProtocol());
	        AuthScope scope = new AuthScope(urlObj.getHost(), urlObj.getPort());
	        UsernamePasswordCredentials creds = new UsernamePasswordCredentials(username, password);
	
	        CredentialsProvider cp = new BasicCredentialsProvider();
	        cp.setCredentials(scope, creds);
	        HttpContext credContext = new BasicHttpContext();
	        credContext.setAttribute(ClientContext.CREDS_PROVIDER, cp);
	
	        HttpGet job = new HttpGet(url);
        	HttpResponse response = httpClient.execute(host,job,credContext);
        	BufferedReader rd;
			rd = new BufferedReader
			          (new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
			    json += line;
			}
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        return json;
	}
	
	protected void onPostExecute(String result) {
		try {
			myactivity.manageTorrentList(result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
  }

}

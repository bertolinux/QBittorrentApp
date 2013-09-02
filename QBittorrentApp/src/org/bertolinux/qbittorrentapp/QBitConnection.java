package org.bertolinux.qbittorrentapp;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import android.os.AsyncTask;

public class QBitConnection extends AsyncTask<QBitConnectionData, Void, String> {
	protected MainActivity myactivity;
	private String specificUrl;
	private String hash;
	public QBitConnection(MainActivity activity) {
		myactivity = activity;
	}
	
	public void setSpecificUrl(String text) {
		specificUrl = text;
	}
	
	public void setHash(String hash) {
		this.hash = hash;
	}
	
	@Override
	protected String doInBackground(QBitConnectionData... connectionData) {
		String url = connectionData[0].getUrl() + specificUrl;
		String output = "";
		
		DefaultHttpClient httpClient = new DefaultHttpClient();
        try {
            URL urlObj = new URL(url);
	        HttpHost host = new HttpHost(urlObj.getHost(), urlObj.getPort(), urlObj.getProtocol());
	        AuthScope scope = new AuthScope(urlObj.getHost(), urlObj.getPort());
	        UsernamePasswordCredentials creds = new UsernamePasswordCredentials(connectionData[0].getUsername(), connectionData[0].getPassword());
	
	        CredentialsProvider cp = new BasicCredentialsProvider();
	        cp.setCredentials(scope, creds);
	        HttpContext credContext = new BasicHttpContext();
	        credContext.setAttribute(ClientContext.CREDS_PROVIDER, cp);
	
	        HttpPost job = new HttpPost(url);
	        
	        if (this.hash != null) {
		        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		        nameValuePairs.add(new BasicNameValuePair("hash", hash));
		        job.setEntity(new UrlEncodedFormEntity(nameValuePairs));		        
	        }
	        
	        HttpResponse response = httpClient.execute(host,job,credContext);	        
	        BufferedReader rd;
			rd = new BufferedReader (new InputStreamReader(response.getEntity().getContent()));
			
			String line = "";
			while ((line = rd.readLine()) != null) {
				output += line;
			}
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        return output;
	}
}

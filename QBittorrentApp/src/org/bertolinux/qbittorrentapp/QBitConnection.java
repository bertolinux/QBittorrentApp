package org.bertolinux.qbittorrentapp;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
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
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import android.os.AsyncTask;

public abstract class QBitConnection extends AsyncTask<QBitConnectionData, Void, String> {
	private String specificUrl;
	private String hash;

	protected MainActivity myactivity;
	protected File upload; 
	
	public QBitConnection(MainActivity activity) {
		myactivity = activity;
	}
	
	public void setSpecificUrl(String text) {
		specificUrl = text;
	}
	
	public void setHash(String hash) {
		this.hash = hash;
	}
	
	private void insertHash(HttpPost job) {
		if (this.hash == null)
			return;
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
        nameValuePairs.add(new BasicNameValuePair("hash", this.hash));
        try {
			job.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	private void insertUploadFile(HttpPost job) {
		if (this.upload == null)
			return;
		MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
		reqEntity.addPart("torrentfile", new FileBody(this.upload));
	    job.setEntity(reqEntity);	
	    job.addHeader("Content-Type", "multipart/form-data");		    
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
	        
	        insertHash(job);
	        insertUploadFile(job);
	        
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

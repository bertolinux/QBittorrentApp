package org.bertolinux.qbittorrentapp;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpHost;
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

import android.app.Dialog;
import android.os.AsyncTask;

public class SendCommand extends AsyncTask<Object, Integer, String> {
	private MainActivity myactivity;
	private Dialog dialog;
	
	public SendCommand(MainActivity activity) {
		myactivity = activity;
	}
	
	@Override
	protected String doInBackground(Object... arg0) {
	    String username = myactivity.getUsername();
	    String password = myactivity.getPassword();
		String url = myactivity.getUrl()+"/command/"+arg0[0];
		String hash = (String) arg0[1];
		dialog = (Dialog) arg0[2];
		
		// TODO Auto-generated method stub
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
	
	        HttpPost job = new HttpPost(url);

	        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
	        nameValuePairs.add(new BasicNameValuePair("hash", hash));
	        job.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	        
	        httpClient.execute(host,job,credContext);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
		return null;
	}
	
	protected void onPostExecute(String result) {
		try {
	        new GetTorrentList(myactivity).execute();
			dialog.dismiss();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
  }

}

package com.login;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.login.R;

import android.content.Intent;
import android.content.SharedPreferences;

public class MainActivity extends Activity {

	SharedPreferences.Editor editor;

	public void start(){

		SharedPreferences settings = getSharedPreferences("pref_meetmethere", 0);
		editor = settings.edit();


		//AUTOLOGIN
		if (settings.getBoolean("autologin", false)){

			ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
			String log=settings.getString("login", "error");
			String pass=settings.getString("password", "error");
			postParameters.add(new BasicNameValuePair("login", log));
			postParameters.add(new BasicNameValuePair("password", pass));

			String response = null;
			try {
				response = CustomHttpClient.executeHttpPost("http://antoines.goldzoneweb.info/login.php", postParameters);
				String res=response.toString();
				res= res.replaceAll("\\s+","");
				if(res.equals("OK")){  
					String mode = settings.getString("mode", "default");
					Intent myIntent;
					if(mode.equals("alone"))
						myIntent = new Intent(this, events.class);
					else
						myIntent = new Intent(this, home.class);
					startActivityForResult(myIntent, 0);			     
				}
				else {
					Intent myIntent = new Intent(MainActivity.this, login.class);
					startActivityForResult(myIntent, 0);
				}
			} catch (Exception e) {
				Intent myIntent = new Intent(MainActivity.this, login.class);
				startActivityForResult(myIntent, 0);            	}	        	

		}
		else{
			Intent myIntent = new Intent(MainActivity.this, login.class);
			startActivityForResult(myIntent, 0);	
		}
	}


	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		start();
	}
	public void onResume(){
		super.onResume();
		setContentView(R.layout.main);
		start();		
	}
	
	public void onRestart(){
		super.onRestart();
		setContentView(R.layout.main);
		start();		
	}
}
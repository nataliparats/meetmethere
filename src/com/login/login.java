package com.login;

import android.app.Activity;
import android.os.Bundle;
import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.login.R;

import android.view.View;
import android.view.Window;
import android.widget.*;
import android.content.Intent;
import android.content.SharedPreferences;

public class login extends Activity {
	EditText un, pw;
	TextView error;
	Button ok, new_account;
	CheckBox rm_pw;
	SharedPreferences.Editor editor;
	RadioGroup radioGroup;

	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.login);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title_style);
		radioGroup = (RadioGroup) findViewById(R.id.radiomode);
		radioGroup.check(R.id.yes);
		un = (EditText) findViewById(R.id.et_un);
		pw = (EditText) findViewById(R.id.et_pw);
		rm_pw = (CheckBox) findViewById(R.id.checkBox1);
		ok = (Button) findViewById(R.id.btn_login);
		error = (TextView) findViewById(R.id.tv_error);
		new_account = (Button) findViewById(R.id.new_account);


		SharedPreferences settings = getSharedPreferences("pref_meetmethere", 0);
		editor = settings.edit();

		radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			public void onCheckedChanged(RadioGroup arg0, int idradio) {
				switch (idradio) {
				case R.id.yes:		
					editor.putString("mode", "default");
					break;
				case R.id.no:
					editor.putString("mode", "alone");
					break;
				}
				editor.commit();

			}
		});
		
		
		un.setOnFocusChangeListener(new View.OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {

				ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
				postParameters.add(new BasicNameValuePair("login", un.getText().toString()));

				String response = null;
				try {
					response = CustomHttpClient.executeHttpPost("http://antoines.goldzoneweb.info/check_login.php", postParameters);
					String res=response.toString();
					res= res.replaceAll("\\s+","");
					if(res.equals("OK"))
						error.setText("Correct Username");         	    		    
					else
						error.setText(res);
				} catch (Exception e) {
					error.setText(e.toString());
				}          	
			}
		});

		new_account.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				Intent myIntent = new Intent(view.getContext(), registration.class);
				startActivityForResult(myIntent, 0);					
			}

		}
				);


		ok.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
				postParameters.add(new BasicNameValuePair("login", un.getText().toString()));
				postParameters.add(new BasicNameValuePair("password", pw.getText().toString()));

				String response = null;
				try {
					response = CustomHttpClient.executeHttpPost("http://antoines.goldzoneweb.info/login.php", postParameters);
					String res=response.toString();
					res= res.replaceAll("\\s+","");
					
					if(res.equals("OK")){
						String encodedPassword = pw.getText().toString();
						String encodedLogin = un.getText().toString();
						editor.putString("password", encodedPassword);
						editor.putString("login", encodedLogin);

						if (rm_pw.isChecked()){
							//save password
							editor.putBoolean("autologin", true);
						} else {
							editor.putBoolean("autologin", false);				
						}
						
						editor.commit();
						Intent myIntent ;
						SharedPreferences settings = getSharedPreferences("pref_meetmethere", 0);
						String mode = settings.getString("mode", "default");
						if(mode.equals("alone"))
							myIntent = new Intent(login.this, events.class);
						else
							myIntent = new Intent(login.this, home.class);
						startActivityForResult(myIntent, 0);
					} else
						error.setText(res);
				} catch (Exception e) {
					error.setText(e.toString());
				}
			}
		});
	}
}
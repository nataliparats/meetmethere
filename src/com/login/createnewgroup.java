package com.login;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.AdapterView.OnItemClickListener;

public class createnewgroup extends Activity_with_Menu {
	/**
	 * @see android.app.Activity#onCreate(Bundle)
	 */
	Button next;
	EditText description,name; 
	SharedPreferences.Editor editor;

	static final int DATE_DIALOG_ID = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.createnewgroup);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title_style);
		
		next=(Button)findViewById(R.id.next);
		description=(EditText)findViewById(R.id.description);
		name=(EditText)findViewById(R.id.name);	

		SharedPreferences settings = getSharedPreferences("pref_meetmethere", 0);
		editor = settings.edit();
		






		next.setOnClickListener(new View.OnClickListener() {	
			@Override
			public void onClick(View v) {


				ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();										
				postParameters.add(new BasicNameValuePair("name", name.getText().toString()));
				postParameters.add(new BasicNameValuePair("description", description.getText().toString()));							
		
		
				String response = null;
				try {
					response = CustomHttpClient.executeHttpPost("http://antoines.goldzoneweb.info/newgroup.php", postParameters);
					String res=response.toString();
					res= res.replaceAll("\\s+","");
					Intent intent = new Intent(v.getContext(), group.class);
					startActivity(intent);	   	            	     	
				} catch (Exception e) {
					//ERROR pop-up
				}
				
			}
		});	
	}




}

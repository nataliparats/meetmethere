package com.login;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class editprofile extends Activity_with_Menu{
	
	/**
	 * @see android.app.Activity#onCreate(Bundle)
	 */
	EditText fullName, phone, email, website, country;
	SharedPreferences.Editor editor;
	String log,pass;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.editprofile);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title_style);
		
		fullName=(EditText) findViewById(R.id.fullname);
		phone=(EditText) findViewById(R.id.phone);
		email=(EditText) findViewById(R.id.email);
		website=(EditText) findViewById(R.id.website);
		country=(EditText) findViewById(R.id.country);
		SharedPreferences settings = getSharedPreferences("pref_meetmethere", 0);
		editor = settings.edit();
		ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
		log=settings.getString("login", "error");
		pass=settings.getString("password", "error");
		postParameters.add(new BasicNameValuePair("login", log));
		postParameters.add(new BasicNameValuePair("password", pass));
		String xml = XMLfunctions.getXML("http://antoines.goldzoneweb.info/xml_profile.php", postParameters);
		Document doc = XMLfunctions.XMLfromString(xml);		
		NodeList nodes = doc.getElementsByTagName("you");
		Element e = (Element)nodes.item(0);
		fullName.setText(fullName.getText()+XMLfunctions.getValue(e, "fullname"));
		phone.setText(phone.getText()+XMLfunctions.getValue(e, "tel"));
		email.setText(email.getText()+XMLfunctions.getValue(e, "email"));
		website.setText(website.getText()+XMLfunctions.getValue(e, "website"));
		country.setText(country.getText()+XMLfunctions.getValue(e, "country"));
	
		final Button save = (Button) findViewById(R.id.save);
		save.setOnClickListener(new View.OnClickListener() {
		    public void onClick(View v) {
		    	save.setVisibility(View.INVISIBLE);
		    	ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
		    	postParameters.add(new BasicNameValuePair("login", log));
				postParameters.add(new BasicNameValuePair("password", pass));
            	postParameters.add(new BasicNameValuePair("fullname", fullName.getText().toString()));
            	postParameters.add(new BasicNameValuePair("phone", phone.getText().toString()));
            	postParameters.add(new BasicNameValuePair("email", email.getText().toString()));
            	postParameters.add(new BasicNameValuePair("website", website.getText().toString()));
            	postParameters.add(new BasicNameValuePair("country", country.getText().toString()));
            	String response = null;
            	try {
            	    response = CustomHttpClient.executeHttpPost("http://antoines.goldzoneweb.info/modify_profile.php", postParameters);
            	    String res=response.toString();
            	    res= res.replaceAll("\\s+","");
            	    if(res.equals("OK")){
            	    	save.setVisibility(View.VISIBLE);
            	    	editor.commit();
            	    	Intent myIntent = new Intent(v.getContext(), profile.class);
    	                startActivityForResult(myIntent, 0);
            	    }
            	   
            	} catch (Exception e) {
            		System.out.println("Error occured");
            	}
            }
        });
		
	}
}

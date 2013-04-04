package com.login;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.login.R;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

public class registration extends Activity {
	EditText user_name,pw1,pw2,name,phone,email,website,city,country;
	Button register;
	SharedPreferences.Editor editor;
	boolean no_error=true;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.registration);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title_style);
		
		user_name=(EditText)findViewById(R.id.user_name);
		pw1=(EditText)findViewById(R.id.pw1);
		pw2=(EditText)findViewById(R.id.pw2);
		name=(EditText)findViewById(R.id.name);
		phone=(EditText)findViewById(R.id.phone);
		email=(EditText)findViewById(R.id.email);
		website=(EditText)findViewById(R.id.website);
		city=(EditText)findViewById(R.id.city);
		country=(EditText)findViewById(R.id.country);              
		register=(Button)findViewById(R.id.register);
		SharedPreferences settings = getSharedPreferences("pref_meetmethere", 0);
		editor = settings.edit();

		register.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				no_error=true;
				
				if(!pw1.getText().toString().equals(pw2.getText().toString())){
					pw2.setHint("Password didn't match");
					pw2.setText("");
					pw2.setHintTextColor(Color.RED);
					no_error=false;
				}
				if (pw1.getText().length()<4){
					pw1.setHint("Password too short");
					pw1.setText("");
					pw1.setHintTextColor(Color.RED);
					no_error=false;
				}
				if (user_name.getText().length()<4){
					user_name.setHint("User Name too short");
					user_name.setHintTextColor(Color.RED);	
					user_name.setText("");
					no_error=false;
					
				}
				if (name.getText().length()<4) {
					name.setText("");
					name.setHint("Name too short");
					name.setHintTextColor(Color.RED);
					no_error=false;
				}
				if (phone.getText().length()<=8){
					phone.setHint("Phone Number too short");
					phone.setHintTextColor(Color.RED);	
					no_error=false;
					phone.setText("");
				}
				if (email.getText().length()==0){
					email.setHint("email empty");
					email.setHintTextColor(Color.RED);
					no_error=false;
					email.setText("");
				}
				
				if (no_error)
				{

					ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
					postParameters.add(new BasicNameValuePair("login", user_name.getText().toString()));
					postParameters.add(new BasicNameValuePair("password1", pw1.getText().toString()));
					postParameters.add(new BasicNameValuePair("password2", pw2.getText().toString()));

					postParameters.add(new BasicNameValuePair("fullname", name.getText().toString()));
					postParameters.add(new BasicNameValuePair("tel", phone.getText().toString()));
					postParameters.add(new BasicNameValuePair("email", email.getText().toString()));
					postParameters.add(new BasicNameValuePair("city", city.getText().toString()));
					postParameters.add(new BasicNameValuePair("country", country.getText().toString()));
					postParameters.add(new BasicNameValuePair("website", website.getText().toString()));

					String response = null;
					try {
						response = CustomHttpClient.executeHttpPost("http://antoines.goldzoneweb.info/register.php", postParameters);
						String res=response.toString();
						res= res.replaceAll("\\s+","");
						if(res.equals("OK")){
							String encodedPassword = pw1.getText().toString();
							String encodedLogin = user_name.getText().toString();
							editor.putString("password", encodedPassword);
							editor.putString("login", encodedLogin);
							editor.commit();
							Intent myIntent = new Intent(view.getContext(), home.class);
							startActivityForResult(myIntent, 0);
						}
						else
							user_name.setText(res);
					} catch (Exception e) {
						//ERROR pop-up
						user_name.setText("EEROR");
					}
				}



			}

	});


}
}

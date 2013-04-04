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
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;


public class profile extends Activity_with_Menu {
	/**
	 * @see android.app.Activity#onCreate(Bundle)
	 */
	TextView fullName, phone, email, website, country;
	SharedPreferences.Editor editor;
	String contactlog,id,yourrequest;
	int confirmed;
	Button edit;
	RadioGroup radioGroup;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.profile);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title_style);
		radioGroup = (RadioGroup) findViewById(R.id.radiop);
		fullName=(TextView) findViewById(R.id.fullname);
		phone=(TextView) findViewById(R.id.phone);
		email=(TextView) findViewById(R.id.email);
		website=(TextView) findViewById(R.id.website);
		country=(TextView) findViewById(R.id.country);
		edit = (Button) findViewById(R.id.editprofile);
		SharedPreferences settings = getSharedPreferences("pref_meetmethere", 0);
		editor = settings.edit();
		ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
		String log=settings.getString("login", "error");
		String pass=settings.getString("password", "error");
		contactlog=log;
		Intent intent = getIntent();
		if (intent.getExtras()!=null){
			yourrequest=intent.getExtras().getString("yourrequest");
			confirmed=Integer.parseInt(intent.getExtras().getString("confirmed"));
			if (yourrequest.equals("false") && (confirmed==0||confirmed==2) ){
				TextView rdlabel=(TextView) findViewById(R.id.rdlabel);
				if (confirmed==0)
					rdlabel.setText("Your contact request has been send");		
				else
					rdlabel.setText("your request has been rejected");
				findViewById(R.id.yes).setVisibility(View.INVISIBLE);
				findViewById(R.id.no).setVisibility(View.INVISIBLE);
			}
			else{
				TextView rdlabel=(TextView) findViewById(R.id.rdlabel);
				rdlabel.setText("contact");		
				findViewById(R.id.yes).setVisibility(View.VISIBLE);
				findViewById(R.id.no).setVisibility(View.VISIBLE);
			}
			radioGroup.setVisibility(View.VISIBLE);
			if (confirmed==1)
				radioGroup.check(R.id.yes);
			else if (confirmed==2)
				radioGroup.check(R.id.no);


			contactlog=intent.getExtras().getString("friend");
			id=intent.getExtras().getString("id");

			postParameters.add(new BasicNameValuePair("login", contactlog));

			String xml = XMLfunctions.getXML("http://antoines.goldzoneweb.info/xml_profile.php", postParameters);
			Document doc = XMLfunctions.XMLfromString(xml);		
			NodeList nodes = doc.getElementsByTagName("you");
			Element e = (Element)nodes.item(0);
			fullName.setText(fullName.getText()+XMLfunctions.getValue(e, "fullname"));
			phone.setText(phone.getText()+"\n Phone : "+XMLfunctions.getValue(e, "tel"));
			email.setText(email.getText()+"\n E-mail : "+XMLfunctions.getValue(e, "email"));
			website.setText(website.getText()+"\n Website : "+XMLfunctions.getValue(e, "website"));
			country.setText(country.getText()+"\n Country : "+XMLfunctions.getValue(e, "country"));

		}else{
			edit.setVisibility(View.VISIBLE);
			radioGroup.setVisibility(View.INVISIBLE);
			postParameters.add(new BasicNameValuePair("login", log));

			String xml = XMLfunctions.getXML("http://antoines.goldzoneweb.info/xml_profile.php", postParameters);
			Document doc = XMLfunctions.XMLfromString(xml);		
			NodeList nodes = doc.getElementsByTagName("you");
			Element e = (Element)nodes.item(0);
			fullName.setText(fullName.getText()+XMLfunctions.getValue(e, "fullname"));
			phone.setText(phone.getText()+"\n Phone: "+XMLfunctions.getValue(e, "tel"));
			email.setText(email.getText()+"\n E-mail: "+XMLfunctions.getValue(e, "email"));
			website.setText(website.getText()+"\n Website: "+XMLfunctions.getValue(e, "website"));
			country.setText(country.getText()+"\n Country: "+XMLfunctions.getValue(e, "country"));
		}

		
		if(log.equals(contactlog)){
			edit.setVisibility(View.VISIBLE);
			edit.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					Intent myIntent = new Intent(v.getContext(), editprofile.class);
					startActivityForResult(myIntent, 0);
				}
			});
		}
		
		
		radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			public void onCheckedChanged(RadioGroup arg0, int idradio) {
				ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
				SharedPreferences settings = getSharedPreferences("pref_meetmethere", 0);
				String log = settings.getString("login", "error");
				String pass = settings.getString("password", "error");
				postParameters.add(new BasicNameValuePair("login", log));
				postParameters.add(new BasicNameValuePair("password", pass));
				postParameters.add(new BasicNameValuePair("id", id));
				switch (idradio) {
				case R.id.yes:		
					confirmed=1;
					postParameters.add(new BasicNameValuePair("confirmed","1"));
					break;
				case R.id.no:
					confirmed=2;
					postParameters.add(new BasicNameValuePair("confirmed","2"));
					break;
				}
				try {
					CustomHttpClient.executeHttpPost("http://antoines.goldzoneweb.info/accept_contact.php", postParameters);
					Intent myIntent = new Intent(profile.this, contacts.class);
					startActivityForResult(myIntent, 0);
				}catch (Exception e) {System.out.println("Error occured");}
				
				
				

			}
		});



	}
}

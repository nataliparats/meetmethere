package com.login;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.w3c.dom.Element;

import com.google.android.maps.GeoPoint;

import android.widget.AdapterView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;

public class addeventaddress extends Activity_with_Menu {
	Button search;
	EditText address;
	ListView lv;
	SharedPreferences.Editor editor;
	String title,description,datestart,timestart,dateend,timeend,log,pass;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.addeventaddress);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title_style);
		
		lv=(ListView)findViewById(R.id.list);
		search=(Button)findViewById(R.id.search);
		address=(EditText)findViewById(R.id.address);
		SharedPreferences settings = getSharedPreferences("pref_meetmethere", 0);
		editor = settings.edit();
		
		Intent intent = getIntent();
		log=settings.getString("login", "error");
		pass=settings.getString("password", "error");		
		title=intent.getExtras().getString("title");
		description=intent.getExtras().getString("description");		
		datestart=intent.getExtras().getString("datestart");
		timestart=intent.getExtras().getString("timestart");		
	    dateend=intent.getExtras().getString("dateend");
		timeend=intent.getExtras().getString("timeend");		
		
		
		search.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Geocoder geocoder = new Geocoder(getBaseContext(),
						Locale.getDefault());
				try {
					List<Address> addresses = geocoder.getFromLocationName(address.getText().toString(),100);
					ArrayList<HashMap<String, String>> l=new ArrayList<HashMap<String, String>> ();
					for (int i=0;i<addresses.size();i++){
						HashMap<String, String> map = new HashMap<String, String>();	
						String str="";
						for (int j = 0; j < addresses.get(i).getMaxAddressLineIndex(); j++){
							str=str+"\n"+addresses.get(i).getAddressLine(j);
						}				
						map.put("title", str);
						map.put("latitude", Double.toString(addresses.get(i).getLatitude()));
						map.put("longitude", Double.toString(addresses.get(i).getLongitude()));
						l.add(map);							
					}

					ListAdapter adapter = new SimpleAdapter(addeventaddress.this, l , R.layout.item_list, new String[] { "title"}, 
							new int[] { R.id.item_title});
					lv.setAdapter(adapter);

					lv.setTextFilterEnabled(true);	
					lv.setOnItemClickListener(new OnItemClickListener() {
						private Intent putExtra;

						public void onItemClick(AdapterView<?> parent, View view, int position, long id) {        		
							@SuppressWarnings("unchecked")
							HashMap<String, String> o = (HashMap<String, String>) lv.getItemAtPosition(position);	        									
							
							//SEND DATA
							ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
							postParameters.add(new BasicNameValuePair("login", log));
							postParameters.add(new BasicNameValuePair("password", pass));										
							postParameters.add(new BasicNameValuePair("title", title));
							postParameters.add(new BasicNameValuePair("description", description));							
							postParameters.add(new BasicNameValuePair("datestart", datestart));						
							postParameters.add(new BasicNameValuePair("timestart", timestart));						
							postParameters.add(new BasicNameValuePair("dateend", dateend));							
							postParameters.add(new BasicNameValuePair("timeend", timeend));	
							postParameters.add(new BasicNameValuePair("longitude", o.get("longitude")));
							postParameters.add(new BasicNameValuePair("latitude", o.get("latitude")));
							postParameters.add(new BasicNameValuePair("address", o.get("title")));

					
							String response = null;
							try {
								lv.setAdapter(null);
								response = CustomHttpClient.executeHttpPost("http://antoines.goldzoneweb.info/addevent.php", postParameters);
								String res=response.toString();
								res= res.replaceAll("\\s+","");
								SharedPreferences settings = getSharedPreferences("pref_meetmethere", 0);
								String mode = settings.getString("mode", "default");
								Intent intent;
								if(mode.equals("default"))
									intent = new Intent(view.getContext(), addeventinvite.class);
								else
									intent = new Intent(view.getContext(), events.class);
								intent.putExtra("eventid", res);
								startActivity(intent);	   	            	     	
							} catch (Exception e) {
								//ERROR pop-up
								address.setText("EEROR");
							}
							

						}
					});


				} catch (IOException e) {
					e.printStackTrace();
				}



			}
		});

	}
}

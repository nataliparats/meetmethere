package com.login;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.google.android.maps.MapView;

/*import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;*/

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.CalendarContract.Events;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;

public class event_info extends MapActivity_with_Menu{
	private TextView title,startdate,enddate,address,description,location;
	private TextView info,map,participants;
	private MapView myMapView;
	private Intent intent;
	String strtitle, strstartdate, strenddate,  straddress, strdescription;
	int willAttend=-1;
	RadioGroup radioGroup;
	int checkedRadioButton;
	String id;
	SharedPreferences.Editor editor;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		
		SharedPreferences settings = getSharedPreferences("pref_meetmethere", 0);
		String mode = settings.getString("mode", "default");
		if(mode.equals("default"))
			setContentView(R.layout.event);
		else if (mode.equals("alone"))
			setContentView(R.layout.alone_event);
		
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title_style);
		
		intent = getIntent();
		title = (TextView) findViewById(R.id.title);
		startdate = (TextView) findViewById(R.id.startdate);
		enddate = (TextView) findViewById(R.id.enddate);
		address = (TextView) findViewById(R.id.address);
		description = (TextView) findViewById(R.id.description);
		radioGroup = (RadioGroup) findViewById(R.id.rdgroup);
		location=(TextView)findViewById(R.id.myLocationText);
		myMapView = (MapView)findViewById(R.id.mapView);
		myMapView.setVisibility(View.GONE);
		info = (TextView) findViewById(R.id.info);
		map = (TextView) findViewById(R.id.map);
		participants = (TextView) findViewById(R.id.participants);
		id =intent.getExtras().getString("id");
		strtitle = intent.getExtras().getString("title");
		strstartdate = intent.getExtras().getString("startdate");
		strenddate = intent.getExtras().getString("enddate");
		straddress = intent.getExtras().getString("address");
		if (willAttend==-1)
			willAttend = Integer.parseInt(intent.getExtras().getString("willAttend"));
		strdescription = intent.getExtras().getString("description");
		location.setText(" "+strtitle+"\n "+strstartdate+"\n "+description);

		location.setVisibility(View.GONE);
		title.setText(strtitle);
		startdate.setText("Start Date: "+strstartdate);
		enddate.setText("End Date: "+strenddate);
		address.setText("Address: "+straddress);
		description.setText("Description: "+strdescription);
		title.setVisibility(View.VISIBLE);
		startdate.setVisibility(View.VISIBLE);
		enddate.setVisibility(View.VISIBLE);
		address.setVisibility(View.VISIBLE);
		description.setVisibility(View.VISIBLE);
		participants.setVisibility(View.VISIBLE);
		radioGroup.setVisibility(View.VISIBLE);
		editor = settings.edit();
		String log=settings.getString("login", "error");
		String pass=settings.getString("password", "error");	

		switch (willAttend){

		case 1: radioGroup.check(R.id.yes);break;
		case 2: radioGroup.check(R.id.no);break;


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
					willAttend=1;
					postParameters.add(new BasicNameValuePair("confirmed","1"));
					break;
				case R.id.no:
					willAttend=2;
					Log.i("radioGroup","no");
					postParameters.add(new BasicNameValuePair("confirmed","2"));
					break;
				default:
					Log.i("radioGroup","yes");
					Log.e("Event_info","error radiogroup element inexistant");				
					break;
				}
				try {
					CustomHttpClient.executeHttpPost("http://antoines.goldzoneweb.info/accept_event.php", postParameters);

				}catch (Exception e) {System.out.println("Error occured");}




			}
		});

		info.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

			}
		});
		
	}

	public void onClick(View v){
		if(v.getId() == R.id.info){

		}
		else if(v.getId() == R.id.map){
			title.setVisibility(View.GONE);
			startdate.setVisibility(View.GONE);
			enddate.setVisibility(View.GONE);
			address.setVisibility(View.GONE);
			description.setVisibility(View.GONE);
			participants.setVisibility(View.GONE);
			radioGroup.setVisibility(View.GONE);
			Intent myIntent = new Intent(v.getContext(), mapevent.class);
			myIntent.putExtra("longitude", intent.getExtras().getString("longitude"));
			myIntent.putExtra("latitude", intent.getExtras().getString("latitude"));
			myIntent.putExtra("title", intent.getExtras().getString("title"));
			myIntent.putExtra("startdate",intent.getExtras().getString("startdate"));
			myIntent.putExtra("enddate",intent.getExtras().getString("enddate"));
			myIntent.putExtra("address",intent.getExtras().getString("address"));
			myIntent.putExtra("description", intent.getExtras().getString("description"));
			myIntent.putExtra("willAttend", Integer.toString(willAttend));
			myIntent.putExtra("id", id);

			startActivityForResult(myIntent, 0);
		}
		else if(v.getId() == R.id.participants){
			title.setVisibility(View.GONE);
			startdate.setVisibility(View.GONE);
			enddate.setVisibility(View.GONE);
			address.setVisibility(View.GONE);
			description.setVisibility(View.GONE);
			participants.setVisibility(View.GONE);
			radioGroup.setVisibility(View.GONE);
			Intent myIntent = new Intent(v.getContext(), participants.class);
			myIntent.putExtra("willAttend", Integer.toString(willAttend));
			myIntent.putExtra("longitude", intent.getExtras().getString("longitude"));
			myIntent.putExtra("latitude", intent.getExtras().getString("latitude"));
			myIntent.putExtra("title", intent.getExtras().getString("title"));
			myIntent.putExtra("startdate",intent.getExtras().getString("startdate"));
			myIntent.putExtra("enddate",intent.getExtras().getString("enddate"));
			myIntent.putExtra("address",intent.getExtras().getString("address"));
			myIntent.putExtra("id", id);
			myIntent.putExtra("description", intent.getExtras().getString("description"));
			startActivityForResult(myIntent, 0);
		}
	}

}

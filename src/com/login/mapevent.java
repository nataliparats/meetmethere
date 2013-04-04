package com.login;

import java.util.ArrayList;
import java.util.List;

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
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class mapevent extends MapActivity_with_Menu {

	private MapController mapController;
	private Criteria criteria;
	private LocationManager locationManager;
	private MapView myMapView;
	TextView location;
	private TextView info,map;
	Intent intent;
	String title, date, description;
	double latitude, longitude;

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
		
		location=(TextView)findViewById(R.id.myLocationText);
		info = (TextView) findViewById(R.id.info);
		map = (TextView) findViewById(R.id.map);
		intent = getIntent();
		latitude = Double.parseDouble(intent.getExtras().getString("latitude"));
		longitude = Double.parseDouble(intent.getExtras().getString("longitude"));
		title = intent.getExtras().getString("title");
		date = intent.getExtras().getString("startdate")+" "+intent.getExtras().getString("enddate");
		description=intent.getExtras().getString("description");
		location.setText(" "+title+"\n "+date+"\n "+description);		
		location.setVisibility(View.VISIBLE);
		
		myMapView = (MapView)findViewById(R.id.mapView);
		myMapView.setVisibility(View.VISIBLE);
		mapController = myMapView.getController();
		myMapView.setSatellite(false);
		myMapView.setStreetView(false);
		myMapView.setBuiltInZoomControls(true);
		mapController.setZoom(17);
		List<Overlay> mapOverlays;
		Drawable drawable;
		HelloItemizedOverlay itemizedOverlay;
		mapOverlays = myMapView.getOverlays();
		drawable = this.getResources().getDrawable(R.drawable.androidmarker);
		itemizedOverlay = new HelloItemizedOverlay(drawable,this);
		GeoPoint point = new GeoPoint(
		            (int) (latitude * 1E6), 
		            (int) (longitude * 1E6));
		OverlayItem overlayitem = new OverlayItem(point, title, date+"\n"+description);
		itemizedOverlay.addOverlay(overlayitem);
		mapOverlays.add(itemizedOverlay);
		mapController.setCenter(point);
		
		/*map.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				intent = new Intent(v.getContext(), mapevent.class);
				startActivityForResult(intent, 0);
			}
		});
		info.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				myMapView.setVisibility(View.GONE);
				location.setVisibility(View.GONE);
				Intent myIntent = new Intent(v.getContext(), event.class);
				myIntent.putExtra("longitude", intent.getExtras().getString("longitude"));
				myIntent.putExtra("latitude", intent.getExtras().getString("latitude"));
				myIntent.putExtra("title", intent.getExtras().getString("title"));
				myIntent.putExtra("startdate",intent.getExtras().getString("startdate"));
				myIntent.putExtra("enddate",intent.getExtras().getString("enddate"));
				myIntent.putExtra("address",intent.getExtras().getString("address"));
				myIntent.putExtra("description", intent.getExtras().getString("description"));
				startActivityForResult(myIntent, 0);
			}
		});*/
		
	}

	public void onClick(View v){
		if(v.getId() == R.id.info){
			myMapView.setVisibility(View.GONE);
			location.setVisibility(View.GONE);
			Intent myIntent = new Intent(v.getContext(), event_info.class);
			myIntent.putExtra("longitude", intent.getExtras().getString("longitude"));
			myIntent.putExtra("latitude", intent.getExtras().getString("latitude"));
			myIntent.putExtra("title", intent.getExtras().getString("title"));
			myIntent.putExtra("startdate",intent.getExtras().getString("startdate"));
			myIntent.putExtra("enddate",intent.getExtras().getString("enddate"));
			myIntent.putExtra("address",intent.getExtras().getString("address"));
			myIntent.putExtra("willAttend", intent.getExtras().getString("willAttend"));
			myIntent.putExtra("description", intent.getExtras().getString("description"));
			myIntent.putExtra("id", intent.getExtras().getString("id"));

			startActivityForResult(myIntent, 0);
		}
		else if(v.getId() == R.id.participants){
			myMapView.setVisibility(View.GONE);
			location.setVisibility(View.GONE);
			Intent myIntent = new Intent(v.getContext(), participants.class);
			myIntent.putExtra("longitude", intent.getExtras().getString("longitude"));
			myIntent.putExtra("latitude", intent.getExtras().getString("latitude"));
			myIntent.putExtra("title", intent.getExtras().getString("title"));
			myIntent.putExtra("startdate",intent.getExtras().getString("startdate"));
			myIntent.putExtra("enddate",intent.getExtras().getString("enddate"));
			myIntent.putExtra("address",intent.getExtras().getString("address"));
			myIntent.putExtra("willAttend", intent.getExtras().getString("willAttend"));
			myIntent.putExtra("id", intent.getExtras().getString("id"));
			myIntent.putExtra("description", intent.getExtras().getString("description"));
			startActivityForResult(myIntent, 0);
		}
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
}

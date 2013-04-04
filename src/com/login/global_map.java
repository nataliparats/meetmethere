package com.login;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

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
import android.widget.TextView;

public class global_map extends MapActivity_with_Menu {

	private MapController mapController;
	private Criteria criteria;
	private LocationManager locationManager;
	private MapView myMapView;
	TextView location;
	SharedPreferences.Editor editor;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map_event);
		location=(TextView)findViewById(R.id.myLocationText);
		myMapView = (MapView)findViewById(R.id.mapView);
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
		
		SharedPreferences settings = getSharedPreferences("pref_meetmethere", 0);
		editor = settings.edit();

		ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
		String log=settings.getString("login", "error");
		String pass=settings.getString("password", "error");
		postParameters.add(new BasicNameValuePair("login", log));
		postParameters.add(new BasicNameValuePair("password", pass));
		String xml = XMLfunctions.getXML("http://antoines.goldzoneweb.info/xml_event.php", postParameters);
		Document doc = XMLfunctions.XMLfromString(xml);

		NodeList nodes = doc.getElementsByTagName("event");
		
		for (int i = 0; i < nodes.getLength(); i++) {							
			Element e = (Element)nodes.item(i);					
			double latitude = Double.parseDouble(XMLfunctions.getValue(e, "latitude"));
			double longitude = Double.parseDouble(XMLfunctions.getValue(e, "longitude"));
			String title = XMLfunctions.getValue(e, "title");
			String date = XMLfunctions.getValue(e, "date");
			String description = XMLfunctions.getValue(e, "description");
			itemizedOverlay = new HelloItemizedOverlay(drawable,this);
			GeoPoint point = new GeoPoint(
			            (int) (latitude * 1E6), 
			            (int) (longitude * 1E6));
			OverlayItem overlayitem = new OverlayItem(point, title, date+"\n"+description);
			itemizedOverlay.addOverlay(overlayitem);
			mapOverlays.add(itemizedOverlay);
			mapController.setCenter(point);
		}		
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
}

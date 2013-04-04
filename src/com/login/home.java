/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.login;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.ExpandableListContextMenuInfo;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Demonstrates expandable lists using a custom {@link ExpandableListAdapter}
 * from {@link BaseExpandableListAdapter}.
 */

public class home extends ExpandableListActivity_with_Menu implements ExpandableListView.OnChildClickListener{

	ExpandableListAdapter mAdapter;
	ArrayList<HashMap<String, String>> invitations = new ArrayList<HashMap<String, String>>();
	ArrayList<HashMap<String, String>> contactrequests = new ArrayList<HashMap<String, String>>();
	ArrayList<HashMap<String, String>> events = new ArrayList<HashMap<String, String>>();
	SharedPreferences.Editor editor;
	String log,pass;
	Element e;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		getExpandableListView().setOnChildClickListener(this);
		
		// retrieve children
		SharedPreferences settings = getSharedPreferences("pref_meetmethere", 0);
		ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
		String log = settings.getString("login", "error");
		String pass = settings.getString("password", "error");
		postParameters.add(new BasicNameValuePair("login", log));
		postParameters.add(new BasicNameValuePair("password", pass));
		String xml = XMLfunctions.getXML("http://antoines.goldzoneweb.info/xml_home.php", postParameters);
		Document doc = XMLfunctions.XMLfromString(xml);

		// get invitations
		HashMap<String, String> map = new HashMap<String, String>();
		NodeList nodes = doc.getElementsByTagName("invitation");
		for (int i = 0; i < nodes.getLength(); i++) {
			map = new HashMap<String, String>();
			Element e = (Element) nodes.item(i);
			map.put("willAttend", XMLfunctions.getValue(e, "willAttend"));
			map.put("latitude", XMLfunctions.getValue(e, "latitude"));
			map.put("address", XMLfunctions.getValue(e, "address"));
			map.put("longitude", XMLfunctions.getValue(e, "longitude"));
			map.put("title", XMLfunctions.getValue(e, "title"));
			map.put("id", XMLfunctions.getValue(e, "id"));
			map.put("description", XMLfunctions.getValue(e, "description"));
			map.put("startdate", XMLfunctions.getValue(e, "startdate"));
			map.put("enddate", XMLfunctions.getValue(e, "enddate"));		
			invitations.add(map);
		}

		// get contact requests
		nodes = doc.getElementsByTagName("contactrequest");
		for (int i = 0; i < nodes.getLength(); i++) {
			map = new HashMap<String, String>();
			Element e = (Element) nodes.item(i);
			map.put("id", XMLfunctions.getValue(e, "id"));
			map.put("yourrequest", XMLfunctions.getValue(e, "yourrequest"));
			map.put("login", XMLfunctions.getValue(e, "login"));
			map.put("title", XMLfunctions.getValue(e, "fullname"));
			contactrequests.add(map);
		}

		// get events
		nodes = doc.getElementsByTagName("event");
		for (int i = 0; i < nodes.getLength(); i++) {
			map = new HashMap<String, String>();
			Element e = (Element) nodes.item(i);
			map.put("willAttend", XMLfunctions.getValue(e, "willAttend"));
			map.put("latitude", XMLfunctions.getValue(e, "latitude"));
			map.put("longitude", XMLfunctions.getValue(e, "longitude"));
			map.put("title", XMLfunctions.getValue(e, "title"));
			map.put("startdate", XMLfunctions.getValue(e, "startdate"));
			map.put("enddate", XMLfunctions.getValue(e, "enddate"));			
			map.put("address", XMLfunctions.getValue(e, "address"));			

			map.put("id", XMLfunctions.getValue(e, "id"));
			map.put("description", XMLfunctions.getValue(e, "description"));
			map.put("startdate", XMLfunctions.getValue(e, "startdate"));
			map.put("enddate", XMLfunctions.getValue(e, "enddate"));

			events.add(map);
		}

		// Set up our adapter
		mAdapter = new MyExpandableListAdapter(invitations, contactrequests, events);
		setListAdapter(mAdapter);
		registerForContextMenu(getExpandableListView());

		// Makes the list load with the groups expanded
		int count = mAdapter.getGroupCount();
		for(int i = 0; i < count; i++){
			this.getExpandableListView().expandGroup(i);
		}

		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title_style);




		this.getExpandableListView().setOnItemClickListener(null);
		this.getExpandableListView().setOnChildClickListener(new ExpandableListView.OnChildClickListener()
		{
			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				return parent.showContextMenuForChild(v);
			}
		});
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		ExpandableListView.ExpandableListContextMenuInfo info = (ExpandableListView.ExpandableListContextMenuInfo) menuInfo;
		String title = ((TextView) info.targetView).getText().toString();
		menu.setHeaderTitle(title);

		int type = ExpandableListView.getPackedPositionType(info.packedPosition);

		if (type == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
			super.onCreateContextMenu(menu, v, menuInfo);
			int groupPos = ExpandableListView.getPackedPositionGroup(info.packedPosition);
			if (groupPos==1 || groupPos==0){
				menu.add(1, 0, 0, "ACCEPT");
				menu.add(1, 1, 0, "DECLINE");
				menu.add(1, 2, 0, "SHOW INFO");
			}
			else if (groupPos==2){
				menu.add(1, 0, 0, "SHOW INFO");
				menu.add(1, 1, 0, "SHOW ON A MAP");
				menu.add(1, 2, 0, "WILL NOT ATTEND");

			}
		}
	}	


	@Override
	public boolean onContextItemSelected(MenuItem item) {
		ExpandableListContextMenuInfo info = (ExpandableListContextMenuInfo) item.getMenuInfo();

		int type = ExpandableListView.getPackedPositionType(info.packedPosition);
		if (type == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
			int groupPos = ExpandableListView.getPackedPositionGroup(info.packedPosition);
			int childPos = ExpandableListView.getPackedPositionChild(info.packedPosition);
			@SuppressWarnings("unchecked")
			HashMap<String, String> o =  (HashMap<String, String>) mAdapter.getChild(groupPos, childPos);	        		
			SharedPreferences settings = getSharedPreferences("pref_meetmethere", 0);
			ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
			String log = settings.getString("login", "error");
			String pass = settings.getString("password", "error");
			postParameters.add(new BasicNameValuePair("login", log));
			postParameters.add(new BasicNameValuePair("password", pass));
			postParameters.add(new BasicNameValuePair("id", o.get("id")));
			if (groupPos==1 || groupPos==0){
				switch(item.getItemId()){
				case 0:
					postParameters.add(new BasicNameValuePair("confirmed","1"));
					break;
				case 1:
					postParameters.add(new BasicNameValuePair("confirmed","2"));
					break;
				case 2:
					if (groupPos==0){
						Intent intent = new Intent(home.this, event_info.class);
						intent.putExtra("longitude", o.get("longitude"));
						intent.putExtra("latitude", o.get("latitude"));
						intent.putExtra("willAttend", o.get("willAttend"));
						intent.putExtra("address", o.get("address"));
						intent.putExtra("id", o.get("id"));
						intent.putExtra("title", o.get("title"));
						intent.putExtra("startdate",o.get("startdate"));
						intent.putExtra("enddate",o.get("enddate"));
						intent.putExtra("address",o.get("address"));
						intent.putExtra("description", o.get("description"));	
						startActivity(intent);
					}
					else if (groupPos==1){
						String friend= o.get("login");
						Intent intent = new Intent(home.this, profile.class);
						intent.putExtra("friend", friend);
						intent.putExtra("yourrequest", "true");
						intent.putExtra("id", o.get("id"));
						intent.putExtra("confirmed", "0");
						startActivity(intent);
					}
					break;
				}

				String response = null;
				try {
					if (groupPos==1)
						response = CustomHttpClient.executeHttpPost("http://antoines.goldzoneweb.info/accept_contact.php", postParameters);
					else
						response = CustomHttpClient.executeHttpPost("http://antoines.goldzoneweb.info/accept_event.php", postParameters);

					String res=response.toString();
					res= res.replaceAll("\\s+","");
					if(res.equals("OK")){
						Intent myIntent = new Intent(home.this, home.class);
						startActivityForResult(myIntent, 0);
					}

				} catch (Exception e) {System.out.println("Error occured");}
			}
			else if (groupPos==2){
				switch(item.getItemId()){
				case 0:
					Intent intent = new Intent(home.this, event_info.class);
					intent.putExtra("longitude", o.get("longitude"));
					intent.putExtra("latitude", o.get("latitude"));
					intent.putExtra("willAttend", o.get("willAttend"));
					intent.putExtra("address", o.get("address"));
					intent.putExtra("id", o.get("id"));
					intent.putExtra("title", o.get("title"));
					intent.putExtra("startdate",o.get("startdate"));
					intent.putExtra("enddate",o.get("enddate"));
					intent.putExtra("address",o.get("address"));
					intent.putExtra("description", o.get("description"));
					startActivity(intent);	
					break;
				case 1:					
					Intent intent2 = new Intent(home.this, mapevent.class);
					intent2.putExtra("longitude", o.get("longitude"));
					intent2.putExtra("latitude", o.get("latitude"));
					intent2.putExtra("willAttend", o.get("willAttend"));
					intent2.putExtra("address", o.get("address"));
					intent2.putExtra("id", o.get("id"));
					intent2.putExtra("title", o.get("title"));
					intent2.putExtra("startdate",o.get("startdate"));
					intent2.putExtra("enddate",o.get("enddate"));
					intent2.putExtra("address",o.get("address"));
					intent2.putExtra("description", o.get("description"));
					startActivity(intent2);	
					break;
				case 2:
					postParameters.add(new BasicNameValuePair("confirmed","2"));
					String response = null;
					try {
						response = CustomHttpClient.executeHttpPost("http://antoines.goldzoneweb.info/accept_event.php", postParameters);

						String res=response.toString();
						res= res.replaceAll("\\s+","");
						if(res.equals("OK")){
							Intent myIntent = new Intent(home.this, home.class);
							startActivityForResult(myIntent, 0);
						}

					} catch (Exception e) {System.out.println("Error occured");}
					break;
				}
			}
		} 
		else if (type == ExpandableListView.PACKED_POSITION_TYPE_GROUP) {
			return true;
		}
		return false;
	}

	/**
	 * A simple adapter which maintains an ArrayList of photo resource Ids. Each
	 * photo is displayed as an image. This adapter supports clearing the list
	 * of photos and adding a new photo.
	 * 
	 */
	public class MyExpandableListAdapter extends BaseExpandableListAdapter {

		private String[] groups = { "Event Invitations", "Contact Requests","Upcoming Events" };

		private ArrayList<HashMap<String, String>> invitations;
		private ArrayList<HashMap<String, String>> contactrequests;
		private ArrayList<HashMap<String, String>> events;

		public MyExpandableListAdapter(
				ArrayList<HashMap<String, String>> invitations,
				ArrayList<HashMap<String, String>> contactrequests,
				ArrayList<HashMap<String, String>> events) {
			this.invitations = invitations;
			this.contactrequests = contactrequests;
			this.events = events;
		}

		public HashMap<String, String> getChild(int groupPosition, int childPosition) {
			switch (groupPosition) {
			case 0:
				return invitations.get(childPosition);
			case 1:
				return contactrequests.get(childPosition);
			case 2:
				return events.get(childPosition);
			}
			return new HashMap<String, String>();
		}

		public long getChildId(int groupPosition, int childPosition) {
			return childPosition;
		}

		public int getChildrenCount(int groupPosition) {
			switch (groupPosition) {
			case 0:
				return invitations.size();
			case 1:
				return contactrequests.size();
			case 2:
				return events.size();
			}
			return 0;
		}

		public TextView getGenericView() {
			AbsListView.LayoutParams lp = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 64);

			TextView textView = new TextView(home.this);
			textView.setLayoutParams(lp);
			textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
			textView.setPadding(36, 0, 0, 0);
			return textView;
		}

		public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
			TextView textView = getGenericView();
			textView.setText(getChild(groupPosition, childPosition).get("title"));
			return textView;
		}

		public Object getGroup(int groupPosition) {
			return groups[groupPosition];
		}

		public int getGroupCount() {
			return groups.length;
		}

		public long getGroupId(int groupPosition) {
			return groupPosition;
		}

		public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
			TextView textView = getGenericView();
			textView.setText(getGroup(groupPosition).toString());
			textView.setPadding(60, 0, 0, 0);
			return textView;
		}

		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return true;
		}

		public boolean hasStableIds() {
			return true;
		}

	}
}
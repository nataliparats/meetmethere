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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;

public class participants extends MapActivity_with_Menu {
	/**
	 * @see android.app.Activity#onCreate(Bundle)
	 */

	SharedPreferences.Editor editor;
	ListView lv;
	EditText search;
	ArrayList<HashMap<String, String>> mylist;
	ArrayList<HashMap<String, String>> listsearch = new ArrayList<HashMap<String, String>>();
	Intent intent;

	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.event);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title_style);
		
		intent = getIntent();
		
		lv = (ListView) findViewById(R.id.participants_list);
		search = (EditText) findViewById(R.id.participants_search);
		lv.setVisibility(View.VISIBLE);
		search.setVisibility(View.VISIBLE);
		SharedPreferences settings = getSharedPreferences("pref_meetmethere", 0);
		editor = settings.edit();
		mylist = new ArrayList<HashMap<String, String>>();

		ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
		String log = settings.getString("login", "error");
		String pass = settings.getString("password", "error");
		postParameters.add(new BasicNameValuePair("login", log));
		postParameters.add(new BasicNameValuePair("password", pass));
		String xml = XMLfunctions.getXML(
				"http://antoines.goldzoneweb.info/xml_friends.php",
				postParameters);
		Document doc = XMLfunctions.XMLfromString(xml);

		NodeList nodes = doc.getElementsByTagName("friend");
		HashMap<String, String> map = new HashMap<String, String>();
		//mylist.add(map);

		for (int i = 0; i < nodes.getLength(); i++) {
			map = new HashMap<String, String>();

			Element e = (Element) nodes.item(i);
			map.put("fullname", XMLfunctions.getValue(e, "fullname"));
			map.put("login", XMLfunctions.getValue(e, "login"));
			mylist.add(map);
		}

		ListAdapter adapter = new SimpleAdapter(this, mylist,
				R.layout.item_list, new String[] { "fullname" },
				new int[] { R.id.item_title });

		lv.setAdapter(adapter);

		lv.setTextFilterEnabled(true);
		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				@SuppressWarnings("unchecked")
				HashMap<String, String> o = (HashMap<String, String>) lv
				.getItemAtPosition(position);
	
					String participant= o.get("login");
					Intent intent = new Intent(view.getContext(), profile.class);
					intent.putExtra("friend", participant);
					startActivity(intent);
			}
		});


		search.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

				int textlength = search.getText().toString().length();
				listsearch.clear();
				for (int i = 0; i < mylist.size(); i++) {
					if (textlength <= mylist.get(i).get("fullname").length()) {
						if (search
								.getText()
								.toString()
								.equalsIgnoreCase(
										(String) mylist.get(i).get("fullname")
										.subSequence(0, textlength))) {
							listsearch.add(mylist.get(i));
						}
					}
				}
				ListAdapter adapter2 = new SimpleAdapter(participants.this,
						listsearch, R.layout.item_list,
						new String[] { "fullname" },
						new int[] { R.id.item_title });

				lv.setAdapter(adapter2);
			}
		});

	}
	
	public void onClick(View v){
		if(v.getId() == R.id.info){
			lv.setVisibility(View.GONE);
			search.setVisibility(View.GONE);
			Intent myIntent = new Intent(v.getContext(), event_info.class);
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
		else if(v.getId() == R.id.map){
			lv.setVisibility(View.GONE);
			search.setVisibility(View.GONE);
			Intent myIntent = new Intent(v.getContext(), mapevent.class);
			myIntent.putExtra("longitude", intent.getExtras().getString("longitude"));
			myIntent.putExtra("latitude", intent.getExtras().getString("latitude"));
			myIntent.putExtra("title", intent.getExtras().getString("title"));
			myIntent.putExtra("id", intent.getExtras().getString("id"));
			myIntent.putExtra("startdate",intent.getExtras().getString("startdate"));
			myIntent.putExtra("enddate",intent.getExtras().getString("enddate"));
			myIntent.putExtra("address",intent.getExtras().getString("address"));
			myIntent.putExtra("description", intent.getExtras().getString("description"));
			startActivityForResult(myIntent, 0);
		}
	}
}


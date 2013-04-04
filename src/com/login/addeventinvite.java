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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class addeventinvite extends Activity_with_Menu {
	/**
	 * @see android.app.Activity#onCreate(Bundle)
	 */

	SharedPreferences.Editor editor;
	ListView lv;
	EditText search;
	ArrayList<HashMap<String, String>> mylist;
	ArrayList<HashMap<String, String>> listsearch = new ArrayList<HashMap<String, String>>();
	String eventid,log,pass;
	Button finish;
	ListAdapter adapter;


	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.addeventinvite);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title_style);
		
		lv = (ListView) findViewById(R.id.contacts_list_invite);
		search = (EditText) findViewById(R.id.invite_search);
		finish = (Button) findViewById(R.id.finishinvite);
		SharedPreferences settings = getSharedPreferences("pref_meetmethere", 0);
		editor = settings.edit();
		mylist = new ArrayList<HashMap<String, String>>();

		Intent intent = getIntent();
		eventid=intent.getExtras().getString("eventid");
		log=settings.getString("login", "error");
		pass=settings.getString("password", "error");		


		ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
		postParameters.add(new BasicNameValuePair("login", log));
		postParameters.add(new BasicNameValuePair("password", pass));
		String xml = XMLfunctions.getXML(
				"http://antoines.goldzoneweb.info/xml_friends.php",
				postParameters);
		Document doc = XMLfunctions.XMLfromString(xml);

		NodeList nodes = doc.getElementsByTagName("friend");

		for (int i = 0; i < nodes.getLength(); i++) {
			HashMap<String, String> map = new HashMap<String, String>();

			Element e = (Element) nodes.item(i);
			map.put("id", XMLfunctions.getValue(e, "id"));
			map.put("fullname", XMLfunctions.getValue(e, "fullname"));
			mylist.add(map);
		}

		adapter = new SimpleAdapter(this, mylist,
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
				String friendid=o.get("id");
				ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
				postParameters.add(new BasicNameValuePair("eventid", eventid));
				postParameters.add(new BasicNameValuePair("friendid", friendid));
				String response = null;
				try {
					response = CustomHttpClient.executeHttpPost("http://antoines.goldzoneweb.info/invite.php", postParameters);
					String res=response.toString();
					mylist.remove((int)id);
					adapter = new SimpleAdapter(addeventinvite.this, mylist , R.layout.item_list, 
							new String[] { "fullname", "email" }, 
							new int[] { R.id.item_title, R.id.item_subtitle });
					lv.setAdapter(adapter);
				} catch (Exception e) {
				}	


			}
		});


		finish.setOnClickListener(new View.OnClickListener() {

			@Override public void onClick(View v) {
				Intent myIntent = new Intent(v.getContext(), events.class);
				startActivityForResult(myIntent, 0);	            	
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
				ListAdapter adapter2 = new SimpleAdapter(addeventinvite.this,
						listsearch, R.layout.item_list,
						new String[] { "fullname" },
						new int[] { R.id.item_title });

				lv.setAdapter(adapter2);

			}
		});

	}
}

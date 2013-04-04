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
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;

public class contacts extends Activity_with_Menu {
	/**
	 * @see android.app.Activity#onCreate(Bundle)
	 */

	SharedPreferences.Editor editor;
	Button newcontact;
	ListView lv;
	EditText search;
	RadioGroup radioGroup;
	ArrayList<HashMap<String, String>> mylist,mylist1,mylist0;
	ArrayList<HashMap<String, String>> listsearch = new ArrayList<HashMap<String, String>>();
	ListAdapter adapter;

	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.contacts);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title_style);
		
		radioGroup = (RadioGroup) findViewById(R.id.radioc);
		radioGroup.check(R.id.yes);	
		newcontact=(Button)findViewById(R.id.newcontact);

		lv = (ListView) findViewById(R.id.contacts_list);
		search = (EditText) findViewById(R.id.contacts_search);
		SharedPreferences settings = getSharedPreferences("pref_meetmethere", 0);
		editor = settings.edit();
		mylist = new ArrayList<HashMap<String, String>>();
		mylist0 = new ArrayList<HashMap<String, String>>();
		mylist1 = new ArrayList<HashMap<String, String>>();


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

		for (int i = 0; i < nodes.getLength(); i++) {
			map = new HashMap<String, String>();

			Element e = (Element) nodes.item(i);
			map.put("id", XMLfunctions.getValue(e, "id"));
			map.put("confirmed", XMLfunctions.getValue(e, "confirmed"));
			map.put("fullname", XMLfunctions.getValue(e, "fullname"));
			map.put("login", XMLfunctions.getValue(e, "login"));
			map.put("yourrequest", XMLfunctions.getValue(e, "yourrequest"));
			int c=Integer.parseInt(XMLfunctions.getValue(e, "confirmed"));
			switch(c){
			case 0: mylist0.add(map);	break;
			case 1:mylist1.add(map);	break;
			}
			mylist.add(map);
		}

		adapter = new SimpleAdapter(this, mylist1,
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

				String friend= o.get("login");
				Intent intent = new Intent(view.getContext(), profile.class);
				intent.putExtra("friend", friend);
				intent.putExtra("yourrequest", o.get("yourrequest"));
				intent.putExtra("confirmed", o.get("confirmed"));
				intent.putExtra("id", o.get("id"));
				startActivity(intent);

			}
		});

		radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			public void onCheckedChanged(RadioGroup arg0, int idradio) {
				switch (idradio) {
				case R.id.yes:		
					adapter = new SimpleAdapter(contacts.this, mylist1 , R.layout.item_list, 
							new String[] { "fullname"}, 
							new int[] { R.id.item_title});
					search.setText("");
					lv.setAdapter(adapter);					
					break;
				case R.id.invite:
					adapter = new SimpleAdapter(contacts.this, mylist0 , R.layout.item_list, 
							new String[] { "fullname"}, 
							new int[] { R.id.item_title});
					search.setText("");
					lv.setAdapter(adapter);
					break;
				case R.id.all:		
					adapter = new SimpleAdapter(contacts.this, mylist , R.layout.item_list, 
							new String[] { "fullname"}, 
							new int[] { R.id.item_title});
					search.setText("");
					lv.setAdapter(adapter);

				}

			}
		});

		newcontact.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent myIntent = new Intent(v.getContext(), addnewcontact.class);
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
				ArrayList<HashMap<String, String>> currentlist=mylist;
				int idradio =radioGroup.getCheckedRadioButtonId();
				switch(idradio){
				case R.id.yes:
					currentlist=mylist1;
					break;
				case R.id.invite:
					currentlist=mylist0;
					break;
				case R.id.all:
					currentlist=mylist;
					break;
				}


				int textlength = search.getText().toString().length();
				listsearch.clear();
				for (int i = 0; i < currentlist.size(); i++) {
					if (textlength <= currentlist.get(i).get("fullname").length()) {
						if (search
								.getText()
								.toString()
								.equalsIgnoreCase(
										(String) currentlist.get(i).get("fullname")
										.subSequence(0, textlength))) {
							listsearch.add(currentlist.get(i));
						}
					}
				}
				ListAdapter adapter2 = new SimpleAdapter(contacts.this,
						listsearch, R.layout.item_list,
						new String[] { "fullname" },
						new int[] { R.id.item_title });

				lv.setAdapter(adapter2);

			}
		});

	}
}

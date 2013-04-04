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
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class group extends Activity_with_Menu {
	/**
	 * @see android.app.Activity#onCreate(Bundle)
	 */

	SharedPreferences.Editor editor;
	ListView lv;
	EditText search;
	ArrayList<HashMap<String, String>> mylist;
	ArrayList<HashMap<String, String>> listsearch = new ArrayList<HashMap<String, String>>();

	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.group);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title_style);
		
		lv = (ListView) findViewById(R.id.group_list);
		search = (EditText) findViewById(R.id.group_search);
		SharedPreferences settings = getSharedPreferences("pref_meetmethere", 0);
		editor = settings.edit();
		mylist = new ArrayList<HashMap<String, String>>();

		ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
		String log = settings.getString("login", "error");
		String pass = settings.getString("password", "error");
		String xml = XMLfunctions.getXML(
				"http://antoines.goldzoneweb.info/xml_group.php",
				postParameters);
		Document doc = XMLfunctions.XMLfromString(xml);

		NodeList nodes = doc.getElementsByTagName("group");
		HashMap<String, String> map = new HashMap<String, String>();	
		map.put("name", "Create new group");
		mylist.add(map);

		for (int i = 0; i < nodes.getLength(); i++) {
			map = new HashMap<String, String>();

			Element e = (Element) nodes.item(i);
			map.put("name", XMLfunctions.getValue(e, "name"));
			map.put("description", XMLfunctions.getValue(e, "description"));
			map.put("id", XMLfunctions.getValue(e, "id"));
			mylist.add(map);
		}

		ListAdapter adapter = new SimpleAdapter(this, mylist,
				R.layout.item_list, new String[] { "name" , "description" },
				new int[] { R.id.item_title , R.id.item_subtitle });

		lv.setAdapter(adapter);

		lv.setTextFilterEnabled(true);
		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				@SuppressWarnings("unchecked")
				HashMap<String, String> o = (HashMap<String, String>) lv
				.getItemAtPosition(position);


				if (o.get("name").equals("Create new group")){
					Intent myIntent = new Intent(view.getContext(), createnewgroup.class);
					startActivityForResult(myIntent, 0);
				}	
				else {	
					Intent intent = new Intent(view.getContext(), groupmember.class);
					intent.putExtra("id", o.get("id"));
					intent.putExtra("name", o.get("name"));
					intent.putExtra("description", o.get("description"));
					startActivity(intent);
				}
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
					if (textlength <= mylist.get(i).get("name").length()) {
						if (search
								.getText()
								.toString()
								.equalsIgnoreCase(
										(String) mylist.get(i).get("name")
										.subSequence(0, textlength))) {
							listsearch.add(mylist.get(i));
						}
					}
				}
				ListAdapter adapter2 = new SimpleAdapter(group.this,
						listsearch, R.layout.item_list,
						new String[] { "name" ,"email" },
						new int[] { R.id.item_title,R.id.item_subtitle });

				lv.setAdapter(adapter2);


			}
		});

	}
}

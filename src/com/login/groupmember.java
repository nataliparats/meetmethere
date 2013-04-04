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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;


public class groupmember extends Activity_with_Menu {
	/**
	 * @see android.app.Activity#onCreate(Bundle)
	 */
	SharedPreferences.Editor editor;
	ListView lv;
	String groupid,log,pass;
	EditText search;
	ArrayList<HashMap<String, String>> mylist;
	ArrayList<HashMap<String, String>> listsearch = new ArrayList<HashMap<String, String>>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.groupmember);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title_style);
		
		lv = (ListView) findViewById(R.id.group_list);
		search = (EditText) findViewById(R.id.group_search);
		final Intent intent = getIntent();
		SharedPreferences settings = getSharedPreferences("pref_meetmethere", 0);
		editor = settings.edit();
		ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
		log=settings.getString("login", "error");
		pass=settings.getString("password", "error");	
		groupid=intent.getExtras().getString("id");
		postParameters.add(new BasicNameValuePair("login", log));
		postParameters.add(new BasicNameValuePair("groupid", groupid));



		String xml = XMLfunctions.getXML("http://antoines.goldzoneweb.info/groupmember.php", postParameters);
		Document doc = XMLfunctions.XMLfromString(xml);		
		NodeList you = doc.getElementsByTagName("you");
		mylist = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> map = new HashMap<String, String>();	
		if (you.getLength()==0){
			map.put("fullname", "Join : "+ intent.getExtras().getString("name"));
			mylist.add(map);
		}
		else{
			map.put("fullname", "You are a member of this group");
			mylist.add(map);
		}
		NodeList nodes = doc.getElementsByTagName("member");

		for (int i = 0; i < nodes.getLength(); i++) {
			map = new HashMap<String, String>();

			Element e = (Element) nodes.item(i);
			map.put("fullname", XMLfunctions.getValue(e, "fullname"));
			map.put("id", XMLfunctions.getValue(e, "id"));
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

				if (o.get("fullname").equals("You are a member of this group")){}
				else if (o.get("fullname").equals("Join : "+ intent.getExtras().getString("name"))){
							ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
							postParameters.add(new BasicNameValuePair("groupid", groupid));
							postParameters.add(new BasicNameValuePair("login", log));
							postParameters.add(new BasicNameValuePair("password", pass));
							String response = null;
							try {
								response = CustomHttpClient.executeHttpPost("http://antoines.goldzoneweb.info/joingroup.php", postParameters);
								String res=response.toString();
								Intent intent = new Intent(view.getContext(), group.class);
								startActivity(intent);
							} catch (Exception e) {
							}	

				}	
				else {	
					String friend= o.get("login");
					Intent intent = new Intent(view.getContext(), profile.class);
					intent.putExtra("friend", friend);
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
				ListAdapter adapter2 = new SimpleAdapter(groupmember.this,
						listsearch, R.layout.item_list,
						new String[] { "fullname" },
						new int[] { R.id.item_title });

				lv.setAdapter(adapter2);
			}
		});

	}





}

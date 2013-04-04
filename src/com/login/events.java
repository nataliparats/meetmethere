package com.login;

import java.util.ArrayList;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import java.util.ArrayList;
import java.util.HashMap;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.Toast;
public class events extends Activity_with_Menu{
	/**
	 * @see android.app.Activity#onCreate(Bundle)
	 */


	SharedPreferences.Editor editor;
	ListView lv;
	Button mapbutton,newevent;
	EditText search;
	ArrayList<HashMap<String, String>> mylist,mylist0,mylist1,mylist2;
	ArrayList<HashMap<String, String>> listsearch=new ArrayList<HashMap<String, String>> ();
	Element e;
	RadioGroup radioGroup;
	ListAdapter adapter;


	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.events);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title_style);
		radioGroup = (RadioGroup) findViewById(R.id.radio);
		radioGroup.check(R.id.yes);
		lv=(ListView)findViewById(R.id.list);
		mapbutton=(Button)findViewById(R.id.map);
		newevent=(Button)findViewById(R.id.newevent);

		search=(EditText)findViewById(R.id.search);
		SharedPreferences settings = getSharedPreferences("pref_meetmethere", 0);
		editor = settings.edit();
		mylist = new ArrayList<HashMap<String, String>>();
		mylist1 = new ArrayList<HashMap<String, String>>();
		mylist0 = new ArrayList<HashMap<String, String>>();
		mylist2 = new ArrayList<HashMap<String, String>>();

		ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
		String log=settings.getString("login", "error");
		String pass=settings.getString("password", "error");
		postParameters.add(new BasicNameValuePair("login", log));
		postParameters.add(new BasicNameValuePair("password", pass));
		String xml = XMLfunctions.getXML("http://antoines.goldzoneweb.info/xml_event.php", postParameters);
		Document doc = XMLfunctions.XMLfromString(xml);

		NodeList nodes = doc.getElementsByTagName("event");
		HashMap<String, String> map = new HashMap<String, String>();	

		for (int i = 0; i < nodes.getLength(); i++) {							
			map = new HashMap<String, String>();	

			e = (Element)nodes.item(i);
			map.put("willAttend", XMLfunctions.getValue(e, "willAttend"));
			map.put("latitude", XMLfunctions.getValue(e, "latitude"));
			map.put("address", XMLfunctions.getValue(e, "address"));
			map.put("longitude", XMLfunctions.getValue(e, "longitude"));
			map.put("title", XMLfunctions.getValue(e, "title"));
			map.put("id", XMLfunctions.getValue(e, "id"));
			map.put("description", XMLfunctions.getValue(e, "description"));
			map.put("date", XMLfunctions.getValue(e, "startdate")+" - "+XMLfunctions.getValue(e, "enddate"));
			int wa=Integer.parseInt(XMLfunctions.getValue(e, "willAttend"));
			switch(wa){
			case 0: mylist0.add(map);	break;
			case 1:mylist1.add(map);	break;
			case 2:mylist2.add(map);	break;
			}
			mylist.add(map);

		}		

		adapter = new SimpleAdapter(this, mylist1 , R.layout.item_list, 
				new String[] { "title", "date" }, 
				new int[] { R.id.item_title, R.id.item_subtitle });

		lv.setAdapter(adapter);



		lv.setTextFilterEnabled(true);	
		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {        		
				@SuppressWarnings("unchecked")
				HashMap<String, String> o = (HashMap<String, String>) lv.getItemAtPosition(position);	        				
				Intent intent = new Intent(view.getContext(), event_info.class);
				intent.putExtra("longitude", o.get("longitude"));
				intent.putExtra("latitude", o.get("latitude"));
				intent.putExtra("willAttend", o.get("willAttend"));
				intent.putExtra("id", o.get("id"));
				intent.putExtra("title", o.get("title"));
				intent.putExtra("address", o.get("address"));
				intent.putExtra("startdate",XMLfunctions.getValue(e, "startdate"));
				intent.putExtra("enddate",XMLfunctions.getValue(e, "enddate"));
				intent.putExtra("address",o.get("address"));
				intent.putExtra("description", o.get("description"));
				startActivity(intent);	


			}
		});


		radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			public void onCheckedChanged(RadioGroup arg0, int idradio) {
				switch (idradio) {
				case R.id.yes:		
					adapter = new SimpleAdapter(events.this, mylist1 , R.layout.item_list, 
							new String[] { "title", "date" }, 
							new int[] { R.id.item_title, R.id.item_subtitle });
					search.setText("");
					lv.setAdapter(adapter);					
					break;
				case R.id.no:
					adapter = new SimpleAdapter(events.this, mylist2 , R.layout.item_list, 
							new String[] { "title", "date" }, 
							new int[] { R.id.item_title, R.id.item_subtitle });
					search.setText("");
					lv.setAdapter(adapter);
					break;
				case R.id.invite:
					adapter = new SimpleAdapter(events.this, mylist0 , R.layout.item_list, 
							new String[] { "title", "date" }, 
							new int[] { R.id.item_title, R.id.item_subtitle });
					search.setText("");
					lv.setAdapter(adapter);
					break;
				case R.id.all:		
					adapter = new SimpleAdapter(events.this, mylist , R.layout.item_list, 
							new String[] { "title", "date" }, 
							new int[] { R.id.item_title, R.id.item_subtitle });
					search.setText("");
					lv.setAdapter(adapter);

				}

			}
		});

		newevent.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent myIntent = new Intent(v.getContext(), addevent.class);
				startActivityForResult(myIntent, 0);	            	     	
			}
		});



		mapbutton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent myIntent = new Intent(v.getContext(), global_map.class);
				startActivityForResult(myIntent, 0);	            	     	
			}
		});



		search.addTextChangedListener(new TextWatcher(){

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
				case R.id.no:
					currentlist=mylist2;
					break;
				case R.id.invite:
					currentlist=mylist0;
					break;
				case R.id.all:
					currentlist=mylist;
					break;
				}


				int textlength=search.getText().toString().length();
				listsearch.clear();
				for(int i=0;i<currentlist.size();i++ ){
					if (textlength<=currentlist.get(i).get("title").length()){
						if(search.getText().toString().equalsIgnoreCase((String) currentlist.get(i).get("title").subSequence(0, textlength))){
							listsearch.add(currentlist.get(i));
						}


					}

				}
				ListAdapter adapter2 = new SimpleAdapter(events.this, listsearch , R.layout.item_list, 
						new String[] { "title", "date" }, 
						new int[] { R.id.item_title, R.id.item_subtitle });

				lv.setAdapter(adapter2);

			}});

	}
}

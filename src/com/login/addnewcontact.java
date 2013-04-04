package com.login;

import java.util.ArrayList;import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import android.widget.SimpleAdapter;
import android.widget.Toast;
public class addnewcontact extends Activity_with_Menu{
	/**
	 * @see android.app.Activity#onCreate(Bundle)
	 */


	SharedPreferences.Editor editor;
	ListView lv;
	EditText search;
	ListAdapter adapter;
	String log,pass;
	ArrayList<HashMap<String, String>> mylist;
	ArrayList<HashMap<String, String>> listsearch=new ArrayList<HashMap<String, String>> ();

	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.addnewcontact);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title_style);
		
		lv=(ListView)findViewById(R.id.list_friend);
		search=(EditText)findViewById(R.id.search_friend);
		SharedPreferences settings = getSharedPreferences("pref_meetmethere", 0);
		editor = settings.edit();
		mylist = new ArrayList<HashMap<String, String>>();

		ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
		log=settings.getString("login", "error");
		pass=settings.getString("password", "error");
		postParameters.add(new BasicNameValuePair("login", log));
		postParameters.add(new BasicNameValuePair("password", pass));
		String xml = XMLfunctions.getXML("http://antoines.goldzoneweb.info/not_yet_friend.php", postParameters);
		Document doc = XMLfunctions.XMLfromString(xml);

		NodeList nodes = doc.getElementsByTagName("user");
		HashMap<String, String> map = new HashMap<String, String>();	

		for (int i = 0; i < nodes.getLength(); i++) {							
			map = new HashMap<String, String>();	

			Element e = (Element)nodes.item(i);
			map.put("fullname", XMLfunctions.getValue(e, "fullname"));
			map.put("email", XMLfunctions.getValue(e, "email"));
			map.put("id", XMLfunctions.getValue(e, "id"));
			mylist.add(map);	



		}		

		adapter = new SimpleAdapter(this, mylist , R.layout.item_list, 
				new String[] { "fullname", "email" }, 
				new int[] { R.id.item_title, R.id.item_subtitle });

		lv.setAdapter(adapter);

		Button finish = (Button) findViewById(R.id.finishfriend);
		finish.setOnClickListener(new View.OnClickListener() {

			@Override public void onClick(View v) {
				Intent myIntent = new Intent(v.getContext(), contacts.class);
				startActivityForResult(myIntent, 0);	            	
			}
		});
		
		lv.setTextFilterEnabled(true);	
		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {        		
				@SuppressWarnings("unchecked")
				HashMap<String, String> o = (HashMap<String, String>) lv.getItemAtPosition(position);	        		
				String friendid=o.get("id");
				ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
				postParameters.add(new BasicNameValuePair("login", log));
				postParameters.add(new BasicNameValuePair("password", pass));
				postParameters.add(new BasicNameValuePair("friendid", friendid));
				String response = null;
				try {
					response = CustomHttpClient.executeHttpPost("http://antoines.goldzoneweb.info/newfriend.php", postParameters);
					String res=response.toString();
					mylist.remove((int)id);
					adapter = new SimpleAdapter(addnewcontact.this, mylist , R.layout.item_list, 
							new String[] { "fullname", "email" }, 
							new int[] { R.id.item_title, R.id.item_subtitle });
					lv.setAdapter(adapter);
					
				} catch (Exception e) {
				}	
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

				int textlength=search.getText().toString().length();
				listsearch.clear();
				for(int i=0;i<mylist.size();i++ ){
					if (textlength<=mylist.get(i).get("fullname").length()){
						if(search.getText().toString().equalsIgnoreCase((String) mylist.get(i).get("fullname").subSequence(0, textlength))){
							listsearch.add(mylist.get(i));
						}


					}

				}
				ListAdapter adapter2 = new SimpleAdapter(addnewcontact.this, listsearch , R.layout.item_list, 
						new String[] { "fullname", "email" }, 
						new int[] { R.id.item_title, R.id.item_subtitle });

				lv.setAdapter(adapter2);

			}});

	}
}

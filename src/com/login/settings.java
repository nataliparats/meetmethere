package com.login;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;

public class settings extends Activity_with_Menu {
	RadioGroup radioGroup;
	SharedPreferences.Editor editor;
	int checkedRadioButton;
	Button viewprofile,editprofile,quit;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.settings);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title_style);
		radioGroup = (RadioGroup) findViewById(R.id.rd);
		SharedPreferences settings = getSharedPreferences("pref_meetmethere", 0);
		editor = settings.edit();
		String mode = settings.getString("mode", "default");
		if(mode.equals("default"))
			radioGroup.check(R.id.yes);
		else if (mode.equals("alone"))
			radioGroup.check(R.id.no);

		viewprofile=(Button)findViewById(R.id.view_profile);
		quit=(Button)findViewById(R.id.quit);
		viewprofile.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(settings.this, profile.class);
				startActivityForResult(intent, 0);					
			}
		});
		quit.setOnClickListener(new View.OnClickListener() {		
			@Override
			public void onClick(View v) {
			}
		});

		radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			public void onCheckedChanged(RadioGroup arg0, int idradio) {
				switch (idradio) {
				case R.id.yes:		
					editor.putString("mode", "default");
					break;
				case R.id.no:
					editor.putString("mode", "alone");
					break;
				}
				editor.commit();
				Intent myIntent = new Intent(settings.this, settings.class);
				startActivityForResult(myIntent, 0);	


			}
		});	


	}
}

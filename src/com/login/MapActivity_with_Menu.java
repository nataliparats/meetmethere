package com.login;

import com.google.android.maps.MapActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

public abstract class MapActivity_with_Menu extends MapActivity {
    /** Called when the activity is first created. */

    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
		SharedPreferences settings = getSharedPreferences("pref_meetmethere", 0);
		String mode = settings.getString("mode", "default");
		if(mode.equals("default"))
			inflater.inflate(R.menu.menu, menu);
		else if (mode.equals("alone"))
			inflater.inflate(R.menu.alone_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	Intent homeIntent = new Intent(this, home.class);
    	Intent settingsIntent = new Intent(this, settings.class);
    	Intent contactsintent = new Intent(this, contacts.class);
    	Intent eventsintent = new Intent(this, events.class);
    	Intent groupintent = new Intent(this, group.class);

    	 switch (item.getItemId()) {
         case R.id.home:     	startActivityForResult(homeIntent, 0);
                             break;
         case R.id.contacts: 	startActivityForResult(contactsintent, 0);
                             break;
         case R.id.events:		startActivityForResult(eventsintent, 0);
                             break;
         case R.id.groups:		startActivityForResult(groupintent, 0);
         					break;
         case R.id.settings:		startActivityForResult(settingsIntent, 0);
         					break;
         case R.id.logout:
        	 				SharedPreferences.Editor editor;
			        		SharedPreferences settings = getSharedPreferences("pref_meetmethere", 0);
			        		editor = settings.edit();
			        		editor.putBoolean("autologin", false); 
							Intent myIntent = new Intent(this, login.class);
							startActivityForResult(myIntent, 0);	
        	 				break;
     }
        return true;
    }

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
}
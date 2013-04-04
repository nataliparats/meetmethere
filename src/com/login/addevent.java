package com.login;

import java.util.Calendar;
import java.util.HashMap;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import android.widget.TimePicker;

public class addevent extends Activity_with_Menu {
    /**
     * @see android.app.Activity#onCreate(Bundle)
     */
    Button next;
    EditText description,title;
    TextView datestart,timestart,dateend,timeend;
    SharedPreferences.Editor editor;

    private TextView mStartDateDisplay, mEndDateDisplay;
    private Button mPickStartDate, mPickEndDate;
    private int mYear;
    private int mMonth;
    private int mDay;

    private TextView mStartTimeDisplay,mEndTimeDisplay;
    private Button mPickStartTime,mPickEndTime;

    private int mHour;
    private int mMinute;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.addevent);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title_style);

        next=(Button)findViewById(R.id.next);
        description=(EditText)findViewById(R.id.description);
        title=(EditText)findViewById(R.id.title);
        dateend=(TextView)findViewById(R.id.endate);
        datestart=(TextView)findViewById(R.id.stdate);
        timeend=(TextView)findViewById(R.id.entimeDisplay);
        timestart=(TextView)findViewById(R.id.sttimeDisplay);

		SharedPreferences settings = getSharedPreferences("pref_meetmethere", 0);
		editor = settings.edit();

		// get the current date
		final Calendar c = Calendar.getInstance();
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mDay = c.get(Calendar.DAY_OF_MONTH);
		// get the current time
		mHour = c.get(Calendar.HOUR_OF_DAY);
		mMinute = c.get(Calendar.MINUTE);


		// capture our View elements
		mStartDateDisplay = (TextView) findViewById(R.id.stdate);
		mPickStartDate = (Button) findViewById(R.id.startdate);
		mEndDateDisplay = (TextView) findViewById(R.id.endate);
		mPickEndDate = (Button) findViewById(R.id.dateend);

		// add a click listener to the start date button
		mPickStartDate.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				showDialog(1);
			}
		});

		// add a click listener to the end date button
		mPickEndDate.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				showDialog(2);
			}
		});

		// capture our View elements
		mStartTimeDisplay = (TextView) findViewById(R.id.sttimeDisplay);
		mPickStartTime = (Button) findViewById(R.id.timestart);
		mEndTimeDisplay = (TextView) findViewById(R.id.entimeDisplay);
		mPickEndTime = (Button) findViewById(R.id.timeend);

		// add a click listener to the button
		mPickStartTime.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				showDialog(3);
			}
		});
		mPickEndTime.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				showDialog(4);
			}
		});


		// display the current date for start and end date (method are below)
		updateDisplayStartDate();
		updateDisplayEndDate();
		// display the current time for start and end time (methods are below)
		updateDisplayStartTime();
		updateDisplayEndTime();


		next.setOnClickListener(new View.OnClickListener() {	
			@Override
			public void onClick(View v) {
				if (title.getText().length()<4){
					title.setHint("Title too short");
					title.setHintTextColor(Color.RED);	
					title.setText("");
				}
				else if (description.getText().length()<4){
					description.setHint("Description too short");
					description.setHintTextColor(Color.RED);	
					description.setText("");
				}
				else{
					Intent intent = new Intent(v.getContext(), addeventaddress.class);
					intent.putExtra("title", title.getText().toString());
					intent.putExtra("description", description.getText().toString());			
					intent.putExtra("datestart", datestart.getText().toString());
					intent.putExtra("timestart", timestart.getText().toString());
					intent.putExtra("dateend", dateend.getText().toString());
					intent.putExtra("timeend", timeend.getText().toString());
					startActivity(intent);
				}
			}
		});	
	}

	// updates the start date in the TextView
	private void updateDisplayStartDate() {
		mStartDateDisplay.setText(
				new StringBuilder()
				// Month is 0 based so add 1
				.append(mMonth + 1).append("-")
				.append(mDay).append("-")
				.append(mYear).append(" "));
	}

	// updates the end date in the TextView
	private void updateDisplayEndDate() {
		mEndDateDisplay.setText(
				new StringBuilder()
				// Month is 0 based so add 1
				.append(mMonth + 1).append("-")
				.append(mDay).append("-")
				.append(mYear).append(" "));
	}

	private DatePickerDialog.OnDateSetListener mStartDateSetListener =
			new DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view, int year, 
				int monthOfYear, int dayOfMonth) {
			if(year<mYear || (year==mYear && monthOfYear<mMonth) || (year==mYear && monthOfYear==mMonth && dayOfMonth<mDay)){
				return;
			}else{
				mYear = year;
				mMonth = monthOfYear;
				mDay = dayOfMonth;
				updateDisplayStartDate();
			}
		}
	};

	private DatePickerDialog.OnDateSetListener mEndDateSetListener =
			new DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view, int year, 
				int monthOfYear, int dayOfMonth) {
			if(year<mYear || (year==mYear && monthOfYear<mMonth) || (year==mYear && monthOfYear==mMonth && dayOfMonth<mDay)){
				return;
			}else{
				mYear = year;
				mMonth = monthOfYear;
				mDay = dayOfMonth;
				updateDisplayEndDate();
			}
		}
	};


	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case 1:
			return new DatePickerDialog(this,
					mStartDateSetListener,
					mYear, mMonth, mDay);
		case 2:
			return new DatePickerDialog(this,
					mEndDateSetListener,
					mYear, mMonth, mDay);
		case 3:
			return new TimePickerDialog(this,
					mStartTimeSetListener, mHour, mMinute, false);
		case 4:
			return new TimePickerDialog(this,
					mEndTimeSetListener, mHour, mMinute, false);

		}
		return null;
	}

	// updates the time we display in the TextView
	private void updateDisplayStartTime() {
		mStartTimeDisplay.setText(
				new StringBuilder()
				.append(pad(mHour)).append(":")
				.append(pad(mMinute)));
	}
	private void updateDisplayEndTime() {
		mEndTimeDisplay.setText(
				new StringBuilder()
				.append(pad(mHour)).append(":")
				.append(pad(mMinute)));
	}

	private static String pad(int c) {
		if (c >= 10)
			return String.valueOf(c);
		else
			return "0" + String.valueOf(c);
	}

	// the callback received when the user "sets" the time in the dialog
	private TimePickerDialog.OnTimeSetListener mStartTimeSetListener =
			new TimePickerDialog.OnTimeSetListener() {
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			mHour = hourOfDay;
			mMinute = minute;
			updateDisplayStartTime();
		}
	};
	private TimePickerDialog.OnTimeSetListener mEndTimeSetListener =
			new TimePickerDialog.OnTimeSetListener() {
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			mHour = hourOfDay;
			mMinute = minute;
			updateDisplayEndTime();
		}
	};

}

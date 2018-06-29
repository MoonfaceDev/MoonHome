package com.moonface.home;

import android.annotation.SuppressLint;
import android.app.*;
import android.content.Intent;
import android.os.*;
import android.support.design.widget.CollapsingToolbarLayout;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import android.graphics.*;
import android.util.*;

import java.text.SimpleDateFormat;
import java.util.*;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import java.util.HashMap;
import android.widget.LinearLayout;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;
import android.app.Activity;
import android.content.SharedPreferences;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ChildEventListener;
import com.moonface.Util.InputUtil;
import com.moonface.Util.ParametersUtil;

import android.view.View;
import java.util.Calendar;
import java.text.SimpleDateFormat;

public class AddEventActivity extends AppCompatActivity {
	
	private FirebaseDatabase _firebase = FirebaseDatabase.getInstance();
	private Toolbar _toolbar;
	private FloatingActionButton _fab;
	private FloatingActionButton delete_button;
	private CollapsingToolbarLayout collapsingToolbarLayout;
	private static double type = 0;
	private static double date1 = 0;
	private static double date2 = 0;
	private static double date3 = 0;
	private static String date = "";
	private int PLACE_PICKER_REQUEST = 1;
	private HashMap<String, Object> my_calendar = new HashMap<>();

	private EditText title;
	private ImageButton date_button;
	private static TextView event_date;
	private static Button starting_time;
	private static Button ending_time;
	private EditText location;
	private EditText address;
	private EditText description;
	private Switch allday_switch;
	private ImageButton location_button;

	private SharedPreferences data;
	private DatabaseReference calendar_data;
	private Calendar calendar =Calendar.getInstance();
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.add_event);
		initialize();
		initializeLogic();
	}
	
    private void initialize() {
		
		Toolbar _toolbar = findViewById(R.id._toolbar);
		setSupportActionBar(_toolbar);
        collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle(getString(R.string.add_event_label));
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));
        _fab = findViewById(R.id._fab);
        delete_button = findViewById(R.id.delete_button);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            _toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View _v) {
                    onBackPressed();
                }
            });
        }

		title = findViewById(R.id.title);
		date_button = findViewById(R.id.date_button);
		event_date = findViewById(R.id.event_date);
		starting_time = findViewById(R.id.starting_time);
		ending_time = findViewById(R.id.ending_time);
		location = findViewById(R.id.location);
		address = findViewById(R.id.address);
        description = findViewById(R.id.description);
        allday_switch = findViewById(R.id.allday_switch);
        location_button = findViewById(R.id.location_button);

		data = getSharedPreferences("data", Activity.MODE_PRIVATE);
		calendar_data = _firebase.getReference("users").child(data.getString("id","")).child("calendar");
		date_button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				showDatePickerDialog(date_button);
			}
		});
		event_date.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				showDatePickerDialog(event_date);
			}
		});
		
		starting_time.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				showTimePickerDialog(starting_time);
				type = 1;
			}
		});
		
		ending_time.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				showTimePickerDialog(ending_time);
				type = 2;
			}
		});

		location_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                try {
                    PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                    startActivityForResult(builder.build(AddEventActivity.this), PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });
		allday_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked == true){
                    starting_time.setText(" 00 : 00 ");
                    ending_time.setText(" 23 : 59 ");
                    starting_time.setEnabled(false);
                    ending_time.setEnabled(false);
                }
                else{
                    starting_time.setEnabled(true);
                    ending_time.setEnabled(true);
                }
            }
        });

		_fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				InputUtil.hideKeyboard(getApplicationContext(), AddEventActivity.this);
				if (title.getText().toString().length() < 4) {
					SketchwareUtil.showMessage(getApplicationContext(), getString(R.string.cannot_save_event_message));
					title.setError(getString(R.string.title_too_short_message));
				}
				else {
					if (49 < title.getText().toString().length()) {
						SketchwareUtil.showMessage(getApplicationContext(), getString(R.string.cannot_save_event_message));
						title.setError(getString(R.string.title_too_long_message));
					}
					else {
						if (49 < location.getText().toString().length()) {
							SketchwareUtil.showMessage(getApplicationContext(), getString(R.string.cannot_save_event_message));
							location.setError(getString(R.string.location_too_long_message));
						}
						else {
							my_calendar = new HashMap<>();
							my_calendar.put("".concat("title"), title.getText().toString());
							my_calendar.put("".concat("time_start"), starting_time.getText().toString());
							my_calendar.put("".concat("time_end"), ending_time.getText().toString());
							my_calendar.put("".concat("location"), location.getText().toString());
							my_calendar.put("".concat("address"), address.getText().toString());
							my_calendar.put("".concat("description"), description.getText().toString());
							my_calendar.put("date", event_date.getText().toString());
							my_calendar.put("uid", data.getString("id", ""));
							calendar_data.child(ParametersUtil.removeChars(title.getText().toString().concat(event_date.getText().toString().replace("/", "").concat(starting_time.getText().toString().concat(ending_time.getText().toString()))), new char[]{'/','.','#','$','[',']'})).updateChildren(my_calendar);
							SketchwareUtil.showMessage(getApplicationContext(), getString(R.string.event_saved_successfully));
							finish();
						}
					}
				}
			}
		});
		delete_button.setOnClickListener(new View.OnClickListener(){
		    public void onClick(View view){
                calendar_data.child(ParametersUtil.removeChars(title.getText().toString().concat(event_date.getText().toString().replace("/", "").concat(starting_time.getText().toString().concat(ending_time.getText().toString()))), new char[]{'/','.','#','$','[',']'})).removeValue();
                SketchwareUtil.showMessage(getApplicationContext(), getString(R.string.event_deleted_successfully));
                finish();
            }
        });
	}
	private void initializeLogic() {
        if(getIntent().getStringExtra("title").equals("0")) {
            event_date.setText(new SimpleDateFormat("d/M/yyyy").format(calendar.getTime()));
            title.setText("");
            starting_time.setText(new SimpleDateFormat(" HH : mm ").format(calendar.getTime()));
            calendar.add(Calendar.HOUR, 1);
            ending_time.setText(new SimpleDateFormat(" HH : mm ").format(calendar.getTime()));
            location.setText("");
            address.setText("");
            description.setText("");
            delete_button.setVisibility(View.GONE);
        }
        else{
            event_date.setText(getIntent().getStringExtra("date"));
            title.setText(getIntent().getStringExtra("title"));
            starting_time.setText(getIntent().getStringExtra("starting_time"));
            ending_time.setText(getIntent().getStringExtra("ending_time"));
            location.setText(getIntent().getStringExtra("location"));
            address.setText(getIntent().getStringExtra("address"));
            description.setText(getIntent().getStringExtra("description"));
            date_button.setEnabled(false);
            event_date.setEnabled(false);
            title.setEnabled(false);
            starting_time.setEnabled(false);
            ending_time.setEnabled(false);
            location.setEnabled(false);
            address.setEnabled(false);
            description.setEnabled(false);
            allday_switch.setEnabled(false);
            if(starting_time.getText() == " 00 : 00 " && ending_time.getText() == " 23 : 59 "){
                allday_switch.setChecked(true);
            }
            _fab.setVisibility(View.GONE);
            collapsingToolbarLayout.setTitle("View Event");
            location_button.setEnabled(false);
        }
	}
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
	    if(requestCode == PLACE_PICKER_REQUEST && resultCode == RESULT_OK){
            Place place = PlacePicker.getPlace(data, this);
            String locationStr = String.format("%s", place.getName());
            String addressStr = String.format("%s", place.getAddress());
            location.setText(locationStr);
            address.setText(addressStr);
        }
    }
	public void showTimePickerDialog(View v) {
		DialogFragment newFragment = new TimePickerFragment();
		newFragment.show(getFragmentManager(), "timePicker");
	}
	public static class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			
			final Calendar c = Calendar.getInstance();
			int hour = c.get(Calendar.HOUR_OF_DAY);
			int minute = c.get(Calendar.MINUTE);
			return new TimePickerDialog(getActivity(), this, hour, minute, true);
		}
		
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			if(type == 1){
				starting_time.setText(" " + hourOfDay + " : " + minute + " ");
				_replaceAll(1);
			}
			if(type == 2){
				ending_time.setText(" " + hourOfDay + " : " + minute + " ");
				_replaceAll(2);
			}
			    }
	}
	public void showDatePickerDialog(View v) {
		    DialogFragment newFragment = new DatePickerFragment();
		    newFragment.show(getFragmentManager(), "datePicker");
	}
	public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
		 @Override
		    public Dialog onCreateDialog(Bundle savedInstanceState) {
			        // Use the current date as the default date in the picker
			        final Calendar c = Calendar.getInstance();
			        int year = c.get(Calendar.YEAR);
			        int month = c.get(Calendar.MONTH);
			        int day = c.get(Calendar.DAY_OF_MONTH);
			
			        // Create a new instance of DatePickerDialog and return it
			        return new DatePickerDialog(getActivity(), this, year, month, day);
			    }
		public void onDateSet(DatePicker view, int year, int month, int day) {
			date1 = day;
			date2 = month;
			date3 = year;
			_setDate();
		}
	}

	@Override
	public void onBackPressed() {
		finish();
	}
	private static void _replaceAll(final double _type) {
		if (_type == 1) {
			starting_time.setText(starting_time.getText().toString().replace(" 0 ", " 00 ").replace(" 1 ", " 01 ").replace(" 2 ", " 02 ").replace(" 3 ", " 03 ").replace(" 4 ", " 04 ").replace(" 5 ", " 05 ").replace(" 6 ", " 06 ").replace(" 7 ", " 07 ").replace(" 8 ", " 08 ").replace(" 9 ", " 09 "));
		}
		if (_type == 2) {
			ending_time.setText(ending_time.getText().toString().replace(" 0 ", " 00 ").replace(" 1 ", " 01 ").replace(" 2 ", " 02 ").replace(" 3 ", " 03 ").replace(" 4 ", " 04 ").replace(" 5 ", " 05 ").replace(" 6 ", " 06 ").replace(" 7 ", " 07 ").replace(" 8 ", " 08 ").replace(" 9 ", " 09 "));
		}
	}
	
	
	private static void _setDate () {
		date = String.valueOf((long)(date1)).concat("/".concat(String.valueOf((long)(date2 + 1)).concat("/".concat(String.valueOf((long)(date3))))));
		event_date.setText(date);
	}
	
	
	@Deprecated
	public void showMessage(String _s) {
		Toast.makeText(getApplicationContext(), _s, Toast.LENGTH_SHORT).show();
	}
	
	@Deprecated
	public int getLocationX(View _v) {
		int _location[] = new int[2];
		_v.getLocationInWindow(_location);
		return _location[0];
	}
	
	@Deprecated
	public int getLocationY(View _v) {
		int _location[] = new int[2];
		_v.getLocationInWindow(_location);
		return _location[1];
	}
	
	@Deprecated
	public int getRandom(int _min, int _max) {
		Random random = new Random();
		return random.nextInt(_max - _min + 1) + _min;
	}
	
	@Deprecated
	public ArrayList<Double> getCheckedItemPositionsToArray(ListView _list) {
		ArrayList<Double> _result = new ArrayList<Double>();
		SparseBooleanArray _arr = _list.getCheckedItemPositions();
		for (int _iIdx = 0; _iIdx < _arr.size(); _iIdx++) {
			if (_arr.valueAt(_iIdx))
			_result.add((double)_arr.keyAt(_iIdx));
		}
		return _result;
	}
	
	@Deprecated
	public float getDip(int _input){
		return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, _input, getResources().getDisplayMetrics());
	}
	
	@Deprecated
	public int getDisplayWidthPixels(){
		return getResources().getDisplayMetrics().widthPixels;
	}
	
	@Deprecated
	public int getDisplayHeightPixels(){
		return getResources().getDisplayMetrics().heightPixels;
	}
	
}

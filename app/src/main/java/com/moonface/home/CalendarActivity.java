package com.moonface.home;

import android.os.*;
import android.view.*;
import android.widget.*;
import android.content.*;
import android.graphics.*;
import android.util.*;
import java.util.*;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import java.util.HashMap;
import java.util.ArrayList;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.BaseAdapter;
import java.util.Timer;
import java.util.TimerTask;
import android.app.Activity;
import android.content.SharedPreferences;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ChildEventListener;
import com.moonface.Util.ParametersUtil;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;

public class CalendarActivity extends AppCompatActivity {
	
	private FirebaseDatabase _firebase = FirebaseDatabase.getInstance();
	
	private Toolbar _toolbar;
	private FloatingActionButton _fab;
	private double date1 = 0;
	private double date2 = 0;
	private double date3 = 0;
	private String date = "";
	private HashMap<String, Object> my_calendar = new HashMap<>();
	private double position = 0;

	private ArrayList<HashMap<String, Object>> calendar_list = new ArrayList<>();
	private ArrayList<HashMap<String, Object>> events_list = new ArrayList<>();
	
	private LinearLayout linear13;
	private DatePicker dp;
	private ListView listview1;
	
	private SharedPreferences data;
	private DatabaseReference calendar_data;
    private Intent openEvent_intent = new Intent();
    @Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.calendar);
		initialize();
		initializeLogic();
	}
	
	private void initialize() {
		
		_toolbar = findViewById(R.id._toolbar);
		setSupportActionBar(_toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _v) {
				onBackPressed();
			}
		});
		_fab = findViewById(R.id._fab);
		
		linear13 = findViewById(R.id.linear13);
		dp = findViewById(R.id.dp);
		listview1 = findViewById(R.id.listview1);
		data = getSharedPreferences("data", Activity.MODE_PRIVATE);
		calendar_data = _firebase.getReference("users").child(data.getString("id","")).child("calendar");
		listview1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> _param1, View _param2, int _param3, long _param4) {
				final int _position = _param3;
				_openEvent(events_list.get(_position).get("title").toString(), events_list.get(_position).get("date").toString(), events_list.get(_position).get("time_start").toString(), events_list.get(_position).get("time_end").toString(), events_list.get(_position).get("location").toString(), events_list.get(_position).get("address").toString(), events_list.get(_position).get("description").toString());
			}
		});
		_fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				_openEvent("0", "0", "0", "0", "0", "0", "0");
			}
		});
	}
	private void initializeLogic() {
		Window w = this.getWindow();w.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);w.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS); w.setStatusBarColor(Color.parseColor("#002f6c"));
		Calendar calendar = Calendar.getInstance();
		dp.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),new DatePicker.OnDateChangedListener(){
			@Override public void onDateChanged(DatePicker datePicker, int i, int i1, int i2)
			{
				date1 = datePicker.getDayOfMonth();
				date2 = datePicker.getMonth();
				date3 = datePicker.getYear();
				_change();
			}
		}
		);
		android.graphics.drawable.GradientDrawable linearLx = new android.graphics.drawable.GradientDrawable();  linearLx.setColor(0xFFE0E0E0);  linearLx.setCornerRadius(5);  linear13.setBackground(linearLx);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { linear13.setElevation(5f);}
		date1=calendar.get(Calendar.DAY_OF_MONTH);
		date2=calendar.get(Calendar.MONTH);
		date3=calendar.get(Calendar.YEAR);
	}
	
	@Override
	public void onBackPressed() {
		finish();
	}
	public void onStart() {
		super.onStart();
		calendar_list.clear();
		events_list.clear();
		date = String.valueOf((long)(date1)).concat("/".concat(String.valueOf((long)(date2 + 1)).concat("/".concat(String.valueOf((long)(date3))))));
		calendar_data.addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot _dataSnapshot) {
				calendar_list = new ArrayList<>();
				try {
					GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
					for (DataSnapshot _data : _dataSnapshot.getChildren()) {
						HashMap<String, Object> _map = _data.getValue(_ind);
						calendar_list.add(_map);
					}
				}
				catch (Exception _e) {
					_e.printStackTrace();
				}
				position = 0;
				for(int _repeat52 = 0; _repeat52 < calendar_list.size(); _repeat52++) {
					if (calendar_list.get((int)position).get("uid").toString().equals(data.getString("id", ""))) {
						if (calendar_list.get((int)position).get("date").toString().replace("/", "").equals(date.replace("/", ""))) {
							my_calendar = calendar_list.get((int)position);
							events_list.add(my_calendar);
						}
						else {

						}
					}
					else {

					}
					position++;
					if (position == calendar_list.size()) {
						listview1.setAdapter(new Listview1Adapter(events_list));
						break;
					}
				}
			}
			@Override
			public void onCancelled(DatabaseError _databaseError) {
			}
		});
	}
    private void _openEvent (final String _title, final String _date, final String _starting_time, final String _ending_time, final String _location, final String _address, final String _description) {
        openEvent_intent.putExtra("title", _title);
        openEvent_intent.putExtra("date", _date);
        openEvent_intent.putExtra("starting_time", _starting_time);
        openEvent_intent.putExtra("ending_time", _ending_time);
        openEvent_intent.putExtra("location", _location);
        openEvent_intent.putExtra("address", _address);
        openEvent_intent.putExtra("description", _description);
        openEvent_intent.setClass(getApplicationContext(), AddEventActivity.class);
        startActivity(openEvent_intent);
    }
	private void _change () {
		calendar_list.clear();
		events_list.clear();
		date = String.valueOf((long)(date1)).concat("/".concat(String.valueOf((long)(date2 + 1)).concat("/".concat(String.valueOf((long)(date3))))));
		calendar_data.addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot _dataSnapshot) {
				calendar_list = new ArrayList<>();
				try {
					GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
					for (DataSnapshot _data : _dataSnapshot.getChildren()) {
						HashMap<String, Object> _map = _data.getValue(_ind);
						calendar_list.add(_map);
					}
				}
				catch (Exception _e) {
					_e.printStackTrace();
				}
				position = 0;
				for(int _repeat52 = 0; _repeat52 < calendar_list.size(); _repeat52++) {
					if (calendar_list.get((int)position).get("uid").toString().equals(data.getString("id", ""))) {
						if (calendar_list.get((int)position).get("date").toString().replace("/", "").equals(date.replace("/", ""))) {
							my_calendar = calendar_list.get((int)position);
							events_list.add(my_calendar);
						}
						else {
							
						}
					}
					else {
						
					}
					position++;
					if (position == calendar_list.size()) {
						listview1.setAdapter(new Listview1Adapter(events_list));
						break;
					}
				}
			}
			@Override
			public void onCancelled(DatabaseError _databaseError) {
			}
		});
	}
	
	
	private void _refresh () {
		((BaseAdapter)listview1.getAdapter()).notifyDataSetChanged();
	}
	
	
	public class Listview1Adapter extends BaseAdapter {
		ArrayList<HashMap<String, Object>> _data;
		public Listview1Adapter(ArrayList<HashMap<String, Object>> _arr) {
			_data = _arr;
		}
		
		@Override
		public int getCount() {
			return _data.size();
		}
		
		@Override
		public HashMap<String, Object> getItem(int _index) {
			return _data.get(_index);
		}
		
		@Override
		public long getItemId(int _index) {
			return _index;
		}
		@Override
		public View getView(final int _position, View _view, ViewGroup _viewGroup) {
			LayoutInflater _inflater = (LayoutInflater)getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View _v = _view;
			if (_v == null) {
				_v = _inflater.inflate(R.layout.calendar_event, null);
			}
			
			final LinearLayout linear1 = _v.findViewById(R.id.linear1);
			final TextView title = _v.findViewById(R.id.title);
			final ImageView erase = _v.findViewById(R.id.erase);
			final TextView date = _v.findViewById(R.id.date);
			final TextView starting_time = _v.findViewById(R.id.starting_time);
			final TextView ending_time = _v.findViewById(R.id.ending_time);
			final TextView location = _v.findViewById(R.id.location);
			
			android.graphics.drawable.GradientDrawable linearLx = new android.graphics.drawable.GradientDrawable();  linearLx.setColor(0xFFE0E0E0);  linearLx.setCornerRadius(5);  linear1.setBackground(linearLx);
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { linear1.setElevation(5f);}
			title.setText(events_list.get(_position).get("".concat("title")).toString());
			date.setText(events_list.get(_position).get("date").toString());
			starting_time.setText(events_list.get(_position).get("".concat("time_start")).toString());
			ending_time.setText(events_list.get(_position).get("".concat("time_end")).toString());
			location.setText(events_list.get(_position).get("".concat("location")).toString());
			erase.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View _view) {
					calendar_data.child(ParametersUtil.removeChars(title.getText().toString().concat(date.getText().toString().replace("/", "").concat(starting_time.getText().toString().concat(ending_time.getText().toString()))), new char[]{'/','.','#','$','[',']'})).removeValue();
					events_list.remove(_position);
					_refresh();
					SketchwareUtil.showMessage(getApplicationContext(), getString(R.string.event_deleted_successfully));
				}
			});
			
			return _v;
		}
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

package com.moonface.home;

import android.app.*;
import android.os.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import android.content.*;
import android.graphics.*;
import android.media.*;
import android.net.*;
import android.text.*;
import android.util.*;
import android.webkit.*;
import android.animation.*;
import android.view.animation.*;
import java.util.*;
import java.text.*;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import java.util.HashMap;
import java.util.ArrayList;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ChildEventListener;
import com.moonface.Util.DrawableUtil;
import com.moonface.Util.InputUtil;

import android.app.Activity;
import android.content.SharedPreferences;
import android.view.View;

public class NotesActivity extends AppCompatActivity {
	
	private FirebaseDatabase _firebase = FirebaseDatabase.getInstance();
	
	private Toolbar _toolbar;
	private HashMap<String, Object> note = new HashMap<>();
	private double organize = 0;
	private HashMap<String, Object> list_data = new HashMap<>();
	private String title_str = "";
	private String content_str = "";
	private String color_str = "";
	
	private ArrayList<HashMap<String, Object>> notes_list = new ArrayList<>();
	private ArrayList<String> title_list = new ArrayList<>();
	private ArrayList<String> content_list = new ArrayList<>();
	private ArrayList<String> color_list = new ArrayList<>();
	private ArrayList<HashMap<String, Object>> retrieve_data = new ArrayList<>();
	
	private LinearLayout linear_note;
	private ListView listview1;
	private EditText edittext1;
	private ImageView imageview3;
	private EditText edittext2;
	
	private DatabaseReference notes_data;
	private ChildEventListener _notes_data_child_listener;
	private SharedPreferences data;
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.notes);
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
		linear_note = findViewById(R.id.linear_note);
		listview1 = findViewById(R.id.listview1);
		edittext1 = findViewById(R.id.edittext1);
		imageview3 = findViewById(R.id.imageview3);
		edittext2 = findViewById(R.id.edittext2);
		data = getSharedPreferences("data", Activity.MODE_PRIVATE);
		notes_data = _firebase.getReference("users").child(data.getString("id","")).child("notes");
		
		imageview3.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				InputUtil.hideKeyboard(getApplicationContext(), NotesActivity.this);
				edittext1.setText(edittext1.getText().toString().replace("~", ""));
				edittext2.setText(edittext2.getText().toString().replace("~", ""));
				if ((edittext1.getText().toString().length() > 0) && (edittext2.getText().toString().length() > 0)) {
					notes_list.clear();
					title_list.clear();
					content_list.clear();
					color_list.clear();
					if (title_str.equals("")) {
						title_str = edittext1.getText().toString();
						content_str = edittext2.getText().toString();
						color_str = DrawableUtil.getMatColor();
					}
					else {
						title_str = title_str.concat("~".concat(edittext1.getText().toString()));
						content_str = content_str.concat("~".concat(edittext2.getText().toString()));
						color_str = color_str.concat("~".concat(DrawableUtil.getMatColor()));
					}
					_data_upload();
					title_list.addAll(Arrays.asList(title_str.split("~")));
					content_list.addAll(Arrays.asList(content_str.split("~")));
					color_list.addAll(Arrays.asList(color_str.split("~")));
					for(int _repeat100 = 0; _repeat100 < title_list.size(); _repeat100++) {
						note = new HashMap<>();
						note.put("key", "value");
						notes_list.add(note);
						note.clear();
					}
					_refresh_list();
					edittext1.setText("");
					edittext2.setText("");
				}
			}
		});
	}
	private void initializeLogic() {
		Window w = this.getWindow();w.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);w.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS); w.setStatusBarColor(Color.parseColor("#008ba3"));
		android.graphics.drawable.GradientDrawable linearLy = new android.graphics.drawable.GradientDrawable();  linearLy.setColor(0xFFE0E0E0);  linearLy.setCornerRadius(15);  linear_note.setBackground(linearLy);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { linear_note.setElevation(10f);}
		notes_data.addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot _dataSnapshot) {
				retrieve_data = new ArrayList<>();
				try {
					GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
					for (DataSnapshot _data : _dataSnapshot.getChildren()) {
						HashMap<String, Object> _map = _data.getValue(_ind);
						retrieve_data.add(_map);
					}
				}
				catch (Exception _e) {
					_e.printStackTrace();
				}
				organize = 0;
				for(int i=0; i<retrieve_data.size(); i++){
					if (retrieve_data.get(i).get("uid").toString().equals(data.getString("id", ""))) {
						title_str = retrieve_data.get(i).get("title_list").toString();
						content_str = retrieve_data.get(i).get("content_list").toString();
						color_str = retrieve_data.get(i).get("color_list").toString();
						break;
					}
				}
				if (title_str.length() > 0) {
					title_list.addAll(Arrays.asList(title_str.split("~")));
					content_list.addAll(Arrays.asList(content_str.split("~")));
					color_list.addAll(Arrays.asList(color_str.split("~")));
					for(int _repeat126 = 0; _repeat126 < title_list.size(); _repeat126++) {
						note = new HashMap<>();
						note.put("key", "value");
						notes_list.add(note);
						note.clear();
					}
					_refresh_list();
				}
			}
			@Override
			public void onCancelled(DatabaseError _databaseError) {
			}
		});
		if (!(notes_list.size() == 0)) {
			listview1.setVisibility(View.VISIBLE);
		}
		else {
			listview1.setVisibility(View.GONE);
		}
	}
	
	@Override
	public void onBackPressed() {
		finish();
	}
	private void _refresh_list () {
		listview1.setAdapter(new Listview1Adapter(notes_list));
		((BaseAdapter)listview1.getAdapter()).notifyDataSetChanged();
		if (!(notes_list.size() == 0)) {
			listview1.setVisibility(View.VISIBLE);
		}
		else {
			listview1.setVisibility(View.GONE);
		}
	}
	
	
	private void _data_upload () {
		list_data = new HashMap<>();
		list_data.put("title_list", title_str);
		list_data.put("content_list", content_str);
		list_data.put("color_list", color_str);
		list_data.put("uid", data.getString("id", ""));
		notes_data.child(data.getString("id", "")).updateChildren(list_data);
		list_data.clear();
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
				_v = _inflater.inflate(R.layout.note, null);
			}
			
			final TextView textview1 = _v.findViewById(R.id.textview1);
			final ImageView delete = _v.findViewById(R.id.delete);
			final TextView textview2 = _v.findViewById(R.id.textview2);
			final LinearLayout back_linear = _v.findViewById(R.id.back_linear);
			
			textview1.setText(title_list.get(_position));
			textview2.setText(content_list.get(_position));
            back_linear.setBackground(DrawableUtil.roundedBackground(15, color_list.get(_position)));
            back_linear.setElevation(10f);
			delete.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View _view) {
					title_list.remove(_position);
					content_list.remove(_position);
					color_list.remove(_position);
					notes_list.remove(_position);
					title_str = "";
					content_str = "";
					color_str = "";
					organize = 0;
					for(int _repeat189 = 0; _repeat189 < title_list.size(); _repeat189++) {
						if (title_str.equals("")) {
							title_str = title_list.get((int)(organize));
							content_str = content_list.get((int)(organize));
							color_str = color_list.get((int)(organize));
						}
						else {
							title_str = title_str.concat("~".concat(title_list.get((int)(organize))));
							content_str = content_str.concat("~".concat(content_list.get((int)(organize))));
							color_str = color_str.concat("~".concat(color_list.get((int)(organize))));
						}
						organize++;
					}
					_data_upload();
					_refresh_list();
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

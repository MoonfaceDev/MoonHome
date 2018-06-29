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
import android.app.Activity;
import android.content.SharedPreferences;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ChildEventListener;
import com.moonface.Util.InputUtil;

import android.view.View;
import android.widget.AdapterView;

public class ShoppingListActivity extends AppCompatActivity {
	
	private FirebaseDatabase _firebase = FirebaseDatabase.getInstance();
	
	private Toolbar _toolbar;
	private String list_str = "";
	private HashMap<String, Object> map_data = new HashMap<>();
	
	private ArrayList<String> shopping_list = new ArrayList<>();
	private ArrayList<HashMap<String, Object>> retrieve_data = new ArrayList<>();
	
	private LinearLayout linear3;
	private ListView listview1;
	private EditText edittext1;
	private ImageView imageview3;
	
	private SharedPreferences data;
	private DatabaseReference shopping_list_data;
	private ChildEventListener _shopping_list_data_child_listener;
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.shopping_list);
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
		linear3 = findViewById(R.id.linear3);
		listview1 = findViewById(R.id.listview1);
		edittext1 = findViewById(R.id.edittext1);
		imageview3 = findViewById(R.id.imageview3);
		data = getSharedPreferences("data", Activity.MODE_PRIVATE);
		shopping_list_data = _firebase.getReference("users").child(data.getString("id","")).child("shopping_list");
		
		listview1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> _param1, View _param2, int _param3, long _param4) {
				final int _position = _param3;
				shopping_list.remove(_position);
				list_str = "";
				int organize = 0;
				for(int _repeat141 = 0; _repeat141 < shopping_list.size(); _repeat141++) {
					if (list_str.equals("")) {
						list_str = shopping_list.get(organize);
					}
					else {
						list_str = list_str.concat("#".concat(shopping_list.get(organize)));
					}
					organize++;
				}
				_data_upload();
				_refresh_list();
			}
		});
		
		imageview3.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (edittext1.getText().toString().length() > 0) {
					edittext1.setText(edittext1.getText().toString().replace("#", ""));
					shopping_list.clear();
					if (list_str.equals("")) {
						list_str = edittext1.getText().toString();
					}
					else {
						list_str = list_str.concat("#".concat(edittext1.getText().toString()));
					}
					shopping_list.addAll(Arrays.asList(list_str.split("#")));
					_data_upload();
					_refresh_list();
					edittext1.setText("");
				}
				InputUtil.hideKeyboard(getApplicationContext(), ShoppingListActivity.this);
			}
		});
	}
	private void initializeLogic() {
		Window w = this.getWindow();w.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);w.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS); w.setStatusBarColor(Color.parseColor("#320b86"));
		android.graphics.drawable.GradientDrawable linearLx = new android.graphics.drawable.GradientDrawable();  linearLx.setColor(0xFFE0E0E0);  linearLx.setCornerRadius(15);  linear3.setBackground(linearLx);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { linear3.setElevation(10f);}
		android.graphics.drawable.GradientDrawable linearLy = new android.graphics.drawable.GradientDrawable();  linearLy.setColor(0xFFE0E0E0);  linearLy.setCornerRadius(15);  listview1.setBackground(linearLy);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { listview1.setElevation(10f);}
		shopping_list_data.addListenerForSingleValueEvent(new ValueEventListener() {
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
				if(retrieve_data.size() > 0) {
					if (retrieve_data.get(0).get("uid").toString().equals(data.getString("id", ""))) {
						list_str = retrieve_data.get(0).get("list").toString();
					}
				}
				if (list_str.length() > 0) {
					shopping_list.addAll(Arrays.asList(list_str.split("#")));
					_refresh_list();
				}
			}
			@Override
			public void onCancelled(DatabaseError _databaseError) {
			}
		});
		if (!(shopping_list.size() == 0)) {
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
	private void _data_upload () {
		map_data = new HashMap<>();
		map_data.put("list", list_str);
		map_data.put("uid", data.getString("id", ""));
		shopping_list_data.child(data.getString("id", "")).updateChildren(map_data);
		map_data.clear();
	}
	
	
	private void _refresh_list () {
		listview1.setAdapter(new ArrayAdapter<String>(getBaseContext(), R.layout.shoppinglist_item, shopping_list));
		((BaseAdapter)listview1.getAdapter()).notifyDataSetChanged();
		if (!(shopping_list.size() == 0)) {
			listview1.setVisibility(View.VISIBLE);
		}
		else {
			listview1.setVisibility(View.GONE);
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

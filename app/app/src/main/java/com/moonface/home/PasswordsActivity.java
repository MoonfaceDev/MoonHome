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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
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

public class PasswordsActivity extends AppCompatActivity {
	
	private FirebaseDatabase _firebase = FirebaseDatabase.getInstance();
	
	private Toolbar _toolbar;
	private HashMap<String, Object> password = new HashMap<>();
	private String service_str = "";
	private String username_str = "";
	private String password_str = "";
	
	private ArrayList<HashMap<String, Object>> passwords_list = new ArrayList<>();
	private ArrayList<String> service_list = new ArrayList<>();
	private ArrayList<String> username_list = new ArrayList<>();
	private ArrayList<String> password_list = new ArrayList<>();
	private ArrayList<HashMap<String, Object>> retrieve_list = new ArrayList<>();
	
	private LinearLayout linear3;
	private LinearLayout linear10;
	private EditText edittext1;
	private ImageView imageview2;
	private EditText edittext2;
	private EditText edittext3;
	private ListView listview1;
	
	private DatabaseReference passwords_data;
	private SharedPreferences data;
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.passwords);
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
		linear10 = findViewById(R.id.linear10);
		edittext1 = findViewById(R.id.edittext1);
		imageview2 = findViewById(R.id.imageview2);
		edittext2 = findViewById(R.id.edittext2);
		edittext3 = findViewById(R.id.edittext3);
		listview1 = findViewById(R.id.listview1);
		data = getSharedPreferences("data", Activity.MODE_PRIVATE);
		passwords_data = _firebase.getReference("users").child(data.getString("id","")).child("passwords");
		
		imageview2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				InputUtil.hideKeyboard(getApplicationContext(), PasswordsActivity.this);
				edittext1.setText(edittext1.getText().toString().replace("#", ""));
				edittext2.setText(edittext2.getText().toString().replace("#", ""));
				edittext3.setText(edittext3.getText().toString().replace("#", ""));
				if ((edittext1.getText().toString().length() > 0) && ((edittext2.getText().toString().length() > 0) && (edittext3.getText().toString().length() > 0))) {
					passwords_list.clear();
					service_list.clear();
					username_list.clear();
					password_list.clear();
					if (service_str.equals("")) {
						service_str = edittext1.getText().toString();
						username_str = edittext2.getText().toString();
						password_str = edittext3.getText().toString();
					}
					else {
						service_str = service_str.concat("#".concat(edittext1.getText().toString()));
						username_str = username_str.concat("#".concat(edittext2.getText().toString()));
						password_str = password_str.concat("#".concat(edittext3.getText().toString()));
					}
					_data_upload();
					service_list.addAll(Arrays.asList(service_str.split("#")));
					username_list.addAll(Arrays.asList(username_str.split("#")));
					password_list.addAll(Arrays.asList(password_str.split("#")));
					for(int _repeat85 = 0; _repeat85 < service_list.size(); _repeat85++) {
						password = new HashMap<>();
						password.put("key", "value");
						passwords_list.add(password);
						password.clear();
					}
					_refresh_list();
					edittext1.setText("");
					edittext2.setText("");
					edittext3.setText("");
				}
			}
		});
	}
	private void initializeLogic() {
		Window w = this.getWindow();w.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);w.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS); w.setStatusBarColor(Color.parseColor("#ba000d"));
		android.graphics.drawable.GradientDrawable linearLx = new android.graphics.drawable.GradientDrawable();  linearLx.setColor(0xFFF44336);  linearLx.setCornerRadius(15);  linear3.setBackground(linearLx);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { linear3.setElevation(10f);}
		android.graphics.drawable.GradientDrawable linearLy = new android.graphics.drawable.GradientDrawable();  linearLy.setColor(0xFFF44336);  linearLy.setCornerRadius(15);  linear10.setBackground(linearLy);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { linear10.setElevation(10f);}
		linear10.setVisibility(View.GONE);
		passwords_data.addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot _dataSnapshot) {
				retrieve_list = new ArrayList<>();
				try {
					GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
					for (DataSnapshot _data : _dataSnapshot.getChildren()) {
						HashMap<String, Object> _map = _data.getValue(_ind);
						retrieve_list.add(_map);
					}
				}
				catch (Exception _e) {
					_e.printStackTrace();
				}

				if(retrieve_list.size() > 0) {
                    if (retrieve_list.get(0).get("uid").toString().equals(data.getString("id", ""))) {
                        service_str = retrieve_list.get(0).get("service_list").toString();
                        username_str = retrieve_list.get(0).get("username_list").toString();
                        password_str = retrieve_list.get(0).get("password_list").toString();
                    }
                }
				if (service_str.length() > 0) {
					service_list.addAll(Arrays.asList(service_str.split("#")));
					username_list.addAll(Arrays.asList(username_str.split("#")));
					password_list.addAll(Arrays.asList(password_str.split("#")));
					for(int _repeat82 = 0; _repeat82 < service_list.size(); _repeat82++) {
						password = new HashMap<>();
						password.put("key", "value");
						passwords_list.add(password);
						password.clear();
					}
				}
				_refresh_list();
			}
			@Override
			public void onCancelled(DatabaseError _databaseError) {
			}
		});
	}
	
	@Override
	public void onBackPressed() {
		finish();
	}
	private void _data_upload () {
		password = new HashMap<>();
		password.put("service_list", service_str);
		password.put("username_list", username_str);
		password.put("password_list", password_str);
		password.put("uid", data.getString("id", ""));
		passwords_data.child(data.getString("id", "")).updateChildren(password);
		password.clear();
	}
	
	
	private void _refresh_list () {
		listview1.setAdapter(new Listview1Adapter(passwords_list));
		((BaseAdapter)listview1.getAdapter()).notifyDataSetChanged();
		if (!(service_list.size() == 0)) {
			linear10.setVisibility(View.VISIBLE);
		}
		else {
			linear10.setVisibility(View.GONE);
		}
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
				_v = _inflater.inflate(R.layout.password_template, null);
			}
			
			final TextView textview1 = _v.findViewById(R.id.textview1);
			final ImageView imageview1 = _v.findViewById(R.id.imageview1);
			final TextView textview2 = _v.findViewById(R.id.textview2);
			final TextView textview3 = _v.findViewById(R.id.textview3);
			
			textview1.setText(service_list.get(_position));
			textview2.setText(username_list.get(_position));
			textview3.setText(password_list.get(_position));
			imageview1.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View _view) {
					passwords_list.remove(_position);
					service_list.remove(_position);
					username_list.remove(_position);
					password_list.remove(_position);
					service_str = "";
					username_str = "";
					password_str = "";
					int organize = 0;
					for(int _repeat26 = 0; _repeat26 < service_list.size(); _repeat26++) {
						if (service_str.equals("")) {
							service_str = service_list.get(organize);
							username_str = username_list.get(organize);
							password_str = password_list.get(organize);
						}
						else {
							service_str = service_str.concat("#".concat(service_list.get(organize)));
							username_str = username_str.concat("#".concat(username_list.get(organize)));
							password_str = password_str.concat("#".concat(password_list.get(organize)));
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

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
import android.content.Intent;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ChildEventListener;
import android.app.Activity;
import android.content.SharedPreferences;
import android.animation.ObjectAnimator;
import java.util.Timer;
import java.util.TimerTask;
import android.view.View;
import android.widget.AdapterView;

public class InboxActivity extends AppCompatActivity {

	private Timer _timer = new Timer();
	private FirebaseDatabase _firebase = FirebaseDatabase.getInstance();

	private Toolbar _toolbar;
	private FloatingActionButton _fab;

	private ArrayList<HashMap<String, Object>> title_list = new ArrayList<>();

	private ListView listview1;
	
	private Intent compose_int = new Intent();
	private DatabaseReference inbox;
	private ChildEventListener _inbox_child_listener;
	private SharedPreferences data;
	private ObjectAnimator rotation = new ObjectAnimator();
	private TimerTask after;
	private SharedPreferences mailData;
	private Intent openEmail_intent = new Intent();
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.inbox);
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
		
		listview1 = findViewById(R.id.listview1);
		data = getSharedPreferences("data", Activity.MODE_PRIVATE);
		inbox = _firebase.getReference("users").child(data.getString("id","")).child("inbox");

		listview1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> _param1, View _param2, int _param3, long _param4) {
				final int _position = _param3;
				_openEmail(title_list.get(_position).get("key").toString(), title_list.get(_position).get("subject").toString(), title_list.get(_position).get("from").toString(), title_list.get(_position).get("date").toString(), title_list.get(_position).get("email").toString());
			}
		});
		
		_fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				compose_int.setClass(getApplicationContext(), ComposeActivity.class);
				compose_int.putExtra("reply", "0");
				compose_int.putExtra("replySubject", "0");
				startActivity(compose_int);
			}
		});
		
		_inbox_child_listener = new ChildEventListener() {
			@Override
			public void onChildAdded(DataSnapshot _param1, String _param2) {
				GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
				final String _childKey = _param1.getKey();
				final HashMap<String, Object> _childValue = _param1.getValue(_ind);
				if (_childValue.get("to").toString().equals(data.getString("email", ""))) {
					_childValue.put("key", _childKey);
				    title_list.add(_childValue);
				}
				listview1.setAdapter(new Listview1Adapter(title_list));
				((BaseAdapter)listview1.getAdapter()).notifyDataSetChanged();
			}
			
			@Override
			public void onChildChanged(DataSnapshot _param1, String _param2) {
				GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
				final String _childKey = _param1.getKey();
				final HashMap<String, Object> _childValue = _param1.getValue(_ind);
				
			}
			
			@Override
			public void onChildMoved(DataSnapshot _param1, String _param2) {
				
			}
			
			@Override
			public void onChildRemoved(DataSnapshot _param1) {
				GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
				final String _childKey = _param1.getKey();
				final HashMap<String, Object> _childValue = _param1.getValue(_ind);
                _childValue.put("key", _childKey);
				title_list.remove(_childValue);
				listview1.setAdapter(new Listview1Adapter(title_list));
				((BaseAdapter)listview1.getAdapter()).notifyDataSetChanged();
			}
			
			@Override
			public void onCancelled(DatabaseError _param1) {
				final String _errorCode = String.valueOf(_param1.getCode());
				final String _errorMessage = _param1.getMessage();
				
			}
		};
		inbox.addChildEventListener(_inbox_child_listener);
	}
	private void initializeLogic() {
		Window w = this.getWindow();w.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);w.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			w.setStatusBarColor(Color.parseColor("#c79100"));
		}
	}
	
	@Override
	public void onBackPressed() {
		finish();
	}

	private void _openEmail (final String key, final String _subject, final String _from, final String _date, final String _email) {
		openEmail_intent.putExtra("key", key);
		openEmail_intent.putExtra("subject", _subject);
		openEmail_intent.putExtra("from_email", _from);
		openEmail_intent.putExtra("date", _date);
		openEmail_intent.putExtra("email", _email);
		openEmail_intent.putExtra("running", true);
		openEmail_intent.setClass(getApplicationContext(), ViewEmailActivity.class);
		startActivity(openEmail_intent);
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
				_v = _inflater.inflate(R.layout.mail_title, null);
			}
			
			final LinearLayout content = _v.findViewById(R.id.content);
			final TextView date = _v.findViewById(R.id.date);
			final TextView from = _v.findViewById(R.id.from);
			final TextView expand_check = _v.findViewById(R.id.expand_check);
			final ImageView delete = _v.findViewById(R.id.delete);
			final TextView title = _v.findViewById(R.id.title);
			final ImageView reply = _v.findViewById(R.id.reply);
			final ImageView expand = _v.findViewById(R.id.expand);
			final TextView email = _v.findViewById(R.id.email);
			
			content.setVisibility(View.GONE);
			title.setText(_data.get(_position).get("subject").toString());
			from.setText(_data.get(_position).get("from").toString());
			date.setText(_data.get(_position).get("date").toString());
			email.setText(_data.get(_position).get("email").toString());
			expand.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View _view) {
					if (expand_check.getText().toString().equals(".")) {
						expand_check.setText("");
						rotation.setTarget(expand);
						rotation.setPropertyName("rotation");
						rotation.setFloatValues((float)(0), (float)(180));
						rotation.setDuration(200);
						rotation.start();
						after = new TimerTask() {
							@Override
							public void run() {
								runOnUiThread(new Runnable() {
									@Override
									public void run() {
										rotation.cancel();
										after.cancel();
										expand.setRotation((float)(180));
										content.setVisibility(View.GONE);
									}
								});
							}
						};
						_timer.schedule(after, 200);
					}
					else {
						expand_check.setText(".");
						rotation.setTarget(expand);
						rotation.setPropertyName("rotation");
						rotation.setFloatValues((float)(180), (float)(0));
						rotation.setDuration(200);
						rotation.start();
						after = new TimerTask() {
							@Override
							public void run() {
								runOnUiThread(new Runnable() {
									@Override
									public void run() {
										rotation.cancel();
										after.cancel();
										expand.setRotation((float)(0));
										content.setVisibility(View.VISIBLE);
									}
								});
							}
						};
						_timer.schedule(after, 200);
					}
				}
			});
			delete.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View _view) {
					inbox.child(title_list.get(_position).get("key").toString()).removeValue();
				}
			});
			reply.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View _view) {
					compose_int.setClass(getApplicationContext(), ComposeActivity.class);
					compose_int.putExtra("reply", _data.get(_position).get("from").toString());
					compose_int.putExtra("replySubject", _data.get(_position).get("subject").toString());
					startActivity(compose_int);
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

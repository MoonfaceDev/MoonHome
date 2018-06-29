package com.moonface.home;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.SparseBooleanArray;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Random;

public class ComposeActivity extends AppCompatActivity {
	
	private FirebaseDatabase _firebase = FirebaseDatabase.getInstance();
	
	private Toolbar _toolbar;
	private HashMap<String, Object> in_map = new HashMap<>();
	
	private ArrayList<HashMap<String, Object>> email_list = new ArrayList<>();
	private ArrayList<String> to_list = new ArrayList<>();

	private ListView listview1;
	private TextView from;
    private AutoCompleteTextView edittext1;
	private EditText subject;
	private EditText compose_email;
	
	private SharedPreferences data;
	private DatabaseReference user_list = _firebase.getReference("user_list");
	private DatabaseReference inbox;
	private Calendar time = Calendar.getInstance();
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.compose);
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

		listview1 = findViewById(R.id.listview1);
		from = findViewById(R.id.from);
        edittext1 = findViewById(R.id.edittext1);

		subject = findViewById(R.id.subject);
		compose_email = findViewById(R.id.compose);
		data = getSharedPreferences("data", Activity.MODE_PRIVATE);
		
		listview1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> _param1, View _param2, int _param3, long _param4) {
				final int _position = _param3;
				edittext1.setText(to_list.get(_position));
				listview1.setVisibility(View.GONE);
			}
		});
	}
	private void initializeLogic() {
		Window w = this.getWindow();w.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);w.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS); w.setStatusBarColor(Color.parseColor("#c79100"));
		listview1.setVisibility(View.GONE);
		from.setText(data.getString("email", ""));
		user_list.addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot _dataSnapshot) {
				email_list = new ArrayList<>();
				try {
					GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
					for (DataSnapshot _data : _dataSnapshot.getChildren()) {
						HashMap<String, Object> _map = _data.getValue(_ind);
						email_list.add(_map);
					}
                    if (getIntent().getStringExtra("reply").equals("0")) {
                        String[] arr = new String[email_list.size()];
                        for(int i=0; i<email_list.size(); i++){
                            arr[i] = email_list.get(i).get("user").toString();
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_dropdown_item_1line, arr);
                        edittext1.setThreshold(3);
                        edittext1.setAdapter(adapter);
                    }
				}
				catch (Exception _e) {
					_e.printStackTrace();
				}
				
			}
			@Override
			public void onCancelled(DatabaseError _databaseError) {
			}
		});
		if (!getIntent().getStringExtra("reply").equals("0")) {
			edittext1.setText(getIntent().getStringExtra("reply"));
			subject.setText("RE: ".concat(getIntent().getStringExtra("replySubject")));
			edittext1.setEnabled(false);
			subject.setEnabled(false);
		}
	}
	
	@Override
	public void onBackPressed() {
		finish();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.compose, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.action_send:
			    getIdFromEmail(edittext1.getText().toString());
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
	private void getIdFromEmail(final String email){
        user_list.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot _dataSnapshot) {
                email_list = new ArrayList<>();
                try {
                    GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
                    for (DataSnapshot _data : _dataSnapshot.getChildren()) {
                        HashMap<String, Object> _map = _data.getValue(_ind);
                        if(_map.get("user").toString().equals(email)){
                            uploadData(_data.getKey());
                            break;
                        }
                    }
                }
                catch (Exception _e) {
                    _e.printStackTrace();
                }

            }
            @Override
            public void onCancelled(DatabaseError _databaseError) {
            }
        });

    }
    private void uploadData(String toId){
        in_map = new HashMap<>();
        in_map.put("subject", subject.getText().toString());
        in_map.put("to", edittext1.getText().toString());
        in_map.put("from", from.getText().toString());
        in_map.put("email", compose_email.getText().toString());
        time = Calendar.getInstance();
        in_map.put("date", new SimpleDateFormat("dd/MM HH:mm").format(time.getTime()));
        inbox = _firebase.getReference("users").child(toId).child("inbox");
        inbox.push().updateChildren(in_map);
        finish();
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

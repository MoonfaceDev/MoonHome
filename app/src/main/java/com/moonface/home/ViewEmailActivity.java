package com.moonface.home;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Random;

public class ViewEmailActivity extends AppCompatActivity {

    private FirebaseDatabase _firebase = FirebaseDatabase.getInstance();

    private TextView subject;
	private TextView content;
	private TextView from;
	private TextView time;

    private DatabaseReference inbox;
    private Intent reply_intent = new Intent();
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.view_email);
		initialize();
		initializeLogic();
	}
	
	private void initialize() {

        Toolbar _toolbar = findViewById(R.id._toolbar);
		setSupportActionBar(_toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _v) {
				onBackPressed();
			}
		});
		subject = findViewById(R.id.subject);
		content = findViewById(R.id.content);
		from = findViewById(R.id.from);
		time = findViewById(R.id.time);
        SharedPreferences data = getSharedPreferences("data", Activity.MODE_PRIVATE);
        inbox = _firebase.getReference("users").child(data.getString("id","")).child("inbox");
	}
	private void initializeLogic() {
		Window w = this.getWindow();w.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            w.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			w.setStatusBarColor(Color.parseColor("#c79100"));
		}
		subject.setText(getIntent().getStringExtra("subject"));
		from.setText(getIntent().getStringExtra("from_email"));
		time.setText(getIntent().getStringExtra("date"));
		content.setText(getIntent().getStringExtra("email"));
	}
	
	@Override
	public void onBackPressed() {
		if(getIntent().getBooleanExtra("running", false)) {
            finish();
        } else {
		    Intent intent = new Intent();
		    intent.setClass(this, InboxActivity.class);
		    startActivity(intent);
		    finish();
        }
	}

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.view_email, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_reply:
                reply_intent.setClass(getApplicationContext(), ComposeActivity.class);
                reply_intent.putExtra("reply", getIntent().getStringExtra("from_email"));
                reply_intent.putExtra("replySubject", getIntent().getStringExtra("subject"));
                startActivity(reply_intent);
                return true;

            case R.id.action_delete:
                inbox.child(getIntent().getStringExtra("key")).removeValue();
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);

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

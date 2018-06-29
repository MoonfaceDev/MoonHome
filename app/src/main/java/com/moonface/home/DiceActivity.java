package com.moonface.home;

import android.os.*;
import android.view.*;
import android.widget.*;
import android.graphics.*;
import android.util.*;

import java.util.*;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import java.util.ArrayList;
import android.widget.LinearLayout;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import java.util.Timer;
import java.util.TimerTask;
import android.content.Context;
import android.os.Vibrator;
import android.view.View;
import android.widget.AdapterView;
import android.graphics.Typeface;

public class DiceActivity extends AppCompatActivity {
	
	private Timer _timer = new Timer();
	
	private Toolbar _toolbar;
	private int sides = 0;
	private double result = 0;
	private double roll_num = 0;
	
	private ArrayList<String> sides_select = new ArrayList<>();
	
	private LinearLayout linear3;
	private Button button1;
	private Spinner spinner1;
	private TextView textview3;
	
	private TimerTask roll_timer;
	private Vibrator vibrate;
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.dice);
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
		button1 = findViewById(R.id.button1);
		spinner1 = findViewById(R.id.spinner1);
		textview3 = findViewById(R.id.textview3);
		vibrate = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		
		button1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				button1.setEnabled(false);
				spinner1.setEnabled(false);
				result = SketchwareUtil.getRandom(1, sides);
				roll_timer = new TimerTask() {
					@Override
					public void run() {
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								roll_num = SketchwareUtil.getRandom(1, sides);
								textview3.setText(String.valueOf((long)(roll_num)));
								if (result == roll_num) {
									roll_timer.cancel();
									textview3.setText(String.valueOf((long)(result)));
									vibrate.vibrate((long)(100));
									button1.setEnabled(true);
									spinner1.setEnabled(true);
								}
							}
						});
					}
				};
				_timer.scheduleAtFixedRate(roll_timer, 0, Math.round(1000 / sides));
			}
		});
		
		spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> _param1, View _param2, int _param3, long _param4) {
				final int _position = _param3;
				sides = Integer.parseInt(sides_select.get(_position));
				textview3.setText(String.valueOf(sides));
			}
			
			@Override
			public void onNothingSelected(AdapterView<?> _param1) {
				
			}
		});
	}
	private void initializeLogic() {
		Window w = this.getWindow();w.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);w.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS); w.setStatusBarColor(Color.parseColor("#ba000d"));
		android.graphics.drawable.GradientDrawable lineary = new android.graphics.drawable.GradientDrawable();  lineary.setColor(0xFF000000);  lineary.setCornerRadius(30); 
		linear3.setBackground(lineary);
		android.graphics.drawable.GradientDrawable linearz = new android.graphics.drawable.GradientDrawable();  linearz.setColor(0xFFFFFFFF);  linearz.setCornerRadius(30); 
		textview3.setBackground(linearz);
		sides_select.add("2");
		sides_select.add("4");
		sides_select.add("6");
		sides_select.add("8");
		sides_select.add("10");
		sides_select.add("12");
		sides_select.add("20");
		sides_select.add("100");
		spinner1.setAdapter(new ArrayAdapter<String>(getBaseContext(), R.layout.dice_spinner_item, sides_select));
		spinner1.setSelection(2);
	}
	
	@Override
	public void onBackPressed() {
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

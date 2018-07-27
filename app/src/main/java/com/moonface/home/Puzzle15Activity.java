package com.moonface.home;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.SparseBooleanArray;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.moonface.Util.InputUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class Puzzle15Activity extends AppCompatActivity {
	
	private Timer _timer = new Timer();
    private FirebaseDatabase _firebase = FirebaseDatabase.getInstance();

    private Toolbar _toolbar;
	private double randomPlace = 0;
	private double num1 = 0;
	private double num2 = 0;
	private double changed = 0;
	private int seconds = 0;
	private double blankRow = 0;
	private double i = 0;
	private double a = 0;
	private double j = 0;
	private double b = 0;
	private double row = 0;
	private double parity = 0;
	private double position = 0;
	private double score = 0;
	private double highscore = -1;
    private HashMap<String, Object> dataMap = new HashMap<>();

    private ArrayList<Double> puzzle = new ArrayList<>();
	private ArrayList<Double> distributionList = new ArrayList<>();
    private ArrayList<HashMap<String, Object>> getData = new ArrayList<>();


	private TextView textview17;
	private TextView textview18;
	private LinearLayout linear_game;
	private TextView textview1;
	private TextView textview2;
	private TextView textview3;
	private TextView textview4;
	private TextView textview5;
	private TextView textview6;
	private TextView textview7;
	private TextView textview8;
	private TextView textview9;
	private TextView textview10;
	private TextView textview11;
	private TextView textview12;
	private TextView textview13;
	private TextView textview14;
	private TextView textview15;
	private TextView textview16;
	
	private AlertDialog.Builder winDialog;
	private TimerTask timecount;
	private Calendar timerFormat = Calendar.getInstance();
    private SharedPreferences data;
    private DatabaseReference highscoreData;
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.puzzle15);
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
		textview17 = findViewById(R.id.textview17);
		textview18 = findViewById(R.id.textview18);
		linear_game = findViewById(R.id.linear_game);
		textview1 = findViewById(R.id.textview1);
		textview2 = findViewById(R.id.textview2);
		textview3 = findViewById(R.id.textview3);
		textview4 = findViewById(R.id.textview4);
		textview5 = findViewById(R.id.textview5);
		textview6 = findViewById(R.id.textview6);
		textview7 = findViewById(R.id.textview7);
		textview8 = findViewById(R.id.textview8);
		textview9 = findViewById(R.id.textview9);
		textview10 = findViewById(R.id.textview10);
		textview11 = findViewById(R.id.textview11);
		textview12 = findViewById(R.id.textview12);
		textview13 = findViewById(R.id.textview13);
		textview14 = findViewById(R.id.textview14);
		textview15 = findViewById(R.id.textview15);
		textview16 = findViewById(R.id.textview16);
        data = getSharedPreferences("data", Activity.MODE_PRIVATE);
        highscoreData = _firebase.getReference("users").child(data.getString("id","")).child("moongamesData");
        winDialog = new AlertDialog.Builder(this);
		
		textview1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				_press(0);
			}
		});
		
		textview2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				_press(1);
			}
		});
		
		textview3.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				_press(2);
			}
		});
		
		textview4.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				_press(3);
			}
		});
		
		textview5.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				_press(4);
			}
		});
		
		textview6.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				_press(5);
			}
		});
		
		textview7.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				_press(6);
			}
		});
		
		textview8.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				_press(7);
			}
		});
		
		textview9.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				_press(8);
			}
		});
		
		textview10.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				_press(9);
			}
		});
		
		textview11.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				_press(10);
			}
		});
		
		textview12.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				_press(11);
			}
		});
		
		textview13.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				_press(12);
			}
		});
		
		textview14.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				_press(13);
			}
		});
		
		textview15.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				_press(14);
			}
		});
		
		textview16.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				_press(15);
			}
		});
	}
	private void initializeLogic() {
		Window w = this.getWindow();w.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);w.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            w.setStatusBarColor(Color.parseColor("#ba000d"));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			textview1.setElevation(10f);
			textview2.setElevation(10f);
			textview3.setElevation(10f);
			textview4.setElevation(10f);
			textview5.setElevation(10f);
			textview6.setElevation(10f);
			textview7.setElevation(10f);
			textview8.setElevation(10f);
			textview9.setElevation(10f);
			textview10.setElevation(10f);
			textview11.setElevation(10f);
			textview12.setElevation(10f);
			textview13.setElevation(10f);
			textview14.setElevation(10f);
			textview15.setElevation(10f);
			textview16.setElevation(10f);
			linear_game.setElevation(5f);
			int screenWidth = InputUtil.getDisplayWidthPixels(this);
			int boardWidth = screenWidth/4*3;
			int cellWidth = boardWidth/4;
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(cellWidth,cellWidth);
			textview1.setLayoutParams(params);
            textview2.setLayoutParams(params);
            textview3.setLayoutParams(params);
            textview4.setLayoutParams(params);
            textview5.setLayoutParams(params);
            textview6.setLayoutParams(params);
            textview7.setLayoutParams(params);
            textview8.setLayoutParams(params);
            textview9.setLayoutParams(params);
            textview10.setLayoutParams(params);
            textview11.setLayoutParams(params);
            textview12.setLayoutParams(params);
            textview13.setLayoutParams(params);
            textview14.setLayoutParams(params);
            textview15.setLayoutParams(params);
            textview16.setLayoutParams(params);
		}
		GradientDrawable linearLy = new GradientDrawable();  linearLy.setColor(0xFF263238);  linearLy.setCornerRadius(10);  linear_game.setBackground(linearLy);
        highscoreData.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot _dataSnapshot) {
                getData = new ArrayList<>();
                try {
                    GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
                    for (DataSnapshot _data : _dataSnapshot.getChildren()) {
                        HashMap<String, Object> _map = _data.getValue(_ind);
                        if(_data.getKey().equals("puzzle_15")) {
                            getData.add(_map);
                        }
                    }
                }
                catch (Exception _e) {
                    _e.printStackTrace();
                }
                if(getData.size()>0) {
                    if (getData.get(0).get("uid").toString().equals(data.getString("id", ""))) {
                        highscore = Integer.parseInt(getData.get(0).get("highscore").toString());
                        timerFormat.setTimeInMillis((long) (highscore));
                        timerFormat.set(Calendar.SECOND, (int) (highscore));
                        textview18.setText(getString(R.string.highscore_label) + new SimpleDateFormat("mm:ss").format(timerFormat.getTime()));
                    } else {
                        highscore = -1;
                        textview18.setText("");
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError _databaseError) {
            }
        });
		_startGame();
	}
	
	@Override
	public void onBackPressed() {
		finish();
	}
	private void _startGame () {
		distributionList.clear();
		puzzle.clear();
		randomPlace = 0;
		num1 = 0;
		num2 = 0;
		changed = 0;
		blankRow = 0;
		position = 0;
		while(true) {
			randomPlace = SketchwareUtil.getRandom(0, 15);
			if (!puzzle.contains(randomPlace)) {
				puzzle.add(randomPlace);
			}
			if (puzzle.size() == 16) {
				break;
			}
		}
		_isSolvable();
		if ((parity % 2) == (blankRow % 2)) {
			
		}
		else {
			position = 0;
			for(int _repeat235 = 0; _repeat235 < 16; _repeat235++) {
				if (puzzle.get((int) (position)) == 14) {
					a = position;
				}
				if (puzzle.get((int) (position)) == 15) {
					b = position;
				}
				position++;
			}
			puzzle.add((int)(a), 15d);
			puzzle.remove((int)(a + 1));
			puzzle.add((int)(b), 14d);
			puzzle.remove((int)(b + 1));
		}
		_setBackground();
		_setTextOfCells();
		timerFormat.setTimeInMillis((long)(0));
		seconds = 0;
		timecount = new TimerTask() {
			@Override
			public void run() {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						seconds++;
						timerFormat.setTimeInMillis((long)(seconds));
						timerFormat.set(Calendar.SECOND, seconds);
						textview17.setText(new SimpleDateFormat("mm:ss").format(timerFormat.getTime()));
					}
				});
			}
		};
		_timer.scheduleAtFixedRate(timecount, 1000, 1000);
	}
	
	
	private void _setBackground () {
		textview1.setBackgroundResource(R.drawable.tile15_bg);
		textview2.setBackgroundResource(R.drawable.tile15_bg);
		textview3.setBackgroundResource(R.drawable.tile15_bg);
		textview4.setBackgroundResource(R.drawable.tile15_bg);
		textview5.setBackgroundResource(R.drawable.tile15_bg);
		textview6.setBackgroundResource(R.drawable.tile15_bg);
		textview7.setBackgroundResource(R.drawable.tile15_bg);
		textview8.setBackgroundResource(R.drawable.tile15_bg);
		textview9.setBackgroundResource(R.drawable.tile15_bg);
		textview10.setBackgroundResource(R.drawable.tile15_bg);
		textview11.setBackgroundResource(R.drawable.tile15_bg);
		textview12.setBackgroundResource(R.drawable.tile15_bg);
		textview13.setBackgroundResource(R.drawable.tile15_bg);
		textview14.setBackgroundResource(R.drawable.tile15_bg);
		textview15.setBackgroundResource(R.drawable.tile15_bg);
		textview16.setBackgroundResource(R.drawable.tile15_bg);
		if (puzzle.get(0) == 0) {
			textview1.setBackgroundColor(Color.TRANSPARENT);
		}
		if (puzzle.get(1) == 0) {
			textview2.setBackgroundColor(Color.TRANSPARENT);
		}
		if (puzzle.get(2) == 0) {
			textview3.setBackgroundColor(Color.TRANSPARENT);
		}
		if (puzzle.get(3) == 0) {
			textview4.setBackgroundColor(Color.TRANSPARENT);
		}
		if (puzzle.get(4) == 0) {
			textview5.setBackgroundColor(Color.TRANSPARENT);
		}
		if (puzzle.get(5) == 0) {
			textview6.setBackgroundColor(Color.TRANSPARENT);
		}
		if (puzzle.get(6) == 0) {
			textview7.setBackgroundColor(Color.TRANSPARENT);
		}
		if (puzzle.get(7) == 0) {
			textview8.setBackgroundColor(Color.TRANSPARENT);
		}
		if (puzzle.get(8) == 0) {
			textview9.setBackgroundColor(Color.TRANSPARENT);
		}
		if (puzzle.get(9) == 0) {
			textview10.setBackgroundColor(Color.TRANSPARENT);
		}
		if (puzzle.get(10) == 0) {
			textview11.setBackgroundColor(Color.TRANSPARENT);
		}
		if (puzzle.get(11) == 0) {
			textview12.setBackgroundColor(Color.TRANSPARENT);
		}
		if (puzzle.get(12) == 0) {
			textview13.setBackgroundColor(Color.TRANSPARENT);
		}
		if (puzzle.get(13) == 0) {
			textview14.setBackgroundColor(Color.TRANSPARENT);
		}
		if (puzzle.get(14) == 0) {
			textview15.setBackgroundColor(Color.TRANSPARENT);
		}
		if (puzzle.get(15) == 0) {
			textview16.setBackgroundColor(Color.TRANSPARENT);
		}
	}
	
	
	private void _setTextOfCells () {
		textview1.setText(String.valueOf((long)(puzzle.get(0).doubleValue())));
		textview2.setText(String.valueOf((long)(puzzle.get(1).doubleValue())));
		textview3.setText(String.valueOf((long)(puzzle.get(2).doubleValue())));
		textview4.setText(String.valueOf((long)(puzzle.get(3).doubleValue())));
		textview5.setText(String.valueOf((long)(puzzle.get(4).doubleValue())));
		textview6.setText(String.valueOf((long)(puzzle.get(5).doubleValue())));
		textview7.setText(String.valueOf((long)(puzzle.get(6).doubleValue())));
		textview8.setText(String.valueOf((long)(puzzle.get(7).doubleValue())));
		textview9.setText(String.valueOf((long)(puzzle.get(8).doubleValue())));
		textview10.setText(String.valueOf((long)(puzzle.get(9).doubleValue())));
		textview11.setText(String.valueOf((long)(puzzle.get(10).doubleValue())));
		textview12.setText(String.valueOf((long)(puzzle.get(11).doubleValue())));
		textview13.setText(String.valueOf((long)(puzzle.get(12).doubleValue())));
		textview14.setText(String.valueOf((long)(puzzle.get(13).doubleValue())));
		textview15.setText(String.valueOf((long)(puzzle.get(14).doubleValue())));
		textview16.setText(String.valueOf((long)(puzzle.get(15).doubleValue())));
		if (textview1.getText().toString().equals("0")) {
			textview1.setText("");
		}
		if (textview2.getText().toString().equals("0")) {
			textview2.setText("");
		}
		if (textview3.getText().toString().equals("0")) {
			textview3.setText("");
		}
		if (textview4.getText().toString().equals("0")) {
			textview4.setText("");
		}
		if (textview5.getText().toString().equals("0")) {
			textview5.setText("");
		}
		if (textview6.getText().toString().equals("0")) {
			textview6.setText("");
		}
		if (textview7.getText().toString().equals("0")) {
			textview7.setText("");
		}
		if (textview8.getText().toString().equals("0")) {
			textview8.setText("");
		}
		if (textview9.getText().toString().equals("0")) {
			textview9.setText("");
		}
		if (textview10.getText().toString().equals("0")) {
			textview10.setText("");
		}
		if (textview11.getText().toString().equals("0")) {
			textview11.setText("");
		}
		if (textview12.getText().toString().equals("0")) {
			textview12.setText("");
		}
		if (textview13.getText().toString().equals("0")) {
			textview13.setText("");
		}
		if (textview14.getText().toString().equals("0")) {
			textview14.setText("");
		}
		if (textview15.getText().toString().equals("0")) {
			textview15.setText("");
		}
		if (textview16.getText().toString().equals("0")) {
			textview16.setText("");
		}
	}
	
	
	private void _press (final double _cell) {
		if (!(puzzle.get((int) (_cell)) == 0)) {
			num1 = _cell;
			if (changed == 0) {
				if (_cell > 0) {
					if (puzzle.get((int) (_cell - 1)) == 0) {
						if (!((_cell % 4) == 0)) {
							num2 = _cell - 1;
							_change(num1, num2);
						}
					}
				}
			}
			if (changed == 0) {
				if (_cell < 15) {
					if (puzzle.get((int) (_cell + 1)) == 0) {
						if (!(((_cell + 1) % 4) == 0)) {
							num2 = _cell + 1;
							_change(num1, num2);
						}
					}
				}
			}
			if (changed == 0) {
				if (_cell < 12) {
					if (puzzle.get((int) (_cell + 4)) == 0) {
						num2 = _cell + 4;
						_change(num1, num2);
					}
				}
			}
			if (changed == 0) {
				if (_cell > 3) {
					if (puzzle.get((int) (_cell - 4)) == 0) {
						num2 = _cell - 4;
						_change(num1, num2);
					}
				}
			}
		}
		changed = 0;
	}
	
	
	private void _change (final double _num1, final double _num2) {
		changed = 1;
		puzzle.add((int)(_num2), puzzle.get((int) (_num1)));
		puzzle.remove((int)(_num2 + 1));
		puzzle.add((int)(_num1), 0d);
		puzzle.remove((int)(_num1 + 1));
		_setBackground();
		_setTextOfCells();
		_check();
	}
	
	
	private void _check () {
		if (((((puzzle.get(0) == 1) && (puzzle.get(1) == 2)) && ((puzzle.get(2) == 3) && (puzzle.get(3) == 4))) && (((puzzle.get(4) == 5) && (puzzle.get(5) == 6)) && ((puzzle.get(6) == 7) && (puzzle.get(7) == 8)))) && ((((puzzle.get(8) == 9) && (puzzle.get(9) == 10)) && ((puzzle.get(10) == 11) && (puzzle.get(11) == 12))) && (((puzzle.get(12) == 13) && (puzzle.get(13) == 14)) && ((puzzle.get(14) == 15) && (puzzle.get(15) == 0))))) {
			_gameOver();
		}
	}
	
	
	private void _gameOver () {
		winDialog.setTitle(R.string.well_done_label);
		winDialog.setMessage(textview17.getText().toString());
		winDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface _dialog, int _which) {
				_startGame();
			}
		});
		winDialog.setCancelable(false);
		winDialog.create().show();
		timecount.cancel();
		score = seconds;
		if(score < highscore || highscore == -1)
		{
            highscore = score;
            dataMap = new HashMap<>();
            dataMap.put("uid", data.getString("id", ""));
            dataMap.put("highscore", String.valueOf((long)(highscore)));
            highscoreData.child("puzzle_15").updateChildren(dataMap);
            dataMap.clear();
			timerFormat.setTimeInMillis((long)(highscore));
			timerFormat.set(Calendar.SECOND, (int)(highscore));
			textview18.setText(getString(R.string.highscore_label) + new SimpleDateFormat("mm:ss").format(timerFormat.getTime()));
        }
	}
	
	
	private void _isSolvable () {
		parity = 0;
		row = 0;
		blankRow = 0;
		i = 0;
		while(true) {
			if (i == 16) {
				break;
			}
			if ((i % 4) == 0) {
				row++;
			}
			if (puzzle.get((int) (i)) == 0) {
				blankRow = row;
			}
			j = i + 1;
			_countParity();
			i++;
		}
	}
	
	
	private void _countParity () {
		while(true) {
			if (j == 16) {
				break;
			}
			if ((puzzle.get((int) (i)) > puzzle.get((int) (j))) && !(puzzle.get((int) (j)) == 0)) {
				parity++;
			}
			j++;
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

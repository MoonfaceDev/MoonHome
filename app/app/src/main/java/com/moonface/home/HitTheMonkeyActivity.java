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
import android.widget.Button;
import android.widget.TextView;
import android.widget.ImageView;
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
import android.view.View;
import android.graphics.Typeface;

public class HitTheMonkeyActivity extends AppCompatActivity {
	
	private Timer _timer = new Timer();
	private FirebaseDatabase _firebase = FirebaseDatabase.getInstance();
	
	private Toolbar _toolbar;
	private double object = 0;
	private double position = 0;
	private String object_view = "";
	private double current_points = 0;
	private double bananas = 0;
	private double general_points = 0;
	private double increment = 0;
	private double check_press = 0;
	private double time_length = 0;
	private HashMap<String, Object> dataMap = new HashMap<>();

	private ArrayList<HashMap<String, Object>> getData = new ArrayList<>();
	
	private LinearLayout linear_game;
	private Button button1;
	private TextView points;
	private ImageView babana_4;
	private ImageView banana_3;
	private ImageView banana_2;
	private ImageView banana_1;
	private LinearLayout linear5;
	private LinearLayout linear6;
	private TextView textview_1;
	private TextView textview_2;
	private TextView textview_3;
	private TextView textview_4;
	private TextView textview_5;
	private TextView textview_6;
	
	private TimerTask change;
	private TimerTask check;
	private DatabaseReference monkeyPoints;
	private SharedPreferences data;
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.hit_the_monkey);
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
		linear_game = findViewById(R.id.linear_game);
		button1 = findViewById(R.id.button1);
		points = findViewById(R.id.points);
		babana_4 = findViewById(R.id.babana_4);
		banana_3 = findViewById(R.id.banana_3);
		banana_2 = findViewById(R.id.banana_2);
		banana_1 = findViewById(R.id.banana_1);
		linear5 = findViewById(R.id.linear5);
		linear6 = findViewById(R.id.linear6);
		textview_1 = findViewById(R.id.textview_1);
		textview_2 = findViewById(R.id.textview_2);
		textview_3 = findViewById(R.id.textview_3);
		textview_4 = findViewById(R.id.textview_4);
		textview_5 = findViewById(R.id.textview_5);
		textview_6 = findViewById(R.id.textview_6);
		data = getSharedPreferences("data", Activity.MODE_PRIVATE);
		monkeyPoints = _firebase.getReference("users").child(data.getString("id","")).child("moongamesData");
		
		button1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				button1.setVisibility(View.INVISIBLE);
				textview_1.setEnabled(true);
				textview_2.setEnabled(true);
				textview_3.setEnabled(true);
				textview_4.setEnabled(true);
				textview_5.setEnabled(true);
				textview_6.setEnabled(true);
				object = 0;
				position = 0;
				current_points = 0;
				bananas = 3;
				increment = 1;
				banana_1.setImageResource(R.drawable.banana);
				banana_2.setImageResource(R.drawable.banana);
				banana_3.setImageResource(R.drawable.banana);
				time_length = 1000;
				change = new TimerTask() {
					@Override
					public void run() {
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								_session();
							}
						});
					}
				};
				_timer.schedule(change, 100);
			}
		});
		
		textview_1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (position == 1) {
					if (object == 1) {
						increment++;
						current_points = current_points + increment;
						general_points = general_points + increment;
						_updateData();
						points.setText(String.valueOf((long)(general_points)));
					}
					if (object == 2) {
						bananas--;
						if (bananas == 3) {
							babana_4.setImageResource(R.drawable.empty);
						}
						if (bananas == 2) {
							banana_3.setImageResource(R.drawable.empty);
						}
						if (bananas == 1) {
							banana_2.setImageResource(R.drawable.empty);
						}
						if (bananas == 0) {
							banana_1.setImageResource(R.drawable.empty);
							_game_over();
						}
					}
					if (object == 3) {
						if (bananas == 4) {
							bananas--;
						}
						if (bananas == 3) {
							babana_4.setImageResource(R.drawable.banana);
						}
						if (bananas == 2) {
							banana_3.setImageResource(R.drawable.banana);
						}
						if (bananas == 1) {
							banana_2.setImageResource(R.drawable.banana);
						}
						bananas++;
					}
				}
				else {
					bananas--;
					if (bananas == 3) {
						babana_4.setImageResource(R.drawable.empty);
					}
					if (bananas == 2) {
						banana_3.setImageResource(R.drawable.empty);
					}
					if (bananas == 1) {
						banana_2.setImageResource(R.drawable.empty);
					}
					if (bananas == 0) {
						banana_1.setImageResource(R.drawable.empty);
						_game_over();
					}
				}
				_enable_false();
			}
		});
		
		textview_2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (position == 2) {
					if (object == 1) {
						increment++;
						current_points = current_points + increment;
						general_points = general_points + increment;
						_updateData();
						points.setText(String.valueOf((long)(general_points)));
					}
					if (object == 2) {
						bananas--;
						if (bananas == 3) {
							babana_4.setImageResource(R.drawable.empty);
						}
						if (bananas == 2) {
							banana_3.setImageResource(R.drawable.empty);
						}
						if (bananas == 1) {
							banana_2.setImageResource(R.drawable.empty);
						}
						if (bananas == 0) {
							banana_1.setImageResource(R.drawable.empty);
							_game_over();
						}
					}
					if (object == 3) {
						if (bananas == 4) {
							bananas--;
						}
						if (bananas == 3) {
							babana_4.setImageResource(R.drawable.banana);
						}
						if (bananas == 2) {
							banana_3.setImageResource(R.drawable.banana);
						}
						if (bananas == 1) {
							banana_2.setImageResource(R.drawable.banana);
						}
						bananas++;
					}
				}
				else {
					bananas--;
					if (bananas == 3) {
						babana_4.setImageResource(R.drawable.empty);
					}
					if (bananas == 2) {
						banana_3.setImageResource(R.drawable.empty);
					}
					if (bananas == 1) {
						banana_2.setImageResource(R.drawable.empty);
					}
					if (bananas == 0) {
						banana_1.setImageResource(R.drawable.empty);
						_game_over();
					}
				}
				_enable_false();
			}
		});
		
		textview_3.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (position == 3) {
					if (object == 1) {
						increment++;
						current_points = current_points + increment;
						general_points = general_points + increment;
						_updateData();
						points.setText(String.valueOf((long)(general_points)));
					}
					if (object == 2) {
						bananas--;
						if (bananas == 3) {
							babana_4.setImageResource(R.drawable.empty);
						}
						if (bananas == 2) {
							banana_3.setImageResource(R.drawable.empty);
						}
						if (bananas == 1) {
							banana_2.setImageResource(R.drawable.empty);
						}
						if (bananas == 0) {
							banana_1.setImageResource(R.drawable.empty);
							_game_over();
						}
					}
					if (object == 3) {
						if (bananas == 4) {
							bananas--;
						}
						if (bananas == 3) {
							babana_4.setImageResource(R.drawable.banana);
						}
						if (bananas == 2) {
							banana_3.setImageResource(R.drawable.banana);
						}
						if (bananas == 1) {
							banana_2.setImageResource(R.drawable.banana);
						}
						bananas++;
					}
				}
				else {
					bananas--;
					if (bananas == 3) {
						babana_4.setImageResource(R.drawable.empty);
					}
					if (bananas == 2) {
						banana_3.setImageResource(R.drawable.empty);
					}
					if (bananas == 1) {
						banana_2.setImageResource(R.drawable.empty);
					}
					if (bananas == 0) {
						banana_1.setImageResource(R.drawable.empty);
						_game_over();
					}
				}
				_enable_false();
			}
		});
		
		textview_4.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (position == 4) {
					if (object == 1) {
						increment++;
						current_points = current_points + increment;
						general_points = general_points + increment;
						_updateData();
						points.setText(String.valueOf((long)(general_points)));
					}
					if (object == 2) {
						bananas--;
						if (bananas == 3) {
							babana_4.setImageResource(R.drawable.empty);
						}
						if (bananas == 2) {
							banana_3.setImageResource(R.drawable.empty);
						}
						if (bananas == 1) {
							banana_2.setImageResource(R.drawable.empty);
						}
						if (bananas == 0) {
							banana_1.setImageResource(R.drawable.empty);
							_game_over();
						}
					}
					if (object == 3) {
						if (bananas == 4) {
							bananas--;
						}
						if (bananas == 3) {
							babana_4.setImageResource(R.drawable.banana);
						}
						if (bananas == 2) {
							banana_3.setImageResource(R.drawable.banana);
						}
						if (bananas == 1) {
							banana_2.setImageResource(R.drawable.banana);
						}
						bananas++;
					}
				}
				else {
					bananas--;
					if (bananas == 3) {
						babana_4.setImageResource(R.drawable.empty);
					}
					if (bananas == 2) {
						banana_3.setImageResource(R.drawable.empty);
					}
					if (bananas == 1) {
						banana_2.setImageResource(R.drawable.empty);
					}
					if (bananas == 0) {
						banana_1.setImageResource(R.drawable.empty);
						_game_over();
					}
				}
				_enable_false();
			}
		});
		
		textview_5.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (position == 5) {
					if (object == 1) {
						increment++;
						current_points = current_points + increment;
						general_points = general_points + increment;
						_updateData();
						points.setText(String.valueOf((long)(general_points)));
					}
					if (object == 2) {
						bananas--;
						if (bananas == 3) {
							babana_4.setImageResource(R.drawable.empty);
						}
						if (bananas == 2) {
							banana_3.setImageResource(R.drawable.empty);
						}
						if (bananas == 1) {
							banana_2.setImageResource(R.drawable.empty);
						}
						if (bananas == 0) {
							banana_1.setImageResource(R.drawable.empty);
							_game_over();
						}
					}
					if (object == 3) {
						if (bananas == 4) {
							bananas--;
						}
						if (bananas == 3) {
							babana_4.setImageResource(R.drawable.banana);
						}
						if (bananas == 2) {
							banana_3.setImageResource(R.drawable.banana);
						}
						if (bananas == 1) {
							banana_2.setImageResource(R.drawable.banana);
						}
						bananas++;
					}
				}
				else {
					bananas--;
					if (bananas == 3) {
						babana_4.setImageResource(R.drawable.empty);
					}
					if (bananas == 2) {
						banana_3.setImageResource(R.drawable.empty);
					}
					if (bananas == 1) {
						banana_2.setImageResource(R.drawable.empty);
					}
					if (bananas == 0) {
						banana_1.setImageResource(R.drawable.empty);
						_game_over();
					}
				}
				_enable_false();
			}
		});
		
		textview_6.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (position == 6) {
					if (object == 1) {
						increment++;
						current_points = current_points + increment;
						general_points = general_points + increment;
						_updateData();
						points.setText(String.valueOf((long)(general_points)));
					}
					if (object == 2) {
						bananas--;
						if (bananas == 3) {
							babana_4.setImageResource(R.drawable.empty);
						}
						if (bananas == 2) {
							banana_3.setImageResource(R.drawable.empty);
						}
						if (bananas == 1) {
							banana_2.setImageResource(R.drawable.empty);
						}
						if (bananas == 0) {
							banana_1.setImageResource(R.drawable.empty);
							_game_over();
						}
					}
					if (object == 3) {
						if (bananas == 4) {
							bananas--;
						}
						if (bananas == 3) {
							babana_4.setImageResource(R.drawable.banana);
						}
						if (bananas == 2) {
							banana_3.setImageResource(R.drawable.banana);
						}
						if (bananas == 1) {
							banana_2.setImageResource(R.drawable.banana);
						}
						bananas++;
					}
				}
				else {
					bananas--;
					if (bananas == 3) {
						babana_4.setImageResource(R.drawable.empty);
					}
					if (bananas == 2) {
						banana_3.setImageResource(R.drawable.empty);
					}
					if (bananas == 1) {
						banana_2.setImageResource(R.drawable.empty);
					}
					if (bananas == 0) {
						banana_1.setImageResource(R.drawable.empty);
						_game_over();
					}
				}
				_enable_false();
			}
		});
	}
	private void initializeLogic() {
		Window w = this.getWindow();w.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);w.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS); w.setStatusBarColor(Color.parseColor("#ba000d"));
		android.graphics.drawable.GradientDrawable linearx = new android.graphics.drawable.GradientDrawable();  linearx.setColor(0xFF895514);  linearx.setCornerRadius(30); linear_game.setBackground(linearx);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { linear_game.setElevation(30f);}
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { 
			linear5.setElevation(10f);
			linear6.setElevation(10f);
		}
		button1.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/fluent_sans_regular.ttf"), 1);
		textview_1.setEnabled(false);
		textview_2.setEnabled(false);
		textview_3.setEnabled(false);
		textview_4.setEnabled(false);
		textview_5.setEnabled(false);
		textview_6.setEnabled(false);
		monkeyPoints.addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot _dataSnapshot) {
				getData = new ArrayList<>();
				try {
					GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
					for (DataSnapshot _data : _dataSnapshot.getChildren()) {
						HashMap<String, Object> _map = _data.getValue(_ind);
						if(_data.getKey().equals("hit_the_monkey")) {
                            getData.add(_map);
                        }
					}
				}
				catch (Exception _e) {
					_e.printStackTrace();
				}
				if(getData.size()>0) {
                    if (getData.get(0).get("uid").toString().equals(data.getString("id", ""))) {
                        general_points = Double.parseDouble(getData.get(0).get("points").toString());
                        points.setText(String.valueOf((long) (general_points)));
                    } else {
                        general_points = 0;
                        points.setText(String.valueOf((long) (general_points)));
                    }
                }
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
	private void _random_object (final double _obj) {
		if ((_obj > 0) && (_obj < 11)) {
			object = 1;
			object_view = "🐒";
			check = new TimerTask() {
				@Override
				public void run() {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							if (check_press == 0) {
								bananas--;
								if (bananas == 3) {
									babana_4.setImageResource(R.drawable.empty);
								}
								if (bananas == 2) {
									banana_3.setImageResource(R.drawable.empty);
								}
								if (bananas == 1) {
									banana_2.setImageResource(R.drawable.empty);
								}
								if (bananas == 0) {
									banana_1.setImageResource(R.drawable.empty);
									_game_over();
								}
							}
							check.cancel();
							check_press = 0;
						}
					});
				}
			};
			_timer.schedule(check, (int)(time_length));
		}
		if ((_obj > 10) && (_obj < 15)) {
			object = 2;
			object_view = "💣";
			check = new TimerTask() {
				@Override
				public void run() {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							check.cancel();
							check_press = 0;
						}
					});
				}
			};
			_timer.schedule(check, (int)(time_length - 10));
		}
		if ((_obj > 14) && (_obj < 16)) {
			object = 3;
			object_view = "🍌";
			check = new TimerTask() {
				@Override
				public void run() {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							if (check_press == 0) {
								bananas--;
								if (bananas == 3) {
									babana_4.setImageResource(R.drawable.empty);
								}
								if (bananas == 2) {
									banana_3.setImageResource(R.drawable.empty);
								}
								if (bananas == 1) {
									banana_2.setImageResource(R.drawable.empty);
								}
								if (bananas == 0) {
									banana_1.setImageResource(R.drawable.empty);
									_game_over();
								}
							}
							check.cancel();
							check_press = 0;
						}
					});
				}
			};
			_timer.schedule(check, (int)(time_length - 10));
		}
	}
	
	
	private void _setPosition (final double _position) {
		textview_1.setText("");
		textview_2.setText("");
		textview_3.setText("");
		textview_4.setText("");
		textview_5.setText("");
		textview_6.setText("");
		position = _position;
		if (_position == 1) {
			textview_1.setText(object_view);
		}
		if (_position == 2) {
			textview_2.setText(object_view);
		}
		if (_position == 3) {
			textview_3.setText(object_view);
		}
		if (_position == 4) {
			textview_4.setText(object_view);
		}
		if (_position == 5) {
			textview_5.setText(object_view);
		}
		if (_position == 6) {
			textview_6.setText(object_view);
		}
	}
	
	
	private void _game_over () {
		textview_1.setEnabled(false);
		textview_2.setEnabled(false);
		textview_3.setEnabled(false);
		textview_4.setEnabled(false);
		textview_5.setEnabled(false);
		textview_6.setEnabled(false);
		textview_1.setText("");
		textview_2.setText("");
		textview_3.setText("");
		textview_4.setText("");
		textview_5.setText("");
		textview_6.setText("");
		button1.setVisibility(View.VISIBLE);
		change.cancel();
	}
	
	
	private void _enable_false () {
		textview_1.setEnabled(false);
		textview_2.setEnabled(false);
		textview_3.setEnabled(false);
		textview_4.setEnabled(false);
		textview_5.setEnabled(false);
		textview_6.setEnabled(false);
		textview_1.setText("");
		textview_2.setText("");
		textview_3.setText("");
		textview_4.setText("");
		textview_5.setText("");
		textview_6.setText("");
		check_press = 1;
	}
	
	
	private void _session () {
		change.cancel();
		change = new TimerTask() {
			@Override
			public void run() {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						change.cancel();
						for(int _repeat14 = 0; _repeat14 < (int)(Math.round(time_length / 200)); _repeat14++) {
							time_length--;
						}
						_random_object(SketchwareUtil.getRandom(1, 15));
						_setPosition(SketchwareUtil.getRandom(1, 6));
						textview_1.setEnabled(true);
						textview_2.setEnabled(true);
						textview_3.setEnabled(true);
						textview_4.setEnabled(true);
						textview_5.setEnabled(true);
						textview_6.setEnabled(true);
						change = new TimerTask() {
							@Override
							public void run() {
								runOnUiThread(new Runnable() {
									@Override
									public void run() {
										_session();
									}
								});
							}
						};
						_timer.schedule(change, (int)(time_length));
					}
				});
			}
		};
		_timer.schedule(change, 100);
	}
	
	
	private void _updateData () {
		dataMap = new HashMap<>();
		dataMap.put("uid", data.getString("id", ""));
		dataMap.put("points", String.valueOf((long)(general_points)));
		monkeyPoints.child("hit_the_monkey").updateChildren(dataMap);
		dataMap.clear();
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

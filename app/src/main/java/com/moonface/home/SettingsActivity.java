package com.moonface.home;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.media.AudioManager;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.SparseBooleanArray;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Random;

public class SettingsActivity extends AppCompatActivity {
	
	private FirebaseDatabase _firebase = FirebaseDatabase.getInstance();
	
	private Toolbar _toolbar;
	private String language_string = "";
	private String server_version = "";
	private String device_version = "";
	private HashMap<String, Object> update_background = new HashMap<>();
	private AudioManager audioManager;
	private BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver(){
		@Override
		public void onReceive(Context ctxt, Intent intent) {
			int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
			textview11.setText(String.valueOf((level)).concat("%"));
			_setBatIcon(level);
		}
	};
    private BroadcastReceiver mVolInfoReceiver = new BroadcastReceiver(){
        @Override
        public void onReceive(Context ctxt, Intent intent) {
            seekbar1.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
            seekbar2.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_RING));
            seekbar3.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_ALARM) - 1);
        }
    };
    private BroadcastReceiver mDndInfoReceiver = new BroadcastReceiver(){
        @Override
        public void onReceive(Context ctxt, Intent intent) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                NotificationManager n = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                if (n != null && n.getCurrentInterruptionFilter() != NotificationManager.INTERRUPTION_FILTER_ALL) {
                    seekbar2.setEnabled(false);
                } else {
                    seekbar2.setEnabled(true);
                }
            }
        }
    };
	
	private ArrayList<String> background_list = new ArrayList<>();

	private Spinner background;
	private LinearLayout linear_moonface_version;
	private TextView textview9;
	private ImageView imageview9;
	private LinearLayout linear_model;
	private TextView model;
	private LinearLayout linear_device_language;
	private TextView device_language;
	private LinearLayout linear_android_version;
	private TextView android_version;
	private LinearLayout linear_screen_size;
	private TextView screen_size;
	private LinearLayout linear_battery;
	private ImageView imageview10;
	private TextView textview11;
	private SeekBar seekbar1;
	private SeekBar seekbar2;
	private SeekBar seekbar3;
	
	private DatabaseReference info = _firebase.getReference("app_info");
	private Intent update_int = new Intent();
	private SharedPreferences data;
	private DatabaseReference users_data = _firebase.getReference("users");
	private ObjectAnimator reloadAnimation = new ObjectAnimator();
	private AlertDialog.Builder descriptionDialog;
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.settings);
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
		audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		background = findViewById(R.id.background);
		linear_moonface_version = findViewById(R.id.linear_moonface_version);
		textview9 = findViewById(R.id.textview9);
		imageview9 = findViewById(R.id.imageview9);
		linear_model = findViewById(R.id.linear_model);
		model = findViewById(R.id.model);
		linear_device_language = findViewById(R.id.linear_device_language);
		device_language = findViewById(R.id.device_language);
		linear_android_version = findViewById(R.id.linear_android_version);
		android_version = findViewById(R.id.android_version);
		linear_screen_size = findViewById(R.id.linear_screen_size);
		screen_size = findViewById(R.id.screen_size);
		linear_battery = findViewById(R.id.linear_battery);
		imageview10 = findViewById(R.id.imageview10);
		textview11 = findViewById(R.id.textview11);
		seekbar1 = findViewById(R.id.seekbar1);
		seekbar2 = findViewById(R.id.seekbar2);
		seekbar3 = findViewById(R.id.seekbar3);
		data = getSharedPreferences("data", Activity.MODE_PRIVATE);
		descriptionDialog = new AlertDialog.Builder(this);

        device_version = BuildConfig.VERSION_NAME;

        background.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> _param1, View _param2, int _param3, long _param4) {
				data.edit().putString("background", String.valueOf((long)(_param3))).commit();
				update_background = new HashMap<>();
				update_background.put("background", String.valueOf((long)(_param3)));
				users_data.child(data.getString("id", "")).updateChildren(update_background);
				update_background.clear();
			}
			
			@Override
			public void onNothingSelected(AdapterView<?> _param1) {
				
			}
		});
		linear_moonface_version.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				info.addListenerForSingleValueEvent(new ValueEventListener() {
					@Override
					public void onDataChange(DataSnapshot _dataSnapshot) {
						HashMap<Object, String> infoMap = new HashMap<>();
						try {
							infoMap = (HashMap<Object, String>) _dataSnapshot.getValue();
						}
						catch (Exception _e) {
							_e.printStackTrace();
						}
						server_version = infoMap.get("last version").toString();
						if (server_version.equals(device_version)) {
							textview9.setText(getString(R.string.updated_to_label).concat(device_version));
							SketchwareUtil.showMessage(getApplicationContext(), getString(R.string.moonace_up_to_date_message));
						}
						else {
							textview9.setText(getString(R.string.update_label).concat(server_version.concat(getString(R.string.is_available_label))));
							update_int.setAction(Intent.ACTION_VIEW);
							update_int.setData(Uri.parse(getString(R.string.download_link)));
							startActivity(update_int);
						}
					}
					@Override
					public void onCancelled(DatabaseError _databaseError) {
					}
				});
				reloadAnimation.setTarget(imageview9);
				reloadAnimation.setPropertyName("rotation");
				reloadAnimation.setFloatValues((float)(0), (float)(359));
				reloadAnimation.setDuration(500);
				reloadAnimation.start();
			}
		});
		
		textview9.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				info.addListenerForSingleValueEvent(new ValueEventListener() {
					@Override
					public void onDataChange(DataSnapshot _dataSnapshot) {
						HashMap<Object, String> infoMap = new HashMap<>();
						try {
							infoMap = (HashMap<Object, String>) _dataSnapshot.getValue();
						}
						catch (Exception _e) {
							_e.printStackTrace();
						}
						server_version = infoMap.get("last version").toString();
						if (server_version.equals(device_version)) {
							textview9.setText(getString(R.string.updated_to_label).concat(device_version));
                            SketchwareUtil.showMessage(getApplicationContext(), getString(R.string.moonace_up_to_date_message));
						}
						else {
                            textview9.setText(getString(R.string.update_label).concat(server_version.concat(getString(R.string.is_available_label))));
							update_int.setAction(Intent.ACTION_VIEW);
                            update_int.setData(Uri.parse(getString(R.string.download_link)));
							startActivity(update_int);
						}
					}
					@Override
					public void onCancelled(DatabaseError _databaseError) {
					}
				});
				reloadAnimation.setTarget(imageview9);
				reloadAnimation.setPropertyName("rotation");
				reloadAnimation.setFloatValues((float)(0), (float)(359));
				reloadAnimation.setDuration(500);
				reloadAnimation.start();
			}
		});
		
		imageview9.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				info.addListenerForSingleValueEvent(new ValueEventListener() {
					@Override
					public void onDataChange(DataSnapshot _dataSnapshot) {
						HashMap<Object, String> infoMap = new HashMap<>();
						try {
							infoMap = (HashMap<Object, String>) _dataSnapshot.getValue();
						}
						catch (Exception _e) {
							_e.printStackTrace();
						}
						server_version = infoMap.get("last version").toString();
						if (server_version.equals(device_version)) {
							textview9.setText(getString(R.string.updated_to_label).concat(device_version));
                            SketchwareUtil.showMessage(getApplicationContext(), getString(R.string.moonace_up_to_date_message));
						}
						else {
                            textview9.setText(getString(R.string.update_label).concat(server_version.concat(getString(R.string.is_available_label))));
							update_int.setAction(Intent.ACTION_VIEW);
                            update_int.setData(Uri.parse(getString(R.string.download_link)));
							startActivity(update_int);
						}
					}
					@Override
					public void onCancelled(DatabaseError _databaseError) {
					}
				});
				reloadAnimation.setTarget(imageview9);
				reloadAnimation.setPropertyName("rotation");
				reloadAnimation.setFloatValues((float)(0), (float)(359));
				reloadAnimation.setDuration(500);
				reloadAnimation.start();
			}
		});

		linear_model.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				descriptionDialog.setTitle(R.string.device_model_label);
				descriptionDialog.setMessage(model.getText().toString());
				descriptionDialog.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface _dialog, int _which) {
						
					}
				});
				descriptionDialog.create().show();
			}
		});

		linear_device_language.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				descriptionDialog.setTitle(R.string.device_language_label);
				descriptionDialog.setMessage(device_language.getText().toString());
				descriptionDialog.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface _dialog, int _which) {
						
					}
				});
				descriptionDialog.create().show();
			}
		});

		linear_android_version.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				descriptionDialog.setTitle(R.string.android_version_label);
				descriptionDialog.setMessage(android_version.getText().toString());
				descriptionDialog.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface _dialog, int _which) {
						
					}
				});
				descriptionDialog.create().show();
			}
		});

		linear_screen_size.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				descriptionDialog.setTitle(R.string.screen_size_label);
				descriptionDialog.setMessage(screen_size.getText().toString());
				descriptionDialog.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface _dialog, int _which) {
						
					}
				});
				descriptionDialog.create().show();
			}
		});

		linear_battery.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				descriptionDialog.setTitle(R.string.battery_label);
				descriptionDialog.setMessage(textview11.getText().toString());
				descriptionDialog.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface _dialog, int _which) {
						
					}
				});
				descriptionDialog.create().show();
			}
		});
		seekbar1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged (SeekBar _param1, int _param2, boolean _param3) {
				final int _progressValue = _param2;
                if (_param3) {
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, _progressValue, 0);
				}
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar _param1) {
				
			}
			
			@Override
			public void onStopTrackingTouch(SeekBar _param2) {
				
			}
		});
		
		seekbar2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged (SeekBar _param1, int _param2, boolean _param3) {
				final int _progressValue = _param2;
                if (_param3) {
                    audioManager.setStreamVolume(AudioManager.STREAM_RING, _progressValue, 0);
                }
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar _param1) {
				
			}
			
			@Override
			public void onStopTrackingTouch(SeekBar _param2) {
				
			}
		});
		
		seekbar3.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged (SeekBar _param1, int _param2, boolean _param3) {
				final int _progressValue = _param2;
                if (_param3) {
                    audioManager.setStreamVolume(AudioManager.STREAM_ALARM, _progressValue + 1, 0);
                }
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar _param1) {
				
			}
			
			@Override
			public void onStopTrackingTouch(SeekBar _param2) {
				
			}
		});
	}
	private void initializeLogic() {
        background_list.add(getString(R.string.default_background));
        background_list.add(getString(R.string.deer_background));
        background_list.add(getString(R.string.eiffel_tower));
        background_list.add(getString(R.string.forest_background));
        background_list.add(getString(R.string.milky_way));
        background_list.add(getString(R.string.new_york_background));
        background_list.add(getString(R.string.pyramids_background));
        background_list.add(getString(R.string.sunrise_background));
        background_list.add(getString(R.string.village_background));
        model.setText(Build.MANUFACTURER.concat(" ".concat(Build.MODEL)));
        language_string = Locale.getDefault().getDisplayLanguage();
        android_version.setText(Build.VERSION.RELEASE);
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getRealSize(size);
        int width = size.x;
        int height = size.y;
        background.setAdapter(new ArrayAdapter<>(getBaseContext(), android.R.layout.simple_spinner_dropdown_item, background_list));
        device_language.setText(language_string);
        screen_size.setText(String.valueOf((long) (width)).concat("x".concat(String.valueOf((long) (height)))));
        background.setSelection((int) (Double.parseDouble(data.getString("background", ""))));
        info.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot _dataSnapshot) {
                HashMap<Object, String> infoMap = new HashMap<>();
                try {
                    infoMap = (HashMap<Object, String>) _dataSnapshot.getValue();
                } catch (Exception _e) {
                    _e.printStackTrace();
                }
                server_version = infoMap.get("last version").toString();
                if (server_version.equals(device_version)) {
                    textview9.setText(getString(R.string.updated_to_label).concat(device_version));
                } else {
                    textview9.setText(getString(R.string.update_label).concat(server_version.concat(getString(R.string.is_available_label))));
                }
            }

            @Override
            public void onCancelled(DatabaseError _databaseError) {
            }
        });
        registerReceiver(mBatInfoReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        registerReceiver(mVolInfoReceiver, new IntentFilter("android.media.VOLUME_CHANGED_ACTION"));
        registerReceiver(mDndInfoReceiver, new IntentFilter("android.app.action.INTERRUPTION_FILTER_CHANGED"));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            NotificationManager n = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            if (n != null && n.getCurrentInterruptionFilter() != NotificationManager.INTERRUPTION_FILTER_ALL) {
                seekbar2.setEnabled(false);
            } else {
                seekbar2.setEnabled(true);
            }
        }
        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        seekbar1.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
        seekbar1.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
        seekbar2.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_RING));
        seekbar2.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_RING));
        seekbar3.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_ALARM) - 1);
        seekbar3.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_ALARM) - 1);
    }
	@Override
	public void onBackPressed() {
		finish();
	}
	private void _setBatIcon (final double _batteryPrecentage) {
		if (_batteryPrecentage > 94) {
			imageview10.setImageResource(R.drawable.ic_battery_full_grey);
		}
		if ((_batteryPrecentage > 84) && (_batteryPrecentage < 95)) {
			imageview10.setImageResource(R.drawable.ic_battery_90_grey);
		}
		if ((_batteryPrecentage > 74) && (_batteryPrecentage < 85)) {
			imageview10.setImageResource(R.drawable.ic_battery_80_grey);
		}
		if ((_batteryPrecentage > 59) && (_batteryPrecentage < 75)) {
			imageview10.setImageResource(R.drawable.ic_battery_60_grey);
		}
		if ((_batteryPrecentage > 44) && (_batteryPrecentage < 60)) {
			imageview10.setImageResource(R.drawable.ic_battery_50_grey);
		}
		if ((_batteryPrecentage > 29) && (_batteryPrecentage < 45)) {
			imageview10.setImageResource(R.drawable.ic_battery_30_grey);
		}
		if ((_batteryPrecentage > 14) && (_batteryPrecentage < 30)) {
			imageview10.setImageResource(R.drawable.ic_battery_10_grey);
		}
		if (_batteryPrecentage < 15) {
			imageview10.setImageResource(R.drawable.ic_battery_alert_grey);
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

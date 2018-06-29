package com.moonface.home;

import android.app.*;
import android.content.pm.PackageManager;
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
import java.util.ArrayList;
import java.util.HashMap;
import android.widget.LinearLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ScrollView;
import android.widget.Button;
import android.app.Activity;
import android.content.SharedPreferences;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ChildEventListener;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.graphics.Typeface;

public class AboutActivity extends AppCompatActivity {
	
	private FirebaseDatabase _firebase = FirebaseDatabase.getInstance();
	
	private Toolbar _toolbar;
	private String server_version = "";
	private String device_version = "";

	private TextView textview2;
	private TextView textview3;
	private Button button1;
	private Button button2;
	private TextView textview17;
	private TextView textview20;
	private TextView textview23;
	private TextView textview32;
	private TextView textview35;
	private TextView textview38;
	private TextView textview41;
	private TextView textview4;
	private TextView whats_new_textview;
	
	private DatabaseReference info = _firebase.getReference("app_info");
	private Intent update_int = new Intent();
	private Intent contact_us = new Intent();
	private Intent link = new Intent();
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.about);
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
        textview2 = findViewById(R.id.textview2);
        textview3 = findViewById(R.id.textview3);
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        textview17 = findViewById(R.id.textview17);
        textview20 = findViewById(R.id.textview20);
        textview23 = findViewById(R.id.textview23);
        textview32 = findViewById(R.id.textview32);
        textview35 = findViewById(R.id.textview35);
        textview38 = findViewById(R.id.textview38);
        textview41 = findViewById(R.id.textview41);
        textview4 = findViewById(R.id.textview4);
        whats_new_textview = findViewById(R.id.whats_new_textview);

        device_version = BuildConfig.VERSION_NAME;

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                info.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot _dataSnapshot) {
                        HashMap<String, Object> infoMap = new HashMap<>();
                        try {
                            infoMap = (HashMap<String, Object>) _dataSnapshot.getValue();
                        } catch (Exception _e) {
                            _e.printStackTrace();
                        }
                        server_version = infoMap.get("last version").toString();
                        if (server_version.equals(device_version)) {
                            SketchwareUtil.showMessage(getApplicationContext(), getString(R.string.updated_notice));
                        } else {
                            update_int.setAction(Intent.ACTION_VIEW);
                            update_int.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.moonface.home"));
                            startActivity(update_int);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError _databaseError) {
                    }
                });
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                contact_us.setAction(Intent.ACTION_VIEW);
                contact_us.setData(Uri.parse("mailto:moonfaceapps@gmail.com"));
                startActivity(contact_us);
            }
        });

        textview17.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _linkTo(textview17.getText().toString());
            }
        });

        textview20.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _linkTo(textview20.getText().toString());
            }
        });

        textview23.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _linkTo(textview23.getText().toString());
            }
        });

        textview32.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _linkTo(textview32.getText().toString());
            }
        });

        textview35.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _linkTo(textview35.getText().toString());
            }
        });

        textview38.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _linkTo(textview38.getText().toString());
            }
        });

        textview41.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _linkTo(textview41.getText().toString());
            }
        });

    }
	private void initializeLogic() {
		textview2.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/fluent_sans_regular.ttf"), 1);
		textview4.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/fluent_sans_regular.ttf"), 1);
		textview3.setText("MoonHome's Version: ".concat(device_version));
		info.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap<String, Object> infoMap = new HashMap<>();
                try {
                    infoMap = (HashMap<String, Object>) dataSnapshot.getValue();
                } catch (Exception _e) {
                    _e.printStackTrace();
                }
                whats_new_textview.setText(infoMap.get("what's new").toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
	}
	
	@Override
	public void onBackPressed() {
		finish();
	}
	private void _linkTo (final String _url) {
		link.setAction(Intent.ACTION_VIEW);
		link.setData(Uri.parse(_url));
		startActivity(link);
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

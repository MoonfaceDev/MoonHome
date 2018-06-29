package com.moonface.home;

import android.app.*;
import android.os.*;
import android.support.design.widget.CollapsingToolbarLayout;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ScrollView;
import android.widget.ImageView;
import android.widget.Button;
import java.util.Timer;
import java.util.TimerTask;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.graphics.Typeface;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

public class GameDescriptionActivity extends AppCompatActivity {
	
	private Timer _timer = new Timer();
	private FirebaseDatabase _firebase = FirebaseDatabase.getInstance();
	private SharedPreferences data;

	private Toolbar _toolbar;
	private String packageName = "";
	private CollapsingToolbarLayout collapsingToolbarLayout;
	private double readState = 0;
    private int ratingIndex;
    private DecimalFormat ratingFormat;

    private ArrayList<HashMap<String, Object>> ratingMap = new ArrayList<>();
    private HashMap<String, Object> userRating = new HashMap<>();
    private HashMap<String, Object> userDownload = new HashMap<>();
	
	private LinearLayout linear5;
	private LinearLayout linear6;
	private TextView textview7;
	private LinearLayout linear11;
	private ImageView imageview2;
	private TextView title;
	private TextView author;
	private Button button1;
	private Button button2;
	private Button button3;
	private TextView short_description;
	private TextView full_description;
	private TextView averageRating;
	private TextView downloads;
	private RatingBar ratingbar;
	private LinearLayout linear_rating;
	private ImageView screenshot1;
    private ImageView screenshot2;
    private ImageView screenshot3;
    private ImageView screenshot4;
    private ImageView screenshot5;
	
	private TimerTask check;
	private Intent download = new Intent();
	private DatabaseReference _gameData;
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.game_description);
		initialize();
		initializeLogic();
	}
	
	private void initialize() {

        _toolbar = findViewById(R.id._toolbar);
        setSupportActionBar(_toolbar);
        collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            _toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View _v) {
                    onBackPressed();
                }
            });
        }
        ratingFormat = new DecimalFormat("#");
        linear5 = findViewById(R.id.linear5);
        linear6 = findViewById(R.id.linear6);
        textview7 = findViewById(R.id.textview7);
        linear11 = findViewById(R.id.linear11);
        imageview2 = findViewById(R.id.imageview2);
        title = findViewById(R.id.title);
        author = findViewById(R.id.author);
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        short_description = findViewById(R.id.short_description);
        full_description = findViewById(R.id.full_description);
        averageRating = findViewById(R.id.averageRating);
        downloads = findViewById(R.id.downloads);
        ratingbar = findViewById(R.id.ratingbar);
        linear_rating = findViewById(R.id.linear_rating);
        screenshot1 = findViewById(R.id.screenshot1);
        screenshot2 = findViewById(R.id.screenshot2);
        screenshot3 = findViewById(R.id.screenshot3);
        screenshot4 = findViewById(R.id.screenshot4);
        screenshot5 = findViewById(R.id.screenshot5);

        data = getSharedPreferences("data", Activity.MODE_PRIVATE);
        _gameData = _firebase.getReference("games").child(getIntent().getStringExtra("key"));

        textview7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (readState == 0) {
                    linear11.setVisibility(View.VISIBLE);
                    textview7.setText(getString(R.string.read_less));
                    readState = 1;
                } else {
                    linear11.setVisibility(View.GONE);
                    textview7.setText(getString(R.string.read_more));
                    readState = 0;
                }
            }
        });

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri packageURI = Uri.parse("package:".concat(packageName));
                Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, packageURI);
                startActivity(uninstallIntent);
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent launchIntent = getPackageManager().getLaunchIntentForPackage(packageName);
                {
                    startActivity(launchIntent);
                }
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                download.setAction(Intent.ACTION_VIEW);
                download.setData(Uri.parse(getIntent().getStringExtra("download link")));
                startActivity(download);
                userDownload = new HashMap<>();
                userDownload.put("uid", data.getString("id",""));
                _gameData.child("downloads").child(data.getString("id","")).updateChildren(userDownload);
                userDownload.clear();
            }
        });
        ratingbar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if(fromUser) {
					userRating = new HashMap<>();
					userRating.put("uid", data.getString("id", ""));
					userRating.put("val", ratingFormat.format(rating));
					_gameData.child("ratings").child(data.getString("id", "")).updateChildren(userRating);
				}
            }
        });
	}
	private void initializeLogic() {
		Window w = this.getWindow();w.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);w.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS); w.setStatusBarColor(Color.parseColor("#4199c6"));
		linear5.setVisibility(View.GONE);
		linear6.setVisibility(View.GONE);
		linear11.setVisibility(View.GONE);
		packageName = getIntent().getStringExtra("package name");
		title.setText(getIntent().getStringExtra("title"));
		author.setText(getIntent().getStringExtra("author"));
		short_description.setText(getIntent().getStringExtra("short description"));
		full_description.setText(getIntent().getStringExtra("full description"));
		downloads.setText(String.valueOf(formatDownloads(Integer.parseInt(getIntent().getStringExtra("downloads")))));
		if(!(getIntent().getStringExtra("ratings").equals("NaN"))) {
            averageRating.setText(getIntent().getStringExtra("ratings"));
        }
        else{
		    averageRating.setText("0");
		    linear_rating.setVisibility(View.GONE);
        }
		imageview2.setImageBitmap(getDecodedImage(getIntent().getStringExtra("icon")));
        screenshot1.setImageBitmap(getDecodedImage(getIntent().getStringExtra("screenshot1")));
        screenshot2.setImageBitmap(getDecodedImage(getIntent().getStringExtra("screenshot2")));
        screenshot3.setImageBitmap(getDecodedImage(getIntent().getStringExtra("screenshot3")));
        screenshot4.setImageBitmap(getDecodedImage(getIntent().getStringExtra("screenshot4")));
        screenshot5.setImageBitmap(getDecodedImage(getIntent().getStringExtra("screenshot5")));
		check = new TimerTask() {
			@Override
			public void run() {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						android.content.pm.PackageManager pm = getPackageManager();
						try {
							pm.getPackageInfo(packageName , android.content.pm.PackageManager.GET_ACTIVITIES);
                            linear5.setVisibility(View.VISIBLE);
							return;
						} catch (android.content.pm.PackageManager.NameNotFoundException e) {
                            linear6.setVisibility(View.VISIBLE);
						}
					}
				});
			}
		};
		_timer.scheduleAtFixedRate(check, 10, 10);
        setUserRating();
	}
	public void setUserRating(){
	    _gameData.child("ratings").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot _dataSnapshot) {
                ratingMap = new ArrayList<>();
                try {
                    GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
                    for (DataSnapshot _data : _dataSnapshot.getChildren()) {
                        HashMap<String, Object> _map = _data.getValue(_ind);
                        ratingMap.add(_map);
                    }
                    for(int i=0;i<ratingMap.size();i++){
                        if(ratingMap.get(i).get("uid").toString() == data.getString("id","")){
                            ratingIndex = i;
                        }
                    }
                    if(ratingMap.size() > 0) {
                        ratingbar.setRating(Integer.parseInt(ratingMap.get(ratingIndex).get("val").toString()));
                    }
                    else{
                        ratingbar.setRating(0);
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
	
	@Override
	public void onBackPressed() {
		finish();
	}
    public String formatDownloads(int downloadsCount){
        if(downloadsCount<1000){
            return String.valueOf(downloadsCount);
        }
        else if(downloadsCount<1000000){
            return String.valueOf(Math.round(downloadsCount/1000)) + "K";
        }
        else if(downloadsCount<1000000000){
            return String.valueOf(Math.round(downloadsCount/1000000)) + "M";
        }
        else{
            return String.valueOf(Math.round(downloadsCount/1000000000)) + "B";
        }
    }
    public Bitmap getDecodedImage(String _imageString){
        if(!(_imageString.equals("0"))) {
            byte[] imageBytes = android.util.Base64.decode(_imageString, android.util.Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        }
        else{
            return null;
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

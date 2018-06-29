package com.moonface.home;

import android.os.*;
import android.view.*;
import android.widget.*;
import android.content.*;
import android.graphics.*;
import android.text.*;
import android.util.*;

import java.util.*;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import java.util.ArrayList;
import java.util.HashMap;
import android.widget.LinearLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.HorizontalScrollView;
import android.widget.ListView;
import android.widget.BaseAdapter;
import android.content.Intent;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.GenericTypeIndicator;
import com.moonface.Util.InputUtil;

import android.view.View;
import android.widget.AdapterView;
import static com.moonface.Util.AnimationsUtil.fadeIn;
import static com.moonface.Util.AnimationsUtil.fadeOut;
import static com.moonface.Util.DrawableUtil.roundedBackground;
import static com.moonface.Util.ListUtil.filterMapList;


public class MoongamesActivity extends AppCompatActivity {
	
	private FirebaseDatabase _firebase = FirebaseDatabase.getInstance();
	
	private Toolbar _toolbar;
	private String packageName = "";
	private String imageString = "";
	private double position = 0;
	private int downloads;
	private double ratingAverage;
	private double rating_sum;
	private Boolean complete1;
	private Boolean complete2;
	private String before;

	private ArrayList<HashMap<String, Object>> dataMap = new ArrayList<>();
	private ArrayList<HashMap<String, Object>> ratingMap = new ArrayList<>();
    private ArrayList<HashMap<String, Object>> downloadsMap = new ArrayList<>();
    private ArrayList<String> key_list = new ArrayList<>();

    private HorizontalScrollView hscroll1;
	private LinearLayout linear4;
	private LinearLayout linear5;
	private LinearLayout linear_1;
	private LinearLayout linear_2;
	private LinearLayout linear_3;
	private LinearLayout linear_4;
	private ImageView imageview_1;
	private TextView textview_1;
	private ImageView imageview_2;
	private TextView textview_2;
	private ImageView imageview_3;
	private TextView textview_3;
	private ImageView imageview_4;
	private TextView textview_4;
	private ListView listview1;
	private EditText searchbox;
	private ImageView reset_search;
	
	private Intent builtin_game_int = new Intent();
	private Intent download_int = new Intent();
	private Intent recommend_game = new Intent();
	private DatabaseReference database = _firebase.getReference("games");
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.moongames);
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
        hscroll1 = findViewById(R.id.hscroll1);
        linear4 = findViewById(R.id.linear4);
        linear5 = findViewById(R.id.linear5);
        linear_1 = findViewById(R.id.linear_1);
        linear_2 = findViewById(R.id.linear_2);
        linear_3 = findViewById(R.id.linear_3);
        linear_4 = findViewById(R.id.linear_4);
        imageview_1 = findViewById(R.id.imageview_1);
        textview_1 = findViewById(R.id.textview_1);
        imageview_2 = findViewById(R.id.imageview_2);
        textview_2 = findViewById(R.id.textview_2);
        imageview_3 = findViewById(R.id.imageview_3);
        textview_3 = findViewById(R.id.textview_3);
        imageview_4 = findViewById(R.id.imageview_4);
        textview_4 = findViewById(R.id.textview_4);
        listview1 = findViewById(R.id.listview1);
        searchbox = findViewById(R.id.searchbox);
        reset_search = findViewById(R.id.reset_search);

        imageview_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builtin_game_int.setClass(getApplicationContext(), TicTacToeActivity.class);
                startActivity(builtin_game_int);
            }
        });

        textview_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builtin_game_int.setClass(getApplicationContext(), TicTacToeActivity.class);
                startActivity(builtin_game_int);
            }
        });

        imageview_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builtin_game_int.setClass(getApplicationContext(), HitTheMonkeyActivity.class);
                startActivity(builtin_game_int);
            }
        });

        textview_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builtin_game_int.setClass(getApplicationContext(), HitTheMonkeyActivity.class);
                startActivity(builtin_game_int);
            }
        });

        imageview_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builtin_game_int.setClass(getApplicationContext(), DiceActivity.class);
                startActivity(builtin_game_int);
            }
        });

        textview_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builtin_game_int.setClass(getApplicationContext(), DiceActivity.class);
                startActivity(builtin_game_int);
            }
        });

        imageview_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builtin_game_int.setClass(getApplicationContext(), Puzzle15Activity.class);
                startActivity(builtin_game_int);
            }
        });

        textview_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builtin_game_int.setClass(getApplicationContext(), Puzzle15Activity.class);
                startActivity(builtin_game_int);
            }
        });

        listview1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> _param1, View _param2, int _param3, long _param4) {
                final int _position = _param3;
                getDownloads(_position);
                getRating(_position);
                launchPreview(_position);
            }
        });
        searchbox.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence _param1, int _param2, int _param3, int _param4) {
                final String _charSeq = _param1.toString();
                    if (searchbox.getText().toString().length() != 0) {
                        if(before.length() == 0){
                            fadeIn(reset_search, 200);
                        }
                    } else {
                        fadeOut(reset_search, 200);
                    }
            }

            @Override
            public void beforeTextChanged(CharSequence _param1, int _param2, int _param3, int _param4) {
                before = _param1.toString();
            }

            @Override
            public void afterTextChanged(Editable _param1) {
            }
        });
        reset_search.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                searchbox.setText("");
                listview1.setAdapter(new Listview1Adapter(filterMapList(dataMap, "title", searchbox.getText())));
                InputUtil.hideKeyboard(getApplicationContext(), MoongamesActivity.this);
            }
        });
    }
	private void initializeLogic() {
		Window w = this.getWindow();w.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);w.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS); w.setStatusBarColor(Color.parseColor("#ba000d"));
		linear_1.setBackground(roundedBackground(15, "#EEEEEE"));
		linear_2.setBackground(roundedBackground(15, "#EEEEEE"));
		linear_3.setBackground(roundedBackground(15, "#EEEEEE"));
		linear_4.setBackground(roundedBackground(15, "#EEEEEE"));
		hscroll1.setBackground(roundedBackground(5, "#F44336"));
        linear4.setBackground(roundedBackground(5, "#F44336"));
        linear5.setBackground(roundedBackground(15, "#EEEEEE"));
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			linear_1.setElevation(10f);
			linear_2.setElevation(10f);
			linear_3.setElevation(10f);
			linear_4.setElevation(10f);
			hscroll1.setElevation(5f);
			linear4.setElevation(5f);
			linear5.setElevation(10f);
			searchbox.setElevation(10f);
		}
		_getData();
        searchbox.setOnEditorActionListener(new EditText.OnEditorActionListener() { public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == android.view.inputmethod.EditorInfo.IME_ACTION_SEARCH)
            {
                listview1.setAdapter(new Listview1Adapter(filterMapList(dataMap, "title", searchbox.getText())));
                InputUtil.hideKeyboard(getApplicationContext(), MoongamesActivity.this);
                return true;
            }
            return false;
        }});
	}
	
	@Override
	public void onBackPressed() {
		finish();
	}
	private void _getData () {
		database.addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot _dataSnapshot) {
				dataMap = new ArrayList<>();
				key_list = new ArrayList<>();
				try {
					GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
					for (DataSnapshot _data : _dataSnapshot.getChildren()) {
						HashMap<String, Object> _map = _data.getValue(_ind);
						dataMap.add(_map);
						key_list.add(_data.getKey());
					}
				}
				catch (Exception _e) {
					_e.printStackTrace();
				}
				position = 0;
				while(true) {
					if ((position == dataMap.size()) || (position > dataMap.size())) {
						listview1.setAdapter(new Listview1Adapter(dataMap));
						break;
					}
					if (dataMap.get((int)position).get("verified").toString().equals("false")) {
						dataMap.remove((int)(position));
					}
					position++;
				}
			}
			@Override
			public void onCancelled(DatabaseError _databaseError) {
			}
		});
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
				_v = _inflater.inflate(R.layout.game_preview, null);
			}
			
			final LinearLayout linear3 = _v.findViewById(R.id.linear3);
			final LinearLayout linear1 = _v.findViewById(R.id.linear1);
			final LinearLayout linear6 = _v.findViewById(R.id.linear6);
			final LinearLayout linear2 = _v.findViewById(R.id.linear2);
			final ImageView imageview1 = _v.findViewById(R.id.imageview1);
			final TextView textview1 = _v.findViewById(R.id.textview1);
			final TextView textview2 = _v.findViewById(R.id.textview2);
			
			linear1.setBackground(roundedBackground(15, "#EEEEEE"));
			linear6.setBackground(roundedBackground(5, "#000000"));
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { 
				linear1.setElevation(10f);
			}
			textview1.setText(dataMap.get(_position).get("title").toString());
			textview2.setText(dataMap.get(_position).get("author").toString());
			imageString = dataMap.get(_position).get("icon").toString();
			byte[] imageBytes = android.util.Base64.decode(imageString, android.util.Base64.DEFAULT);
			Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
			imageview1.setImageBitmap(decodedImage);
			packageName = dataMap.get(_position).get("package name").toString();
			
			return _v;
		}
	}
	public void getDownloads(final int _position){
	    complete1 = false;
	    DatabaseReference _downloads = database.child(key_list.get(_position)).child("downloads");
        _downloads.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot _dataSnapshot) {
                downloadsMap = new ArrayList<>();
                try {
                    GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
                    for (DataSnapshot _data : _dataSnapshot.getChildren()) {
                        HashMap<String, Object> _map = _data.getValue(_ind);
                        downloadsMap.add(_map);
                    }
                    downloads = downloadsMap.size();
                    complete1 = true;
                    launchPreview(_position);
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

    public void getRating(final int _position){
	    ratingAverage = 0;
        rating_sum = 0;
        complete2 = false;
        DatabaseReference _ratings = database.child(key_list.get(_position)).child("ratings");
        _ratings.addListenerForSingleValueEvent(new ValueEventListener() {
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
                        rating_sum += Integer.parseInt(ratingMap.get(i).get("val").toString());
                    }
                    ratingAverage = rating_sum/ratingMap.size();
                    complete2 = true;
                    launchPreview(_position);
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
    public void launchPreview(int _position) {
        if (complete1 && complete2) {
            download_int.putExtra("key", key_list.get(_position));
            download_int.putExtra("icon", dataMap.get(_position).get("icon").toString());
            download_int.putExtra("screenshot1", dataMap.get(_position).get("screenshot1").toString());
            download_int.putExtra("screenshot2", dataMap.get(_position).get("screenshot2").toString());
            download_int.putExtra("screenshot3", dataMap.get(_position).get("screenshot3").toString());
            download_int.putExtra("screenshot4", dataMap.get(_position).get("screenshot4").toString());
            download_int.putExtra("screenshot5", dataMap.get(_position).get("screenshot5").toString());
            download_int.putExtra("title", dataMap.get(_position).get("title").toString());
            download_int.putExtra("author", dataMap.get(_position).get("author").toString());
            download_int.putExtra("short description", dataMap.get(_position).get("short description").toString());
            download_int.putExtra("full description", dataMap.get(_position).get("full description").toString());
            download_int.putExtra("download link", dataMap.get(_position).get("download link").toString());
            download_int.putExtra("package name", dataMap.get(_position).get("package name").toString());
            download_int.putExtra("downloads", String.valueOf(downloads));
            download_int.putExtra("ratings", String.valueOf(ratingAverage));
            download_int.setClass(getApplicationContext(), GameDescriptionActivity.class);
            startActivity(download_int);
            HashMap<String,Object> hashMap = new HashMap<>();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.moongames, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_create:
                recommend_game.setClass(getApplicationContext(), RecommendGameActivity.class);
                startActivity(recommend_game);
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

package com.moonface.home;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.SparseBooleanArray;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class RecommendGameActivity extends AppCompatActivity {
	
	private FirebaseDatabase _firebase = FirebaseDatabase.getInstance();

	private Toolbar _toolbar;
	private FloatingActionButton _fab;
	private boolean icon_state = false;
    private boolean state1 = false;
	private HashMap<String, Object> dataMap = new HashMap<>();
	private String imageString = "";
    private int RESULT_LOAD_IMAGE = 0;
    private long size;
	
	private ImageView imageview2;
	private EditText title;
	private EditText package_name;
	private EditText download_link;
	private TextView author;
	private EditText short_description;
	private EditText full_description;
	private ImageView screenshot1;
    private ImageView screenshot2;
    private ImageView screenshot3;
    private ImageView screenshot4;
    private ImageView screenshot5;

	
	private SharedPreferences data;
	private DatabaseReference database = _firebase.getReference("games");
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.recommend_game);
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
		
		imageview2 = findViewById(R.id.imageview2);
		title = findViewById(R.id.title);
		package_name = findViewById(R.id.package_name);
		download_link = findViewById(R.id.download_link);
		author = findViewById(R.id.author);
		short_description = findViewById(R.id.short_description);
		full_description = findViewById(R.id.full_description);
		data = getSharedPreferences("data", Activity.MODE_PRIVATE);
		screenshot1 = findViewById(R.id.screenshot1);
        screenshot2 = findViewById(R.id.screenshot2);
        screenshot3 = findViewById(R.id.screenshot3);
        screenshot4 = findViewById(R.id.screenshot4);
        screenshot5 = findViewById(R.id.screenshot5);

        imageview2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
			    RESULT_LOAD_IMAGE = 0;
				Intent i = new Intent( Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI); startActivityForResult(i, RESULT_LOAD_IMAGE);
			}
		});
        screenshot1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RESULT_LOAD_IMAGE = 1;
                Intent i = new Intent( Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI); startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });
        screenshot2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RESULT_LOAD_IMAGE = 2;
                Intent i = new Intent( Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI); startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });
        screenshot3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RESULT_LOAD_IMAGE = 3;
                Intent i = new Intent( Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI); startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });
        screenshot4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RESULT_LOAD_IMAGE = 4;
                Intent i = new Intent( Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI); startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });
        screenshot5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RESULT_LOAD_IMAGE = 5;
                Intent i = new Intent( Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI); startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });
		
		_fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (title.getText().toString().length() < 4) {
					title.setError(getString(R.string.title_too_short_message));
				}
				else {
					if (19 < title.getText().toString().length()) {
						title.setError("The title is too long!");
					}
					else {
						if (!package_name.getText().toString().contains(".")) {
							package_name.setError(getString(R.string.package_name_format_error));
						}
						else {
							if (!download_link.getText().toString().contains(".")) {
								download_link.setError(getString(R.string.download_link_format_error));
							}
							else {
								if (download_link.getText().toString().indexOf(".") < 1) {
									download_link.setError(getString(R.string.download_link_format_error));
								}
								else {
									if (download_link.getText().toString().indexOf(".") == (download_link.getText().toString().length() - 1)) {
										download_link.setError(getString(R.string.download_link_format_error));
									}
									else {
										if (short_description.getText().toString().length() < 10) {
											short_description.setError(getString(R.string.short_description_too_short_message));
										}
										else {
											if (80 < short_description.getText().toString().length()) {
												short_description.setError(getString(R.string.short_description_too_long_message));
											}
											else {
												if (full_description.getText().toString().length() < 10) {
													full_description.setError(getString(R.string.full_description_too_short_message));
												}
												else {
													if (4000 < full_description.getText().toString().length()) {
														full_description.setError(getString(R.string.full_description_too_long_message));
													}
													else {
                                                        if (!icon_state) {
                                                            SketchwareUtil.showMessage(getApplicationContext(), getString(R.string.game_icon_error));
                                                        }
                                                        else {
                                                            if (!state1) {
                                                                SketchwareUtil.showMessage(getApplicationContext(), getString(R.string.no_screenshots_message));
                                                            }
                                                            else {
                                                                _uploadData();
                                                                finish();
                                                            }
                                                        }
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		});
	}
	private void initializeLogic() {
		Window w = this.getWindow();w.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);w.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS); w.setStatusBarColor(Color.parseColor("#ba000d"));
		author.setText(getString(R.string.author_label).concat(data.getString("nickname", "")));
	}
	@Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                size = bitmap.getByteCount();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(size<10000000) {
                if (RESULT_LOAD_IMAGE == 0) {
                    icon_state = true;
                    imageview2.setImageURI(selectedImage);
                }
                if (RESULT_LOAD_IMAGE == 1) {
                    state1 = true;
                    screenshot1.setImageURI(selectedImage);
                }
                if (RESULT_LOAD_IMAGE == 2) {
                    screenshot2.setImageURI(selectedImage);
                }
                if (RESULT_LOAD_IMAGE == 3) {
                    screenshot3.setImageURI(selectedImage);
                }
                if (RESULT_LOAD_IMAGE == 4) {
                    screenshot4.setImageURI(selectedImage);
                }
                if (RESULT_LOAD_IMAGE == 5) {
                    screenshot5.setImageURI(selectedImage);
                }
            }
            else{
                Toast.makeText(getApplicationContext(), R.string.selected_image_too_big_message, Toast.LENGTH_SHORT).show();
            }
        }
    }
	
	@Override
	public void onBackPressed() {
		finish();
	}
	private void _uploadData () {
	    dataMap = new HashMap<>();
		dataMap.put("title", title.getText().toString());
		dataMap.put("package name", package_name.getText().toString());
		dataMap.put("download link", download_link.getText().toString());
		dataMap.put("author", data.getString("nickname", ""));
		dataMap.put("short description", short_description.getText().toString());
		dataMap.put("full description", full_description.getText().toString());
		dataMap.put("icon", getImageString(imageview2));
		dataMap.put("verified", "false");
		dataMap.put("screenshot1", getImageString(screenshot1));
        dataMap.put("screenshot2", getImageString(screenshot2));
        dataMap.put("screenshot3", getImageString(screenshot3));
        dataMap.put("screenshot4", getImageString(screenshot4));
        dataMap.put("screenshot5", getImageString(screenshot5));
		database.push().updateChildren(dataMap);
	}
	public String getImageString(ImageView _imageview) {

        java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
        Bitmap bm = ((android.graphics.drawable.BitmapDrawable) _imageview.getDrawable()).getBitmap();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        imageString = android.util.Base64.encodeToString(imageBytes, android.util.Base64.DEFAULT);
        if(imageString.length() != 1662) {
            return imageString;
        }
        else{
            return "0";
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

package com.moonface.home;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseBooleanArray;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class BootAnimationActivity extends AppCompatActivity {
	
	private Timer _timer = new Timer();

	private ImageView imageview1;
	private ImageView imageview3;
	private TextView textview1;

	private TimerTask after;
	private TimerTask repeater;
	private ObjectAnimator alpha = new ObjectAnimator();
	private Intent start_up = new Intent();
	private ObjectAnimator rotate = new ObjectAnimator();
	private ObjectAnimator moonLogo = new ObjectAnimator();
	private ObjectAnimator moonSlogan = new ObjectAnimator();
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.boot_animation);
		initialize();
		initializeLogic();
	}
	
	private void initialize() {

		imageview1 = findViewById(R.id.imageview1);
		imageview3 = findViewById(R.id.imageview3);
		textview1 = findViewById(R.id.textview1);
	}
	private void initializeLogic() {
	    imageview1.setImageResource(R.drawable.sleeping_moon);
		textview1.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/fluent_sans_regular.ttf"), 1);
		repeater = new TimerTask() {
			@Override
			public void run() {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						imageview1.setRotation((float)(0));
						rotate.setTarget(imageview1);
						rotate.setPropertyName("rotation");
						rotate.setFloatValues((float)(20));
						rotate.setDuration(150);
						rotate.setRepeatMode(ValueAnimator.REVERSE);
						rotate.setRepeatCount(1);
						rotate.start();
						after = new TimerTask() {
							@Override
							public void run() {
								runOnUiThread(new Runnable() {
									@Override
									public void run() {
										after.cancel();
										imageview1.setRotation((float)(359));
										rotate.setTarget(imageview1);
										rotate.setPropertyName("rotation");
										rotate.setFloatValues((float)(340));
										rotate.setDuration(150);
										rotate.setRepeatMode(ValueAnimator.REVERSE);
										rotate.setRepeatCount(1);
										rotate.start();
									}
								});
							}
						};
						_timer.schedule(after, 300);
					}
				});
			}
		};
		_timer.scheduleAtFixedRate(repeater, 0, 600);
		after = new TimerTask() {
			@Override
			public void run() {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						rotate.cancel();
						after.cancel();
						repeater.cancel();
						imageview1.setRotation((float)(0));
						alpha.setTarget(imageview1);
						alpha.setPropertyName("alpha");
						alpha.setFloatValues((float)(0.1d));
						alpha.setDuration(250);
						alpha.setRepeatMode(ValueAnimator.REVERSE);
						alpha.setRepeatCount(1);
						alpha.start();
						moonLogo.setTarget(imageview3);
						moonLogo.setPropertyName("alpha");
						moonLogo.setFloatValues((float)(1));
						moonLogo.setDuration(500);
						moonLogo.start();
						moonSlogan.setTarget(textview1);
						moonSlogan.setPropertyName("alpha");
						moonSlogan.setFloatValues((float)(1));
						moonSlogan.setDuration(500);
						moonSlogan.start();
						after = new TimerTask() {
							@Override
							public void run() {
								runOnUiThread(new Runnable() {
									@Override
									public void run() {
										after.cancel();
										imageview1.setImageResource(R.drawable.moonface_logo);
										after = new TimerTask() {
											@Override
											public void run() {
												runOnUiThread(new Runnable() {
													@Override
													public void run() {
														imageview1.setAlpha((float)(1));
														imageview3.setAlpha((float)(1));
														textview1.setAlpha((float)(1));
														after.cancel();
														alpha.cancel();
														moonLogo.cancel();
														moonSlogan.cancel();
														after = new TimerTask() {
															@Override
															public void run() {
																runOnUiThread(new Runnable() {
																	@Override
																	public void run() {
																		after.cancel();
                                                                        start_up.setClass(getApplicationContext(), HomeActivity.class);
                                                                        startActivity(start_up);
																		finish();
																	}
																});
															}
														};
														_timer.schedule(after, 500);
													}
												});
											}
										};
										_timer.schedule(after, 250);
									}
								});
							}
						};
						_timer.schedule(after, 250);
					}
				});
			}
		};
		_timer.schedule(after, 2400);
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
		ArrayList<Double> _result = new ArrayList<>();
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

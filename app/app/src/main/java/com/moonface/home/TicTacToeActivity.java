package com.moonface.home;

import android.app.Dialog;
import android.os.*;
import android.view.*;
import android.widget.*;
import android.graphics.*;
import android.util.*;

import java.util.*;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import java.util.HashMap;
import java.util.ArrayList;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.Button;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.app.Activity;
import android.content.SharedPreferences;
import java.util.Timer;
import java.util.TimerTask;
import android.view.View;
import android.graphics.Typeface;

public class TicTacToeActivity extends AppCompatActivity {
	
	private Timer _timer = new Timer();
	
	private Toolbar _toolbar;
	private double side = 0;
	private HashMap<String, Object> state = new HashMap<>();
	private double random = 0;
	private double chosen = 0;
	private double win_set = 0;
	
	private ArrayList<Double> cells = new ArrayList<>();
	
	private LinearLayout linear7;
	private ImageView imageview3;
	private TextView textview2;
	private ImageView imageview4;
	private Button button2;
	private LinearLayout linear14;
	private LinearLayout linear_1;
	private LinearLayout linear_2;
	private LinearLayout linear_3;
	private ImageView imageview_1;
	private ImageView imageview_2;
	private ImageView imageview_3;
	private LinearLayout linear_4;
	private LinearLayout linear_5;
	private LinearLayout linear_6;
	private ImageView imageview_4;
	private ImageView imageview_5;
	private ImageView imageview_6;
	private LinearLayout linear_7;
	private LinearLayout linear_8;
	private LinearLayout linear_9;
	private ImageView imageview_7;
	private ImageView imageview_8;
	private ImageView imageview_9;
	
	private AlertDialog.Builder end;
	private SharedPreferences data;
	private TimerTask delay;
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.tic_tac_toe);
		initialize();
		initializeLogic();
	}
	public void showChooseSideDialog(){
        LayoutInflater factory = LayoutInflater.from(this);
        final View chooseSideDialogView = factory.inflate(R.layout.choose_side_dialog, null);
        final AlertDialog chooseSideDialog = new AlertDialog.Builder(this).create();
        chooseSideDialog.setView(chooseSideDialogView);
        chooseSideDialogView.findViewById(R.id.buttonX).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                button2.setVisibility(View.INVISIBLE);
                side = 1;
                state = new HashMap<>();
                imageview_1.setImageResource(R.drawable.empty);
                imageview_2.setImageResource(R.drawable.empty);
                imageview_3.setImageResource(R.drawable.empty);
                imageview_4.setImageResource(R.drawable.empty);
                imageview_5.setImageResource(R.drawable.empty);
                imageview_6.setImageResource(R.drawable.empty);
                imageview_7.setImageResource(R.drawable.empty);
                imageview_8.setImageResource(R.drawable.empty);
                imageview_9.setImageResource(R.drawable.empty);
                linear7.setVisibility(View.VISIBLE);
                textview2.setText(data.getString("nickname", ""));
                imageview3.setImageResource(R.drawable.ic_clear_white);
                imageview4.setImageResource(R.drawable.ic_panorama_fisheye_white);
                state.put("1", "0");
                state.put("2", "0");
                state.put("3", "0");
                state.put("4", "0");
                state.put("5", "0");
                state.put("6", "0");
                state.put("7", "0");
                state.put("8", "0");
                state.put("9", "0");
                _your_turn();
                chooseSideDialog.dismiss();
            }
        });
        chooseSideDialogView.findViewById(R.id.buttonO).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                button2.setVisibility(View.INVISIBLE);
                side = 2;
                state = new HashMap<>();
                imageview_1.setImageResource(R.drawable.empty);
                imageview_2.setImageResource(R.drawable.empty);
                imageview_3.setImageResource(R.drawable.empty);
                imageview_4.setImageResource(R.drawable.empty);
                imageview_5.setImageResource(R.drawable.empty);
                imageview_6.setImageResource(R.drawable.empty);
                imageview_7.setImageResource(R.drawable.empty);
                imageview_8.setImageResource(R.drawable.empty);
                imageview_9.setImageResource(R.drawable.empty);
                linear7.setVisibility(View.VISIBLE);
                textview2.setText(data.getString("nickname", ""));
                imageview3.setImageResource(R.drawable.ic_panorama_fisheye_white);
                imageview4.setImageResource(R.drawable.ic_clear_white);
                state.put("1", "0");
                state.put("2", "0");
                state.put("3", "0");
                state.put("4", "0");
                state.put("5", "0");
                state.put("6", "0");
                state.put("7", "0");
                state.put("8", "0");
                state.put("9", "0");
                _enemy_turn();
                chooseSideDialog.dismiss();
            }
        });

        chooseSideDialog.show();
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
		linear7 = findViewById(R.id.linear7);
		imageview3 = findViewById(R.id.imageview3);
		textview2 = findViewById(R.id.textview2);
		imageview4 = findViewById(R.id.btnCapture);
		button2 = findViewById(R.id.button2);
		linear14 = findViewById(R.id.linear14);
		linear_1 = findViewById(R.id.linear_1);
		linear_2 = findViewById(R.id.linear_2);
		linear_3 = findViewById(R.id.linear_3);
		imageview_1 = findViewById(R.id.imageview_1);
		imageview_2 = findViewById(R.id.imageview_2);
		imageview_3 = findViewById(R.id.imageview_3);
		linear_4 = findViewById(R.id.linear_4);
		linear_5 = findViewById(R.id.linear_5);
		linear_6 = findViewById(R.id.linear_6);
		imageview_4 = findViewById(R.id.imageview_4);
		imageview_5 = findViewById(R.id.imageview_5);
		imageview_6 = findViewById(R.id.imageview_6);
		linear_7 = findViewById(R.id.linear_7);
		linear_8 = findViewById(R.id.linear_8);
		linear_9 = findViewById(R.id.linear_9);
		imageview_7 = findViewById(R.id.imageview_7);
		imageview_8 = findViewById(R.id.imageview_8);
		imageview_9 = findViewById(R.id.imageview_9);
		end = new AlertDialog.Builder(this);
		data = getSharedPreferences("data", Activity.MODE_PRIVATE);

		button2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				showChooseSideDialog();
			}
		});
		
		imageview_1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				_press(imageview_1, 1, imageview_1);
			}
		});
		
		imageview_2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				_press(imageview_2, 2, imageview_2);
			}
		});
		
		imageview_3.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				_press(imageview_3, 3, imageview_3);
			}
		});
		
		imageview_4.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				_press(imageview_4, 4, imageview_4);
			}
		});
		
		imageview_5.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				_press(imageview_5, 5, imageview_5);
			}
		});
		
		imageview_6.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				_press(imageview_6, 6, imageview_6);
			}
		});
		
		imageview_7.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				_press(imageview_7, 7, imageview_7);
			}
		});
		
		imageview_8.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				_press(imageview_8, 8, imageview_8);
			}
		});
		
		imageview_9.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				_press(imageview_9, 9, imageview_9);
			}
		});
	}
	private void initializeLogic() {
		Window w = this.getWindow();w.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);w.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS); w.setStatusBarColor(Color.parseColor("#ba000d"));
		linear7.setVisibility(View.INVISIBLE);
		button2.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/fluent_sans_regular.ttf"), 1);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { linear7.setElevation(30f);}
		android.graphics.drawable.GradientDrawable border = new android.graphics.drawable.GradientDrawable();  border.setColor(Color.TRANSPARENT);  border.setCornerRadius(0);
		border.setStroke(1, 0xFF000000); linear_1.setBackgroundDrawable(border);
		linear_2.setBackgroundDrawable(border);
		linear_3.setBackgroundDrawable(border);
		linear_4.setBackgroundDrawable(border);
		linear_5.setBackgroundDrawable(border);
		linear_6.setBackgroundDrawable(border);
		linear_7.setBackgroundDrawable(border);
		linear_8.setBackgroundDrawable(border);
		linear_9.setBackgroundDrawable(border);
		android.graphics.drawable.GradientDrawable buttonLy = new android.graphics.drawable.GradientDrawable();  buttonLy.setColor(0xFF37474F);  buttonLy.setCornerRadius(30);
		buttonLy.setStroke(5, 0xFF000000); button2.setBackgroundDrawable(buttonLy);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { button2.setElevation(30f);}
		android.graphics.drawable.GradientDrawable linearLx = new android.graphics.drawable.GradientDrawable();  linearLx.setColor(0xFF37474F);  linearLx.setCornerRadius(30);
		linearLx.setStroke(5, 0xFF000000); linear14.setBackgroundDrawable(linearLx);
		android.graphics.drawable.GradientDrawable linearLz = new android.graphics.drawable.GradientDrawable();  linearLz.setColor(0xFF37474F);  linearLz.setCornerRadius(0);
		linearLz.setStroke(5, 0xFF000000); linear7.setBackgroundDrawable(linearLz);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { linear14.setElevation(30f);}
		imageview_1.setEnabled(false);
		imageview_2.setEnabled(false);
		imageview_3.setEnabled(false);
		imageview_4.setEnabled(false);
		imageview_5.setEnabled(false);
		imageview_6.setEnabled(false);
		imageview_7.setEnabled(false);
		imageview_8.setEnabled(false);
		imageview_9.setEnabled(false);
	}
	
	@Override
	public void onBackPressed() {
		finish();
	}
	private void _your_turn () {
		if (((!state.get("1").toString().equals("0") && !state.get("2").toString().equals("0")) && (!state.get("3").toString().equals("0") && !state.get("4").toString().equals("0"))) && ((!state.get("5").toString().equals("0") && !state.get("6").toString().equals("0")) && (!state.get("7").toString().equals("0") && (!state.get("8").toString().equals("0") && !state.get("9").toString().equals("0"))))) {
			side = 0;
			random = 0;
			chosen = 0;
			win_set = 0;
			delay = new TimerTask() {
				@Override
				public void run() {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							linear7.setVisibility(View.INVISIBLE);
							end.setTitle(R.string.game_over_label);
							end.setMessage(R.string.draw_label);
							end.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface _dialog, int _which) {
									
								}
							});
							end.create().show();
							imageview_1.setImageResource(R.drawable.empty);
							imageview_2.setImageResource(R.drawable.empty);
							imageview_3.setImageResource(R.drawable.empty);
							imageview_4.setImageResource(R.drawable.empty);
							imageview_5.setImageResource(R.drawable.empty);
							imageview_6.setImageResource(R.drawable.empty);
							imageview_7.setImageResource(R.drawable.empty);
							imageview_8.setImageResource(R.drawable.empty);
							imageview_9.setImageResource(R.drawable.empty);
							button2.setVisibility(View.VISIBLE);
						}
					});
				}
			};
			_timer.schedule(delay, 1000);
		}
		else {
			imageview_1.setEnabled(true);
			imageview_2.setEnabled(true);
			imageview_3.setEnabled(true);
			imageview_4.setEnabled(true);
			imageview_5.setEnabled(true);
			imageview_6.setEnabled(true);
			imageview_7.setEnabled(true);
			imageview_8.setEnabled(true);
			imageview_9.setEnabled(true);
		}
	}
	
	
	private void _enemy_turn () {
		if (((!state.get("1").toString().equals("0") && !state.get("2").toString().equals("0")) && (!state.get("3").toString().equals("0") && !state.get("4").toString().equals("0"))) && ((!state.get("5").toString().equals("0") && !state.get("6").toString().equals("0")) && (!state.get("7").toString().equals("0") && (!state.get("8").toString().equals("0") && !state.get("9").toString().equals("0"))))) {
			side = 0;
			random = 0;
			chosen = 0;
			win_set = 0;
			delay = new TimerTask() {
				@Override
				public void run() {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							linear7.setVisibility(View.INVISIBLE);
							end.setTitle(R.string.game_over_label);
							end.setMessage(R.string.draw_label);
							end.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface _dialog, int _which) {
									
								}
							});
							end.create().show();
							imageview_1.setImageResource(R.drawable.empty);
							imageview_2.setImageResource(R.drawable.empty);
							imageview_3.setImageResource(R.drawable.empty);
							imageview_4.setImageResource(R.drawable.empty);
							imageview_5.setImageResource(R.drawable.empty);
							imageview_6.setImageResource(R.drawable.empty);
							imageview_7.setImageResource(R.drawable.empty);
							imageview_8.setImageResource(R.drawable.empty);
							imageview_9.setImageResource(R.drawable.empty);
							button2.setVisibility(View.VISIBLE);
						}
					});
				}
			};
			_timer.schedule(delay, 1000);
		}
		else {
			imageview_1.setEnabled(false);
			imageview_2.setEnabled(false);
			imageview_3.setEnabled(false);
			imageview_4.setEnabled(false);
			imageview_5.setEnabled(false);
			imageview_6.setEnabled(false);
			imageview_7.setEnabled(false);
			imageview_8.setEnabled(false);
			imageview_9.setEnabled(false);
			if (state.get("1").toString().equals("0")) {
				cells.add(1d);
			}
			if (state.get("2").toString().equals("0")) {
				cells.add(2d);
			}
			if (state.get("3").toString().equals("0")) {
				cells.add(3d);
			}
			if (state.get("4").toString().equals("0")) {
				cells.add(4d);
			}
			if (state.get("5").toString().equals("0")) {
				cells.add(5d);
			}
			if (state.get("6").toString().equals("0")) {
				cells.add(6d);
			}
			if (state.get("7").toString().equals("0")) {
				cells.add(7d);
			}
			if (state.get("8").toString().equals("0")) {
				cells.add(8d);
			}
			if (state.get("9").toString().equals("0")) {
				cells.add(9d);
			}
			random = SketchwareUtil.getRandom(1, cells.size());
			chosen = cells.get((int) (random - 1));
			_check_state();
			_check_state_enemy();
			state.put(String.valueOf((long)(chosen)), "2");
			delay = new TimerTask() {
				@Override
				public void run() {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							if (chosen == 1) {
								_enemy_pressed(imageview_1);
							}
							if (chosen == 2) {
								_enemy_pressed(imageview_2);
							}
							if (chosen == 3) {
								_enemy_pressed(imageview_3);
							}
							if (chosen == 4) {
								_enemy_pressed(imageview_4);
							}
							if (chosen == 5) {
								_enemy_pressed(imageview_5);
							}
							if (chosen == 6) {
								_enemy_pressed(imageview_6);
							}
							if (chosen == 7) {
								_enemy_pressed(imageview_7);
							}
							if (chosen == 8) {
								_enemy_pressed(imageview_8);
							}
							if (chosen == 9) {
								_enemy_pressed(imageview_9);
							}
							_enemy_check(chosen);
							cells.clear();
						}
					});
				}
			};
			_timer.schedule(delay, 1000);
		}
	}
	
	
	private void _press (final ImageView _cell, final double _num, final View _linear) {
		if (state.get(String.valueOf((long)(_num))).toString().equals("0")) {
			if (side == 1) {
				_cell.setImageResource(R.drawable.ic_clear_white);
			}
			if (side == 2) {
				_cell.setImageResource(R.drawable.ic_panorama_fisheye_white);
			}
			state.put(String.valueOf((long)(_num)), "1");
			_check(_num);
		}
	}
	
	
	private void _check (final double _num) {
		if (state.get("1").toString().equals("1") && (state.get("2").toString().equals("1") && state.get("3").toString().equals("1"))) {
			win_set = 1;
		}
		if (state.get("4").toString().equals("1") && (state.get("5").toString().equals("1") && state.get("6").toString().equals("1"))) {
			win_set = 1;
		}
		if (state.get("7").toString().equals("1") && (state.get("8").toString().equals("1") && state.get("9").toString().equals("1"))) {
			win_set = 1;
		}
		if (state.get("1").toString().equals("1") && (state.get("4").toString().equals("1") && state.get("7").toString().equals("1"))) {
			win_set = 1;
		}
		if (state.get("2").toString().equals("1") && (state.get("5").toString().equals("1") && state.get("8").toString().equals("1"))) {
			win_set = 1;
		}
		if (state.get("3").toString().equals("1") && (state.get("6").toString().equals("1") && state.get("9").toString().equals("1"))) {
			win_set = 1;
		}
		if (state.get("1").toString().equals("1") && (state.get("5").toString().equals("1") && state.get("9").toString().equals("1"))) {
			win_set = 1;
		}
		if (state.get("3").toString().equals("1") && (state.get("5").toString().equals("1") && state.get("7").toString().equals("1"))) {
			win_set = 1;
		}
		if (win_set == 1) {
			_win(win_set);
		}
		else {
			_enemy_turn();
		}
	}
	
	
	private void _win (final double _side) {
		side = 0;
		random = 0;
		chosen = 0;
		win_set = 0;
		delay = new TimerTask() {
			@Override
			public void run() {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						linear7.setVisibility(View.INVISIBLE);
						end.setTitle(R.string.game_over_label);
						if (_side == 1) {
							end.setMessage(R.string.won);
						}
						if (_side == 2) {
							end.setMessage(R.string.lost);
						}
						end.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface _dialog, int _which) {
								
							}
						});
						end.create().show();
						imageview_1.setImageResource(R.drawable.empty);
						imageview_2.setImageResource(R.drawable.empty);
						imageview_3.setImageResource(R.drawable.empty);
						imageview_4.setImageResource(R.drawable.empty);
						imageview_5.setImageResource(R.drawable.empty);
						imageview_6.setImageResource(R.drawable.empty);
						imageview_7.setImageResource(R.drawable.empty);
						imageview_8.setImageResource(R.drawable.empty);
						imageview_9.setImageResource(R.drawable.empty);
						button2.setVisibility(View.VISIBLE);
					}
				});
			}
		};
		_timer.schedule(delay, 1000);
	}
	
	
	private void _enemy_check (final double _num) {
		if (state.get("1").toString().equals("2") && (state.get("2").toString().equals("2") && state.get("3").toString().equals("2"))) {
			win_set = 2;
		}
		if (state.get("4").toString().equals("2") && (state.get("5").toString().equals("2") && state.get("6").toString().equals("2"))) {
			win_set = 2;
		}
		if (state.get("7").toString().equals("2") && (state.get("8").toString().equals("2") && state.get("9").toString().equals("2"))) {
			win_set = 2;
		}
		if (state.get("1").toString().equals("2") && (state.get("4").toString().equals("2") && state.get("7").toString().equals("2"))) {
			win_set = 2;
		}
		if (state.get("2").toString().equals("2") && (state.get("5").toString().equals("2") && state.get("8").toString().equals("2"))) {
			win_set = 2;
		}
		if (state.get("3").toString().equals("2") && (state.get("6").toString().equals("2") && state.get("9").toString().equals("2"))) {
			win_set = 2;
		}
		if (state.get("1").toString().equals("2") && (state.get("5").toString().equals("2") && state.get("9").toString().equals("2"))) {
			win_set = 2;
		}
		if (state.get("3").toString().equals("2") && (state.get("5").toString().equals("2") && state.get("7").toString().equals("2"))) {
			win_set = 2;
		}
		if (win_set == 2) {
			_win(win_set);
		}
		else {
			_your_turn();
		}
	}
	
	
	private void _enemy_pressed (final ImageView _cell) {
		if (side == 1) {
			_cell.setImageResource(R.drawable.ic_panorama_fisheye_white);
		}
		if (side == 2) {
			_cell.setImageResource(R.drawable.ic_clear_white);
		}
	}
	
	
	private void _check_state_enemy () {
		if (state.get("1").toString().equals("2") && state.get("2").toString().equals("2")) {
			if (state.get("3").toString().equals("0")) {
				chosen = 3;
			}
		}
		if (state.get("2").toString().equals("2") && state.get("3").toString().equals("2")) {
			if (state.get("1").toString().equals("0")) {
				chosen = 1;
			}
		}
		if (state.get("1").toString().equals("2") && state.get("3").toString().equals("2")) {
			if (state.get("2").toString().equals("0")) {
				chosen = 2;
			}
		}
		if (state.get("4").toString().equals("2") && state.get("5").toString().equals("2")) {
			if (state.get("6").toString().equals("0")) {
				chosen = 6;
			}
		}
		if (state.get("5").toString().equals("2") && state.get("6").toString().equals("2")) {
			if (state.get("4").toString().equals("0")) {
				chosen = 4;
			}
		}
		if (state.get("4").toString().equals("2") && state.get("6").toString().equals("2")) {
			if (state.get("5").toString().equals("0")) {
				chosen = 5;
			}
		}
		if (state.get("7").toString().equals("2") && state.get("8").toString().equals("2")) {
			if (state.get("9").toString().equals("0")) {
				chosen = 9;
			}
		}
		if (state.get("8").toString().equals("2") && state.get("9").toString().equals("2")) {
			if (state.get("7").toString().equals("0")) {
				chosen = 7;
			}
		}
		if (state.get("7").toString().equals("2") && state.get("9").toString().equals("2")) {
			if (state.get("8").toString().equals("0")) {
				chosen = 8;
			}
		}
		if (state.get("1").toString().equals("2") && state.get("4").toString().equals("2")) {
			if (state.get("7").toString().equals("0")) {
				chosen = 7;
			}
		}
		if (state.get("4").toString().equals("2") && state.get("7").toString().equals("2")) {
			if (state.get("1").toString().equals("0")) {
				chosen = 1;
			}
		}
		if (state.get("1").toString().equals("2") && state.get("7").toString().equals("2")) {
			if (state.get("4").toString().equals("0")) {
				chosen = 4;
			}
		}
		if (state.get("2").toString().equals("2") && state.get("5").toString().equals("2")) {
			if (state.get("8").toString().equals("0")) {
				chosen = 8;
			}
		}
		if (state.get("5").toString().equals("2") && state.get("8").toString().equals("2")) {
			if (state.get("2").toString().equals("0")) {
				chosen = 2;
			}
		}
		if (state.get("2").toString().equals("2") && state.get("8").toString().equals("2")) {
			if (state.get("5").toString().equals("0")) {
				chosen = 5;
			}
		}
		if (state.get("3").toString().equals("2") && state.get("6").toString().equals("2")) {
			if (state.get("9").toString().equals("0")) {
				chosen = 9;
			}
		}
		if (state.get("6").toString().equals("2") && state.get("9").toString().equals("2")) {
			if (state.get("3").toString().equals("0")) {
				chosen = 3;
			}
		}
		if (state.get("3").toString().equals("2") && state.get("9").toString().equals("2")) {
			if (state.get("6").toString().equals("0")) {
				chosen = 6;
			}
		}
		if (state.get("1").toString().equals("2") && state.get("5").toString().equals("2")) {
			if (state.get("9").toString().equals("0")) {
				chosen = 9;
			}
		}
		if (state.get("5").toString().equals("2") && state.get("9").toString().equals("2")) {
			if (state.get("1").toString().equals("0")) {
				chosen = 1;
			}
		}
		if (state.get("1").toString().equals("2") && state.get("9").toString().equals("2")) {
			if (state.get("5").toString().equals("0")) {
				chosen = 5;
			}
		}
		if (state.get("3").toString().equals("2") && state.get("5").toString().equals("2")) {
			if (state.get("7").toString().equals("0")) {
				chosen = 7;
			}
		}
		if (state.get("5").toString().equals("2") && state.get("7").toString().equals("2")) {
			if (state.get("3").toString().equals("0")) {
				chosen = 3;
			}
		}
		if (state.get("3").toString().equals("2") && state.get("7").toString().equals("2")) {
			if (state.get("5").toString().equals("0")) {
				chosen = 5;
			}
		}
	}
	
	
	private void _check_state () {
		if (state.get("1").toString().equals("1") && state.get("2").toString().equals("1")) {
			if (state.get("3").toString().equals("0")) {
				chosen = 3;
			}
		}
		if (state.get("2").toString().equals("1") && state.get("3").toString().equals("1")) {
			if (state.get("1").toString().equals("0")) {
				chosen = 1;
			}
		}
		if (state.get("1").toString().equals("1") && state.get("3").toString().equals("1")) {
			if (state.get("2").toString().equals("0")) {
				chosen = 2;
			}
		}
		if (state.get("4").toString().equals("1") && state.get("5").toString().equals("1")) {
			if (state.get("6").toString().equals("0")) {
				chosen = 6;
			}
		}
		if (state.get("5").toString().equals("1") && state.get("6").toString().equals("1")) {
			if (state.get("4").toString().equals("0")) {
				chosen = 4;
			}
		}
		if (state.get("4").toString().equals("1") && state.get("6").toString().equals("1")) {
			if (state.get("5").toString().equals("0")) {
				chosen = 5;
			}
		}
		if (state.get("7").toString().equals("1") && state.get("8").toString().equals("1")) {
			if (state.get("9").toString().equals("0")) {
				chosen = 9;
			}
		}
		if (state.get("8").toString().equals("1") && state.get("9").toString().equals("1")) {
			if (state.get("7").toString().equals("0")) {
				chosen = 7;
			}
		}
		if (state.get("7").toString().equals("1") && state.get("9").toString().equals("1")) {
			if (state.get("8").toString().equals("0")) {
				chosen = 8;
			}
		}
		if (state.get("1").toString().equals("1") && state.get("4").toString().equals("1")) {
			if (state.get("7").toString().equals("0")) {
				chosen = 7;
			}
		}
		if (state.get("4").toString().equals("1") && state.get("7").toString().equals("1")) {
			if (state.get("1").toString().equals("0")) {
				chosen = 1;
			}
		}
		if (state.get("1").toString().equals("1") && state.get("7").toString().equals("1")) {
			if (state.get("4").toString().equals("0")) {
				chosen = 4;
			}
		}
		if (state.get("2").toString().equals("1") && state.get("5").toString().equals("1")) {
			if (state.get("8").toString().equals("0")) {
				chosen = 8;
			}
		}
		if (state.get("5").toString().equals("1") && state.get("8").toString().equals("1")) {
			if (state.get("2").toString().equals("0")) {
				chosen = 2;
			}
		}
		if (state.get("2").toString().equals("1") && state.get("8").toString().equals("1")) {
			if (state.get("5").toString().equals("0")) {
				chosen = 5;
			}
		}
		if (state.get("3").toString().equals("1") && state.get("6").toString().equals("1")) {
			if (state.get("9").toString().equals("0")) {
				chosen = 9;
			}
		}
		if (state.get("6").toString().equals("1") && state.get("9").toString().equals("1")) {
			if (state.get("3").toString().equals("0")) {
				chosen = 3;
			}
		}
		if (state.get("3").toString().equals("1") && state.get("9").toString().equals("1")) {
			if (state.get("6").toString().equals("0")) {
				chosen = 6;
			}
		}
		if (state.get("1").toString().equals("1") && state.get("5").toString().equals("1")) {
			if (state.get("9").toString().equals("0")) {
				chosen = 9;
			}
		}
		if (state.get("5").toString().equals("1") && state.get("9").toString().equals("1")) {
			if (state.get("1").toString().equals("0")) {
				chosen = 1;
			}
		}
		if (state.get("1").toString().equals("1") && state.get("9").toString().equals("1")) {
			if (state.get("5").toString().equals("0")) {
				chosen = 5;
			}
		}
		if (state.get("3").toString().equals("1") && state.get("5").toString().equals("1")) {
			if (state.get("7").toString().equals("0")) {
				chosen = 7;
			}
		}
		if (state.get("5").toString().equals("1") && state.get("7").toString().equals("1")) {
			if (state.get("3").toString().equals("0")) {
				chosen = 3;
			}
		}
		if (state.get("3").toString().equals("1") && state.get("7").toString().equals("1")) {
			if (state.get("5").toString().equals("0")) {
				chosen = 5;
			}
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

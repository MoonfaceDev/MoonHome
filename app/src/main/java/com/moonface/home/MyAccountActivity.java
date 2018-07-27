package com.moonface.home;

import android.net.Uri;
import android.os.*;
import android.provider.MediaStore;
import android.view.ViewGroup;
import android.widget.*;
import android.graphics.*;
import android.util.*;

import java.io.IOException;
import java.util.*;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.LinearLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.Intent;
import android.app.Activity;
import android.content.SharedPreferences;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.moonface.Util.DrawableUtil;
import com.squareup.picasso.Picasso;

public class MyAccountActivity extends AppCompatActivity {

	private ImageView profile_pic;
	private TextView profile_title;
	private LinearLayout linear_email;
	private TextView email;
	private LinearLayout linear_id;
	private TextView id;
	private LinearLayout linear_nickname;
	private TextView nickname;
	private LinearLayout linear_reset_password;
	private LinearLayout linear_signout;

	private SharedPreferences data;
	private Intent logout_int = new Intent();
	private FirebaseAuth userAuth;
	private FirebaseDatabase _firebase = FirebaseDatabase.getInstance();
	private AlertDialog.Builder descriptionDialog;
	private AlertDialog.Builder reset_password_dialog;
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.my_account);
		initialize();
		initializeLogic();
	}
	
	private void initialize() {

		Toolbar _toolbar = findViewById(R.id._toolbar);
		setSupportActionBar(_toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _v) {
				onBackPressed();
			}
		});
		profile_pic = findViewById(R.id.profile_pic);
		profile_title = findViewById(R.id.profile_title);
		linear_email = findViewById(R.id.linear_email);
		email = findViewById(R.id.email);
		linear_id = findViewById(R.id.linear_id);
		id = findViewById(R.id.id);
		linear_nickname = findViewById(R.id.linear_nickname);
		nickname = findViewById(R.id.nickname);
		linear_reset_password = findViewById(R.id.linear_reset_password);
		linear_signout = findViewById(R.id.linear_signout);
		data = getSharedPreferences("data", Activity.MODE_PRIVATE);
		userAuth = FirebaseAuth.getInstance();
		descriptionDialog = new AlertDialog.Builder(this);
		reset_password_dialog = new AlertDialog.Builder(this);
		
		linear_email.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				descriptionDialog.setTitle(R.string.email_dialog_title);
				descriptionDialog.setMessage(email.getText().toString());
				descriptionDialog.setPositiveButton("ok", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface _dialog, int _which) {
						
					}
				});
				descriptionDialog.create().show();
			}
		});
		
		linear_id.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				descriptionDialog.setTitle(R.string.user_id_dialog_title);
				descriptionDialog.setMessage(id.getText().toString());
				descriptionDialog.setPositiveButton("ok", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface _dialog, int _which) {
						
					}
				});
				descriptionDialog.create().show();
			}
		});
		
		linear_nickname.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				descriptionDialog.setTitle(R.string.nickname_dialog_title);
				descriptionDialog.setMessage(nickname.getText().toString());
				descriptionDialog.setPositiveButton("ok", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface _dialog, int _which) {
						
					}
				});
				descriptionDialog.create().show();
			}
		});

		linear_reset_password.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				final String emailAddress = data.getString("email", "");
				reset_password_dialog.setMessage(getString(R.string.reset_password_confirmation)+emailAddress+"?");
				reset_password_dialog.setTitle(R.string.reset_password);
				reset_password_dialog.setPositiveButton(R.string.send_button, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface _dialog, int _which) {
						if (!emailAddress.equals("")) {
							userAuth.sendPasswordResetEmail(emailAddress).addOnCompleteListener(new OnCompleteListener<Void>() {
								@Override
								public void onComplete(Task<Void> task) {
									final String _errorMessage = task.getException() != null ? task.getException().getMessage() : "";
									if (task.isSuccessful()) {
										SketchwareUtil.showMessage(getApplicationContext(), getString(R.string.email_sent_message));
									} else {
										SketchwareUtil.showMessage(getApplicationContext(), _errorMessage);
									}
								}
							});
						}
					}
				});
				reset_password_dialog.setNegativeButton(R.string.cancel_button, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
				reset_password_dialog.create().show();
			}
		});

		linear_signout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				DatabaseReference userdata = _firebase.getReference("users").child(userAuth.getCurrentUser().getUid());
				String deviceToken = FirebaseInstanceId.getInstance().getToken();
				if (deviceToken != null) {
					userdata.child("device_token").child(deviceToken).removeValue();
				}
			    userAuth.signOut();
				logout_int.setClass(getApplicationContext(), MainActivity.class);
				logout_int.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(logout_int);
				finish();
			}
		});
	}
	private void initializeLogic() {
		email.setText(data.getString("email", ""));
		id.setText(data.getString("id", ""));
		nickname.setText(data.getString("nickname", ""));
		profile_title.setText(data.getString("nickname", ""));
		Picasso.get().load(data.getString("profile_pic","")).transform(new DrawableUtil.CircleTransform()).into(profile_pic);
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

package com.moonface.home;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.SparseBooleanArray;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.moonface.Util.DrawableUtil;
import com.moonface.Util.FileUtil;
import com.moonface.Util.InputUtil;
import com.moonface.Util.ParametersUtil;
import com.moonface.Util.PermissionsRequest;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

public class MoonchatActivity extends AppCompatActivity {
	
	private FirebaseDatabase _firebase = FirebaseDatabase.getInstance();
	private FirebaseStorage _storage = FirebaseStorage.getInstance();

	private ArrayList<ChatMessage> chat_map;

    private RelativeLayout attach_linear;
	private ListView listview1;
	private EditText edittext1;
	private ImageView attached_image;

	private Intent camera;
	private Intent picker = new Intent(Intent.ACTION_PICK);
    private static String mImageFileLocation = "";
    private final int CAMERA_REQUEST = 1;
    private final int PICKER_REQUEST = 2;
	private Uri uri;
	private DatabaseReference chat_data = _firebase.getReference("chat");
	private StorageReference chat_images = _storage.getReference("chat");
	private SharedPreferences data;
	private Calendar time_get = Calendar.getInstance();
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.moonchat);
		initialize();
		initializeLogic();
	}
	
	private void initialize() {
	    chat_data.keepSynced(true);
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
		listview1 = findViewById(R.id.listview1);
		edittext1 = findViewById(R.id.edittext1);
		ImageView send = findViewById(R.id.send);
		ImageView attach = findViewById(R.id.attach);
		attach_linear = findViewById(R.id.attach_linear);
		attached_image = findViewById(R.id.attached_image);
		ImageView cancel = findViewById(R.id.cancel);
		data = getSharedPreferences("data", Activity.MODE_PRIVATE);

		send.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if ((edittext1.getText().toString().replace(" ", "").length() > 0) && (200 > edittext1.getText().toString().length())) {
					final ChatMessage message = new ChatMessage();
					message.setMessage(edittext1.getText().toString());
					message.setName(data.getString("nickname",""));
					message.setTime(new SimpleDateFormat("dd/MM HH:mm").format(time_get.getTime()));
					message.setAdmin(data.getBoolean("admin", false));
					message.setUid(data.getString("id",""));
					if(uri != null) {
                        Bitmap bitmap = FileUtil.getBitmap(uri, getApplicationContext());
                        bitmap = DrawableUtil.getResizedBitmap(bitmap, 512);
                        FileUtil.saveBitmap(bitmap, getExternalCacheDir().toString() + "/chatImage.jpg");
                        chat_images.child(uri.getLastPathSegment()).putFile(FileUtil.getUri(getApplicationContext(), new File(getExternalCacheDir().toString() + "/chatImage.jpg"))).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                boolean delete = new File(getExternalCacheDir().toString() + "/chatImage.jpg").delete();
                                uri = null;
                                taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        message.setImageUrl(uri.toString());
                                        chat_data.push().setValue(message);
                                    }
                                });
                            }
                        });
                        edittext1.setText("");
                        attached_image.setImageBitmap(null);
                        attach_linear.setVisibility(View.GONE);
                    } else {
					    message.setImageUrl("");
                        chat_data.push().setValue(message);
                        edittext1.setText("");
                    }
				}
				else {
					if (0 > edittext1.getText().toString().replace(" ", "").length()) {
						SketchwareUtil.showMessage(getApplicationContext(), getString(R.string.message_too_short));
					}
					if (edittext1.getText().toString().length() > 200) {
						SketchwareUtil.showMessage(getApplicationContext(), getString(R.string.message_too_long));
					}
				}
				InputUtil.hideKeyboard(getApplicationContext(), MoonchatActivity.this);
			}
		});

		attach.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence methods[] = new CharSequence[] {getString(R.string.camera), getString(R.string.gallery)};
                AlertDialog.Builder builder = new AlertDialog.Builder(MoonchatActivity.this);
                builder.setTitle(R.string.profile_photo_dialog_title);
                builder.setItems(methods, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case 0:
                                camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                if (camera.resolveActivity(getPackageManager()) != null) {
                                    File photoFile;
                                    photoFile = createImageFile();

                                    uri = FileProvider.getUriForFile(getApplicationContext(), getString(R.string.authorities), photoFile);
                                    camera.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                                    if(PermissionsRequest.isGranted(PermissionsRequest.CAMERA, getApplicationContext())) {
                                        startActivityForResult(camera, CAMERA_REQUEST);
                                    }
                                    else {
                                        PermissionsRequest permissionsRequest = new PermissionsRequest(MoonchatActivity.this, new String[]{PermissionsRequest.CAMERA});
                                        permissionsRequest.showRequest();
                                    }
                                }
                                break;
                            case 1:
                                picker.setType("image/*");
                                startActivityForResult(picker, PICKER_REQUEST);
                                break;
                        }
                    }
                });
                builder.show();
            }
        });
		cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                attached_image.setImageBitmap(null);
                attach_linear.setVisibility(View.GONE);
            }
        });

		ChildEventListener _chat_data_child_listener = new ChildEventListener() {
			@Override
			public void onChildAdded(DataSnapshot _param1, String _param2) {
				chat_data.addListenerForSingleValueEvent(new ValueEventListener() {
					@Override
					public void onDataChange(DataSnapshot _dataSnapshot) {
						chat_map = new ArrayList<>();
						try {
							GenericTypeIndicator<ChatMessage> _ind = new GenericTypeIndicator<ChatMessage>() {
							};
							for (DataSnapshot _data : _dataSnapshot.getChildren()) {
								final ChatMessage _map = _data.getValue(_ind);
								_map.setKey(_data.getKey());
								chat_map.add(_map);
							}
							final Listview1Adapter adapter = new Listview1Adapter(chat_map);
							listview1.setAdapter(adapter);
							adapter.notifyDataSetChanged();
						} catch (Exception _e) {
							_e.printStackTrace();
						}
					}

					@Override
					public void onCancelled(DatabaseError _databaseError) {
					}
				});
			}

			@Override
			public void onChildChanged(DataSnapshot _param1, String _param2) {
			}

			@Override
			public void onChildMoved(DataSnapshot _param1, String _param2) {

			}

			@Override
			public void onChildRemoved(DataSnapshot _param1) {
				chat_data.addListenerForSingleValueEvent(new ValueEventListener() {
					@Override
					public void onDataChange(DataSnapshot _dataSnapshot) {
						chat_map = new ArrayList<>();
						try {
							GenericTypeIndicator<ChatMessage> _ind = new GenericTypeIndicator<ChatMessage>() {
							};
							for (DataSnapshot _data : _dataSnapshot.getChildren()) {
								final ChatMessage _map = _data.getValue(_ind);
								_map.setKey(_data.getKey());
								chat_map.add(_map);
							}
							final Listview1Adapter adapter = new Listview1Adapter(chat_map);
							listview1.setAdapter(adapter);
							adapter.notifyDataSetChanged();
						} catch (Exception _e) {
							_e.printStackTrace();
						}
					}

					@Override
					public void onCancelled(DatabaseError _databaseError) {
					}
				});
			}

			@Override
			public void onCancelled(DatabaseError _param1) {
			}
		};
		chat_data.addChildEventListener(_chat_data_child_listener);
	}
	private void initializeLogic() {
		Window w = this.getWindow();w.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			w.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
			w.setStatusBarColor(Color.parseColor("#5a9216"));
		}
		listview1.setDivider(null);
		listview1.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
		listview1.setStackFromBottom(true);
	}
	@Override
	public void onBackPressed() {
		finish();
	}
	public class Listview1Adapter extends BaseAdapter {
		ArrayList<ChatMessage> _data;
		public Listview1Adapter(ArrayList<ChatMessage> _arr) {
			_data = _arr;
		}
		
		@Override
		public int getCount() {
			return _data.size();
		}
		
		@Override
		public ChatMessage getItem(int _index) {
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
				_v = _inflater.inflate(R.layout.message, null);
			}
			
			final LinearLayout linear8 = _v.findViewById(R.id.linear8);
			final LinearLayout linear1 = _v.findViewById(R.id.linear1);
			final ImageView imageview1 = _v.findViewById(R.id.imageview1);
			final TextView textview1 = _v.findViewById(R.id.textview1);
			final TextView textview2 = _v.findViewById(R.id.textview2);
			final ImageView bin = _v.findViewById(R.id.bin);
			final TextView textview3 = _v.findViewById(R.id.textview3);
			final ImageView attached_image = _v.findViewById(R.id.attached_image);

			double width = Math.round(SketchwareUtil.getDisplayWidthPixels(getApplicationContext()) * 0.75d);
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams((int) width, LinearLayout.LayoutParams.WRAP_CONTENT);
			lp.setMargins(Math.round(ParametersUtil.dpsToPixels(8,getApplicationContext())), Math.round(ParametersUtil.dpsToPixels(8,getApplicationContext())), Math.round(ParametersUtil.dpsToPixels(8,getApplicationContext())), Math.round(ParametersUtil.dpsToPixels(8,getApplicationContext())));
			linear1.setLayoutParams(lp);
			if (_data.get(_position).getUid().equals(data.getString("id", ""))) {
				bin.setVisibility(View.VISIBLE);
				android.graphics.drawable.GradientDrawable linearLy = new android.graphics.drawable.GradientDrawable();  linearLy.setColor(0xFFCDDC39);  linearLy.setCornerRadius(30);  linear1.setBackground(linearLy);
				linear8.setGravity(Gravity.RIGHT);
			}
			else {
				bin.setVisibility(View.GONE);
				android.graphics.drawable.GradientDrawable linearLy = new android.graphics.drawable.GradientDrawable();  linearLy.setColor(0xFFFFFFFF);  linearLy.setCornerRadius(30);  linear1.setBackground(linearLy);
				linear8.setGravity(Gravity.LEFT);
			}
			textview1.setText(_data.get(_position).getName());
			textview2.setText(_data.get(_position).getTime());
			textview3.setText(_data.get(_position).getMessage());
			imageview1.setVisibility(View.GONE);
			textview1.setTextColor(0xFF000000);
			if (_data.get(_position).isAdmin()) {
                textview1.setTextColor(0xFFB71C1C);
			    imageview1.setImageResource(R.drawable.king);
				imageview1.setVisibility(View.VISIBLE);
			}
			if (data.getBoolean("admin", false)) {
				bin.setVisibility(View.VISIBLE);
			}
            attached_image.setVisibility(View.GONE);
			if(!_data.get(_position).getImageUrl().equals("")) {
                String url = getItem(_position).getImageUrl();
                Picasso.get().load(url).into(attached_image);
                attached_image.setVisibility(View.VISIBLE);
            }
			bin.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View _view) {
					chat_data.child(_data.get(_position).getKey()).removeValue();
				}
			});
			
			return _v;
		}
	}
    File createImageFile() {
        File file = new File(getExternalCacheDir().toString() + "/chatImage.jpg");
        mImageFileLocation = file.getAbsolutePath();
        return file;

    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 1:
                    Bitmap imageBitmap = FileUtil.getBitmap(uri, this);
                    imageBitmap = DrawableUtil.getResizedBitmap(imageBitmap, 512);
                    attached_image.setImageBitmap(imageBitmap);
                    attach_linear.setVisibility(View.VISIBLE);
                    break;
                case 2:
                    uri = data.getData();
                    imageBitmap = FileUtil.getBitmap(uri, this);
                    imageBitmap = DrawableUtil.getResizedBitmap(imageBitmap, 512);
                    attached_image.setImageBitmap(imageBitmap);
                    attach_linear.setVisibility(View.VISIBLE);
                    break;
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

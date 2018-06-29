package com.moonface.home;

import android.app.*;
import android.graphics.drawable.Drawable;
import android.os.*;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.text.*;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import java.util.HashMap;
import java.util.ArrayList;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.moonface.Util.DrawableUtil;
import com.moonface.Util.FileUtil;
import com.moonface.Util.InputUtil;
import com.moonface.Util.ParametersUtil;
import com.moonface.Util.PermissionsRequest;

import android.app.Activity;
import android.content.SharedPreferences;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import android.view.View;

public class MoonchatActivity extends AppCompatActivity {
	
	private FirebaseDatabase _firebase = FirebaseDatabase.getInstance();
	private FirebaseStorage _storage = FirebaseStorage.getInstance();
	
	private Toolbar _toolbar;
	private double width = 0;
    private int targetWidth;
    private int targetHeight;
    private ArrayList<ChatMessage> chat_map;

    private RelativeLayout attach_linear;
	private ListView listview1;
	private EditText edittext1;
	private ImageView send;
	private ImageView attach;
	private ImageView attached_image;
	private ImageView cancel;

	private Intent camera;
	private Intent picker = new Intent(Intent.ACTION_PICK);
    private static String mImageFileLocation = "";
    private final int CAMERA_REQUEST = 1;
    private final int PICKER_REQUEST = 2;
	private Uri uri;
	private DatabaseReference chat_data = _firebase.getReference("chat");
	private StorageReference chat_images = _storage.getReference("chat");
	private ChildEventListener _chat_data_child_listener;
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
		listview1 = findViewById(R.id.listview1);
		edittext1 = findViewById(R.id.edittext1);
		send = findViewById(R.id.send);
		attach = findViewById(R.id.attach);
		attach_linear = findViewById(R.id.attach_linear);
		attached_image = findViewById(R.id.attached_image);
		cancel = findViewById(R.id.cancel);
		data = getSharedPreferences("data", Activity.MODE_PRIVATE);
        targetWidth = attached_image.getMaxWidth();
        targetHeight = 300;

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
                        bitmap = DrawableUtil.getRotatedBitmap(bitmap, DrawableUtil.getRotation(mImageFileLocation));
                        bitmap = DrawableUtil.getResizedBitmap(bitmap, 512);
                        FileUtil.saveBitmap(bitmap, getExternalCacheDir().toString() + "/chatImage.jpg");
                        chat_images.child(uri.getLastPathSegment()).putFile(FileUtil.getUri(getApplicationContext(), new File(getExternalCacheDir().toString() + "/chatImage.jpg"))).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                boolean delete = new File(getExternalCacheDir().toString() + "/chatImage.jpg").delete();
                                uri = null;
                                message.setImageUrl(taskSnapshot.getStorage().getDownloadUrl().toString());
                                chat_data.push().setValue(message);
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

		_chat_data_child_listener = new ChildEventListener() {
			@Override
			public void onChildAdded(DataSnapshot _param1, String _param2) {
				final String _childKey = _param1.getKey();
				chat_data.addListenerForSingleValueEvent(new ValueEventListener() {
					@Override
					public void onDataChange(DataSnapshot _dataSnapshot) {
					    chat_map = new ArrayList<>();
						try {
							GenericTypeIndicator<ChatMessage> _ind = new GenericTypeIndicator<ChatMessage>() {};
                            for (DataSnapshot _data : _dataSnapshot.getChildren()) {
                                final ChatMessage _map = _data.getValue(_ind);
                                _map.setKey(_data.getKey());
                                chat_map.add(_map);
                            }
                            getImage(0);
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
			public void onChildChanged(DataSnapshot _param1, String _param2) {
				GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
				final String _childKey = _param1.getKey();
				final HashMap<String, Object> _childValue = _param1.getValue(_ind);
			}
			
			@Override
			public void onChildMoved(DataSnapshot _param1, String _param2) {
				
			}
			
			@Override
			public void onChildRemoved(DataSnapshot _param1) {
				GenericTypeIndicator<ChatMessage> _ind = new GenericTypeIndicator<ChatMessage>() {};
				final String _childKey = _param1.getKey();
				final ChatMessage _childValue = _param1.getValue(_ind);
				chat_data.addListenerForSingleValueEvent(new ValueEventListener() {
					@Override
					public void onDataChange(DataSnapshot _dataSnapshot) {
                        chat_map = new ArrayList<>();
                        try {
                            GenericTypeIndicator<ChatMessage> _ind = new GenericTypeIndicator<ChatMessage>() {};
                            for (DataSnapshot _data : _dataSnapshot.getChildren()) {
                                final ChatMessage _map = _data.getValue(_ind);
                                _map.setKey(_data.getKey());
                                chat_map.add(_map);
                            }
                            getImage(0);
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
			public void onCancelled(DatabaseError _param1) {
				final String _errorCode = String.valueOf(_param1.getCode());
				final String _errorMessage = _param1.getMessage();
				
			}
		};
		chat_data.addChildEventListener(_chat_data_child_listener);
	}
	private void initializeLogic() {
		Window w = this.getWindow();w.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);w.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS); w.setStatusBarColor(Color.parseColor("#5a9216"));
		listview1.setDivider(null);
		listview1.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
		listview1.setStackFromBottom(true);
	}
	@Override
	public void onBackPressed() {
		finish();
	}
	private void getImage(int i) throws IOException {
        if(i == chat_map.size()) {
            final Listview1Adapter adapter = new Listview1Adapter(chat_map);
            listview1.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            return;
        }
	    final ChatMessage _map = chat_map.get(i);
	    if (!_map.getImageUrl().equals("")) {
            final File image = File.createTempFile("temp_image", ".jpg", getExternalCacheDir());
            final int finalI = i;
            _storage.getReferenceFromUrl(_map.getImageUrl()).getFile(image).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Uri fileUri = FileProvider.getUriForFile(getApplicationContext(), getString(R.string.authorities), image);
                    Bitmap img = null;
                    try {
                        img = MediaStore.Images.Media.getBitmap(getContentResolver(), fileUri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    _map.setImage(img);
                    chat_map.set(finalI, _map);
                    boolean delete = image.delete();
                    try {
                        getImage(finalI+1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        } else {
            try {
                getImage(i+1);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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
			
			width = Math.round(SketchwareUtil.getDisplayWidthPixels(getApplicationContext()) * 0.75d);
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams((int)width, LinearLayout.LayoutParams.WRAP_CONTENT);
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
                attached_image.setImageBitmap((Bitmap) _data.get(_position).getImage());
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
                    imageBitmap = DrawableUtil.getRotatedBitmap(imageBitmap, DrawableUtil.getRotation(mImageFileLocation));
                    imageBitmap = DrawableUtil.getResizedBitmap(imageBitmap, 512);
                    attached_image.setImageBitmap(imageBitmap);
                    attach_linear.setVisibility(View.VISIBLE);
                    break;
                case 2:
                    uri = data.getData();
                    imageBitmap = FileUtil.getBitmap(uri, this);
                    imageBitmap = DrawableUtil.getRotatedBitmap(imageBitmap, DrawableUtil.getRotation(uri.toString()));
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

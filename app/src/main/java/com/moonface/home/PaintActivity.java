package com.moonface.home;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.*;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.view.menu.ListMenuItemView;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.*;
import android.widget.*;
import android.content.*;
import android.graphics.*;
import android.net.*;
import android.util.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.LinearLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.HorizontalScrollView;
import android.widget.Switch;
import android.widget.SeekBar;
import android.app.Activity;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.CompoundButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.moonface.Util.DrawableUtil;
import com.moonface.Util.FileUtil;
import com.moonface.Util.FirebaseUtil;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

public class PaintActivity extends AppCompatActivity {
	
	private FirebaseStorage _storage = FirebaseStorage.getInstance();
	private FirebaseDatabase _database = FirebaseDatabase.getInstance();
	private Toolbar _toolbar;
	
	private LinearLayout linear2;
	private LinearLayout red;
	private LinearLayout pink;
	private LinearLayout purple;
	private LinearLayout blue;
	private LinearLayout cyan;
	private LinearLayout green;
	private LinearLayout lime;
	private LinearLayout yellow;
	private LinearLayout orange;
	private LinearLayout brown;
	private LinearLayout grey;
	private LinearLayout black;
	private LinearLayout white;
	private TextView red_button;
	private TextView pink_button;
	private TextView purple_button;
	private TextView blue_button;
	private TextView cyan_button;
	private TextView green_button;
	private TextView lime_button;
	private TextView yellow_button;
	private TextView orange_button;
	private TextView brown_button;
	private TextView grey_button;
	private TextView black_button;
	private TextView white_button;
	private Switch switch1;
	private Switch switch2;
	private SeekBar seekbar1;

	private int colorBefore = 0;
	private String url;
	private File folder;
	private File cacheFolder;
	private View viewBefore = null;
	private String paintingName;
	private SharedPreferences data;
	private StorageReference paintingReference = _storage.getReference("/saved_paintings");
	private DatabaseReference paintingData;
    private AlertDialog.Builder edittextdialog;
	
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.paint);
		initialize();
		initializeLogic();
	}
	
	private void initialize() {
		cacheFolder = new File(getExternalCacheDir().toString() + "/paintings");
		if(!cacheFolder.exists()){
			if(!folder.mkdir()){
				Log.d("Paint", "Can't create folder");
			}
		}
        folder = new File(cacheFolder.getPath() + "/" + FirebaseAuth.getInstance().getCurrentUser().getUid());
        if(!folder.exists()){
            if(!folder.mkdir()){
                Log.d("Paint", "Can't create folder");
            }
        }
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
		linear2 = findViewById(R.id.linear2);
		red = findViewById(R.id.red);
		pink = findViewById(R.id.pink);
		purple = findViewById(R.id.purple);
		blue = findViewById(R.id.blue);
		cyan = findViewById(R.id.cyan);
		green = findViewById(R.id.green);
		lime = findViewById(R.id.lime);
		yellow = findViewById(R.id.yellow);
		orange = findViewById(R.id.orange);
		brown = findViewById(R.id.brown);
		grey = findViewById(R.id.grey);
		black = findViewById(R.id.black);
		white = findViewById(R.id.white);
		red_button = findViewById(R.id.red_button);
		pink_button = findViewById(R.id.pink_button);
		purple_button = findViewById(R.id.purple_button);
		blue_button = findViewById(R.id.blue_button);
		cyan_button = findViewById(R.id.cyan_button);
		green_button = findViewById(R.id.green_button);
		lime_button = findViewById(R.id.lime_button);
		yellow_button = findViewById(R.id.yellow_button);
		orange_button = findViewById(R.id.orange_button);
		brown_button = findViewById(R.id.brown_button);
		grey_button = findViewById(R.id.grey_button);
		black_button = findViewById(R.id.black_button);
		white_button = findViewById(R.id.white_button);
		switch1 = findViewById(R.id.switch1);
		switch2 = findViewById(R.id.switch2);
		seekbar1 = findViewById(R.id.seekbar1);
		data = getSharedPreferences("data", MODE_PRIVATE);
		paintingData = _database.getReference("users").child(data.getString("id","")).child("paintData");
        edittextdialog = new AlertDialog.Builder(this);

		red_button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				mPaint.setColor(Color.RED);
				_set_scale(red);
			}
		});
		
		pink_button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				mPaint.setColor(0xFFF8BBD0);
				_set_scale(pink);
			}
		});
		
		purple_button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				mPaint.setColor(0xFF9C27B0);
				_set_scale(purple);
			}
		});
		
		blue_button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				mPaint.setColor(0xFF2196F3);
				_set_scale(blue);
			}
		});
		
		cyan_button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				mPaint.setColor(Color.CYAN);
				_set_scale(cyan);
			}
		});
		
		green_button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				mPaint.setColor(0xFF4CAF50);
				_set_scale(green);
			}
		});
		
		lime_button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				mPaint.setColor(0xFFCDDC39);
				_set_scale(lime);
			}
		});
		
		yellow_button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				mPaint.setColor(Color.YELLOW);
				_set_scale(yellow);
			}
		});
		
		orange_button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				mPaint.setColor(0xFFFF9800);
				_set_scale(orange);
			}
		});
		
		brown_button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				mPaint.setColor(0xFF895514);
				_set_scale(brown);
			}
		});
		
		grey_button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				mPaint.setColor(0xFF9E9E9E);
				_set_scale(grey);
			}
		});
		
		black_button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				mPaint.setColor(Color.BLACK);
				_set_scale(black);
			}
		});
		
		white_button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				mPaint.setColor(Color.WHITE);
				_set_scale(white);
			}
		});
		
		switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton _param1, boolean _param2)  {
                if (_param2) {
					mPaint.setStyle(Paint.Style.FILL);
					seekbar1.setEnabled(false);
				}
				else {
					mPaint.setStyle(Paint.Style.STROKE);
					seekbar1.setEnabled(true);
				}
			}
		});
		switch2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    colorBefore = mPaint.getColor();
                    mPaint.setColor(Color.parseColor("#EEEEEE"));
                    red_button.setEnabled(false);
                    pink_button.setEnabled(false);
                    purple_button.setEnabled(false);
                    blue_button.setEnabled(false);
                    cyan_button.setEnabled(false);
                    green_button.setEnabled(false);
                    lime_button.setEnabled(false);
                    yellow_button.setEnabled(false);
                    orange_button.setEnabled(false);
                    brown_button.setEnabled(false);
                    grey_button.setEnabled(false);
                    black_button.setEnabled(false);
                    white_button.setEnabled(false);
                    _set_scale(null);
                }
                else {
                    if(colorBefore != 0) {
                        mPaint.setColor(colorBefore);
                    }
                    _set_scale(viewBefore);
                    red_button.setEnabled(true);
                    pink_button.setEnabled(true);
                    purple_button.setEnabled(true);
                    blue_button.setEnabled(true);
                    cyan_button.setEnabled(true);
                    green_button.setEnabled(true);
                    lime_button.setEnabled(true);
                    yellow_button.setEnabled(true);
                    orange_button.setEnabled(true);
                    brown_button.setEnabled(true);
                    grey_button.setEnabled(true);
                    black_button.setEnabled(true);
                    white_button.setEnabled(true);
                }
            }
        });
		
		seekbar1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged (SeekBar _param1, int _param2, boolean _param3) {
				final int _progressValue = _param2;
				mPaint.setStrokeWidth(_progressValue);
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
		Window w = this.getWindow();w.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			w.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
		}
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			w.setStatusBarColor(Color.parseColor("#4f9a94"));
		}
		_set_scale(red);
		_setview();
		dv = new DrawingView(this);linear2.addView(dv);
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setDither(true);
		mPaint.setColor(Color.RED);
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setStrokeJoin(Paint.Join.ROUND);
		mPaint.setStrokeCap(Paint.Cap.ROUND);
		mPaint.setStrokeWidth(12);
        final ProgressDialog progressDialog = new ProgressDialog(PaintActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getString(R.string.retrieving_data));
        progressDialog.show();
        paintingData.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final ArrayList<HashMap<String, Object>> retrieve_list = new ArrayList<>();
                try {
                    GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
                    for (DataSnapshot _data : dataSnapshot.getChildren()) {
                        HashMap<String, Object> _map = _data.getValue(_ind);
                        retrieve_list.add(_map);
                    }
                }
                catch (Exception _e) {
                    _e.printStackTrace();
                }
                CharSequence names[] = new CharSequence[retrieve_list.size()+1];
                SpannableString boldOption = new SpannableString(getString(R.string.create_new_painting));
                boldOption.setSpan(new StyleSpan(Typeface.BOLD), 0, getString(R.string.create_new_painting).length(),0);
                names[0] = boldOption;
                for(int i=0; i<retrieve_list.size(); i++){
                    names[i+1] = retrieve_list.get(i).get("name").toString();
                }
                final AlertDialog.Builder builder = new AlertDialog.Builder(PaintActivity.this);
                builder.setItems(names, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(which == 0){
                            LinearLayout dialayout = new LinearLayout(getApplicationContext());
                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                            dialayout.setLayoutParams(params);
                            dialayout.setOrientation(LinearLayout.VERTICAL);

                            final EditText diaedittext = new EditText(getApplicationContext());
                            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                            layoutParams.setMargins(8,8,8,8);
                            diaedittext.setLayoutParams(layoutParams);
                            dialayout.addView(diaedittext);
                            edittextdialog.setView(dialayout);
                            diaedittext.setHint(R.string.paint_save_hint);
                            edittextdialog.setTitle(R.string.paint_save_title);
                            edittextdialog.setCancelable(false);
                            edittextdialog.setPositiveButton(R.string.paint_save_button, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface _dialog, int _which) {
                                	paintingName = diaedittext.getText().toString();
									if(paintingName.length() == 0){
										Toast.makeText(getApplicationContext(), getString(R.string.paint_name_blank_message), Toast.LENGTH_LONG).show();
										builder.show();
									}
                                    for(int i=0;i<retrieve_list.size();i++){
                                    	if(paintingName.equals(retrieve_list.get(i).get("name").toString())){
                                            Toast.makeText(getApplicationContext(), getString(R.string.paint_name_message), Toast.LENGTH_LONG).show();
                                            builder.show();
                                            break;
										}
									}
                                    for (char c : paintingName.toCharArray()) {
                                        if (!Character.isLetterOrDigit(c)){
                                            Toast.makeText(getApplicationContext(), getString(R.string.paint_name_invalid), Toast.LENGTH_LONG).show();
                                            builder.show();
                                            break;
                                        }
                                    }
                                    linear2.setBackgroundColor(Color.parseColor("#EEEEEE"));

                                }
                            });
                            edittextdialog.create().show();
                        } else {
                            url = retrieve_list.get(which-1).get("url").toString();
                            paintingName = retrieve_list.get(which-1).get("name").toString();
                            final ProgressDialog progress = new ProgressDialog(PaintActivity.this);
                            progress.setCancelable(false);
                            progress.setTitle(R.string.paint_prog_title);
                            progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                            progress.setIndeterminate(true);
                            progress.show();
                            File pictureFile = new File(folder.toString() + "/" + paintingName + ".jpg");
                            if(pictureFile.exists()) {
                                linear2.setBackground(DrawableUtil.BitmapToDrawable(FileUtil.getBitmap(pictureFile, getApplicationContext()), getApplicationContext()));
                                progress.dismiss();
                                Toast.makeText(getApplicationContext(), getString(R.string.painting_loaded_successfully_message), Toast.LENGTH_SHORT).show();
                            } else {
                                Picasso.get().load(url).into(new Target() {
                                    @Override
                                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                        linear2.setBackground(new BitmapDrawable(getResources(), bitmap));
                                        progress.dismiss();
                                        Toast.makeText(getApplicationContext(), getString(R.string.painting_loaded_successfully_message), Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                                    }

                                    @Override
                                    public void onPrepareLoad(Drawable placeHolderDrawable) {
                                    }
                                });
                            }
                        }
                    }
                });
                builder.setCancelable(false);
                builder.show();
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                databaseError.toException().printStackTrace();
            }
        });
    }
	
	DrawingView dv;
	private Paint mPaint;
	private Canvas mCanvas;
	
	public class DrawingView extends View {
	    public int width;
		public int height;
		private Bitmap mBitmap;
		private Path mPath;
		private Paint mBitmapPaint;
		Context context; private Paint circlePaint; private Path circlePath;
		
		public DrawingView(Context c) { super(c); context=c; mPath = new Path(); mBitmapPaint = new Paint(Paint.DITHER_FLAG); circlePaint = new Paint(); circlePath = new Path(); circlePaint.setAntiAlias(true); circlePaint.setColor(Color.BLACK); circlePaint.setStyle(Paint.Style.STROKE); circlePaint.setStrokeJoin(Paint.Join.MITER); circlePaint.setStrokeWidth(4f); }
		
		@Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		    super.onSizeChanged(w, h, oldw, oldh);
		    mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		    mCanvas = new Canvas(mBitmap);
		}
		
		@Override
        protected void onDraw(Canvas canvas) {
		    super.onDraw(canvas);
		    canvas.drawBitmap( mBitmap, 0, 0, mBitmapPaint);
			canvas.drawPath( mPath, mPaint); canvas.drawPath( circlePath, circlePaint);
			invalidate(); }
		
		private float mX, mY;
		private static final float TOUCH_TOLERANCE = 4;
		private void touch_start(float x, float y) { mPath.reset(); mPath.moveTo(x, y); mX = x; mY = y; }
		
		private void touch_move(float x, float y) { float dx = Math.abs(x - mX); float dy = Math.abs(y - mY); if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) { mPath.quadTo(mX, mY, (x + mX)/2, (y + mY)/2); mX = x; mY = y; circlePath.reset(); circlePath.addCircle(mX, mY, 30, Path.Direction.CW); } }
		private void touch_up() { mPath.lineTo(mX, mY); circlePath.reset();
			mCanvas.drawPath(mPath, mPaint);
			mPath.reset(); }
		
		@Override public boolean onTouchEvent(MotionEvent event) {
			float x = event.getX(); float y = event.getY();
			switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN: touch_start(x, y); invalidate(); break;
				case MotionEvent.ACTION_MOVE: touch_move(x, y); invalidate(); break;
				case MotionEvent.ACTION_UP: touch_up(); invalidate(); break; } return true; }
	}
	private static int RESULT_LOAD_IMAGE = 1;
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    super.onActivityResult(requestCode, resultCode, data);
	    if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
	        Uri selectedImage = data.getData();
			try {
                java.io.InputStream is = null;
                if (selectedImage != null) {
                    is = getContentResolver().openInputStream(selectedImage);
                }
                Drawable bg_img = Drawable.createFromStream(is, selectedImage.toString());
                mCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
				linear2.setBackground(bg_img); }
			
			catch (java.io.FileNotFoundException e) {
				Log.e("FileNotFoundException", e.toString());
			}
	    }
	}
	
	@Override
	public void onBackPressed() {
		final AlertDialog.Builder exit_dialog = new AlertDialog.Builder(this);
		exit_dialog.setTitle(R.string.exit);
		exit_dialog.setMessage(getString(R.string.save_before_exit_message) + paintingName + "?");
		String save_button;
		if(url != null){
		    save_button = getString(R.string.save_changes_button);
        } else {
		    save_button = getString(R.string.save_button);
        }
		exit_dialog.setPositiveButton(save_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                _save_method(linear2, true);
            }
        });
		exit_dialog.setNegativeButton(R.string.dont_save_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
		exit_dialog.setNeutralButton(R.string.cancel_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
		exit_dialog.create().show();
	}
	private void _share_method (final View _view) {
		Bitmap image = Bitmap.createBitmap(_view.getWidth(), _view.getHeight(), Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(image);
		android.graphics.drawable.Drawable bgDrawable = _view.getBackground();
		if (bgDrawable!=null) { bgDrawable.draw(canvas); } else{ canvas.drawColor(Color.WHITE); }
		_view.draw(canvas);
		
		File pictureFile = new File(getExternalCacheDir() + "/image.jpg");
        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            image.compress(Bitmap.CompressFormat.PNG, 0, fos);
            fos.close();
        }
        catch (FileNotFoundException e) {
            Log.d("MainActivity", "File not found: " + e.getMessage());
        }
        catch (IOException e) {
            Log.d("MainActivity", "Error accessing file: " + e.getMessage());
        }
		
		Intent iten = new Intent(android.content.Intent.ACTION_SEND);
		iten.setType("*/*");
		Uri uri = FileProvider.getUriForFile(getApplicationContext(), getString(R.string.authorities), new File(getExternalCacheDir() + "/image.jpg"));
		iten.putExtra(Intent.EXTRA_STREAM, uri);
		startActivity(Intent.createChooser(iten, getString(R.string.send_image_label)));
	}
	private void _save_method(final View _view, final boolean finish) {
        Bitmap image = Bitmap.createBitmap(_view.getWidth(), _view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(image);
        android.graphics.drawable.Drawable bgDrawable = _view.getBackground();
        if (bgDrawable != null) {
            bgDrawable.draw(canvas);
        } else {
            canvas.drawColor(Color.WHITE);
        }
        _view.draw(canvas);
        File pictureFile = FileUtil.saveBitmap(image, folder.toString() + "/" + paintingName + ".jpg");
        final ProgressDialog progress = new ProgressDialog(PaintActivity.this);
        progress.setCancelable(false);
        progress.setTitle(R.string.paint_prog_upload_title);
        progress.setMax(100);
        progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        StorageReference saveReference;
        saveReference = paintingReference.child(data.getString("id", "")).child(paintingName);
        saveReference.putFile(FileProvider.getUriForFile(getApplicationContext(), getString(R.string.authorities), pictureFile)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {
                progress.dismiss();
                final HashMap<String, Object> fileMetadata = new HashMap<>();
                fileMetadata.put("name", paintingName);
                taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
					@Override
					public void onSuccess(Uri uri) {
						fileMetadata.put("url", uri.toString());
						paintingData.child(paintingName).updateChildren(fileMetadata);
						Toast.makeText(getApplicationContext(), getString(R.string.painting_saved_successfully_message), Toast.LENGTH_SHORT).show();
						if(finish){
							finish();
						}
					}
				});
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double progress_value = 100 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount();
                progress.setProgress(Integer.parseInt(String.valueOf(Math.round(progress_value))));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
            }
        });
        progress.show();
    }
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.paint, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.action_set_background:
                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE);
				return true;

			case R.id.action_clear:
                mCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
                linear2.setBackgroundColor(Color.parseColor("#EEEEEE"));
				return true;

            case R.id.action_share:
                _share_method(linear2);
                return true;
            case R.id.action_save:
                _save_method(linear2, false);
                return true;
			default:
				return super.onOptionsItemSelected(item);

		}
	}
	
	private void _set_scale (final View _view) {
		red.setScaleX((float)(1.0d));
		red.setScaleY((float)(1.0d));
		pink.setScaleX((float)(1.0d));
		pink.setScaleY((float)(1.0d));
		purple.setScaleX((float)(1.0d));
		purple.setScaleY((float)(1.0d));
		blue.setScaleX((float)(1.0d));
		blue.setScaleY((float)(1.0d));
		cyan.setScaleX((float)(1.0d));
		cyan.setScaleY((float)(1.0d));
		green.setScaleX((float)(1.0d));
		green.setScaleY((float)(1.0d));
		lime.setScaleX((float)(1.0d));
		lime.setScaleY((float)(1.0d));
		yellow.setScaleX((float)(1.0d));
		yellow.setScaleY((float)(1.0d));
		orange.setScaleX((float)(1.0d));
		orange.setScaleY((float)(1.0d));
		brown.setScaleX((float)(1.0d));
		brown.setScaleY((float)(1.0d));
		grey.setScaleX((float)(1.0d));
		grey.setScaleY((float)(1.0d));
		black.setScaleX((float)(1.0d));
		black.setScaleY((float)(1.0d));
		white.setScaleX((float)(1.0d));
		white.setScaleY((float)(1.0d));
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { red.setElevation(4f);}
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { pink.setElevation(4f);}
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { purple.setElevation(4f);}
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { blue.setElevation(4f);}
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { cyan.setElevation(4f);}
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { green.setElevation(4f);}
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { lime.setElevation(4f);}
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { yellow.setElevation(4f);}
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { orange.setElevation(4f);}
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { brown.setElevation(4f);}
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { grey.setElevation(4f);}
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { black.setElevation(4f);}
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { white.setElevation(4f);}
		if(_view != null) {
            _view.setScaleX((float) (1.2d));
            _view.setScaleY((float) (1.2d));
            viewBefore = _view;
        }
	}
	
	
	private void _setview () {
		android.graphics.drawable.GradientDrawable linearRed = new android.graphics.drawable.GradientDrawable();  linearRed.setColor(Color.RED);  linearRed.setCornerRadius(45);  red.setBackground(linearRed);
		android.graphics.drawable.GradientDrawable linearPink = new android.graphics.drawable.GradientDrawable();  linearPink.setColor(0xFFF8BBD0);  linearPink.setCornerRadius(45);  pink.setBackground(linearPink);
		android.graphics.drawable.GradientDrawable linearPurple = new android.graphics.drawable.GradientDrawable();  linearPurple.setColor(0xFF9C27B0);  linearPurple.setCornerRadius(45);  purple.setBackground(linearPurple);
		android.graphics.drawable.GradientDrawable linearBlue = new android.graphics.drawable.GradientDrawable();  linearBlue.setColor(0xFF2196F3);  linearBlue.setCornerRadius(45);  blue.setBackground(linearBlue);
		android.graphics.drawable.GradientDrawable linearCyan = new android.graphics.drawable.GradientDrawable();  linearCyan.setColor(Color.CYAN);  linearCyan.setCornerRadius(45);  cyan.setBackground(linearCyan);
		android.graphics.drawable.GradientDrawable linearGreen = new android.graphics.drawable.GradientDrawable();  linearGreen.setColor(0xFF4CAF50);  linearGreen.setCornerRadius(45);  green.setBackground(linearGreen);
		android.graphics.drawable.GradientDrawable linearLime = new android.graphics.drawable.GradientDrawable();  linearLime.setColor(0xFFCDDC39);  linearLime.setCornerRadius(45);  lime.setBackground(linearLime);
		android.graphics.drawable.GradientDrawable linearYellow = new android.graphics.drawable.GradientDrawable();  linearYellow.setColor(Color.YELLOW);  linearYellow.setCornerRadius(45);  yellow.setBackground(linearYellow);
		android.graphics.drawable.GradientDrawable linearOrange = new android.graphics.drawable.GradientDrawable();  linearOrange.setColor(0xFFFF9800);  linearOrange.setCornerRadius(45);  orange.setBackground(linearOrange);
		android.graphics.drawable.GradientDrawable linearBrown = new android.graphics.drawable.GradientDrawable();  linearBrown.setColor(0xFF895514);  linearBrown.setCornerRadius(45);  brown.setBackground(linearBrown);
		android.graphics.drawable.GradientDrawable linearGrey = new android.graphics.drawable.GradientDrawable();  linearGrey.setColor(0xFF9E9E9E);  linearGrey.setCornerRadius(45);  grey.setBackground(linearGrey);
		android.graphics.drawable.GradientDrawable linearBlack = new android.graphics.drawable.GradientDrawable();  linearBlack.setColor(Color.BLACK);  linearBlack.setCornerRadius(45);  black.setBackground(linearBlack);
		android.graphics.drawable.GradientDrawable linearWhite = new android.graphics.drawable.GradientDrawable();  linearWhite.setColor(Color.WHITE);  linearWhite.setCornerRadius(45);  white.setBackground(linearWhite);
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

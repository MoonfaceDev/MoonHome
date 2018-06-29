package com.moonface.home;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.*;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.FileProvider;
import android.support.v4.view.ViewCompat;
import android.transition.ChangeBounds;
import android.transition.ChangeImageTransform;
import android.transition.ChangeTransform;
import android.transition.Fade;
import android.widget.*;
import android.content.*;
import android.util.*;
import android.transition.TransitionInflater;
import android.transition.TransitionSet;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import android.support.v7.app.AppCompatActivity;
import java.util.ArrayList;
import android.widget.ImageView;
import android.content.Intent;
import android.app.Activity;
import android.content.SharedPreferences;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.moonface.Util.DrawableUtil;
import com.moonface.Util.FileUtil;
import com.moonface.Util.PermissionsRequest;

import android.view.View;

public class MainActivity extends AppCompatActivity {
	private SharedPreferences data;
	private SharedPreferences auth;
	public static ImageView imageview2;
	private final int CAMERA_REQUEST = 1;
	private final int PICKER_REQUEST = 2;
	public static String path = "";
	private Intent picker = new Intent(Intent.ACTION_PICK);
	public static int CURRENT_FRAGMENT;
	public static Uri uri = null;
	public static final long MOVE_DEFAULT_TIME = 300;
	public static final long FADE_DEFAULT_TIME = 200;
    public static String mImageFileLocation = "";
    private int targetWidth;
    private int targetHeight;
    private Intent camera;

    @Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
        FirebaseApp.initializeApp(this);
		setContentView(R.layout.main);
		initialize();
		initializeLogic();
	}
	
	private void initialize() {
		FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.fragment_placeholder, new SignInFragment(), "SIGN_IN_FRAGMENT");
        ft.commit();
	    imageview2 = findViewById(R.id.imageview2);
	    targetWidth = imageview2.getMaxWidth();
	    targetHeight = imageview2.getMaxHeight();
		data = getSharedPreferences("data", Activity.MODE_PRIVATE);
		auth = getSharedPreferences("auth", Activity.MODE_PRIVATE);
		imageview2.setOnClickListener(new View.OnClickListener(){
		    public void onClick(View view){
		        if(CURRENT_FRAGMENT == 2) {
                    CharSequence methods[] = new CharSequence[] {getString(R.string.camera), getString(R.string.gallery)};
		            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
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
                                            PermissionsRequest permissionsRequest = new PermissionsRequest(MainActivity.this, new String[]{PermissionsRequest.CAMERA});
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
            }
        });
	}

    private void initializeLogic() {
		if (data.getString("remember_me", "").equals("")) {
			data.edit().putString("remember_me", "0").apply();
		}
		if (auth.getString("logout", "").equals("1")) {
			auth.edit().putString("logout", "").apply();
			FirebaseAuth.getInstance().signOut();
			data.edit().putString("remember_me", "0").apply();
		}
        FileUtil.removeFolder(getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DCIM).getAbsolutePath());
	}
	public static void initializeImageview2(int fragment){
	    if(fragment == 1) {
            imageview2.setImageResource(R.drawable.moonface_logo);
        }
        if(fragment == 2){
	        imageview2.setImageResource(R.drawable.ic_add_white);
        }
    }
    public static void setCurrentFragment(FragmentManager _fm, Fragment _fragment, ArrayList<View> _view, ArrayList<String> _names){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Fragment previousFragment = _fm.findFragmentById(R.id.fragment_placeholder);
            Fade exitFade = new Fade();
            exitFade.setDuration(FADE_DEFAULT_TIME);
            previousFragment.setExitTransition(exitFade);
            TransitionSet enterTransitionSet = new TransitionSet();
            enterTransitionSet.addTransition(new ChangeBounds()).addTransition(new ChangeTransform()).addTransition(new ChangeImageTransform());
            enterTransitionSet.setDuration(MOVE_DEFAULT_TIME);
            Fade enterFade = new Fade();
            enterFade.setStartDelay(MOVE_DEFAULT_TIME);
            enterFade.setDuration(FADE_DEFAULT_TIME);
            _fragment.setEnterTransition(enterFade);
            _fragment.setSharedElementEnterTransition(enterTransitionSet);
            FragmentTransaction ft = _fm.beginTransaction();
            for(int i=0;i<_view.size();i++){
                ft.addSharedElement(_view.get(i), _names.get(i));
            }
            ft.replace(R.id.fragment_placeholder, _fragment);
            ft.addToBackStack(null);
            ft.commit();
        }
	}
    File createImageFile() {
        long timeMillis = System.currentTimeMillis();
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("HHmmss");
        String date = simpleDateFormat1.format(new Date(timeMillis)) + "_" + simpleDateFormat2.format(new Date(timeMillis));
        File file = new File(getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DCIM).getAbsolutePath() + File.separator + date);
        mImageFileLocation = file.getAbsolutePath();
        return file;

    }
    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
	    super.onActivityResult(requestCode, resultCode, data);
	    if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 1:
                        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                        bmOptions.inJustDecodeBounds = true;
                        BitmapFactory.decodeFile(mImageFileLocation, bmOptions);
                        int cameraImageWidth = bmOptions.outWidth;
                        int cameraImageHeight = bmOptions.outHeight;
                        int scaleFactor = Math.min(cameraImageWidth/targetWidth, cameraImageHeight/targetHeight);
                        bmOptions.inSampleSize = scaleFactor;
                        bmOptions.inJustDecodeBounds = false;
                        Bitmap photoReducedSizeBitmp = BitmapFactory.decodeFile(mImageFileLocation, bmOptions);
                        try {
                            photoReducedSizeBitmp = rotateIfNeeded(mImageFileLocation, photoReducedSizeBitmp);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Bitmap imageBitmap = DrawableUtil.getSquaredBitmap(photoReducedSizeBitmp);
                        imageBitmap = DrawableUtil.getCircleBitmap(imageBitmap, imageview2.getWidth());
                        imageview2.setImageBitmap(imageBitmap);
                        break;
                case 2:
                    uri = data.getData();
                    try {
                        imageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                        imageBitmap = DrawableUtil.getSquaredBitmap(imageBitmap);
                        imageBitmap = DrawableUtil.getCircleBitmap(imageBitmap, imageview2.getWidth());
                        imageview2.setImageBitmap(imageBitmap);
                    } catch (IOException e) {
                        Log.e("ERROR", e.toString());
                    }
                    break;
            }
        }
    }
    public static Bitmap rotateIfNeeded(String photoPath, Bitmap bitmap) throws IOException {
        ExifInterface ei = new ExifInterface(photoPath);
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_UNDEFINED);

        Bitmap rotatedBitmap = null;
        switch(orientation) {

            case ExifInterface.ORIENTATION_ROTATE_90:
                rotatedBitmap = DrawableUtil.getRotatedBitmap(bitmap, 90);
                break;

            case ExifInterface.ORIENTATION_ROTATE_180:
                rotatedBitmap = DrawableUtil.getRotatedBitmap(bitmap, 180);
                break;

            case ExifInterface.ORIENTATION_ROTATE_270:
                rotatedBitmap = DrawableUtil.getRotatedBitmap(bitmap, 270);
                break;

            case ExifInterface.ORIENTATION_NORMAL:
            default:
                rotatedBitmap = bitmap;
        }
        return rotatedBitmap;
    }
    @Override
    public void onBackPressed(){
	    finish();
    }
    @Override
    protected void onResume() {
        super.onResume();
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
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (permissions[0]) {
            case (PermissionsRequest.CAMERA):
            if (PermissionsRequest.isGranted(PermissionsRequest.CAMERA, getApplicationContext())) {
                startActivityForResult(camera, CAMERA_REQUEST);
            } else {
                Toast.makeText(getApplicationContext(), getString(R.string.permission_denied_message), Toast.LENGTH_SHORT).show();
            }
            break;
            case (PermissionsRequest.WRITE_EXTERNAL_STORAGE):
                if (PermissionsRequest.isGranted(PermissionsRequest.WRITE_EXTERNAL_STORAGE, getApplicationContext())) {
                    Toast.makeText(getApplicationContext(), R.string.sign_in_message, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.permission_denied_message), Toast.LENGTH_SHORT).show();
                }
        }
    }
}

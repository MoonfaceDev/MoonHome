package com.moonface.home;

import android.app.Activity;
import android.content.Context;
import android.media.ExifInterface;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.FileProvider;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.graphics.*;
import android.content.Intent;
import android.net.Uri;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.moonface.Util.DrawableUtil;
import com.moonface.Util.FileUtil;
import com.moonface.Util.PermissionsRequest;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CameraFragment1 extends Fragment {

    public static ImageView photo_preview;
    public static FloatingActionButton _fab;
    public static String mImageFileLocation = "";
    private static Activity activityContext;
    public static Uri staticImageUri = null;
    public static boolean isEmpty = true;
    private static File photoFile;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RelativeLayout linearFragment1 = (RelativeLayout) inflater.inflate(R.layout.camera_fragment_1, container, false);
        activityContext = getActivity();
        photo_preview = linearFragment1.findViewById(R.id.photo_preview);
        _fab = linearFragment1.findViewById(R.id._fab);
        ViewGroup.LayoutParams params = photo_preview.getLayoutParams();
        _fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(PermissionsRequest.isGranted(PermissionsRequest.WRITE_EXTERNAL_STORAGE, getContext()) && PermissionsRequest.isGranted(PermissionsRequest.CAMERA, getContext())) {
                    CameraFragment1.takePhoto(getContext());
                } else {
                    PermissionsRequest cameraPermissionsRequest = new PermissionsRequest(getActivity(), new String[]{PermissionsRequest.WRITE_EXTERNAL_STORAGE, PermissionsRequest.CAMERA});
                    cameraPermissionsRequest.filterPermissionsList();
                    cameraPermissionsRequest.showRequest();
                }
            }
        });
        return linearFragment1;
    }
    public static void takePhoto(final Context context) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        photoFile = null;
        try {
            photoFile = createImageFile();

        } catch (IOException e) {
            e.printStackTrace();
        }
        Uri imageUri = null;
        if (photoFile != null) {
            imageUri = FileProvider.getUriForFile(context, context.getString(R.string.authorities), photoFile);
        }
        staticImageUri = imageUri;
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        if(intent.resolveActivity(activityContext.getPackageManager()) != null){
            activityContext.startActivityForResult(intent, CAMERA_REQUEST);
        }
    }
    static File createImageFile() throws IOException {

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File storageDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);

        File image = File.createTempFile(imageFileName,".jpg", storageDirectory);
        mImageFileLocation = image.getAbsolutePath();

        return image;

    }
    private static final int CAMERA_REQUEST =1;
    public static void photoPreview(int requestCode, int resultCode, Intent data, Context context) throws IOException {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap output = FileUtil.getBitmap(staticImageUri, context);
            output = DrawableUtil.getRotatedBitmap(output, DrawableUtil.getRotation(mImageFileLocation));
            photo_preview.setImageBitmap(output);
            isEmpty = false;
        }
        else{
            Toast.makeText(context, context.getString(R.string.cannot_load_preview_message), Toast.LENGTH_LONG).show();
            photoFile.delete();
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
}
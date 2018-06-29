package com.moonface.home;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.VideoView;

import com.moonface.Util.PermissionsRequest;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.app.Activity.RESULT_OK;

public class CameraFragment2 extends Fragment {

    public static VideoView video_preview;
    public static FloatingActionButton _fab;
    private static Uri videoUri;
    public static  MediaController mediaController;
    private static Activity activityContext;
    public static Uri staticVideoUri = null;
    public static String mVideoFileLocation = "";
    public static boolean isEmpty = true;
    private static File videoFile;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RelativeLayout linearFragment2 = (RelativeLayout) inflater.inflate(R.layout.camera_fragment_2, container, false);
        activityContext = getActivity();
        video_preview = linearFragment2.findViewById(R.id.video_preview);
        _fab = linearFragment2.findViewById(R.id._fab);
        mediaController = new MediaController(getContext());
        mediaController.setMediaPlayer(video_preview);
        video_preview.setMediaController(mediaController);
        _fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(PermissionsRequest.isGranted(PermissionsRequest.WRITE_EXTERNAL_STORAGE, getContext()) && PermissionsRequest.isGranted(PermissionsRequest.CAMERA, getContext())) {
                    CameraFragment2.takeVideo(getContext());
                } else {
                    PermissionsRequest cameraPermissionRequest = new PermissionsRequest(getActivity(), new String[]{PermissionsRequest.WRITE_EXTERNAL_STORAGE, PermissionsRequest.CAMERA});
                    cameraPermissionRequest.filterPermissionsList();
                    cameraPermissionRequest.showRequest();
                }
            }
        });
        return linearFragment2;
    }
    public static void takeVideo(Context context){
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        videoFile = null;
        try {
            videoFile = createVideoFile();

        } catch (IOException e) {
            e.printStackTrace();
        }
        videoUri = FileProvider.getUriForFile(context, context.getString(R.string.authorities), videoFile);
        staticVideoUri = videoUri;
        takeVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT, videoUri);
        if (takeVideoIntent.resolveActivity(activityContext.getPackageManager()) != null) {
            activityContext.startActivityForResult(takeVideoIntent, CAMERA_REQUEST);
        }
    }
    static File createVideoFile() throws IOException {

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String videoFileName = "VID_" + timeStamp + "_";
        File storageDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);

        File video = File.createTempFile(videoFileName,".mp4", storageDirectory);
        mVideoFileLocation = video.getAbsolutePath();
        return video;

    }
    private static final int CAMERA_REQUEST =2;
    public static void videoPreview(int requestCode, int resultCode, Intent data, Context context) {
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            video_preview.setVideoURI(videoUri);
            video_preview.setVisibility(View.VISIBLE);
            video_preview.start();
            mediaController.show();
            isEmpty = false;
        } else {
            video_preview.setVisibility(View.INVISIBLE);
            Toast.makeText(context, context.getString(R.string.cannot_load_preview_message), Toast.LENGTH_LONG).show();
            videoFile.delete();
        }
    }
}
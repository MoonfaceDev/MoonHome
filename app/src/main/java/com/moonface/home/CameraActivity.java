package com.moonface.home;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.moonface.Util.PermissionsRequest;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;


public class CameraActivity extends AppCompatActivity {
    private Timer _timer = new Timer();
    public static int currentTab = 0;
    public static FloatingActionButton _fab;
    private TimerTask fabShowDelay;
    private ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera);
        _fab = findViewById(R.id._fab);
        Toolbar _toolbar = findViewById(R.id._toolbar);
        setSupportActionBar(_toolbar);

        TabLayout tabLayout = findViewById(R.id.tab_layout);
        setSupportActionBar(_toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        _toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _v) {
                onBackPressed();
            }
        });
        Window w = this.getWindow();w.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);w.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            w.setStatusBarColor(Color.parseColor("#c4001c"));
        }
        if(!getResources().getBoolean(R.bool.is_right_to_left)) {
            tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.photo)));
            tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.video)));
        } else {
            tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.video)));
            tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.photo)));
        }
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        viewPager = findViewById(R.id.pager);
        final CameraPagerAdapter adapter = new CameraPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount(), getApplicationContext());
        viewPager.setAdapter(adapter);
        if(getResources().getBoolean(R.bool.is_right_to_left)) {
            viewPager.setCurrentItem(adapter.getCount() - 1);
        }
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 1) {
            CameraFragment1.photoPreview(requestCode, resultCode, getApplicationContext());
        }
        if(requestCode == 2){
            CameraFragment2.videoPreview(requestCode, resultCode, data, getApplicationContext());
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if(PermissionsRequest.isGranted(PermissionsRequest.WRITE_EXTERNAL_STORAGE, getApplicationContext()) && PermissionsRequest.isGranted(PermissionsRequest.CAMERA, getApplicationContext())) {
            currentTab = viewPager.getCurrentItem();
            switch (currentTab){
                case (0):
                CameraFragment1.takePhoto(getApplicationContext());
                break;
                case (1):
                CameraFragment2.takeVideo(getApplicationContext());
                break;
            }
        } else {
            Toast.makeText(getApplicationContext(), getString(R.string.permission_denied_message), Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.camera, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int fragment;
        currentTab = viewPager.getCurrentItem();
        if(getResources().getBoolean(R.bool.is_right_to_left)) {
            fragment = 1 - currentTab;
        } else {
            fragment = currentTab;
        }
        Uri uri;
        switch (item.getItemId()) {
            case R.id.action_share:
                Intent share = new Intent(android.content.Intent.ACTION_SEND);
                share.setType("*/*");
                switch (fragment) {
                    case 0:
                        if(CameraFragment1.staticImageUri != null) {
                            if(!CameraFragment1.isEmpty) {
                                uri = CameraFragment1.staticImageUri;
                                share.putExtra(Intent.EXTRA_STREAM, uri);
                                startActivity(Intent.createChooser(share, getString(R.string.send_image_label)));
                            } else {
                                Toast.makeText(getApplicationContext(), getString(R.string.image_share_empty), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), getString(R.string.image_share_empty), Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 1:
                        if(CameraFragment2.staticVideoUri != null) {
                            if(!CameraFragment2.isEmpty) {
                                uri = CameraFragment2.staticVideoUri;
                                share.putExtra(Intent.EXTRA_STREAM, uri);
                                startActivity(Intent.createChooser(share, getString(R.string.send_video_label)));
                            } else {
                                Toast.makeText(getApplicationContext(), getString(R.string.video_share_empty), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), getString(R.string.video_share_empty), Toast.LENGTH_SHORT).show();
                        }
                        break;
                }
                return true;

            case R.id.action_delete:
                File removedFile;
                switch (fragment) {
                    case 0:
                        if(CameraFragment1.mImageFileLocation != null) {
                            removedFile = new File(CameraFragment1.mImageFileLocation);
                            if(!CameraFragment1.isEmpty) {
                                boolean tempB2 = removedFile.delete();
                                CameraFragment1.staticImageUri = null;
                                CameraFragment1.mImageFileLocation = null;
                                CameraFragment1.photo_preview.setImageDrawable(null);
                                Toast.makeText(getApplicationContext(), getString(R.string.file_deleted_success), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), getString(R.string.image_share_empty), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), getString(R.string.image_share_empty), Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 1:
                        if(CameraFragment2.mVideoFileLocation != null) {
                            removedFile = new File(CameraFragment2.mVideoFileLocation);
                            if(!CameraFragment2.isEmpty) {
                                boolean tempB1 = removedFile.delete();
                                CameraFragment2.staticVideoUri = null;
                                CameraFragment2.mVideoFileLocation = null;
                                CameraFragment2.video_preview.setVideoURI(null);
                                CameraFragment2.video_preview.setVisibility(View.INVISIBLE);
                                CameraFragment2.mediaController.hide();
                                Toast.makeText(getApplicationContext(), getString(R.string.file_deleted_success), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), getString(R.string.video_share_empty), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), getString(R.string.video_share_empty), Toast.LENGTH_SHORT).show();
                        }
                        break;
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }
}
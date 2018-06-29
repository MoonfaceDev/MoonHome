package com.moonface.home;

import android.annotation.SuppressLint;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.*;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.NavigationView;
import android.view.*;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.*;
import android.content.*;
import android.graphics.*;
import android.util.*;
import java.io.IOException;
import java.util.*;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.widget.LinearLayout;

import java.util.ArrayList;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.EditText;
import java.util.Timer;
import java.util.TimerTask;
import android.content.Intent;
import android.net.Uri;
import android.app.Activity;
import android.content.SharedPreferences;
import java.util.Calendar;
import java.text.SimpleDateFormat;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.graphics.Typeface;

import com.moonface.Util.DrawableUtil;
import com.moonface.Util.ParametersUtil;
import com.moonface.Util.PermissionsRequest;
import com.moonface.Util.TimeUtil;
import com.moonface.Util.SwipeExpander;

public class HomeActivity extends AppCompatActivity {

    private Timer _timer = new Timer();

    private DrawerLayout _drawer;
    private LinearLayout _applist_view;
    private BottomSheetBehavior bottomSheetBehavior;

    private String a = "";
    private String b = "";


    private ImageView back_imageview;
    private LinearLayout linear29;
    private LinearLayout linear18;
    private TextView textview2;
    private TextView textview3;
    private LinearLayout linear22;
    private Button button1;
    private ListView listview1;
    private EditText edittext1;
    private ImageView _drawer_imageview2;
    private TextView _drawer_nickname;
    private TextView _drawer_email;
    private MenuItem _drawer_settings;
    private MenuItem _drawer_account;
    private MenuItem _drawer_pp;
    private MenuItem _drawer_tac;
    private MenuItem _drawer_contact_us;
    private MenuItem _drawer_share;
    private MenuItem _drawer_signout;
    private MenuItem _drawer_about;

    private Intent intent = new Intent();
    private Intent search_int = new Intent();
    private Calendar get_time = Calendar.getInstance();
    private Intent moonchat_int = new Intent();
    private SharedPreferences data;
    private Intent messenger_int = new Intent();
    private Intent camera_int = new Intent();
    private Intent dialer_int = new Intent();
    private AlertDialog.Builder exitDialog;
    private SharedPreferences auth;
    private Intent contact_us = new Intent();
    @Override
    protected void onCreate(Bundle _savedInstanceState) {
        super.onCreate(_savedInstanceState);
        setContentView(R.layout.home);
        initialize();
        initializeLogic();
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        setContentView(R.layout.home);
        initialize();
        initializeLogic();
        super.onConfigurationChanged(newConfig);
    }
    private void initialize() {

        _applist_view = findViewById(R.id._applist_view);
        RelativeLayout _apps_view = findViewById(R.id._apps_view);
        AppBarLayout _appbar = findViewById(R.id._appbar);
        bottomSheetBehavior = BottomSheetBehavior.from(_applist_view);
        Toolbar _toolbar = _appbar.findViewById(R.id._toolbar);
        setSupportActionBar(_toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        _toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _v) {
                onBackPressed();
            }
        });

        _drawer = findViewById(R.id._drawer);ActionBarDrawerToggle _toggle = new ActionBarDrawerToggle(HomeActivity.this, _drawer, _toolbar, R.string.app_name, R.string.app_name);
        _drawer.addDrawerListener(_toggle);
        _toggle.syncState();

        NavigationView _nav_view = findViewById(R.id._nav_view);

        GridView gridview1 = _apps_view.findViewById(R.id.gridview1);
        back_imageview = _apps_view.findViewById(R.id.back_imageview);
        linear29 = _apps_view.findViewById(R.id.linear29);
        linear18 = _applist_view.findViewById(R.id.linear18);
        LinearLayout linear_extension = _apps_view.findViewById(R.id.linear_extension);
        textview2 = _apps_view.findViewById(R.id.textview2);
        textview3 = _apps_view.findViewById(R.id.textview3);
        linear22 = _applist_view.findViewById(R.id.linear22);
        button1 = _applist_view.findViewById(R.id.button1);
        listview1 = _applist_view.findViewById(R.id.listview1);
        ImageView g_logo = _applist_view.findViewById(R.id.g_logo);
        ImageView imageview16 = _applist_view.findViewById(R.id.imageview16);
        edittext1 = _applist_view.findViewById(R.id.edittext1);
        ImageView ic_dialer = _applist_view.findViewById(R.id.ic_dialer);
        ImageView ic_moonchat = _applist_view.findViewById(R.id.ic_moonchat);
        ImageView ic_email = _applist_view.findViewById(R.id.ic_email);
        ImageView ic_camera = _applist_view.findViewById(R.id.ic_camera);
        _drawer_imageview2 = _nav_view.getHeaderView(0).findViewById(R.id.imageview2);
        _drawer_nickname = _nav_view.getHeaderView(0).findViewById(R.id.nickname);
        _drawer_email = _nav_view.getHeaderView(0).findViewById(R.id.email);
        _drawer_settings = _nav_view.getMenu().findItem(R.id.settings);
        _drawer_account = _nav_view.getMenu().findItem(R.id.account);
        _drawer_pp = _nav_view.getMenu().findItem(R.id.pp);
        _drawer_tac = _nav_view.getMenu().findItem(R.id.tac);
        _drawer_contact_us = _nav_view.getMenu().findItem(R.id.contact_us);
        _drawer_share = _nav_view.getMenu().findItem(R.id.share);
        _drawer_signout = _nav_view.getMenu().findItem(R.id.signout);
        _drawer_about = _nav_view.getMenu().findItem(R.id.about);
        data = getSharedPreferences("data", Activity.MODE_PRIVATE);
        exitDialog = new AlertDialog.Builder(this);
        auth = getSharedPreferences("auth", Activity.MODE_PRIVATE);
        _nav_view.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull final MenuItem menuItem) {
                        _drawer.closeDrawer(GravityCompat.START);
                        TimeUtil.after(250, new TimeUtil.DelayedAction() {
                            @Override
                            public void afterDelay() {
                                if(menuItem == _drawer_settings){
                                    intent.setClass(getApplicationContext(), SettingsActivity.class);
                                    startActivity(intent);
                                }
                                if(menuItem == _drawer_account){
                                    intent.setClass(getApplicationContext(), MyAccountActivity.class);
                                    startActivity(intent);
                                }
                                if(menuItem == _drawer_pp){
                                    intent.setClass(getApplicationContext(), PrivacyPolicyActivity.class);
                                    startActivity(intent);
                                }
                                if(menuItem == _drawer_tac){
                                    intent.setClass(getApplicationContext(), TermsAndConditionsActivity.class);
                                    startActivity(intent);
                                }
                                if(menuItem == _drawer_contact_us){
                                    contact_us.setAction(Intent.ACTION_VIEW);
                                    contact_us.setData(Uri.parse("mailto:moonfaceapps@gmail.com"));
                                    startActivity(contact_us);
                                }
                                if(menuItem == _drawer_share){
                                    a = getString(R.string.download_link);
                                    b = getString(R.string.share_message);
                                    Intent ishare = new Intent(android.content.Intent.ACTION_SEND);
                                    ishare.setType("text/plain");
                                    ishare.putExtra(android.content.Intent.EXTRA_SUBJECT, b);
                                    ishare.putExtra(android.content.Intent.EXTRA_TEXT, a);
                                    startActivity(Intent.createChooser(ishare,getString(R.string.share_using_label)));
                                }
                                if(menuItem == _drawer_signout){
                                    auth.edit().putString("logout", "1").apply();
                                    intent.setClass(getApplicationContext(), MainActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                    finish();
                                }
                                if(menuItem == _drawer_about){
                                    intent.setClass(getApplicationContext(), AboutActivity.class);
                                    startActivity(intent);
                                }
                            }
                        });
                        return true;
                    }
                });
        final SwipeExpander extendTime = new SwipeExpander(linear_extension);
        if (!getResources().getBoolean(R.bool.is_right_to_left)) {
            extendTime.setDirection(SwipeExpander.FROM_LEFT);
        } else {
            extendTime.setDirection(SwipeExpander.FROM_RIGHT);
        }
        extendTime.setPeekLength(ParametersUtil.dpsToPixels(180, getApplicationContext()));
        extendTime.start();
        linear_extension.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!SwipeExpander.getAnimator().isRunning()) {
                    if (!SwipeExpander.isExpanded) {
                        extendTime.expand(view);
                    }
                    else {
                        extendTime.collapse(view);
                    }
                }
            }
        });

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!edittext1.getText().toString().equals("")) {
                    search_int.putExtra("query", edittext1.getText().toString());
                    search_int.setClass(getApplicationContext(), SearchActivity.class);
                    startActivity(search_int);
                }
            }
        });

        g_logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search_int.putExtra("query", "-");
                search_int.setClass(getApplicationContext(), SearchActivity.class);
                startActivity(search_int);
            }
        });

        imageview16.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _speech();
            }
        });
        edittext1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });
        edittext1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }
        });

        ic_dialer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.grow_up);
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        view.setScaleX(1.0f);
                        view.setScaleY(1.0f);
                        dialer_int.setClass(getApplicationContext(), DialerActivity.class);
                        startActivity(dialer_int);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                view.startAnimation(animation);
            }
        });

        ic_moonchat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.grow_up);
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        view.setScaleX(1.0f);
                        view.setScaleY(1.0f);
                        moonchat_int.setClass(getApplicationContext(), MoonchatActivity.class);
                        startActivity(moonchat_int);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                view.startAnimation(animation);
            }
        });

        ic_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.grow_up);
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        view.setScaleX(1.0f);
                        view.setScaleY(1.0f);
                        messenger_int.setClass(getApplicationContext(), InboxActivity.class);
                        startActivity(messenger_int);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                view.startAnimation(animation);
            }
        });

        ic_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.grow_up);
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        view.setScaleX(1.0f);
                        view.setScaleY(1.0f);
                        camera_int.setClass(getApplicationContext(), CameraActivity.class);
                        startActivity(camera_int);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                view.startAnimation(animation);
            }
        });
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED: {
                        linear18.setBackgroundResource(R.drawable.expanded_dock_background);
                        linear29.setAlpha(0);
                        linear22.getBackground().setAlpha(0);
                        GradientDrawable gradientDrawable18 = (GradientDrawable) linear18.getBackground();
                        gradientDrawable18.setCornerRadii(new float[]{0, 0, 0, 0, 0, 0, 0, 0});
                        GradientDrawable gradientDrawable22 = (GradientDrawable) linear22.getBackground();
                        gradientDrawable22.setCornerRadii(new float[]{0, 0, 0, 0, 0, 0, 0, 0});
                    }
                    break;
                    case BottomSheetBehavior.STATE_COLLAPSED: {
                        linear18.setBackgroundResource(R.drawable.dock_background);
                        linear29.setAlpha(1);
                        linear22.getBackground().setAlpha(255);
                        GradientDrawable gradientDrawable18 = (GradientDrawable) linear18.getBackground();
                        gradientDrawable18.setCornerRadii(new float[]{16, 16, 16, 16, 0, 0, 0, 0});
                        GradientDrawable gradientDrawable22 = (GradientDrawable) linear22.getBackground();
                        gradientDrawable22.setCornerRadii(new float[]{16, 16, 16, 16, 0, 0, 0, 0});
                    }
                    break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                linear29.setAlpha(1 - slideOffset);
                linear22.getBackground().setAlpha(Math.round((255- Float.parseFloat(String.valueOf(slideOffset*255)))));
                GradientDrawable gradientDrawable18 = (GradientDrawable) linear18.getBackground();
                gradientDrawable18.setCornerRadii(new float[]{(1-slideOffset)*16, (1-slideOffset)*16, (1-slideOffset)*16, (1-slideOffset)*16, 0, 0, 0, 0});
                GradientDrawable gradientDrawable22 = (GradientDrawable) linear22.getBackground();
                gradientDrawable22.setCornerRadii(new float[]{(1-slideOffset)*16, (1-slideOffset)*16, (1-slideOffset)*16, (1-slideOffset)*16, 0, 0, 0, 0});
            }
        });
        listview1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent p_event) {
                if(!onTop(listview1)) {
                    _applist_view.requestDisallowInterceptTouchEvent(true);
                }
                return onTouchEvent(p_event);
            }
        });
        listview1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String packageName = AppAdapter.getPackageName(position);
                Intent intent = getPackageManager().getLaunchIntentForPackage(packageName);
                if(intent != null) {
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), R.string.load_app_error_message, Toast.LENGTH_SHORT).show();
                }
            }
        });
        Drawable[] drawables = new Drawable[]{
                getResources().getDrawable(R.drawable.browser),
                getResources().getDrawable(R.drawable.calculator),
                getResources().getDrawable(R.drawable.market),
                getResources().getDrawable(R.drawable.notes),
                getResources().getDrawable(R.drawable.moon_games),
                getResources().getDrawable(R.drawable.paint),
                getResources().getDrawable(R.drawable.photos),
                getResources().getDrawable(R.drawable.whatsapp),
                getResources().getDrawable(R.drawable.contacts),
                getResources().getDrawable(R.drawable.calendar),
                getResources().getDrawable(R.drawable.passwords),
                getResources().getDrawable(R.drawable.play_store)
        };
        Intent[] intents = new Intent[]{
                new Intent().setClass(this,SearchActivity.class).putExtra("query", "-"),
                new Intent().setClass(this,CalculatorActivity.class),
                new Intent().setClass(this,ShoppingListActivity.class),
                new Intent().setClass(this,NotesActivity.class),
                new Intent().setClass(this,MoongamesActivity.class),
                new Intent().setClass(this,PaintActivity.class),
                new Intent().setAction(Intent.ACTION_VIEW).setData(Uri.parse("content://media/external/images/media")),
                new Intent(getPackageManager().getLaunchIntentForPackage("com.whatsapp")),
                new Intent().setAction(Intent.ACTION_VIEW).setData(Uri.parse("content://contacts/people")),
                new Intent().setClass(this,CalendarActivity.class),
                new Intent().setClass(this,PasswordsActivity.class),
                new Intent(getPackageManager().getLaunchIntentForPackage("com.android.vending"))
        };
        GridAdapter gridAdapter = new GridAdapter(this, drawables, intents);
        gridview1.setAdapter(gridAdapter);
        new Thread(new Runnable() {
            public void run() {
                final AppAdapter appAdapter = new AppAdapter(getApplicationContext());
                listview1.post(new Runnable() {
                    public void run() {
                        listview1.setAdapter(appAdapter);
                    }
                });
            }
        }).start();
    }
    private void initializeLogic() {
        edittext1.setOnEditorActionListener(new EditText.OnEditorActionListener() { public boolean onEditorAction(TextView v, int actionId, KeyEvent event) { if (actionId == android.view.inputmethod.EditorInfo.IME_ACTION_SEARCH) { button1.performClick(); return true; } return false;}});
        _set_background();
        textview2.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/fluent_sans_regular.ttf"), 0);
        textview3.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/fluent_sans_regular.ttf"), 1);
        TimerTask checktouch = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        get_time = Calendar.getInstance();
                        textview2.setText(new SimpleDateFormat("EEEE, MMM dd").format(get_time.getTime()));
                        textview3.setText(new SimpleDateFormat("HH:mm").format(get_time.getTime()));
                    }
                });
            }
        };
        _timer.scheduleAtFixedRate(checktouch, 0, 1000);
        _drawer_nickname.setText(data.getString("nickname",""));
        _drawer_email.setText(data.getString("email",""));
        try {
            Bitmap imageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), SignInFragment.profileUri);
            imageBitmap = DrawableUtil.getResizedBitmap(imageBitmap, 256);
            imageBitmap = DrawableUtil.getSquaredBitmap(imageBitmap);
            imageBitmap = DrawableUtil.getCircleBitmap(imageBitmap, _drawer_imageview2.getWidth());
            _drawer_imageview2.setImageBitmap(imageBitmap);
        }
        catch (IOException io){
            Log.e("", io.toString());
        }
    }

    private final int REQ_CODE_SPEECH_INPUT=100;

    @Override
    public void onBackPressed() {
        if (_drawer.isDrawerOpen(GravityCompat.START)) {
            _drawer.closeDrawer(GravityCompat.START);
        }
        else {
            if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
            else {
                exitDialog.setTitle(R.string.exit);
                exitDialog.setMessage(R.string.exit_dialog_message);
                exitDialog.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface _dialog, int _which) {
                        finish();
                    }
                });
                exitDialog.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface _dialog, int _which) {

                    }
                });
                exitDialog.create().show();
            }
        }
    }
    public boolean onTop (AbsListView view) {
        boolean onTop = true;
        if (view != null && view.getChildCount() > 0) {
            onTop = view.getFirstVisiblePosition() == 0 && view.getChildAt(0).getTop() == view.getPaddingTop();
        }
        return onTop;
    }

    @Override
    public void onStart() {
        super.onStart();
        if(PermissionsRequest.isGranted(PermissionsRequest.WRITE_EXTERNAL_STORAGE, getApplicationContext())) {
            _set_background();
        } else {
            PermissionsRequest permissionsRequest = new PermissionsRequest(this, new String[]{PermissionsRequest.WRITE_EXTERNAL_STORAGE});
            permissionsRequest.showRequest();
        }
    }

    private void _set_background () {
        if (data.getString("background", "").equals("")) {
            data.edit().putString("background", "0").apply();
        }
        if (data.getString("background", "").equals("0")) {
            back_imageview.setImageDrawable(DrawableUtil.getPhoneBackgroundDrawable(this));
        }
        if (data.getString("background", "").equals("1")) {
            back_imageview.setImageDrawable(getResources().getDrawable(R.drawable.deer));
        }
        if (data.getString("background", "").equals("2")) {
            back_imageview.setImageDrawable(getResources().getDrawable(R.drawable.eiffel_tower));
        }
        if (data.getString("background", "").equals("3")) {
            back_imageview.setImageDrawable(getResources().getDrawable(R.drawable.forest));
        }
        if (data.getString("background", "").equals("4")) {
            back_imageview.setImageDrawable(getResources().getDrawable(R.drawable.milky_way));
        }
        if (data.getString("background", "").equals("5")) {
            back_imageview.setImageDrawable(getResources().getDrawable(R.drawable.new_york));
        }
        if (data.getString("background", "").equals("6")) {
            back_imageview.setImageDrawable(getResources().getDrawable(R.drawable.pyramids));
        }
        if (data.getString("background", "").equals("7")) {
            back_imageview.setImageDrawable(getResources().getDrawable(R.drawable.sunrise));
        }
        if (data.getString("background", "").equals("8")) {
            back_imageview.setImageDrawable(getResources().getDrawable(R.drawable.village));
        }
    }


    private void _speech () {
        Intent intent = new Intent(android.speech.RecognizerIntent.ACTION_RECOGNIZE_SPEECH); intent.putExtra(android.speech.RecognizerIntent.EXTRA_LANGUAGE_MODEL, android.speech.RecognizerIntent.LANGUAGE_MODEL_FREE_FORM); intent.putExtra(android.speech.RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault()); intent.putExtra(android.speech.RecognizerIntent.EXTRA_PROMPT, getString(R.string.speech_search_label)); try { startActivityForResult(intent, REQ_CODE_SPEECH_INPUT); } catch (ActivityNotFoundException a) { Toast.makeText(getApplicationContext(), R.string.no_support_speech, Toast.LENGTH_SHORT).show(); } } @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) { super.onActivityResult(requestCode, resultCode, data); switch (requestCode) { case REQ_CODE_SPEECH_INPUT: { if (resultCode == RESULT_OK && null != data) { ArrayList<String> result = data .getStringArrayListExtra(android.speech.RecognizerIntent.EXTRA_RESULTS); edittext1.setText(result.get(0));
        _search_output();
    } break; } }

    }


    private void _search_output () {
        if (!edittext1.getText().toString().equals("")) {
            search_int.putExtra("query", edittext1.getText().toString());
            search_int.setClass(getApplicationContext(), SearchActivity.class);
            startActivity(search_int);
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
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if(PermissionsRequest.isGranted(PermissionsRequest.WRITE_EXTERNAL_STORAGE, getApplicationContext())){
            _set_background();
        } else {
            Toast.makeText(getApplicationContext(), getString(R.string.permission_denied_message), Toast.LENGTH_SHORT).show();
        }
    }
}

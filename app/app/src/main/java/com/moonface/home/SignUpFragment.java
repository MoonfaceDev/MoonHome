package com.moonface.home;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
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
import com.moonface.Util.PermissionsRequest;
import com.moonface.Util.TimeUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class SignUpFragment extends Fragment {

    private FirebaseDatabase _firebase = FirebaseDatabase.getInstance();
    private FirebaseStorage _storage = FirebaseStorage.getInstance();

    private boolean isChecked1 = false;
    private boolean isChecked2 = false;
    private HashMap<String, Object> data_update = new HashMap<>();
    private HashMap<String, Object> user_list_map = new HashMap<>();
    private boolean admin = false;
    private double position = 0;

    private ArrayList<HashMap<String, Object>> list_check = new ArrayList<>();
    private ArrayList<String> nicknames = new ArrayList<>();

    private DatabaseReference userdata = _firebase.getReference("users");
    private SharedPreferences data;
    private Intent terms_and_conditions = new Intent();
    private DatabaseReference user_list = _firebase.getReference("user_list");
    private FirebaseAuth userAuth;
    private OnCompleteListener<AuthResult> _userAuth_create_user_listener;
    private EditText nickname;
    private StorageReference profile_pics = _storage.getReference("profile_pics");
    private ProgressDialog prog;
    private EditText email_signup;
    private TextInputLayout email_input_layout;
    private EditText password_signup;
    private TextInputLayout password_input_layout;
    private CheckBox checkbox1;
    private CheckBox checkbox2;
    private TextView textview6;
    private TextView textview9;
    private Button signup_button;
    private TextView openlogin_tab;
    private TextView tac_button;
    private TextView login_label;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sign_up, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        data = getActivity().getSharedPreferences("data", Activity.MODE_PRIVATE);
        userAuth = FirebaseAuth.getInstance();
        MainActivity.CURRENT_FRAGMENT = 2;

        nickname = view.findViewById(R.id.nickname);
        email_signup = view.findViewById(R.id.email_signup);
        email_input_layout = view.findViewById(R.id.email_input_layout);
        password_signup = view.findViewById(R.id.password_signup);
        password_input_layout = view.findViewById(R.id.password_input_layout);
        checkbox1 = view.findViewById(R.id.checkbox1);
        checkbox2 = view.findViewById(R.id.checkbox2);
        textview6 = view.findViewById(R.id.tac_button);
        textview9 = view.findViewById(R.id.pp_button);
        signup_button = view.findViewById(R.id.signup_button);
        openlogin_tab = view.findViewById(R.id.openlogin_tab);
        tac_button = view.findViewById(R.id.tac_button);
        login_label = view.findViewById(R.id.login_label);

        prog = new ProgressDialog(getContext());
        prog.setMessage(getString(R.string.creating_account));
        prog.setIndeterminate(true);
        prog.setCancelable(false);

        MainActivity.initializeImageview2(2);
        signup_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (0 < nickname.getText().toString().length()) {
                    if (email_signup.getText().toString().length() > 0) {
                        if (password_signup.getText().toString().length() > 0) {
                            if(MainActivity.uri != null) {
                                if (isChecked1) {
                                    if (isChecked2) {
                                        if (!nicknames.contains(nickname.getText().toString())) {
                                            prog.show();
                                            userAuth.createUserWithEmailAndPassword(email_signup.getText().toString(), password_signup.getText().toString()).addOnCompleteListener(getActivity(), _userAuth_create_user_listener);
                                        } else {
                                            SketchwareUtil.showMessage(getContext(), getString(R.string.nickname_in_use));
                                        }
                                    } else {
                                        SketchwareUtil.showMessage(getContext(), getString(R.string.agree_to_privacy_policy));
                                    }
                                } else {
                                    SketchwareUtil.showMessage(getContext(), getString(R.string.agree_to_terms_and_conditions));
                                }
                            } else {
                                Toast.makeText(getContext(), R.string.pick_profile_pic, Toast.LENGTH_SHORT).show();
                                MainActivity.imageview2.setBackground(getResources().getDrawable(R.drawable.roundedbackground_profile_pic_error));
                                TimeUtil.after(1000, new TimeUtil.DelayedAction() {
                                    @Override
                                    public void afterDelay() {
                                        MainActivity.imageview2.setBackground(getResources().getDrawable(R.drawable.roundedbackground_profile_pic));
                                    }
                                });
                                }
                        } else {
                            password_signup.setError(getString(R.string.blank_password));
                        }
                    } else {
                        email_signup.setError(getString(R.string.blank_email));
                    }
                } else {
                    nickname.setError(getString(R.string.blank_nickname));
                }
            }});
        checkbox1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton _param1, boolean _param2)  {
                final boolean _isChecked = _param2;
                isChecked1 = _isChecked;
            }
        });
        checkbox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton _param1, boolean _param2)  {
                final boolean _isChecked = _param2;
                isChecked2 = _isChecked;
            }
        });

        textview6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                terms_and_conditions.setClass(getContext(), TermsAndConditionsActivity.class);
                startActivity(terms_and_conditions);
            }
        });

        textview9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                terms_and_conditions.setClass(getContext(), PrivacyPolicyActivity.class);
                startActivity(terms_and_conditions);
            }
        });

        openlogin_tab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<View> viewArrayList = new ArrayList<>();
                viewArrayList.add(signup_button);
                viewArrayList.add(email_signup);
                viewArrayList.add(password_signup);
                viewArrayList.add(openlogin_tab);
                viewArrayList.add(email_input_layout);
                viewArrayList.add(password_input_layout);
                viewArrayList.add(checkbox1);
                viewArrayList.add(tac_button);
                viewArrayList.add(login_label);
                ArrayList<String> stringArrayList = new ArrayList<>();
                stringArrayList.add("sign_button");
                stringArrayList.add("email");
                stringArrayList.add("password");
                stringArrayList.add("tab_button");
                stringArrayList.add("email_input_layout");
                stringArrayList.add("password_input_layout");
                stringArrayList.add("checkbox");
                stringArrayList.add("button");
                stringArrayList.add("sign_label");
                MainActivity.setCurrentFragment(getActivity().getSupportFragmentManager(), new SignInFragment(), viewArrayList, stringArrayList);
            }
        });

        _userAuth_create_user_listener = new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(Task<AuthResult> _param1) {
                final boolean _success = _param1.isSuccessful();
                final String _errorMessage = _param1.getException() != null ? _param1.getException().getMessage() : "";
                if (_success) {
                    data.edit().putString("email", FirebaseAuth.getInstance().getCurrentUser().getEmail()).commit();
                    data.edit().putString("id", FirebaseAuth.getInstance().getCurrentUser().getUid()).commit();
                    try {
                        _signdata();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    SketchwareUtil.showMessage(getContext(), _errorMessage);
                    prog.dismiss();
                }
            }
        };
        userdata.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot _dataSnapshot) {
                list_check = new ArrayList<>();
                try {
                    GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
                    for (DataSnapshot _data : _dataSnapshot.getChildren()) {
                        HashMap<String, Object> _map = _data.getValue(_ind);
                        list_check.add(_map);
                    }
                }
                catch (Exception _e) {
                    _e.printStackTrace();
                }
                position = 0;
                for(int _repeat63 = 0; _repeat63 < (int)(list_check.size()); _repeat63++) {
                    nicknames.add(list_check.get((int)position).get("name").toString());
                    position++;
                }
            }
            @Override
            public void onCancelled(DatabaseError _databaseError) {
            }
        });
    }
    private void _signdata () throws IOException {
        data.edit().putString("nickname", nickname.getText().toString()).apply();
        data.edit().putBoolean("admin", admin).apply();
        data.edit().putString("id", FirebaseAuth.getInstance().getCurrentUser().getUid()).apply();
        data_update = new HashMap<>();
        data_update.put("user", FirebaseAuth.getInstance().getCurrentUser().getEmail());
        data_update.put("uid", FirebaseAuth.getInstance().getCurrentUser().getUid());
        data_update.put("name", nickname.getText().toString());
        data_update.put("admin", data.getBoolean("admin", false));
        data_update.put("background", "0");
        userdata.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).updateChildren(data_update);
        data_update.clear();
        user_list_map = new HashMap<>();
        user_list_map.put("user", FirebaseAuth.getInstance().getCurrentUser().getEmail());
        user_list.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).updateChildren(user_list_map);
        user_list_map.clear();
        profile_pics.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).putFile(MainActivity.uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                SketchwareUtil.showMessage(getContext(), getString(R.string.accound_created));
                ArrayList<View> viewArrayList = new ArrayList<>();
                viewArrayList.add(signup_button);
                viewArrayList.add(email_signup);
                viewArrayList.add(password_signup);
                viewArrayList.add(openlogin_tab);
                viewArrayList.add(email_input_layout);
                viewArrayList.add(password_input_layout);
                viewArrayList.add(checkbox1);
                viewArrayList.add(tac_button);
                viewArrayList.add(login_label);
                ArrayList<String> stringArrayList = new ArrayList<>();
                stringArrayList.add("sign_button");
                stringArrayList.add("email");
                stringArrayList.add("password");
                stringArrayList.add("tab_button");
                stringArrayList.add("email_input_layout");
                stringArrayList.add("password_input_layout");
                stringArrayList.add("checkbox");
                stringArrayList.add("button");
                stringArrayList.add("sign_label");
                MainActivity.setCurrentFragment(getActivity().getSupportFragmentManager(), new SignInFragment(), viewArrayList, stringArrayList);
                data_update = new HashMap<>();
                data_update.put("profile_pic_url", taskSnapshot.getStorage().getDownloadUrl().toString());
                userdata.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).updateChildren(data_update);
                data_update.clear();
                prog.dismiss();
            }
        });
    }
}

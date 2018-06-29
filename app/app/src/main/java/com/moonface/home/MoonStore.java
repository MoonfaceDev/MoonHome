package com.moonface.home;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.zip.Inflater;

public class MoonStore extends AppCompatActivity {
    private EditText search_edittext;
    private GridView apps_view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moon_store);
        initialize();
        initializeLogic();
    }
    private void initialize(){
        search_edittext = findViewById(R.id.search_edittext);
        apps_view = findViewById(R.id.apps_view);
        search_edittext.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == event.getAction()){
                }
                if(actionId == 4){
                }
                return false;
            }
        });
    }
    private void initializeLogic(){

    }
}

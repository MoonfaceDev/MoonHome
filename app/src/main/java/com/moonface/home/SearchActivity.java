package com.moonface.home;

import android.animation.ObjectAnimator;
import android.os.*;
import android.support.v7.widget.SearchView;
import android.view.*;
import android.widget.*;
import android.graphics.*;
import android.util.*;

import java.util.*;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.LinearLayout;
import android.widget.ImageView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;
import android.webkit.WebView;
import android.content.Intent;
import android.app.Activity;
import android.content.SharedPreferences;
import android.view.View;
import android.webkit.WebViewClient;

import com.moonface.Util.DrawableUtil;
import com.moonface.Util.InputUtil;

public class SearchActivity extends AppCompatActivity {
	
	
	private Toolbar _toolbar;
	
	private WebView webview1;
	
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.search);
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
				finish();
			}
		});
		webview1 = findViewById(R.id.webview1);
		webview1.getSettings().setJavaScriptEnabled(true);
		webview1.getSettings().setSupportZoom(true);
		webview1.getSettings().setLoadsImagesAutomatically(true);
		webview1.clearCache(true);
	}
	private void initializeLogic() {
		Window w = this.getWindow();w.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);w.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS); w.setStatusBarColor(Color.parseColor("#00766c"));
	}
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.browser, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        final android.support.v7.widget.SearchView searchView = (android.support.v7.widget.SearchView) searchItem.getActionView();
        if (!getIntent().getStringExtra("query").equals("-")) {
            webview1.loadUrl("http://www.google.com/search?q=".concat(getIntent().getStringExtra("query")));
            searchView.setQuery("www.google.com/search?q=".concat(getIntent().getStringExtra("query")), false);
        }
        else {
            webview1.loadUrl("https://www.google.com");
            searchView.setQuery(webview1.getUrl().replace("https://", "").replace("http://", ""), false);
        }
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                webview1.loadUrl("http://"+query.replace("https://",""));
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
                webview1.setWebViewClient(new WebViewClient() {
                    @Override
                    public void onPageStarted(WebView _param1, String _param2, Bitmap _param3) {
                        final String _url = _param2;
                        searchView.setQuery(_url.replace("https://", "").replace("http://", ""), false);
                        super.onPageStarted(_param1, _param2, _param3);
                    }

                    @Override
                    public void onPageFinished(WebView _param1, String _param2) {
                        final String _url = _param2;

                        super.onPageFinished(_param1, _param2);
                    }
                });
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
	    switch (item.getItemId()){
            case (R.id.action_refresh):
                webview1.loadUrl(webview1.getUrl());
                return true;
            case (R.id.action_back):
                webview1.goBack();
                return true;
            case (R.id.action_forward):
                webview1.goForward();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
	
	@Override
	public void onBackPressed() {
		if(webview1.canGoBack()) {
			webview1.goBack();
		} else {
			finish();
		}
	}
	
	@Override
	public void onStop() {
		super.onStop();
		InputUtil.hideKeyboard(getApplicationContext(), SearchActivity.this);
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

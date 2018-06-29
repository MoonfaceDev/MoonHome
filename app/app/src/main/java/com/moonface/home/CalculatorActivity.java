package com.moonface.home;

import android.app.*;
import android.os.*;
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

import java.io.IOException;
import java.util.*;
import java.text.*;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.Button;
import android.view.View;
import java.text.DecimalFormat;

public class CalculatorActivity extends AppCompatActivity {
	
	
	private Toolbar _toolbar;

	private static final char ADDITION = '+';
    private static final char SUBTRACTION = '-';
    private static final char MULTIPLICATION = '×';
    private static final char DIVISION = '÷';
    private char CURRENT_ACTION = '0';
    private double valueOne = Double.NaN;
    private double valueTwo;
    private DecimalFormat decimalFormat;

	private TextView result;
	private TextView sub_result;
	private ImageView clear;
	private Button num7;
	private Button num8;
	private Button num9;
	private Button divide;
	private Button num4;
	private Button num5;
	private Button num6;
	private Button multiply;
	private Button num1;
	private Button num2;
	private Button num3;
	private Button minus;
	private Button num0;
	private Button dot;
	private Button equal;
	private Button plus;
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.calculator);
		initialize();
		initializeLogic();
	}
	
	private void initialize() {

		Window w = this.getWindow();w.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);w.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS); w.setStatusBarColor(Color.parseColor("#009faf"));
		_toolbar = (Toolbar) findViewById(R.id._toolbar);
		setSupportActionBar(_toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _v) {
				onBackPressed();
			}
		});
		result = (TextView) findViewById(R.id.result);
		sub_result = (TextView) findViewById(R.id.sub_result);
		clear = (ImageView) findViewById(R.id.clear);
		num7 = (Button) findViewById(R.id.num7);
		num8 = (Button) findViewById(R.id.num8);
		num9 = (Button) findViewById(R.id.num9);
		divide = (Button) findViewById(R.id.divide);
		num4 = (Button) findViewById(R.id.num4);
		num5 = (Button) findViewById(R.id.num5);
		num6 = (Button) findViewById(R.id.num6);
		multiply = (Button) findViewById(R.id.multiply);
		num1 = (Button) findViewById(R.id.num1);
		num2 = (Button) findViewById(R.id.num2);
		num3 = (Button) findViewById(R.id.num3);
		minus = (Button) findViewById(R.id.minus);
		num0 = (Button) findViewById(R.id.num0);
		dot = (Button) findViewById(R.id.dot);
		equal = (Button) findViewById(R.id.equal);
		plus = (Button) findViewById(R.id.plus);
	}
	private void initializeLogic() {
        decimalFormat = new DecimalFormat("#.##########");
        dot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                result.setText(result.getText() + ".");
                if(result.getText().equals(".")){
                    result.setText("0.");
                }
                if(temporaryResult() != null){
                    sub_result.setText(formatNum(temporaryResult()));
                }
            }
        });

        num0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                result.setText(result.getText() + "0");
                if(temporaryResult() != null){
                    sub_result.setText(formatNum(temporaryResult()));
                }
            }
        });

        num1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                result.setText(result.getText() + "1");
                if(temporaryResult() != null){
                    sub_result.setText(formatNum(temporaryResult()));
                }
            }
        });

        num2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                result.setText(result.getText() + "2");
                if(temporaryResult() != null){
                    sub_result.setText(formatNum(temporaryResult()));
                }
            }
        });

        num3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                result.setText(result.getText() + "3");
                if(temporaryResult() != null){
                    sub_result.setText(formatNum(temporaryResult()));
                }
            }
        });

        num4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                result.setText(result.getText() + "4");
                if(temporaryResult() != null){
                    sub_result.setText(formatNum(temporaryResult()));
                }
            }
        });

        num5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                result.setText(result.getText() + "5");
                if(temporaryResult() != null){
                    sub_result.setText(formatNum(temporaryResult()));
                }
            }
        });

        num6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                result.setText(result.getText() + "6");
                if(temporaryResult() != null){
                    sub_result.setText(formatNum(temporaryResult()));
                }
            }
        });

        num7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                result.setText(result.getText() + "7");
                if(temporaryResult() != null){
                    sub_result.setText(formatNum(temporaryResult()));
                }
            }
        });

        num8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                result.setText(result.getText() + "8");
                if(temporaryResult() != null){
                    sub_result.setText(formatNum(temporaryResult()));
                }
            }
        });

        num9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                result.setText(result.getText() + "9");
                if(temporaryResult() != null){
                    sub_result.setText(formatNum(temporaryResult()));
                }
            }
        });

        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(result.getText() != null) {
                    computeCalculation();
                    CURRENT_ACTION = ADDITION;
                    sub_result.setText(formatNum(valueOne));
                    result.setText(null);
                }
            }
        });

        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(result.getText() != "") {
                    computeCalculation();
                    CURRENT_ACTION = SUBTRACTION;
                    sub_result.setText(formatNum(valueOne));
                    result.setText(null);
                }
                else{
                    result.setText("-");
                }
            }
        });

        multiply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(result.getText() != null) {
                    computeCalculation();
                    CURRENT_ACTION = MULTIPLICATION;
                    sub_result.setText(formatNum(valueOne));
                    result.setText(null);
                }
            }
        });

        divide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(result.getText() != null) {
                    computeCalculation();
                    CURRENT_ACTION = DIVISION;
                    sub_result.setText(formatNum(valueOne));
                    result.setText(null);
                }
            }
        });

        equal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(result.getText() != null) {
                    sub_result.setText(null);
                    computeCalculation();
                    result.setText(formatNum(valueOne));
                    valueOne = Double.NaN;
                    CURRENT_ACTION = '0';
                }
            }
        });

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                valueOne = Double.NaN;
                valueTwo = Double.NaN;
                sub_result.setText("");
                result.setText("");
                CURRENT_ACTION = '0';
            }
        });
	}
	
	@Override
	public void onBackPressed() {
		finish();
	}

    private void computeCalculation() {
        if(!Double.isNaN(valueOne)) {
            try {
                valueTwo = Double.parseDouble(result.getText().toString());
                sub_result.setText(null);

                if (CURRENT_ACTION == ADDITION)
                    valueOne = this.valueOne + valueTwo;
                else if (CURRENT_ACTION == SUBTRACTION)
                    valueOne = this.valueOne - valueTwo;
                else if (CURRENT_ACTION == MULTIPLICATION)
                    valueOne = this.valueOne * valueTwo;
                else if (CURRENT_ACTION == DIVISION)
                    valueOne = this.valueOne / valueTwo;
            }catch(Exception ex){
                Log.e("com.moonface.home", ex.toString());
            }
        }
        else {
            try {
                valueOne = Double.parseDouble(result.getText().toString());
            }
            catch (Exception e){}
        }
    }
    private Double temporaryResult() {
        if (result.getText() != ".") {
            valueTwo = Double.parseDouble(result.getText().toString());
            if (CURRENT_ACTION == ADDITION)
                return valueOne + valueTwo;
            else if (CURRENT_ACTION == SUBTRACTION)
                return valueOne - valueTwo;
            else if (CURRENT_ACTION == MULTIPLICATION)
                return valueOne * valueTwo;
            else if (CURRENT_ACTION == DIVISION)
                return valueOne / valueTwo;
            else {
                return null;
            }
        } else {
            return null;
        }
    }
    private String formatNum(double input){
        return decimalFormat.format(input);
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

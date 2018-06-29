package com.moonface.Util;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class InputUtil {
    public static void hideKeyboard(Context context, Activity activity){
    View currentFocus = activity.getCurrentFocus();
    if (currentFocus != null) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
        }
    }
    public static int getDisplayWidthPixels(Context context){
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    public static int getDisplayHeightPixels(Context context){
        return context.getResources().getDisplayMetrics().heightPixels;
    }
}

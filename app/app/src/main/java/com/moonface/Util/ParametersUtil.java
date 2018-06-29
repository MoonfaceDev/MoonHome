package com.moonface.Util;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;

public class ParametersUtil {
    public static float pixelsToDp(float pixels){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        return TypedValue.applyDimension( TypedValue.COMPLEX_UNIT_DIP, pixels, displayMetrics );
    }
    public static float dpsToPixels(float dps, Context context){
        float scale = context.getResources().getDisplayMetrics().density;
        return dps*scale + 0.5f;
    }
    public static String removeChars(String string, char[] chars){
        for(int i=0;i<chars.length;i++){
            string = string.replace(String.valueOf(chars[i]), "");
        }
        return string;
    }
}

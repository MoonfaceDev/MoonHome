package com.moonface.Util;

import android.app.WallpaperManager;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.PictureDrawable;
import android.graphics.drawable.RippleDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.util.Log;

import com.moonface.home.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class DrawableUtil {
    public static final String ANDROID_RESOURCE = "android.resource://";
    public static final String FORESLASH = "/";
    public static Drawable roundedBackground(float _radius, String _backcolor, int _strokewidth, String _strokecolor) {
        GradientDrawable _gradientDrawable = new GradientDrawable();
        _gradientDrawable.setColor(Color.parseColor(_backcolor));
        _gradientDrawable.setCornerRadius(_radius);
        _gradientDrawable.setStroke(_strokewidth, Color.parseColor(_strokecolor));
        return _gradientDrawable;
    }

    public static Drawable roundedBackground(float _radius, String _backcolor) {
        GradientDrawable _gradientDrawable = new GradientDrawable();
        _gradientDrawable.setColor(Color.parseColor(_backcolor));
        _gradientDrawable.setCornerRadius(_radius);
        return _gradientDrawable;
    }
    public static Drawable rippleDrawable(String _ripplecolor, String _backcolor){
        return new RippleDrawable(new ColorStateList(new int[][]{new int[]{}}, new int[]{ Color.parseColor(_ripplecolor)}), new ColorDrawable(Color.parseColor(_backcolor)), null);
    }
    public static Bitmap getCircleBitmap(Bitmap bitmap, int pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xffff0000;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setFilterBitmap(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawOval(rectF, paint);

        paint.setColor(Color.BLUE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth((float) 0);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }
    public static Bitmap getSquaredBitmap(Bitmap srcBmp){
        Bitmap dstBmp;
        if (srcBmp.getWidth() >= srcBmp.getHeight()){

            dstBmp = Bitmap.createBitmap(
                    srcBmp,
                    srcBmp.getWidth()/2 - srcBmp.getHeight()/2,
                    0,
                    srcBmp.getHeight(),
                    srcBmp.getHeight()
            );

        }else{

            dstBmp = Bitmap.createBitmap(
                    srcBmp,
                    0,
                    srcBmp.getHeight()/2 - srcBmp.getWidth()/2,
                    srcBmp.getWidth(),
                    srcBmp.getWidth()
            );
        }
        return dstBmp;
    }
    public static Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }

        return Bitmap.createScaledBitmap(image, width, height, true);
    }
    public static Bitmap getReadyBitmap(Bitmap image, int size){
        int width = image.getWidth();
        int height = image.getHeight();
        float ratio;
        if (width > height) {
            ratio = (float)size / (float)width;
            height = (int)((float)height * ratio);
            width = size;
        } else {
            ratio = (float)size / (float)height;
            width = (int)((float)width * ratio);
            height = size;
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }
    public static Drawable getResizedDrawable(Drawable drawable, int maxSize, Resources resources) {
        Bitmap image = ((BitmapDrawable)drawable).getBitmap();
        image = Bitmap.createScaledBitmap(image, 50, 50, false);
        return new BitmapDrawable(resources, image);
    }
    public static Bitmap getRotatedBitmap(Bitmap source, float angle)
    {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }
    public static Drawable getPhoneBackgroundDrawable(Context context){
        final WallpaperManager wallpaperManager = WallpaperManager.getInstance(context);
        return wallpaperManager.getDrawable();
    }
    public static String getMatColor()
    {
        String[] colors = new String[]{
                "#e51c23","#e91e63","#9c27b0","#673ab7","#3f51b5",
                "#5677fc","#03a9f4","#00bcd4","#009688","#259b24",
                "#8bc34a","#cddc39","#ffeb3b","#ff9800","#ff5722",
                "#795548","#9e9e9e","#607d8b"};
        return colors[new Random().nextInt(colors.length)];
    }
    public static int getRotation(String path) {
        boolean var1 = false;

        try {
            ExifInterface var2 = new ExifInterface(path);
            int var3 = var2.getAttributeInt("Orientation", -1);
            short var5;
            switch (var3) {
                case 3:
                    var5 = 180;
                    break;
                case 4:
                case 5:
                case 7:
                default:
                    var5 = 0;
                    break;
                case 6:
                    var5 = 90;
                    break;
                case 8:
                    var5 = 270;
            }

            return var5;
        } catch (IOException var4) {
            return 0;
        }
    }
}

package com.moonface.Util;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.Log;

import com.moonface.home.R;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileUtil {
    public static void removeFolder(String path){
        File dir = new File(path);
        if (dir.isDirectory())
        {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++)
            {
                new File(dir, children[i]).delete();
            }
        }
    }
    public static void saveBitmap(Bitmap bitmap, Uri uri){
        File pictureFile = new File(uri.toString());
        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
        }
        catch (FileNotFoundException e) {
            Log.d("FileUtil", "File not found: " + e.getMessage());
        }
        catch (IOException e) {
            Log.d("FileUtil", "Error accessing file: " + e.getMessage());
        }
    }
    public static File saveBitmap(Bitmap bitmap, String path){
        File pictureFile = new File(path);
        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
            return pictureFile;
        }
        catch (FileNotFoundException e) {
            Log.d("FileUtil", "File not found: " + e.getMessage());
        }
        catch (IOException e) {
            Log.d("FileUtil", "Error accessing file: " + e.getMessage());
        }
        return null;
    }
    public static Bitmap getBitmap(Uri uri, Context context){
        try {
            InputStream is = context.getContentResolver().openInputStream(uri);
            return BitmapFactory.decodeStream(is);
        }
        catch (FileNotFoundException e) {
            Log.e("FileNotFoundException", e.toString());
        }
        return null;
    }
    public static Bitmap getBitmap(File file, Context context){
        try {
            InputStream is = context.getContentResolver().openInputStream(FileProvider.getUriForFile(context, context.getPackageName()+".fileprovider", file));
            return BitmapFactory.decodeStream(is);
        }
        catch (FileNotFoundException e) {
            Log.e("FileNotFoundException", e.toString());
        }
        return null;
    }
    public static Bitmap getBitmap(String path, Context context){
        InputStream is = new ByteArrayInputStream(path.getBytes());
        return BitmapFactory.decodeStream(is);
    }
    public static Uri getUri(Context context, File file){
        return FileProvider.getUriForFile(context, context.getString(R.string.authorities), file);
    }
    public static void createFolder(String path, String folderName){
        String myfolder = path + "/" + folderName;
        File f=new File(myfolder);
        if(!f.exists()) {
            if (!f.mkdir()) {
                Log.d("FileUtil", "Can't create folder");
            }
        }
    }
}

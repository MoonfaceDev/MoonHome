package com.moonface.Util;

import android.os.Handler;

import java.util.concurrent.Delayed;

public class TimeUtil {
    public interface DelayedAction{
        void afterDelay();
    }

    public static void after(int ms, final DelayedAction delayCallback){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                delayCallback.afterDelay();
            }
        }, ms);
    }
}

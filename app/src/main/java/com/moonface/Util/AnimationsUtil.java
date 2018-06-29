package com.moonface.Util;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

public class AnimationsUtil {
    public static void fadeIn(final View _target, long duration) {
        Animation animation = AnimationUtils.loadAnimation(_target.getContext(), android.R.anim.fade_in);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                _target.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        animation.setDuration(duration);
        _target.startAnimation(animation);
    }

    public static void fadeOut(final View _target, long duration) {
        Animation animation = AnimationUtils.loadAnimation(_target.getContext(), android.R.anim.fade_out);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                _target.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        animation.setDuration(duration);
        _target.startAnimation(animation);
    }
    public static void grow(final View _target, float x, float y, long duration) {

    }
}

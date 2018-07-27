package com.moonface.Util;

import android.animation.ObjectAnimator;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

public class SwipeExpander {
    public static int FROM_TOP = 1;
    public static int FROM_BOTTOM = 2;
    public static int FROM_LEFT = 3;
    public static int FROM_RIGHT = 4;
    private float peekLength = 0;
    private View view;
    private int direction;
    private float V1;
    private float V2;
    private float D1;
    private float D2;
    private float differenceD;
    private float oldT;
    public static Boolean isExpanded = false;
    private static ObjectAnimator expansion = new ObjectAnimator();
    public SwipeExpander(View view){
        this.view = view;
    }
    public void setPeekLength(float length){
        peekLength = length;
    }
    public void setDirection(int direction){
        this.direction = direction;
    }
    public void expand(View v){
        collapse(v);
    }
    public void collapse(View v){
        if (!expansion.isRunning()) {
            if (isExpanded) {
                isExpanded = false;
                expansion.setTarget(v);
                switch (direction) {
                    case (1):
                        expansion.setPropertyName("translationY");
                        expansion.setFloatValues((float) (0 - peekLength));
                        break;
                    case (2):
                        expansion.setPropertyName("translationY");
                        expansion.setFloatValues((float) (peekLength));
                        break;
                    case (3):
                        expansion.setPropertyName("translationX");
                        expansion.setFloatValues((float) (0 - peekLength));
                        break;
                    case (4):
                        expansion.setPropertyName("translationX");
                        expansion.setFloatValues((float) (peekLength));
                        break;
                }
                expansion.setDuration(100);
                expansion.start();
            }
            else{
                isExpanded = true;
                expansion.setTarget(v);
                switch (direction) {
                    case (1):
                        expansion.setPropertyName("translationY");
                        expansion.setFloatValues((float) (0));
                        break;
                    case (2):
                        expansion.setPropertyName("translationY");
                        expansion.setFloatValues((float) (0));
                        break;
                    case (3):
                        expansion.setPropertyName("translationX");
                        expansion.setFloatValues((float) (0));
                        break;
                    case (4):
                        expansion.setPropertyName("translationX");
                        expansion.setFloatValues((float) (0));
                        break;
                }
                expansion.setDuration(100);
                expansion.start();
            }
        }
    }
    public static ObjectAnimator getAnimator(){
        return expansion;
    }
    public void start(){
        switch (direction) {
            case (1):
                view.setTranslationY(0 - peekLength);
            break;
            case (2):
                view.setTranslationY(peekLength);
            break;
            case (3):
                view.setTranslationX(0 - peekLength);
            break;
            case (4):
                view.setTranslationX(peekLength);
            break;
        }
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case (MotionEvent.ACTION_DOWN):
                        switch (direction) {
                            case (1):
                                V1 = event.getY();
                                D1 = event.getRawY();
                                oldT = v.getTranslationY();
                                break;
                            case (2):
                                V1 = event.getY();
                                D1 = event.getRawY();
                                oldT = v.getTranslationY();
                                break;
                            case (3):
                                V1 = event.getX();
                                D1 = event.getRawX();
                                oldT = v.getTranslationX();
                                break;
                            case (4):
                                V1 = event.getX();
                                D1 = event.getRawX();
                                oldT = v.getTranslationX();
                                break;
                        }
                        return true;
                    case (MotionEvent.ACTION_UP):
                        switch (direction) {
                            case (1):
                                V2 = event.getY();
                                break;
                            case (2):
                                V2 = event.getY();
                                break;
                            case (3):
                                V2 = event.getX();
                                break;
                            case (4):
                                V2 = event.getX();
                                break;
                        }
                        switch (direction) {
                            case (1):
                                if(V2>V1){
                                    expand(v);
                                }
                                if(V2<V1){
                                    collapse(v);
                                }
                                break;
                            case (2):
                                if(V2<V1){
                                    expand(v);
                                }
                                if(V2>V1){
                                    collapse(v);
                                }
                                break;
                            case (3):
                                if(V2>V1){
                                    expand(v);
                                }
                                if(V2<V1){
                                    collapse(v);
                                }
                                break;
                            case (4):
                                if(V2<V1){
                                    expand(v);
                                }
                                if(V2>V1){
                                    collapse(v);
                                }
                                break;
                        }
                        if (V1 == V2) {
                            v.performClick();
                        }
                        return true;
                    case (MotionEvent.ACTION_MOVE):
                        switch (direction) {
                            case (1):
                                D2 = event.getRawY();
                                break;
                            case (2):
                                D2 = event.getRawY();
                                break;
                            case (3):
                                D2 = event.getRawX();
                                break;
                            case (4):
                                D2 = event.getRawX();
                                break;
                        }
                        differenceD = D2-D1;
                        switch (direction) {
                            case (1):
                                v.setTranslationY(oldT+differenceD);
                                if(v.getTranslationY()>=0){
                                    v.setTranslationY(0);
                                    isExpanded=true;
                                }
                                if(v.getTranslationY()<=0-peekLength){
                                    v.setTranslationY(0-peekLength);
                                    isExpanded=false;
                                }
                                break;
                            case (2):
                                v.setTranslationY(oldT-differenceD);
                                if(v.getTranslationY()<=0){
                                    v.setTranslationY(0);
                                    isExpanded=true;
                                }
                                if(v.getTranslationY()>=peekLength){
                                    v.setTranslationY(peekLength);
                                    isExpanded=false;
                                }
                                break;
                            case (3):
                                v.setTranslationX(oldT+differenceD);
                                if(v.getTranslationX()>=0){
                                    v.setTranslationX(0);
                                    isExpanded=true;
                                }
                                if(v.getTranslationX()<=0-peekLength){
                                    v.setTranslationX(0-peekLength);
                                    isExpanded=false;
                                }
                                break;
                            case (4):
                                v.setTranslationX(oldT-differenceD);
                                if(v.getTranslationX()<=0){
                                    v.setTranslationX(0);
                                    isExpanded=true;
                                }
                                if(v.getTranslationX()>=peekLength){
                                    v.setTranslationX(peekLength);
                                    isExpanded=false;
                                }
                                break;
                        }
                        return true;
                }
                return false;
            }
        });
    }
}

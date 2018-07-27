package com.moonface.home;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.moonface.Util.ParametersUtil;

public class GridAdapter extends BaseAdapter {

    private final Context mContext;
    private final Drawable[] icons;
    private final Intent[] intents;
    private Drawable backDrawable;
    private ImageView icon;
    private int px = 0;

    GridAdapter(Context context, Drawable[] iconsList, Intent[] intents) {
        this.mContext = context;
        this.icons = iconsList;
        this.intents = intents;
    }
    GridAdapter(Context context, Drawable[] iconsList, Intent[] intents, int padding){
        this.mContext = context;
        this.icons = iconsList;
        this.intents = intents;
        this.px = Math.round(ParametersUtil.dpsToPixels(padding, context));
    }

    @Override
    public int getCount() {
        return icons.length;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater _inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View _v = convertView;
        if (_v == null) {
            if (_inflater != null) {
                _v = _inflater.inflate(R.layout._icon_view, null);
            }
        }
        if (_v != null) {
            icon = _v.findViewById(R.id.icon);
        }
        if (icon != null) {
            icon.setImageDrawable(icons[position]);
            icon.setPadding(px, px, px, px);
            if(backDrawable != null){
                icon.setBackground(backDrawable);
            }
        }
        final ImageView finalIcon = icon;
        if (icon != null) {
            icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.grow_up);
                    animation.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            v.setScaleX(1.0f);
                            v.setScaleY(1.0f);
                            mContext.startActivity(intents[position]);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    finalIcon.startAnimation(animation);
                }
            });
        }
        return icon;
    }

    public Drawable getBackDrawable() {
        return backDrawable;
    }
    public void setBackDrawable(Drawable drawable) {
        backDrawable = drawable;
    }
}

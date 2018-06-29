package com.moonface.home;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.moonface.Util.DrawableUtil;
import com.moonface.Util.ParametersUtil;

public class GridAdapter extends BaseAdapter {

    private final Context mContext;
    private final Drawable[] icons;
    private final Intent[] intents;

    // 1
    public GridAdapter(Context context, Drawable[] iconsList, Intent[] intents) {
        this.mContext = context;
        this.icons = iconsList;
        this.intents = intents;
    }

    // 2
    @Override
    public int getCount() {
        return icons.length;
    }

    // 3
    @Override
    public long getItemId(int position) {
        return position;
    }

    // 4
    @Override
    public Object getItem(int position) {
        return null;
    }

    // 5
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater _inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View _v = convertView;
        if (_v == null) {
            _v = _inflater.inflate(R.layout._icon_view, null);
        }
        final ImageView icon = _v.findViewById(R.id.icon);
        icon.setImageDrawable(icons[position]);
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
                icon.startAnimation(animation);
            }
        });
        return icon;
    }
}

package com.moonface.Util;

import android.content.Context;
import android.util.AttributeSet;

public class SquareImageView extends android.support.v7.widget.AppCompatImageView
{

    public SquareImageView(final Context context)
    {
        super(context);
    }

    public SquareImageView(final Context context, final AttributeSet attrs)
    {
        super(context, attrs);
    }

    public SquareImageView(final Context context, final AttributeSet attrs, final int defStyle)
    {
        super(context, attrs, defStyle);
    }


    @Override
    protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec)
    {
        super.onMeasure(Math.max(widthMeasureSpec,heightMeasureSpec), Math.max(widthMeasureSpec,heightMeasureSpec));
    }
}

package com.moonface.home;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class CameraPagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;
    Context context;

    public CameraPagerAdapter(FragmentManager fm, int NumOfTabs, Context context) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        if(context.getResources().getBoolean(R.bool.is_right_to_left)) {
            position = getCount() - position - 1;
        }
        switch (position) {
            case 0:
                CameraFragment1 tab1 = new CameraFragment1();
                return tab1;
            case 1:
                CameraFragment2 tab2 = new CameraFragment2();
                return tab2;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}

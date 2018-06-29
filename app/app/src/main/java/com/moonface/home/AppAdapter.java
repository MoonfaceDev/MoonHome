package com.moonface.home;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AppAdapter extends BaseAdapter {

    private LayoutInflater layoutInflater;
    private static List<AppDetails> listStorage;

    public AppAdapter(Context context) {
        layoutInflater =(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        listStorage = new ArrayList<AppDetails>();
        List<ApplicationInfo> applicationInfos = context.getPackageManager().getInstalledApplications(0);
        for (int i = 0; i<applicationInfos.size(); i++) {
            ApplicationInfo ai = applicationInfos.get(i);
            if(!isSystemPackage(ai)) {
                String appName = ai.loadLabel(context.getPackageManager()).toString();
                Drawable icon = ai.loadIcon(context.getPackageManager());
                String packageName = ai.packageName;
                listStorage.add(new AppDetails(appName, icon, packageName));
            }
        }
        Collections.sort(listStorage, new Comparator<AppDetails>() {
            public final int
            compare(AppDetails a, AppDetails b) {
                return collator.compare(a.name, b.name);
            }
            private final Collator collator = Collator.getInstance();
        });
    }
    private boolean isSystemPackage(ApplicationInfo appInfo) {
        return (appInfo.flags & appInfo.FLAG_SYSTEM) != 0;
    }

    @Override
    public int getCount() {
        return listStorage.size();
    }
    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public static String getPackageName(int position) {
        return listStorage.get(position).getPackageName();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder listViewHolder;
        if(convertView == null){
            listViewHolder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout._installed_app, parent, false);

            listViewHolder.textInListView = (TextView)convertView.findViewById(R.id.list_app_name);
            listViewHolder.imageInListView = (ImageView)convertView.findViewById(R.id.app_icon);
            convertView.setTag(listViewHolder);
        }else{
            listViewHolder = (ViewHolder)convertView.getTag();
        }
        listViewHolder.textInListView.setText(listStorage.get(position).getName());
        listViewHolder.imageInListView.setImageDrawable(listStorage.get(position).getIcon());

        return convertView;
    }

    class ViewHolder{
        TextView textInListView;
        ImageView imageInListView;
    }
    public class AppDetails {

        private String name;
        Drawable icon;
        String packageName;

        public AppDetails(String name, Drawable icon, String packageName) {
            this.name = name;
            this.icon = icon;
            this.packageName = packageName;
        }

        public String getName() {
            return name;
        }

        public Drawable getIcon() {
            return icon;
        }

        public String getPackageName() {
            return packageName;
        }
    }
}

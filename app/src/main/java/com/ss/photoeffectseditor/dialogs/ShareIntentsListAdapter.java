package com.ss.photoeffectseditor.dialogs;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ss.photoeffectseditor.AppConstants;
import com.ss.photoeffectseditor.R;
import com.ss.photoeffectseditor.widget.VibrateOnTouchListener;

import java.io.File;
import java.util.List;

public class ShareIntentsListAdapter extends BaseAdapter {
    private Context context;
    private String imagePath;
    private List<ResolveInfo> listResolve;
    private PackageManager pm;
    private ShareIntentsDialog dialog;
    private Typeface typeface = null;


    public ShareIntentsListAdapter(Context context, ShareIntentsDialog dialog,
                                   String imagePath) {
        this.context = context;
        this.imagePath = imagePath;
        this.dialog = dialog;
        pm = context.getPackageManager();
        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        sendIntent.setType("image/*");
        File f = new File(imagePath);
        sendIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(f));
        listResolve = pm.queryIntentActivities(sendIntent, 0);
    }

    public void setTypeface(Typeface tf) {
        typeface = tf;
    }

    @Override
    public int getCount() {
        if (listResolve == null)
            return 0;
        return listResolve.size();
    }

    @Override
    public Object getItem(int position) {
        return listResolve.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.share_intent_item, parent,
                    false);
            holder.image = (ImageView) convertView
                    .findViewById(R.id.share_intent_item_icon);
            holder.text = (TextView) convertView
                    .findViewById(R.id.share_intent_item_text);
            if (typeface != null) {
                holder.text.setTypeface(typeface);
            }
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final ResolveInfo info = listResolve.get(position);
        Drawable icon = info.loadIcon(pm);
        holder.image.setImageDrawable(icon);
        holder.text.setText(info.loadLabel(pm));
        final ActivityInfo activityInfo = info.activityInfo;
        final ComponentName componentName = new ComponentName(
                activityInfo.applicationInfo.packageName, activityInfo.name);

        convertView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                VibrateOnTouchListener vEx = new VibrateOnTouchListener(context);
                vEx.onTouchVibrate(AppConstants.ITEM_TOUCH_VIBRATE_TIME_MS);
                Intent intent = new Intent(Intent.ACTION_SEND);
                File f = new File(imagePath);
                intent.setType("image/*");
                intent.setClassName(activityInfo.packageName, activityInfo.name);
                intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(f));
                intent.setComponent(componentName);
                context.startActivity(intent);
                dialog.dismiss();
            }
        });
        return convertView;
    }

    private class ViewHolder {
        public ImageView image;
        public TextView text;
    }

}

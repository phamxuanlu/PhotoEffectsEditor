package com.ss.photoeffectseditor.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ss.photoeffectseditor.R;
import com.ss.photoeffectseditor.asynctasks.PreviewThumbAsyncTask;
import com.ss.photoeffectseditor.widget.BaseToolObject;

import java.util.List;

/**
 * Created by phamxuanlu@gmail.com on 3/17/2015.
 */
@Deprecated
public class EditorListToolContentAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private List<BaseToolObject> data;
    private Bitmap bmThumb;

    public EditorListToolContentAdapter(Context context, List<BaseToolObject> data) {
        this.context = context;
        this.data = data;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        if (this.data == null)
            return 0;
        else return this.data.size();
    }

    @Override
    public BaseToolObject getItem(int position) {
        if (this.data == null) {
            return null;
        } else {
            return this.data.get(position);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.ui_list_tool_content_item, parent, false);
            holder = new ViewHolder();
            holder.toolIcon = (ImageView) convertView.findViewById(R.id.thumbImage);
            holder.toolName = (TextView) convertView.findViewById(R.id.thumbName);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final BaseToolObject obj = this.data.get(position);
        holder.toolIcon.setImageResource(R.drawable.ic_btn_edit);
        holder.toolName.setText(obj.name);
        return convertView;
    }

    private class ViewHolder {
        public ImageView toolIcon;
        public TextView toolName;
        public PreviewThumbAsyncTask task;
        public boolean thumbProcessed;
    }
}

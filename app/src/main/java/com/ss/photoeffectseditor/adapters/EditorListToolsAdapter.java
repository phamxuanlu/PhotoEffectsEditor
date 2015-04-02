package com.ss.photoeffectseditor.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ss.photoeffectseditor.R;
import com.ss.photoeffectseditor.models.ToolObject;

import java.util.List;

/**
 * Created by phamxuanlu@gmail.com on 3/5/2015.
 * Lớp Adapter này chịu trách nhiệm render một List các ToolObject ra HListView
 * tạo các thumbnail preview
 */
public class EditorListToolsAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private List<ToolObject> data;


    public EditorListToolsAdapter(Context context, List<ToolObject> data) {
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
    public ToolObject getItem(int position) {
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
            convertView = inflater.inflate(R.layout.ui_list_tool_item, parent, false);
            holder = new ViewHolder();
            holder.toolIcon = (ImageView) convertView.findViewById(R.id.transformIcon);
            holder.toolName = (TextView) convertView.findViewById(R.id.transformName);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final ToolObject obj = this.data.get(position);
        holder.toolIcon.setImageResource(obj.iconResourceId);
        holder.toolName.setText(obj.name);
        return convertView;
    }

    private class ViewHolder {
        public ImageView toolIcon;
        public TextView toolName;
    }

}

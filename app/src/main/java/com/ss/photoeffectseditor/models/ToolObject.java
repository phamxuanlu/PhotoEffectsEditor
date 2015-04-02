package com.ss.photoeffectseditor.models;

import com.ss.photoeffectseditor.ip.ITransformation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by phamxuanlu@gmail.com on 3/5/2015.
 * Lớp này lưu giữ thông tin giao diện ứng dụng về một phép biến đổi ảnh ví dụ như tên, toolIcon, Transform Object ...
 * Tập hợp List các đối tượng UITransformation được vẽ lên trên giao diện của editor cho người dùng chọn
 */
public class ToolObject {
    public String name;
    public int iconResourceId;
    public ITransformation transform;
    public ToolType toolType;
    public boolean hasParameters;
    public List<ToolObject> detailFeatures;


    public ToolObject() {
        detailFeatures = new ArrayList<ToolObject>();
    }

    public void addChild(ToolObject child) {
        this.detailFeatures.add(child);
    }

    public enum ToolType {
        TOOL_TYPE, CONTENT_TYPE, OPTION_TYPE
    }
}

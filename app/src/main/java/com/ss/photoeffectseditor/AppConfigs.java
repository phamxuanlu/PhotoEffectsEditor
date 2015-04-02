package com.ss.photoeffectseditor;

import com.ss.photoeffectseditor.models.ToolObject;

import java.util.List;

/**
 * Created by phamxuanlu@gmail.com on 3/5/2015.
 * Chứa các dữ liệu tạo sẵn hoặc cấu hình trong chương trình
 */
public class AppConfigs {

    public List<ToolObject> editorFeatures;
    public int deviceWidth;
    public int deviceHeight;
    public int thumbnailSize = 100;

    private static AppConfigs _instance;

    private AppConfigs() {

    }

    public static AppConfigs getInstance() {
        if (_instance == null)
            _instance = new AppConfigs();

        return _instance;
    }
}

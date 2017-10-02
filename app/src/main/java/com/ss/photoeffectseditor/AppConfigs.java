package com.ss.photoeffectseditor;


import com.ss.photoeffectseditor.widget.BaseToolObject;

import java.util.List;

/**
 * Created by phamxuanlu@gmail.com on 3/5/2015.
 * Chứa các dữ liệu tạo sẵn hoặc cấu hình trong chương trình
 * 
 */
public class AppConfigs {

    public List<BaseToolObject> editorFeatures;
    public int deviceWidth;
    public int deviceHeight;
    public int thumbnailSize = 100;
    public String TYPE_FACE = "fonts/Existence-Light.otf";

    private static AppConfigs _instance;

    private AppConfigs() {

    }

    public static AppConfigs getInstance() {
        if (_instance == null)
            _instance = new AppConfigs();

        return _instance;
    }
}

package com.ss.photoeffectseditor.widget;

import com.ss.photoeffectseditor.imageprocessing.ITransformation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by L on 4/1/2015.
 */
public class BaseToolObject {
    public String name;
    public int iconResourceId;
    public ITransformation transform;
    public List<BaseToolObject> childrenTools;

    public BaseToolObject() {
        childrenTools = new ArrayList<BaseToolObject>();

    }

    public void addChild(BaseToolObject child) {
        this.childrenTools.add(child);
    }

    public List<BaseToolObject> getChildrenTools() {
        return this.childrenTools;
    }

}

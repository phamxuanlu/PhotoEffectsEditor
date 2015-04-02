package com.ss.photoeffectseditor.ip;

import android.graphics.Bitmap;

/**
 * Created by L on 4/1/2015.
 */
public abstract class AbstractEffectTransformation extends BaseTransformation {
    protected Bitmap thumbImage;

    public abstract void setThumbImage(Bitmap bmThumb);

    public abstract Bitmap getThumbnail();


}

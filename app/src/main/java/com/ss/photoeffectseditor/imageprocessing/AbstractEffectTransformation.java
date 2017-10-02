package com.ss.photoeffectseditor.imageprocessing;

import android.graphics.Bitmap;

/**
 * Created by L on 4/1/2015.
 */
public abstract class AbstractEffectTransformation extends AbstractBaseTransformation {
    protected Bitmap thumbImage;

    public abstract void setThumbImage(Bitmap bmThumb);
    public abstract Bitmap getThumbnail();

}

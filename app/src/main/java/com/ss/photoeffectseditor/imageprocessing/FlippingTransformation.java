package com.ss.photoeffectseditor.imageprocessing;

import android.graphics.Bitmap;
import android.graphics.Matrix;

/**
 * Created by L on 4/13/2015.
 */
public class FlippingTransformation extends AbstractEffectTransformation {
    public static final int FLIP_VERTICAL = 1;
    public static final int FLIP_HORIZONTAL = 2;

    private int type;

    public FlippingTransformation(int type) {
        this.type = type;
    }

    @Override
    public void setThumbImage(Bitmap bmThumb) {

    }

    @Override
    public Bitmap getThumbnail() {
        return null;
    }

    @Override
    public Bitmap perform(Bitmap inp) {
        Matrix matrix = new Matrix();
        if (type == FLIP_HORIZONTAL) {
            // y = y * -1
            matrix.preScale(1.0f, -1.0f);

        } else if (type == FLIP_VERTICAL) {
            matrix.preScale(-1.0f, 1.0f);
        }
        return Bitmap.createBitmap(inp, 0, 0, inp.getWidth(), inp.getHeight(), matrix, true);
    }
}

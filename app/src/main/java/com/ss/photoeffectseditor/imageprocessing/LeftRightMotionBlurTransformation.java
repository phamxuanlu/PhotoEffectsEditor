package com.ss.photoeffectseditor.imageprocessing;

import android.graphics.Bitmap;

/**
 * Created by L on 4/13/2015.
 */
public class LeftRightMotionBlurTransformation extends AbstractEffectTransformation {

    private ConvolutionMatrix LRMBMatrix;

    public LeftRightMotionBlurTransformation() {
        int[][] config = new int[][]{
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {2, 2, 2, 2, 2, 2, 2, 2, 2},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0}
        };
        LRMBMatrix = new ConvolutionMatrix(config, 18, 0);
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
        return LRMBMatrix.convolute(inp);
    }
}

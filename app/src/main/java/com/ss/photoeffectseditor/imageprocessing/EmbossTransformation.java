package com.ss.photoeffectseditor.imageprocessing;

import android.graphics.Bitmap;

/**
 * Created by phamxuanlu@gmail.com on 4/12/2015.
 */
public class EmbossTransformation extends AbstractEffectTransformation {

    private ConvolutionMatrix embossMatrix;


    public EmbossTransformation() {
        int[][] config = new int[][]{
                {-1, -1, 0},
                {-1,  0, 1},
                { 0,  1, 1}
        };

        embossMatrix = new ConvolutionMatrix(config, 1, 128);
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
        return embossMatrix.convolute(inp);
    }
}

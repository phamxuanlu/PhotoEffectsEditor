package com.ss.photoeffectseditor.imageprocessing;

import android.graphics.Bitmap;
import android.graphics.Color;

public class GrayscaleTransformation extends AbstractEffectTransformation {
    @Override
    public void setThumbImage(Bitmap bmThumb) {
        this.thumbImage = bmThumb;
    }

    @Override
    public Bitmap getThumbnail() {
        return perform(this.thumbImage);
    }

    private boolean isBostColor;
    private int intensityBost;

    public GrayscaleTransformation() {
    }

    public GrayscaleTransformation(int intensityBost) {
        isBostColor = true;
        this.intensityBost = intensityBost;
    }

    @Override
    public Bitmap perform(Bitmap inp) {
        Bitmap bmOut = Bitmap.createBitmap(inp.getWidth(), inp.getHeight(),
                inp.getConfig());
        int A, R, G, B;
        int w = inp.getWidth();
        int h = inp.getHeight();
        int[] colors = new int[w * h];
        inp.getPixels(colors, 0, w, 0, 0, w, h);
        int i = 0;
        int j = 0;
        int pos;
        int val;
        for (i = 0; i < h; i++) {
            for (j = 0; j < w; j++) {
                pos = i * w + j;
                A = (colors[pos] >> 24) & 0xFF;
                R = (colors[pos] >> 16) & 0xFF;
                G = (colors[pos] >> 8) & 0xFF;
                B = colors[pos] & 0xFF;
                if (!isBostColor) {
                    R = G = B = (int) (0.299 * R + 0.587 * G + 0.114 * B);
                } else {
                    val = intensityBost
                            + (int) (0.299 * R + 0.587 * G + 0.114 * B);
                    if (val > 255) {
                        val = 255;
                    }
                    R = G = B = val;
                }
                colors[pos] = Color.argb(A, R, G, B);
            }
        }
        bmOut.setPixels(colors, 0, w, 0, 0, w, h);
        return bmOut;
    }

}

package com.ss.photoeffectseditor.ip;

import android.graphics.Bitmap;

/**
 * Created by L on 3/19/2015.
 */
public class BrightnessTransformation extends AbstractOptimizeTransformation {

    private int brightnessLevel;

    public BrightnessTransformation() {

    }

    public void setBrightnessLevel(int br) {
        this.brightnessLevel = br;
    }

    public int getBrightnessLevel() {
        return this.brightnessLevel;
    }

    @Override
    public Bitmap perform(Bitmap inp) {
        Bitmap bmOut = Bitmap.createBitmap(inp.getWidth(), inp.getHeight(), inp.getConfig());

        int A, R, G, B;
        int i, j;
        int pos;
        int w = inp.getWidth();
        int h = inp.getHeight();
        int[] colors = new int[w * h];
        inp.getPixels(colors, 0, w, 0, 0, w, h);
        for (i = 0; i < h; i++) {
            for (j = 0; j < w; j++) {
                pos = i * w + j;
                A = (colors[pos] >> 24) & 0xFF;
                R = (colors[pos] >> 16) & 0xFF;
                R += this.brightnessLevel;
                if (R < 0 || R > 255) {
                    R -= this.brightnessLevel;
                }
                G = (colors[pos] >> 8) & 0xFF;
                G += this.brightnessLevel;
                if (G < 0 || G > 255) {
                    G -= this.brightnessLevel;
                }
                B = colors[pos] & 0xFF;
                B += this.brightnessLevel;
                if (B < 0 || B > 255) {
                    B -= this.brightnessLevel;
                }
                int nvalue;
                nvalue = (A << 24) | (R << 16) | (G << 8) | B;
                colors[pos] = nvalue;
            }
        }
        bmOut.setPixels(colors, 0, w, 0, 0, w, h);
        return bmOut;
    }
}

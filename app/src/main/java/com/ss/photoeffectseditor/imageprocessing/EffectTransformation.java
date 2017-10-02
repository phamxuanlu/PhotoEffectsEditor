package com.ss.photoeffectseditor.imageprocessing;

import android.graphics.Bitmap;

public class EffectTransformation extends AbstractEffectTransformation {
    private Bitmap mBitmap;
    private EffectObject mEffect;

    public EffectTransformation(EffectObject effect) {
        this.mEffect = effect;
    }

    @Override
    public Bitmap perform(Bitmap inp) {
        this.mBitmap = inp;
        Bitmap bmOut = inp.copy(inp.getConfig(), true);
        int A, R, G, B;
        int w = inp.getWidth();
        int h = inp.getHeight();
        int[] pixels = new int[w * h];
        int i = 0;
        int j = 0;
        int pos = 0;
        int value;
        inp.getPixels(pixels, 0, w, 0, 0, w, h);
        for (i = 0; i < h; i++) {
            for (j = 0; j < w; j++) {
                pos = i * w + j;
                A = (pixels[pos] >> 24) & 0xFF;
                R = mEffect.RED[(pixels[pos] >> 16) & 0xFF];
                G = mEffect.GREEN[(pixels[pos] >> 8) & 0xFF];
                B = mEffect.BLUE[pixels[pos] & 0xFF];
                pixels[pos] = (A << 24) | (R << 16) | (G << 8) | B;
            }
        }
        bmOut.setPixels(pixels, 0, w, 0, 0, w, h);
        return bmOut;
    }

    @Override
    public void setThumbImage(Bitmap bmThumb) {

    }

    @Override
    public Bitmap getThumbnail() {
        return null;
    }
}

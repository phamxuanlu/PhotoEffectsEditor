package com.ss.photoeffectseditor.imageprocessing;

import android.graphics.Bitmap;

public class InvertTransformation extends AbstractEffectTransformation {

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
        int nvalue;
        for (i = 0; i < h; i++) {
            for (j = 0; j < w; j++) {
                pos = i * w + j;
                A = (colors[pos] >> 24) & 0xFF;
                R = 255 - (colors[pos] >> 16) & 0xFF;
                G = 255 - (colors[pos] >> 8) & 0xFF;
                B = 255 - colors[pos] & 0xFF;
                nvalue = (A << 24) | (R << 16) | (G << 8) | B;
                colors[pos] = nvalue;
            }
        }
        bmOut.setPixels(colors, 0, w, 0, 0, w, h);
        return bmOut;
    }

    @Override
    public void setThumbImage(Bitmap bmThumb) {
        this.thumbImage = bmThumb;
    }

    @Override
    public Bitmap getThumbnail() {
        return perform(this.thumbImage);
    }
}

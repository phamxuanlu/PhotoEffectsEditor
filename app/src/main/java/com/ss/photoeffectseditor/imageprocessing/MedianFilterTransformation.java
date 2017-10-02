package com.ss.photoeffectseditor.imageprocessing;

import android.graphics.Bitmap;

/**
 * Created by phamxuanlu@gmail.com on 3/19/2015.
 */
@Deprecated
public class MedianFilterTransformation extends AbstractEffectTransformation {

    private int windowHalfSize;

    public MedianFilterTransformation(int windowSize) {
        windowHalfSize = 1;
    }

    public void setWindowSize(int windowSize) {
        this.windowHalfSize = windowSize / 2;
    }

    @Override
    public Bitmap perform(Bitmap inp) {
        Bitmap bmOut = Bitmap.createBitmap(inp.getWidth(), inp.getHeight(), inp.getConfig());
        int w = inp.getWidth() - windowHalfSize;
        int h = inp.getHeight() - windowHalfSize;
        int[] colors = new int[w * h];
        inp.getPixels(colors, 0, w, 0, 0, w, h);
        int i = 0;
        int j = 0;
        int k, l;
        int pos;
        int processPixelPos;
        int A = 0;
        int avgR = 0;
        int avgG = 0;
        int avgB = 0;
        int totalPixel = 2 * this.windowHalfSize + 1;
        totalPixel *= totalPixel;
        int nvalue;
        for (i = this.windowHalfSize; i < h; i++) {
            for (j = this.windowHalfSize; j < w; j++) {
                processPixelPos = i * w + j;
                A = (colors[processPixelPos] >> 24) & 0xFF;
                avgR = 0;
                avgG = 0;
                avgB = 0;

                //Window
                for (k = -windowHalfSize; k <= windowHalfSize; k++) {
                    for (l = -windowHalfSize; l <= windowHalfSize; l++) {
                        pos = (i + k) * w + (h + l);
                        avgR += (colors[pos] >> 16) & 0xFF;
                        avgG = (colors[pos] >> 8) & 0xFF;
                        avgB = colors[pos] & 0xFF;
                    }
                }

                avgR = (int) ((float) avgR / totalPixel);
                avgG = (int) ((float) avgG / totalPixel);
                avgB = (int) ((float) avgB / totalPixel);
                nvalue = (A << 24) | (avgR << 16) | (avgG << 8) | avgB;
                colors[processPixelPos] = nvalue;
            }
        }

        return null;
    }

    @Override
    public void setThumbImage(Bitmap bmThumb) {

    }

    @Override
    public Bitmap getThumbnail() {
        return null;
    }
}

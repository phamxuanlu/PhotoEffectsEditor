package com.ss.photoeffectseditor.ip;

import android.graphics.Bitmap;

/**
 * Created by phamxuanlu@gmail.com on 3/19/2015.
 */
public class MeanFilterTransformation implements ITransformation {

    private int windowHalfSize;

    public MeanFilterTransformation(int windowSize) {

    }

    public void setWindowSize(int windowSize) {
        this.windowHalfSize = (int) (windowSize / 2);
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
        int k = -windowHalfSize;
        int l = -windowHalfSize;
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
                for (; k <= windowHalfSize; k++) {
                    for (; l <= windowHalfSize; l++) {
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
}

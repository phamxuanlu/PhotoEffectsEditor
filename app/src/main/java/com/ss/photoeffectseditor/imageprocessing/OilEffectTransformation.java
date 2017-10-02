package com.ss.photoeffectseditor.imageprocessing;

import android.graphics.Bitmap;

/**
 * Authour        : PHAMXUANLU@GMAIL.COM
 * License        : phamxuanlu@gmail.com
 * Date           : 12:00 PM - 4/16/2015
 * Description    :
 */

@Deprecated
public class OilEffectTransformation extends AbstractEffectTransformation {

    private final int OIL_FILTER_LEVEL = 256;
    private final int OIL_RANGE = 10;

    private int oilRange;

    public OilEffectTransformation() {
        this.oilRange = 10;
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
        int A, R, G, B;
        int i, j;
        int[] rHis, gHis, bHis;
        int w = inp.getWidth();
        int h = inp.getHeight();
        int rowOffset, colOffset;
        Bitmap bmOut = Bitmap.createBitmap(w, h, inp.getConfig());
        int[] pixels = new int[w * h];
        inp.getPixels(pixels, 0, w, 0, 0, w, h);

        int row, col;
        int pos;
        int maxR, maxG, maxB;
        for (i = 0; i < h; i++) {
            for (j = 0; j < w; j++) {
                rHis = new int[OIL_FILTER_LEVEL];
                gHis = new int[OIL_FILTER_LEVEL];
                bHis = new int[OIL_FILTER_LEVEL];
                for (row = -this.oilRange; row < this.oilRange; row++) {
                    rowOffset = i + row;
                    if (rowOffset >= 0 && rowOffset < h) {
                        for (col = -this.oilRange; col < this.oilRange; col++) {
                            colOffset = j + col;
                            if (colOffset >= 0 && colOffset < w) {
                                pos = rowOffset * w + colOffset;
                                R = (pixels[pos] >> 16) & 0xFF;
                                G = (pixels[pos] >> 8) & 0xFF;
                                B = pixels[pos] & 0xFF;
                                rHis[R]++;
                                gHis[G]++;
                                bHis[B]++;
                            }
                        }
                    }

                }
                maxR = maxG = maxB = 0;
                for (row = 1; row < OIL_FILTER_LEVEL; row++) {
                    if (rHis[row] > rHis[maxR]) {
                        maxR = row;
                    }
                    if (gHis[row] > rHis[maxG]) {
                        maxG = row;
                    }
                    if (bHis[row] > bHis[maxB]) {
                        maxB = row;
                    }
                }

                if (rHis[maxR] != 0 && gHis[maxG] != 0 && bHis[maxB] != 0) {
                    pos = i * w + j;
                    pixels[pos] = (pixels[pos] << 24) | (maxR << 16) | (maxG << 8) | maxB;
                }

            }
        }
        bmOut.setPixels(pixels, 0, w, 0, 0, w, h);
        return bmOut;
    }
}

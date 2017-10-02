package com.ss.photoeffectseditor.imageprocessing;

import android.graphics.Bitmap;

/**
 * Authour        : PHAMXUANLU@GMAIL.COM
 * License        : phamxuanlu@gmail.com
 * Date           : 12:23 PM - 4/26/2015
 * Description    :
 */
public class AverageSmoothTransformation extends AbstractEffectTransformation {

    private int WINDOW_H_SIZE = 2;

    public AverageSmoothTransformation() {

    }

    public void setWindowHalfSize(int whs) {
        this.WINDOW_H_SIZE = whs;
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
        int i, j, k, l;
        int h = inp.getHeight();
        int w = inp.getWidth();
        Bitmap bmOut = Bitmap.createBitmap(w, h, inp.getConfig());
        int[] pixels = new int[w * h];
        inp.getPixels(pixels, 0, w, 0, 0, w, h);
        int pos;
        int cPos;
        int sumR, sumG, sumB;
        int totalPix = WINDOW_H_SIZE * 2 + 1;
        totalPix *= totalPix;
        for (i = WINDOW_H_SIZE; i < h - WINDOW_H_SIZE; i++) {
            for (j = WINDOW_H_SIZE; j < w - WINDOW_H_SIZE; j++) {
                sumR = sumG = sumB = 0;
                cPos = i * w + j;
                A = (pixels[cPos] >> 24) & 0xFF;
                for (k = -WINDOW_H_SIZE; k <= WINDOW_H_SIZE; k++) {
                    for (l = -WINDOW_H_SIZE; l <= WINDOW_H_SIZE; l++) {
                        pos = (i + l) * w + j + k;
                        sumR += (pixels[pos] >> 16) & 0xFF;
                        sumG += (pixels[pos] >> 8) & 0xFF;
                        sumB += pixels[pos] & 0xFF;
                    }
                }

                pixels[cPos] = (A << 24) | ((sumR / totalPix) << 16) | ((sumG / totalPix) << 8) | (sumB / totalPix);
            }
        }
        bmOut.setPixels(pixels, 0, w, 0, 0, w, h);

        return bmOut;
    }
}

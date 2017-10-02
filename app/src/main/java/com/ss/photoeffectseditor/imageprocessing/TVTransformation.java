package com.ss.photoeffectseditor.imageprocessing;

import android.graphics.Bitmap;
import android.graphics.Color;

/**
 * Authour        : PHAMXUANLU@GMAIL.COM
 * License        : phamxuanlu@gmail.com
 * Date           : 10:51 AM - 4/16/2015
 * Description    :
 */
public class TVTransformation extends AbstractEffectTransformation {

    private int gap = 4;

    @Override
    public void setThumbImage(Bitmap bmThumb) {

    }

    @Override
    public Bitmap getThumbnail() {
        return null;
    }

    private int normalize(int val) {
        if (val > 255)
            return 255;
        if (val < 0)
            return 0;
        return val;
    }

    @Override
    public Bitmap perform(Bitmap inp) {
        int R, G, B;
        int i, j, k;
        int w = inp.getWidth();
        int h = inp.getHeight();
        Bitmap bmOut = Bitmap.createBitmap(w, h, inp.getConfig());
        int[] pixels = new int[w * h];
        inp.getPixels(pixels, 0, w, 0, 0, w, h);
        int index;

        for (i = 0; i < w; i++) {
            for (j = 0; j < h; j += gap) {
                R = G = B = 0;
                for (k = 0; k < gap; k++) {
                    index = (j + k) * w + i;
                    if (index < pixels.length) {
                        R += ((pixels[index] >> 16) & 0xFF) / gap;
                        G += ((pixels[index] >> 8) & 0xFF) / gap;
                        B += (pixels[index] & 0xFF) / gap;
                    }
                }

                R = normalize(R);
                G = normalize(G);
                B = normalize(B);

                for (k = 0; k < gap; k++) {
                    index = (j + k) * w + i;
                    if (index < pixels.length) {
                        if (k == 0) {
                            pixels[index] = Color.rgb(R, 0, 0);
                        }
                        if (k == 1) {
                            pixels[index] = Color.rgb(0, G, 0);
                        }
                        if (k == 2) {
                            pixels[index] = Color.rgb(0, 0, B);
                        }
                    }
                }
            }
        }
        bmOut.setPixels(pixels, 0, w, 0, 0, w, h);
        return bmOut;
    }
}

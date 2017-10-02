package com.ss.photoeffectseditor.imageprocessing;

import android.graphics.Bitmap;
import android.graphics.Color;

/**
 * Authour        : PHAMXUANLU@GMAIL.COM
 * License        : phamxuanlu@gmail.com
 * Date           : 4:43 PM - 4/17/2015
 * Description    :
 */
public class LomoEffectTransformation extends AbstractEffectTransformation {

    private int roundRadius;
    private double pixelsFallOff;

    @Override
    public void setThumbImage(Bitmap bmThumb) {

    }

    @Override
    public Bitmap getThumbnail() {
        return null;
    }

    private double scale(double distance) {
        return 1 - Math.pow((distance - roundRadius) / pixelsFallOff, 2);
    }

    private int normalize(double v) {
        if (v > 255)
            return 255;
        if (v < 0)
            return 0;
        return (int) v;
    }

    @Override
    public Bitmap perform(Bitmap inp) {
        int R, G, B;
        int w = inp.getWidth();
        int h = inp.getHeight();
        if (w > h) {
            roundRadius = h / 2;
        } else {
            roundRadius = w / 2;
        }
        double centerX = w / 2;
        double centerY = h / 2;
        int pos;
        pixelsFallOff = 10;
        int i, j;
        double distance;
        double scaler;
        int[] pixels = new int[w * h];
        inp.getPixels(pixels, 0, w, 0, 0, w, h);
        for (i = 0; i < h; i++) {
            for (j = 0; j < w; j++) {
                distance = Math.sqrt(Math.pow(centerX - j, 2) + Math.pow(centerY - i, 2));
                pos = i * w + j;
                if (distance > roundRadius) {
                    R = (pixels[pos] >> 16) & 0xFF;
                    G = (pixels[pos] >> 8) & 0xFF;
                    B = pixels[pos] & 0xFF;

                    scaler = scale(distance);
                    scaler = Math.abs(scaler);
                    R = normalize(R - scaler);
                    G = normalize(G - scaler);
                    B = normalize(B - scaler);

                    pixels[pos] = Color.rgb(R, G, B);
                }
            }
        }
        Bitmap bmOut = Bitmap.createBitmap(w, h, inp.getConfig());
        bmOut.setPixels(pixels, 0, w, 0, 0, w, h);

        return bmOut;
    }
}

package com.ss.photoeffectseditor.imageprocessing;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

import com.ss.photoeffectseditor.utils.GLog;

/**
 * Authour        : PHAMXUANLU@GMAIL.COM
 * License        : phamxuanlu@gmail.com
 * Date           : 2:50 PM - 4/16/2015
 * Description    :
 */
public class SketchEffectTransformation extends AbstractEffectTransformation {
    private int threshold = 7;

    @Override
    public void setThumbImage(Bitmap bmThumb) {

    }

    @Override
    public Bitmap getThumbnail() {
        return null;
    }

    private int getGray(int cl) {
        int R, G, B;
        R = (cl >> 16) & 0xFF;
        G = (cl >> 8) & 0xFF;
        B = cl & 0xFF;

        return (int) (0.299 * R + 0.587 * G + 0.114 * B);
    }

    @Override
    public Bitmap perform(Bitmap inp) {
        int w = inp.getWidth();
        int h = inp.getHeight();
        int[] pixels = new int[w * h];
        inp.getPixels(pixels, 0, w, 0, 0, w, h);
        int[] originPixels = new int[w * h];
        inp.getPixels(originPixels, 0, w, 0, 0, w, h);
        int pos;
        int i, j;
        int sketchColor = Color.parseColor("#5c6274");
        Log.v("Sketch Color", sketchColor + "");
        int centerGray;
        int rightBottomIndex;
        int rightBottomGray;
        Bitmap bmOut = Bitmap.createBitmap(w, h, inp.getConfig());
        for (i = 1; i < h - 1; i++) {
            for (j = 1; j < w - 1; j++) {
                pos = i * w + j;
                centerGray = getGray(originPixels[pos]);
                rightBottomIndex = (i + 1) * w + j + 1;
                if (rightBottomIndex < pixels.length) {
                    rightBottomGray = getGray(originPixels[rightBottomIndex]);
                    if (Math.abs(centerGray - rightBottomGray) >= threshold) {
                        pixels[pos] = sketchColor;
                    } else {
                        pixels[pos] = Color.rgb(255, 255, 255);
                    }
                }
            }
        }
        bmOut.setPixels(pixels, 0, w, 0, 0, w, h);
        return bmOut;
    }
}

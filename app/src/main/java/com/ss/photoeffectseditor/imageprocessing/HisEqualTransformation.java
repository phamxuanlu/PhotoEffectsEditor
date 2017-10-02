package com.ss.photoeffectseditor.imageprocessing;

import android.graphics.Bitmap;

import java.util.HashMap;

/**
 * Created by phamxuanlu@gmail.com on 3/15/2015.
 */
public class HisEqualTransformation extends AbstractEffectTransformation {
    private int[] redIL;
    private int[] greenIL;
    private int[] blueIL;
    //    private double[] redPx;
//    private double[] greenPx;
//    private double[] bluePx;
    private double totalPixel;

    @Override
    public Bitmap perform(Bitmap inp) {
        Bitmap bmOut = inp.copy(inp.getConfig(), true);
        int A, R, G, B;
        int i, j;
        int h = inp.getHeight();
        int w = inp.getWidth();
        totalPixel = w * h;
        int[] pixels = new int[w * h];
        inp.getPixels(pixels, 0, w, 0, 0, w, h);
        HashMap<Integer, Integer> redLT = new HashMap<>();
        HashMap<Integer, Integer> greenLT = new HashMap<>();
        HashMap<Integer, Integer> blueLT = new HashMap<>();

        redIL = new int[256];
        greenIL = new int[256];
        blueIL = new int[256];
        int pos;
        for (i = 0; i < h; i++) {
            for (j = 0; j < w; j++) {
                pos = i * w + j;
                R = (pixels[pos] >> 16) & 0xFF;
                redIL[R]++;
                G = (pixels[pos] >> 8) & 0xFF;
                greenIL[G]++;
                B = pixels[pos] & 0xFF;
                blueIL[B]++;

            }
        }

        int redSP = 0;
        int greenSP = 0;
        int blueSP = 0;
        double sRed, sGreen, sBlue;
        int skRed, skGreen, skBlue;
        for (i = 0; i < 256; i++) {
            redSP += redIL[i];
            greenSP += greenIL[i];
            blueSP += blueIL[i];
            sRed = redSP / totalPixel;
            sGreen = greenSP / totalPixel;
            sBlue = blueSP / totalPixel;
            skRed = (int) (sRed * 255);
            skGreen = (int) (sGreen * 255);
            skBlue = (int) (sBlue * 255);
            redLT.put(i, skRed);
            greenLT.put(i, skGreen);
            blueLT.put(i, skBlue);
        }

        for (i = 0; i < h; i++) {
            for (j = 0; j < w; j++) {
                pos = i * w + j;
                A = (pixels[pos] >> 24) & 0xFF;
                R = (pixels[pos] >> 16) & 0xFF;
                R = redLT.get(R);
                G = (pixels[pos] >> 8) & 0xFF;
                G = greenLT.get(G);
                B = pixels[pos] & 0xFF;
                B = blueLT.get(B);
                pixels[pos] = (A << 24) | (R << 16) | (G << 8) | B;
            }
        }
        bmOut.setPixels(pixels, 0, w, 0, 0, w, h);
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

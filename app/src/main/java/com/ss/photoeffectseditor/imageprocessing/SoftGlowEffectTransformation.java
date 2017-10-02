package com.ss.photoeffectseditor.imageprocessing;

import android.graphics.Bitmap;

/**
 * Authour        : PHAMXUANLU@GMAIL.COM
 * License        : phamxuanlu@gmail.com
 * Date           : 7:48 PM - 4/16/2015
 * Description    :
 */
public class SoftGlowEffectTransformation extends AbstractEffectTransformation {

    @Override
    public void setThumbImage(Bitmap bmThumb) {

    }

    @Override
    public Bitmap getThumbnail() {
        return null;
    }

    private double normalize(double val) {
        if (val > 255)
            return 255;
        if (val < 0)
            return 0;
        return val;
    }

    @Override
    public Bitmap perform(Bitmap inp) {
        GaussianBlurTransformation blurTransformation = new GaussianBlurTransformation();
        blurTransformation.setSigma(0.6);
        Bitmap blurImage = blurTransformation.perform(inp);

        double oriR, oriG, oriB, oriA;
        double pixR, pixG, pixB, pixA;
        double factorA = 1.0;
        double factorB = 0.3;
        int row, col;
        int index;
        int width = inp.getWidth();
        int height = inp.getHeight();
        int[] pixels = new int[width * height];
        inp.getPixels(pixels, 0, width, 0, 0, width, height);
        int[] blurPixels = new int[width * height];
        blurImage.getPixels(blurPixels, 0, width, 0, 0, width, height);

        for (row = 0; row < height; row++) {
            for (col = 0; col < width; col++) {
                index = row * width + col;
                oriR = (blurPixels[index] >> 16) & 0xFF;
                oriG = (blurPixels[index] >> 8) & 0xFF;
                oriB = blurPixels[index] & 0xFF;

                pixA = (pixels[index] >> 24) & 0xFF;
                pixR = (pixels[index] >> 16) & 0xFF;
                pixG = (pixels[index] >> 8) & 0xFF;
                pixB = pixels[index] & 0xFF;

                pixR = oriR * factorA + pixR * factorB;
                pixG = oriG * factorA + pixG * factorB;
                pixB = oriB * factorA + pixB * factorB;

                pixR = normalize(pixR);
                pixG = normalize(pixG);
                pixB = normalize(pixB);

                pixels[index] = (((int) pixA) << 24) | (((int) pixR) << 16) | (((int) pixG) << 8) | ((int) pixB);
            }
        }

        blurImage.setPixels(pixels, 0, width, 0, 0, width, height);
        return blurImage;
    }
}

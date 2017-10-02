package com.ss.photoeffectseditor.imageprocessing;

import android.graphics.Bitmap;

/**
 * Authour        : PHAMXUANLU@GMAIL.COM
 * License        : phamxuanlu@gmail.com
 * Date           : 4:24 PM - 4/16/2015
 * Description    :
 */
public class HDRTransformation extends AbstractEffectTransformation {


    @Override
    public void setThumbImage(Bitmap bmThumb) {

    }

    @Override
    public Bitmap getThumbnail() {
        return null;
    }

    @Override
    public Bitmap perform(Bitmap inp) {
        GaussianBlurTransformation gaussianBlur = new GaussianBlurTransformation();
        gaussianBlur.setSigma(0.6);

        Bitmap smoothImage = gaussianBlur.perform(inp);
        double newR = 0, newG = 0, newB = 0;
        int blurA = 0;
        int row, col;
        int width = inp.getWidth();
        int height = inp.getHeight();
        int[] pixels = new int[width * height];
        inp.getPixels(pixels, 0, width, 0, 0, width, height);
        int[] smoothPixels = new int[width * height];
        int index;
        int oriR, oriG, oriB;
        int smoothR, smoothG, smoothB;
        smoothImage.getPixels(smoothPixels, 0, width, 0, 0, width, height);

        for (row = 0; row < height; row++) {
            for (col = 0; col < width; col++) {
                index = row * width + col;
                smoothR = (smoothPixels[index] >> 16) & 0xFF;
                oriR = (pixels[index] >> 16) & 0xFF;
                if (smoothR / 255.0 <= 0.5) {
                    newR = 2 * (smoothR / 255.0) * (oriR / 255.0);
                } else {
                    newR = 1 - 2 * (1 - oriR / 255.0) * (1 - smoothR / 255.0);
                }

                smoothG = (smoothPixels[index] >> 8) & 0xFF;
                oriG = (pixels[index] >> 8) & 0xFF;
                if (smoothG / 255.0 <= 0.5) {
                    newG = 2 * (smoothG / 255.0) * (oriG / 255.0);
                } else {
                    newG = 1 - 2 * (1 - oriG / 255.0) * (1 - smoothG / 255.0);
                }

                smoothB = smoothPixels[index] & 0xFF;
                oriB = pixels[index] & 0xFF;
                if (smoothB / 255.0 <= 0.5) {
                    newB = 2 * (smoothB / 255.0) * (oriB / 255.0);
                } else {
                    newB = 1 - 2 * (1 - oriB / 255.0) * (1 - smoothB / 255.0);
                }
                blurA = (smoothPixels[index] >> 24) & 0xFF;
                newR *= 255;
                newG *= 255;
                newB *= 255;
                pixels[index] = ( blurA << 24) | (((int) newR) << 16) | (((int) newG) << 8) | ((int) newB);
            }
        }

        smoothImage.setPixels(pixels, 0, width, 0, 0, width, height);
        return smoothImage;
    }
}

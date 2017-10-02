package com.ss.photoeffectseditor.imageprocessing;

import android.graphics.Bitmap;
import android.graphics.Color;

/**
 * Created by phamxuanlu@gmail.com on 4/11/2015.
 */
public class GaussianBlurTransformation extends AbstractEffectTransformation {

    private double sigma = 1.2;
    private double[] kernel;
    private int maskSize;
    private double kernelSum;

    public void setSigma(double sigma) {
        this.sigma = sigma;
    }

    public GaussianBlurTransformation() {

        //pre-calculate Kernel
        int ksize = (int) Math.ceil(sigma * 3 + 1);
        if (ksize == 1) {
            return;
        }
        maskSize = ksize;
        this.kernel = new double[ksize * ksize];
        double scale = -0.5 / (sigma * sigma);
        double cons = -scale / Math.PI;
        double sum = 0;
        for (int i = 0; i < ksize; i++) {
            for (int j = 0; j < ksize; j++) {
                int x = i - (ksize - 1) / 2;
                int y = j - (ksize - 1) / 2;
                kernel[i * ksize + j] = cons * Math.exp(scale * (x * x + y * y));
                sum += kernel[i * ksize + j];
            }
        }

        //Normalize
        for (int i = 0; i < ksize; i++) {
            for (int j = 0; j < ksize; j++) {
                kernel[i * ksize + j] /= sum;
            }
        }
        kernelSum = sum;
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
        if (maskSize == 1)
            return null;
        int width = inp.getWidth();
        int height = inp.getHeight();

        Bitmap bmOut = Bitmap.createBitmap(width, height, inp.getConfig());

        int[] pixels = new int[width * height];
        inp.getPixels(pixels, 0, width, 0, 0, width, height);
        int[] tempPixels = new int[width * height];
        inp.getPixels(tempPixels, 0, width, 0, 0, width, height);

        double sumR = 0, sumG = 0, sumB = 0;
        int index = 0;
        int bound = maskSize / 2;

        int row, col, i, j;
        int pixel_index;
        for (row = bound; row < height - bound; row++) {
            for (col = bound; col < width - bound; col++) {
                index = 0;
                sumR = sumG = sumB = 0;
                for (i = -bound; i <= bound; i++) {
                    for (j = -bound; j < bound; j++) {
                        pixel_index = (row + i) * width + col + j;
                        if (pixel_index < pixels.length) {
                            sumR += ((tempPixels[pixel_index] >> 16) & 0xFF) * kernel[index];
                            sumG += ((tempPixels[pixel_index] >> 8) & 0xFF) * kernel[index];
                            sumB += (tempPixels[pixel_index] & 0xFF) * kernel[index];
                            index++;
                        }
                    }
                }

                pixels[row * width + col] = Color.rgb((int) sumR, (int) sumG, (int) sumB);
            }
        }
        bmOut.setPixels(pixels, 0, width, 0, 0, width, height);
        return bmOut;
    }
}

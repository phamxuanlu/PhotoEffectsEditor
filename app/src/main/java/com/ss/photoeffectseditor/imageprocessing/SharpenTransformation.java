package com.ss.photoeffectseditor.imageprocessing;

import android.graphics.Bitmap;

/**
 * Created by phamxuanlu@gmail.com on 4/13/2015.
 */
public class SharpenTransformation extends AbstractEffectTransformation {
    //private ConvolutionMatrix sharpenMatrix;

    public SharpenTransformation() {
//        int[][] config = new int[][]{
//                {-1, -1, -1},
//                {-1,  9, -1},
//                {-1, -1, -1}
//        };
//
//        sharpenMatrix = new ConvolutionMatrix(config, 1, 1);
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
        int w = inp.getWidth();
        int h = inp.getHeight();
        int[] laplacian = new int[]{0, -1, 0, -1, 4, -1, 0, -1, 0};
        int WINDOW_H_SIZE = 1;
        int pixel_index;
        int laplacian_index;
        Bitmap bmOut = Bitmap.createBitmap(w, h, inp.getConfig());
        int[] pixels = new int[w * h];
        int[] edgePixels = new int[w * h];
        inp.getPixels(pixels, 0, w, 0, 0, w, h);
        for (i = WINDOW_H_SIZE; i < h - WINDOW_H_SIZE; i++) {
            for (j = WINDOW_H_SIZE; j < w - WINDOW_H_SIZE; j++) {
                laplacian_index = 0;
                R = G = B = 0;
                pixel_index = i * w + j;
                A = (pixels[pixel_index] >> 24) & 0xFF;

                for (k = -WINDOW_H_SIZE; k <= WINDOW_H_SIZE; k++) {
                    for (l = -WINDOW_H_SIZE; l <= WINDOW_H_SIZE; l++) {
                        pixel_index = (i + l) * w + j + k;
                        R += ((pixels[pixel_index] >> 16) & 0xFF) * laplacian[laplacian_index];
                        G += ((pixels[pixel_index] >> 8) & 0xFF) * laplacian[laplacian_index];
                        B += (pixels[pixel_index] & 0xFF) * laplacian[laplacian_index];
                        laplacian_index++;
                    }
                }

                R = Math.min(255, Math.max(0, R));
                G = Math.min(255, Math.max(0, G));
                B = Math.min(255, Math.max(0, B));
                edgePixels[i * w + j] = (A << 24) | (R << 16) | (G << 8) | B;
            }
        }

        pixel_index = 1;


        int tmp = 0;
        tmp += ((pixels[pixel_index] >> 16) & 0xFF) << 1;
        tmp += ((pixels[pixel_index] >> 8) & 0xFF) << 2
                + ((pixels[pixel_index] >> 8) & 0xFF);
        tmp += pixels[pixel_index] & 0xFF;


        Math.abs(tmp);

        for (i = 0; i < pixels.length; i++) {
            A = (pixels[i] >> 24) & 0xFF;
            R = ((pixels[i] >> 16) & 0xFF) + ((edgePixels[i] >> 16) & 0xFF);
            G = ((pixels[i] >> 8) & 0xFF) + ((edgePixels[i] >> 8) & 0xFF);
            B = (pixels[i] & 0xFF) + (edgePixels[i] & 0xFF);
            R = Math.min(255, Math.max(0, R));
            G = Math.min(255, Math.max(0, G));
            B = Math.min(255, Math.max(0, B));
            pixels[i] = (A << 24) | (R << 16) | (G << 8) | B;
        }

        bmOut.setPixels(pixels, 0, w, 0, 0, w, h);
        return bmOut;
    }
}

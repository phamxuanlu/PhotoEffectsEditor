package com.ss.photoeffectseditor.imageprocessing;

import android.graphics.Bitmap;

/**
 * Created by phamxuanlu@gmail.com on 4/1/2015.
 */
public class ConvolutionMatrix {

    public int[][] Matrix;
    public int Matrix_Size;
    public int Factor = 1;
    public int Offset = 1;

    public ConvolutionMatrix(int size) {
        Matrix = new int[size][size];
    }

    public ConvolutionMatrix(int[][] config, int factor, int offset) {
        Matrix_Size = config.length;
        this.Factor = factor;
        this.Offset = offset;
        Matrix = new int[Matrix_Size][Matrix_Size];
        for (int i = 0; i < Matrix_Size; i++) {
            for (int j = 0; j < Matrix_Size; j++) {
                Matrix[i][j] = config[i][j];
            }
        }
    }

    public void setFactor(int factor) {
        this.Factor = factor;
    }

    public void setOffset(int offset) {
        this.Offset = offset;
    }

    public void setDefaultVaules(int value) {
        int y;
        for (int x = 0; x < Matrix_Size; x++) {
            for (y = 0; y < Matrix_Size; y++) {
                Matrix[x][y] = value;
            }
        }
    }

    public void applyConfig(int[][] config) {
        int i, j;
        Matrix = new int[Matrix_Size][Matrix_Size];
        Matrix_Size = config.length;
        for (i = 0; i < Matrix_Size; i++) {
            for (j = 0; j < Matrix_Size; j++) {
                Matrix[i][j] = config[i][j];
            }
        }
    }

    public int normalize(int intensity) {
        if (intensity > 255)
            return 255;
        else if (intensity < 0)
            return 0;
        else
            return intensity;
    }

    public Bitmap convolute(Bitmap inp) {
        int A, R, G, B;
        int i, j, k, l;
        int h = inp.getHeight();
        int w = inp.getWidth();
        int[] pixels = new int[w * h];
        int[] pxResult = new int[w * h];
        inp.getPixels(pixels, 0, w, 0, 0, w, h);
        Bitmap bmOut = Bitmap.createBitmap(w, h, inp.getConfig());
        int half_MTS = Matrix_Size / 2;
        int maxH = h - half_MTS;
        int maxW = w - half_MTS;
        int matrixValue; //Convolution Matrix Value
        int pos; //Current Pixel Position
        int conPos; //Convolution Matrix Position
        for (i = half_MTS; i < maxH; i++) {
            for (j = half_MTS; j < maxW; j++) {
                R = G = B = 0;
                pos = i * w + j;
                A = (pixels[pos] >> 24) & 0xFF;
                for (k = -half_MTS; k <= half_MTS; k++) {
                    for (l = -half_MTS; l <= half_MTS; l++) {
                        matrixValue = Matrix[k + half_MTS][l + half_MTS];
                        conPos = (i + k) * w + (j + l);
                        R += ((pixels[conPos] >> 16) & 0xFF) * matrixValue;
                        G += ((pixels[conPos] >> 8) & 0xFF) * matrixValue;
                        B += (pixels[conPos] & 0xFF) * matrixValue;
                    }
                }

                R = normalize(R / Factor + Offset);
                G = normalize(G / Factor + Offset);
                B = normalize(B / Factor + Offset);
                pxResult[pos] = (A << 24) | (R << 16) | (G << 8) | B;
            }
        }
        bmOut.setPixels(pxResult, 0, w, 0, 0, w, h);
        return bmOut;
    }

}

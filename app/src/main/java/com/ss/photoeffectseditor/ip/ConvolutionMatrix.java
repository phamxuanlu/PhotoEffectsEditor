package com.ss.photoeffectseditor.ip;

/**
 * Created by L on 4/1/2015.
 */
public class ConvolutionMatrix {

    public static final int MATRIX_SIZE = 3;

    public double[][] Matrix;
    public double Factor = 1;
    public double Offset = 1;

    public ConvolutionMatrix(int size) {
        Matrix = new double[size][size];
    }

    public void setDefaultVaules(double value) {
        int y;
        for (int x = 0; x < MATRIX_SIZE; ++x) {
            for (y = 0; y < MATRIX_SIZE; ++y) {
                Matrix[x][y] = value;
            }
        }
    }

    public void applyConfig(double[][] config) {
        int i, j;
        for (i = 0; i < MATRIX_SIZE; i++) {
            for (j = 0; j < MATRIX_SIZE; j++) {
                Matrix[i][j] = config[i][j];
            }
        }
    }


//    public static Bitmap convolute(Bitmap bmp, android.graphics.Matrix mat, float factor, int offset) {
//        //get matrix values
//        float[] mxv = new float[MATRIX_SIZE * MATRIX_SIZE];
//        mat.getValues(mxv);
//
//        //cache source pixels
//        int w = bmp.getWidth();
//        int h = bmp.getHeight();
//
//        int[] srcPxs = new int[w * h];
//        bmp.getPixels(srcPxs, 0, w, 0, 0, w, h);
//
//        //clone source pixels in an array
//        //here we'll store result
//        int[] rtPxs = srcPxs.clone();
//        int R, G, B;
//        int rSum, gSum, bSum;
//        int idx;
//        int pix;
//        float mv;
//        int i,j;
//        for(i=0,)
//    }
}

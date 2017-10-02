package com.ss.photoeffectseditor.imageprocessing;

import android.graphics.Bitmap;
import android.widget.SeekBar;

import com.ss.photoeffectseditor.asynctasks.DoTransformationAsyncTask;
import com.ss.photoeffectseditor.utils.AsyncTaskUtils;

public class GammaCorrectionTransformation extends AbstractOptimizeTransformation {
    private float red;
    private float green;
    private float blue;

    public GammaCorrectionTransformation() {
        maxValue = 30;
        this.red = 1.0f;
        this.green = 1.0f;
        this.blue = 1.0f;
    }


    @Override
    protected void setupSeekBar(SeekBar seekBar) {
        seekBar.setMax(maxValue);
        seekBar.setProgress(maxValue / 2);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (currentValue != seekBar.getProgress()) {
                    currentValue = seekBar.getProgress();
                    if (currentValue == 0)
                        currentValue = 1;
                    GammaCorrectionTransformation.this.red = (float) currentValue / (maxValue / 3.0f);
                    GammaCorrectionTransformation.this.green = GammaCorrectionTransformation.this.red;
                    GammaCorrectionTransformation.this.blue = GammaCorrectionTransformation.this.red;
                    if (transTask != null && !transTask.isCancelled()) {
                        transTask.cancel(true);
                    }
                    transTask = new DoTransformationAsyncTask(image, GammaCorrectionTransformation.this, progressDialog, onPerformedListener);
                    AsyncTaskUtils.executeTask(transTask);
                }
            }
        });
    }

    @Override
    public Bitmap perform(Bitmap inp) {
        Bitmap bmOut = Bitmap.createBitmap(inp.getWidth(), inp.getHeight(),
                inp.getConfig());

        int w = inp.getWidth();
        int h = inp.getHeight();

        int A, R, G, B;
        int[] pixels = new int[w * h];
        final int MAX_SIZE = 256;
        final double MAX_DBL_VALUE = 256.0;
        final int MAX_INT_VALUE = 255;
        final float REVERSE = 1.0f;

        int[] gammaR = new int[MAX_SIZE];
        int[] gammaG = new int[MAX_SIZE];
        int[] gammaB = new int[MAX_SIZE];

        int i, j;
        for (i = 0; i < MAX_SIZE; i++) {
            gammaR[i] = (int) Math.min(
                    MAX_INT_VALUE,
                    MAX_DBL_VALUE * Math.pow(i / MAX_DBL_VALUE, REVERSE / red));
            gammaG[i] = (int) Math.min(
                    MAX_INT_VALUE,
                    MAX_DBL_VALUE* Math.pow(i / MAX_DBL_VALUE, REVERSE / green));
            gammaB[i] = (int) Math.min(
                    MAX_INT_VALUE,
                    MAX_DBL_VALUE* Math.pow(i / MAX_DBL_VALUE, REVERSE/ blue));
        }
        inp.getPixels(pixels, 0, w, 0, 0, w, h);
        int pos;
        for (i = 0; i < h; i++) {
            for (j = 0; j < w; j++) {
                pos = i * w + j;
                A = (pixels[pos] >> 24) & 0xFF;
                R = gammaR[(pixels[pos] >> 16) & 0xFF];
                G = gammaG[(pixels[pos] >> 8) & 0xFF];
                B = gammaB[pixels[pos] & 0xFF];
                pixels[pos] = (A << 24) | (R << 16) | (G << 8) | B;
            }
        }
        bmOut.setPixels(pixels, 0, w, 0, 0, w, h);
        return bmOut;
    }

}

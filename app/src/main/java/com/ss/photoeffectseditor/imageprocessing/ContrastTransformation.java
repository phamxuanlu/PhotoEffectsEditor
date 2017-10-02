package com.ss.photoeffectseditor.imageprocessing;

import android.graphics.Bitmap;
import android.widget.SeekBar;

import com.ss.photoeffectseditor.asynctasks.DoTransformationAsyncTask;
import com.ss.photoeffectseditor.utils.AsyncTaskUtils;

/**
 * Created by L on 4/10/2015.
 */
public class ContrastTransformation extends AbstractOptimizeTransformation {

    public ContrastTransformation() {
        maxValue = 100;
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
                    currentValue = seekBar.getProgress() - maxValue / 2;
                    if (currentValue == 0) {
                        currentValue = 1;
                    }
                    if (transTask != null && !transTask.isCancelled()) {
                        transTask.cancel(true);
                    }
                    transTask = new DoTransformationAsyncTask(image, ContrastTransformation.this, progressDialog, onPerformedListener);
                    AsyncTaskUtils.executeTask(transTask);
                }
            }
        });
    }

    @Override
    public Bitmap perform(Bitmap inp) {
        int w = inp.getWidth();
        int h = inp.getHeight();
        int A, R, G, B;
        int i, j;
        int[] pixels = new int[w * h];
        int pos;
        inp.getPixels(pixels, 0, w, 0, 0, w, h);

        Bitmap bmOut = Bitmap.createBitmap(w, h, inp.getConfig());
        double contrast = ((maxValue + currentValue) / (double) maxValue);
        contrast *= contrast;
        int[] contrastLT = new int[256];
        for (i = 0; i < 256; i++) {
            contrastLT[i] = (int) (((((i / 255.0) - 0.5) * contrast) + 0.5) * 255.0);
            if (contrastLT[i] < 0)
                contrastLT[i] = 0;
            if (contrastLT[i] > 255)
                contrastLT[i] = 255;
        }
        int tmpPx;
        for (i = 0; i < h; i++) {
            for (j = 0; j < w; j++) {
                pos = i * w + j;

                A = (pixels[pos] >> 24) & 0xFF;
                R = contrastLT[(pixels[pos] >> 16) & 0xFF];
                G = contrastLT[(pixels[pos] >> 8) & 0xFF];
                B = contrastLT[pixels[pos] & 0xFF];

                pixels[pos] = (A << 24) | (R << 16) | (G << 8) | B;
            }
        }

        bmOut.setPixels(pixels, 0, w, 0, 0, w, h);

        return bmOut;
    }
}

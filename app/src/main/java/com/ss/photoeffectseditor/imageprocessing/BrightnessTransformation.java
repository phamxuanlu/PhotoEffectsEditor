package com.ss.photoeffectseditor.imageprocessing;

import android.graphics.Bitmap;
import android.widget.SeekBar;

import com.ss.photoeffectseditor.asynctasks.DoTransformationAsyncTask;
import com.ss.photoeffectseditor.utils.AsyncTaskUtils;

/**
 * Created by L on 3/19/2015.
 */
public class BrightnessTransformation extends AbstractOptimizeTransformation {


    public BrightnessTransformation() {
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
                int tmpVal = seekBar.getProgress() - maxValue / 2;
                if (currentValue != tmpVal) {
                    currentValue = tmpVal;
                    if (transTask != null && !transTask.isCancelled()) {
                        transTask.cancel(true);
                    }
                    transTask = new DoTransformationAsyncTask(image, BrightnessTransformation.this, progressDialog, onPerformedListener);
                    AsyncTaskUtils.executeTask(transTask);
                }
            }
        });
    }

    @Override
    public Bitmap perform(Bitmap inp) {
        Bitmap bmOut = Bitmap.createBitmap(inp.getWidth(), inp.getHeight(), inp.getConfig());

        int A, R, G, B;
        int i, j;
        int pos;
        int w = inp.getWidth();
        int h = inp.getHeight();
        int[] colors = new int[w * h];
        int nvalue;
        inp.getPixels(colors, 0, w, 0, 0, w, h);
        for (i = 0; i < h; i++) {
            for (j = 0; j < w; j++) {
                pos = i * w + j;
                A = (colors[pos] >> 24) & 0xFF;
                R = (colors[pos] >> 16) & 0xFF;
                R += this.currentValue;
                if (R > 255) {
                    R = 255;
                } else if (R < 0) {
                    R = 0;
                }
                G = (colors[pos] >> 8) & 0xFF;
                G += this.currentValue;
                if (G > 255) {
                    G = 255;
                } else if (G < 0) {
                    G = 0;
                }
                B = colors[pos] & 0xFF;
                B += this.currentValue;
                if (B > 255) {
                    B = 255;
                } else if (B < 0) {
                    B = 0;
                }
                nvalue = (A << 24) | (R << 16) | (G << 8) | B;
                colors[pos] = nvalue;
            }
        }
        bmOut.setPixels(colors, 0, w, 0, 0, w, h);
        return bmOut;
    }
}

package com.ss.photoeffectseditor.imageprocessing;

import android.graphics.Bitmap;
import android.widget.SeekBar;

import com.ss.photoeffectseditor.asynctasks.DoTransformationAsyncTask;
import com.ss.photoeffectseditor.utils.AsyncTaskUtils;

/**
 * Created by L on 4/13/2015.
 */
public class ColorBostUpTransformation extends AbstractOptimizeTransformation {

    public static final int RED = 1;
    public static final int GREEN = 2;
    public static final int BLUE = 3;

    private int type;
    private float percent;

    public ColorBostUpTransformation(int COLOR) {
        this.type = COLOR;
        this.percent = 0;
        this.maxValue = 100;
    }

    @Override
    protected void setupSeekBar(SeekBar seekBar) {
        seekBar.setMax(maxValue);
        seekBar.setProgress(0);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                percent = (float) seekBar.getProgress() / maxValue;
                transTask = new DoTransformationAsyncTask(image, ColorBostUpTransformation.this, progressDialog, onPerformedListener);
                AsyncTaskUtils.executeTask(transTask);
            }
        });
    }

    @Override
    public Bitmap perform(Bitmap inp) {
        int A, R, G, B;
        int w = inp.getWidth();
        int h = inp.getHeight();
        Bitmap bmOut = Bitmap.createBitmap(w, h, inp.getConfig());
        int i, j;
        int[] pixels = new int[w * h];
        int pos;
        inp.getPixels(pixels, 0, w, 0, 0, w, h);
        for (i = 0; i < h; i++) {
            for (j = 0; j < w; j++) {
                pos = i * w + j;
                A = (pixels[pos] >> 24) & 0xFF;

                R = (pixels[pos] >> 16) & 0xFF;
                if (this.type == RED) {
                    R = (int) (R * (1 + percent));
                    if (R > 255)
                        R = 255;
                }
                G = (pixels[pos] >> 8) & 0xFF;
                if (this.type == GREEN) {
                    G = (int) (G * (1 + percent));
                    if (G > 255)
                        G = 255;
                }

                B = pixels[pos] & 0xFF;
                if (this.type == BLUE) {
                    B = (int) (B * (1 + percent));
                    if (B > 255)
                        B = 255;
                }

                pixels[pos] = (A << 24) | (R << 16) | (G << 8) | B;
            }
        }

        bmOut.setPixels(pixels, 0, w, 0, 0, w, h);
        return bmOut;
    }
}

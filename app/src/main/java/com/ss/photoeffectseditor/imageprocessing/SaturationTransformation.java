package com.ss.photoeffectseditor.imageprocessing;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.widget.SeekBar;

import com.ss.photoeffectseditor.asynctasks.DoTransformationAsyncTask;
import com.ss.photoeffectseditor.utils.AsyncTaskUtils;

/**
 * Created by phamxuanlu@gmail.com on 4/13/2015.
 */
public class SaturationTransformation extends AbstractOptimizeTransformation {

    private float adjustAmount;

    public SaturationTransformation() {
        maxValue = 100;
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
                if (currentValue != seekBar.getProgress()) {
                    currentValue = seekBar.getProgress();
                    adjustAmount = (float)currentValue / maxValue;
                    if(transTask!=null&&!transTask.isCancelled()){
                        transTask.cancel(true);
                    }
                    transTask = new DoTransformationAsyncTask(image, SaturationTransformation.this, progressDialog, onPerformedListener);
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
        int w = inp.getWidth();
        int h = inp.getHeight();
        int[] pixels = new int[w * h];
        inp.getPixels(pixels, 0, w, 0, 0, w, h);
        float[] HSV = new float[3];
        float remainSat;
        int pos;
        for (i = 0; i < h; i++) {
            for (j = 0; j < w; j++) {
                pos = i * w + j;
                Color.colorToHSV(pixels[pos], HSV);
                remainSat = 1.0f - HSV[1];
                HSV[1] += remainSat * adjustAmount;
                HSV[1] = (float) Math.max(0.0, Math.min(HSV[1], 1.0));
                pixels[pos] = Color.HSVToColor(HSV);
            }
        }
        bmOut.setPixels(pixels, 0, w, 0, 0, w, h);
        return bmOut;
    }
}

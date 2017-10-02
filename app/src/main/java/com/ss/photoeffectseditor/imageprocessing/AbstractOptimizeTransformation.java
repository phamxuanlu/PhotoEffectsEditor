package com.ss.photoeffectseditor.imageprocessing;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.widget.SeekBar;

import com.ss.photoeffectseditor.asynctasks.DoTransformationAsyncTask;

/**
 * Created by L on 4/1/2015.
 */
public abstract class AbstractOptimizeTransformation extends AbstractBaseTransformation {

    protected Bitmap image;
    protected SeekBar seekBar;
    protected int maxValue;
    protected int minValue;
    protected int currentValue;
    protected ProgressDialog progressDialog;
    protected DoTransformationAsyncTask transTask;
    protected DoTransformationAsyncTask.OnPerformedListener onPerformedListener;

    public void setupTransformation(SeekBar seekBar, Bitmap bm, DoTransformationAsyncTask transformationAsyncTask, DoTransformationAsyncTask.OnPerformedListener onPerformedListener) {
        this.seekBar = seekBar;
        this.onPerformedListener = onPerformedListener;
        this.image = bm;
        this.transTask = transformationAsyncTask;
        setupSeekBar(this.seekBar);
    }

    public void setProgressDialog(ProgressDialog progressDialog) {
        this.progressDialog = progressDialog;
    }

    protected abstract void setupSeekBar(SeekBar seekBar);

}

package com.ss.photoeffectseditor.asynctasks;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.ss.photoeffectseditor.utils.StorageUtils;

import java.io.File;
import java.lang.ref.WeakReference;

/**
 * Created by L on 4/8/2015.
 */
public class SaveBitmapAsyncTask extends AsyncTask<Void, Void, Void> {
    private File saveLocation;
    private String sourcePath;
    private WeakReference<ProgressDialog> saveDialog;
    private OnExecuteCompleteListener executeCompleteListener;

    private SaveBitmapAsyncTask(Builder builder) {
        this.saveLocation = builder.saveLocation;
        this.saveDialog = new WeakReference<ProgressDialog>(builder.saveProgress);
        this.sourcePath = builder.cachePath;
        this.executeCompleteListener = builder.executeCompleteListener;
    }

    @Override
    protected void onPreExecute() {
        if (this.saveDialog != null) {
            ProgressDialog pr = saveDialog.get();
            if (pr != null) {
                pr.show();
            }
        }
    }

    @Override
    protected Void doInBackground(Void... params) {
        Bitmap bm = BitmapFactory.decodeFile(sourcePath);
        StorageUtils.saveBitmap(bm, saveLocation);
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        if (this.saveDialog != null) {
            ProgressDialog pr = saveDialog.get();
            if (pr != null) {
                pr.dismiss();
            }
        }
        if (executeCompleteListener != null) {
            executeCompleteListener.onExecuteComplete();
        }
    }

    public static class Builder {
        private File saveLocation;
        private String cachePath;
        private ProgressDialog saveProgress;
        private OnExecuteCompleteListener executeCompleteListener;

        public Builder setOnExecuteCompleteListener(OnExecuteCompleteListener listener) {
            this.executeCompleteListener = listener;
            return this;
        }

        public Builder setProgressDialog(ProgressDialog progressDialog) {
            this.saveProgress = progressDialog;
            return this;
        }

        public Builder setSourcePath(String path) {
            this.cachePath = path;
            return this;
        }

        public Builder setSaveLocation(File location) {
            this.saveLocation = location;
            return this;
        }

        public SaveBitmapAsyncTask build() {
            return new SaveBitmapAsyncTask(this);
        }
    }
}

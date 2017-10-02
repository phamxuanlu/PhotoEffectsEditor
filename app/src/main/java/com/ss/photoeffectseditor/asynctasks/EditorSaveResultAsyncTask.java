package com.ss.photoeffectseditor.asynctasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;

import com.ss.photoeffectseditor.utils.MediaScanUtils;

import java.io.FileOutputStream;
import java.lang.ref.WeakReference;

/**
 * Created by L on 4/2/2015.
 */
public class EditorSaveResultAsyncTask extends AsyncTask<Void, Void, Void> {

    public interface OnSaveCompletedListener {
        void onSaveCompleted();
    }

    private Context context;
    private Bitmap bm;
    private String path;
    private WeakReference<ProgressDialog> dialogRef;
    private OnSaveCompletedListener saveCompletedListener;

    public EditorSaveResultAsyncTask(Context context, Bitmap bm, String path, OnSaveCompletedListener savedListener, ProgressDialog dialog) {
        this.context = context;
        this.bm = bm;
        this.path = path;
        dialogRef = new WeakReference<ProgressDialog>(dialog);
        this.saveCompletedListener = savedListener;
    }

    @Override
    protected void onPreExecute() {
        if (dialogRef != null) {
            ProgressDialog pr = dialogRef.get();
            if (pr != null) {
                pr.show();
            }
        }
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            FileOutputStream outputStream = new FileOutputStream(this.path);
            this.bm.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.flush();
            outputStream.close();
            MediaScanUtils.scanAndAddFiles(this.context, this.path);
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        if (dialogRef != null) {
            ProgressDialog pr = dialogRef.get();
            if (pr != null) {
                pr.dismiss();
            }
        }
        if (saveCompletedListener != null) {
            saveCompletedListener.onSaveCompleted();
        }
    }
}

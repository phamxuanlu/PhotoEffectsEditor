package com.ss.photoeffectseditor.asynctasks;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.ss.photoeffectseditor.AppConfigs;
import com.ss.photoeffectseditor.imageprocessing.ITransformation;
import com.ss.photoeffectseditor.utils.BitmapUtils;
import com.ss.photoeffectseditor.utils.GLog;

import java.lang.ref.WeakReference;

/**
 * Created by phamxuanlu@gmail.com on 3/6/2015.
 * Đọc ảnh từ đường dẫn truyền vào với kích thước phù hợp được resize trước
 * sau đó apply transform
 */
public class DoTransformationAsyncTask extends AsyncTask<Void, Void, Void> {
    private String path;
    private ITransformation transformation;
    private WeakReference<ProgressDialog> prRef;
    private Bitmap bmSource;
    private Bitmap bmResult;
    private boolean isNeedDecodeFile;
    private OnPerformedListener onPerformedListener;

    private long startTime;
    private long endTime;


    public interface OnPerformedListener {
        void onPerformed(Bitmap bm);
    }

    public DoTransformationAsyncTask(String path, ITransformation transformation,
                                     ProgressDialog progressDialog, OnPerformedListener onPerformedListener) {
        this.transformation = transformation;
        this.path = path;
        this.prRef = new WeakReference<ProgressDialog>(progressDialog);
        this.isNeedDecodeFile = true;
        this.onPerformedListener = onPerformedListener;
    }

    public DoTransformationAsyncTask(Bitmap bmSource, ITransformation transformation,
                                     ProgressDialog progressDialog, OnPerformedListener onPerformedListener) {
        this.bmSource = bmSource;
        this.transformation = transformation;
        this.prRef = new WeakReference<ProgressDialog>(progressDialog);
        this.isNeedDecodeFile = false;
        this.onPerformedListener = onPerformedListener;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        // GLog.v("TRANSFORM","START PERFORM TRANSFORM");
        startTime = System.currentTimeMillis();
        if (prRef != null && prRef.get() != null) {
            prRef.get().show();
        }
    }

    @Override
    protected Void doInBackground(Void... params) {
        if (this.isNeedDecodeFile) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(path, options);
            options.inSampleSize = BitmapUtils.calculateSampleSize(options, AppConfigs.getInstance().deviceWidth, AppConfigs.getInstance().deviceHeight);
            options.inJustDecodeBounds = false;
            Bitmap bm = BitmapFactory.decodeFile(path, options);
            bmResult = transformation.perform(bm);
            bm.recycle();
        } else {
            bmResult = transformation.perform(bmSource);
        }
        return null;
    }


    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        endTime = System.currentTimeMillis();
        GLog.v("TRANSFORM", "EXECUTE TIME = " + ((endTime - startTime) / 1000.0));
        if (prRef != null && prRef.get() != null) {
            prRef.get().dismiss();
        }
        if (onPerformedListener != null) {
            onPerformedListener.onPerformed(bmResult);
        }
    }
}

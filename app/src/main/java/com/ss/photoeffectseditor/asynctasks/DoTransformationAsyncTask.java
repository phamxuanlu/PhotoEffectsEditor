package com.ss.photoeffectseditor.asynctasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.ss.photoeffectseditor.AppConfigs;
import com.ss.photoeffectseditor.ip.ITransformation;
import com.ss.photoeffectseditor.utils.BitmapUtils;

/**
 * Created by phamxuanlu@gmail.com on 3/6/2015.
 * Đọc ảnh từ đường dẫn truyền vào với kích thước phù hợp được resize trước
 * sau đó apply transform
 */
public class DoTransformationAsyncTask extends AsyncTask<Void, Void, Void> {
    private String path;
    private ITransformation transformation;
    private ProgressDialog progressDialog;
    private Context context;
    private Bitmap bmSource;
    private Bitmap bmResult;
    private boolean isNeedDecodeFile;
    private OnPerformedListener onPerformedListener;

    public interface OnPerformedListener {
        public void onPerformed(Bitmap bm);
    }

    public DoTransformationAsyncTask(Context context, String path, ITransformation transformation,
                                     ProgressDialog progressDialog, OnPerformedListener onPerformedListener) {
        this.context = context;
        this.transformation = transformation;
        this.path = path;
        this.progressDialog = progressDialog;
        this.isNeedDecodeFile = true;
        this.onPerformedListener = onPerformedListener;
    }

    public DoTransformationAsyncTask(Context context, Bitmap bmSource, ITransformation transformation,
                                     ProgressDialog progressDialog, OnPerformedListener onPerformedListener) {
        this.context = context;
        this.bmSource = bmSource;
        this.transformation = transformation;
        this.progressDialog = progressDialog;
        this.isNeedDecodeFile = false;
        this.onPerformedListener = onPerformedListener;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog.show();
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
        progressDialog.dismiss();
        if (onPerformedListener != null) {
            onPerformedListener.onPerformed(bmResult);
        }
    }
}

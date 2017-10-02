package com.ss.photoeffectseditor.asynctasks;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.ss.photoeffectseditor.widget.BaseToolObject;

import java.lang.ref.WeakReference;

/**
 * Created by phamxuanlu@gmail.com on 3/6/2015.
 */
public class PreviewThumbAsyncTask extends AsyncTask<Void, Void, Void> {


    private WeakReference<ImageView> thumbRef;
    private BaseToolObject baseToolObject;
    private Bitmap bmThumb;
    private Bitmap processed;
    private OnProcessedListener onProcessedListener;

    private PreviewThumbAsyncTask(Builder builder) {
        this.thumbRef = new WeakReference<ImageView>(builder.btnThumb);
        this.baseToolObject = builder.baseToolObject;
        this.bmThumb = builder.bmThumb;
        this.onProcessedListener = builder.onProcessedListener;
    }


    @Override
    protected Void doInBackground(Void... params) {
        if (baseToolObject != null) {
            if (baseToolObject.transform != null)
                processed = baseToolObject.transform.perform(bmThumb);
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        if (thumbRef != null) {
            ImageView igmbt = thumbRef.get();
            if (igmbt != null) {
                Drawable drawable = igmbt.getDrawable();
                if (drawable instanceof BitmapDrawable) {
                    ((BitmapDrawable) drawable).getBitmap().recycle();
                }
                igmbt.setImageBitmap(processed);
            }
        }
        if (onProcessedListener != null) {
            onProcessedListener.onProcessed();
        }
    }

    public interface OnProcessedListener {
        void onProcessed();
    }

    public static class Builder {
        private ImageView btnThumb;
        private BaseToolObject baseToolObject;
        private Bitmap bmThumb;
        private OnProcessedListener onProcessedListener;

        public Builder(ImageView btnThumb, BaseToolObject effectTool, Bitmap bmThumb) {
            this.btnThumb = btnThumb;
            this.bmThumb = bmThumb;
            this.baseToolObject = effectTool;
        }

        public Builder setOnProcessedListener(OnProcessedListener listener) {
            this.onProcessedListener = listener;
            return this;
        }

        public PreviewThumbAsyncTask build() {
            return new PreviewThumbAsyncTask(this);
        }
    }

}

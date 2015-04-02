package com.ss.photoeffectseditor.utils;

import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;

/**
 * Created by phamxuanlu@gmail.com on 3/15/2015.
 *
 * Bởi vì mặc định android không thể thực hiện song song nhiều AsyncTask
 *
 */
public class AsyncTaskUtils {

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static <T> void executeTask(AsyncTask<T, ?, ?> task, T... params) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, params);
        } else {
            task.execute(params);
        }
    }
}

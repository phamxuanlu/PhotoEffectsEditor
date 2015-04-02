package com.ss.photoeffectseditor.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import com.ss.photoeffectseditor.AppConstants;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StorageUtils {

	public static String getPathFromUri(Uri uri, Context context) {
		String[] filePathColumn = { MediaStore.Images.Media.DATA };
		Cursor cursor = context.getContentResolver().query(uri, filePathColumn,
				null, null, null);
		String filePath;
		if (cursor != null) {
			cursor.moveToFirst();
			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			filePath = cursor.getString(columnIndex);
			cursor.close();
		} else {
			filePath = uri.getPath();
		}
		return filePath;
	}

    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    public static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)
                || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    public static File getCacheDirectory() {
        File file = new File(Environment.getExternalStorageDirectory(),
                AppConstants.APP_CACHE_FOLDER);
        if (!file.exists()) {
            if (!file.mkdirs()) {
                GLog.v("CACHE FOLDER", "not create");
            }
        }

        return file;
    }

    public static File createImageFile(String prefix, File dir, String extension) {
        String id = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        if (isExternalStorageWritable()) {
            try {
                File file = new File(dir, prefix + id + extension);
                if (file.exists() == false) {
                    file.getParentFile().mkdirs();
                    file.createNewFile();
                }
                return file;
            } catch (IOException ex) {

            }
        }
        return null;
    }

	public static String convertToMiB(long length) {
		int kb = (int) Math.ceil(length / 1024.0f);
		if (kb >= 1024) {
			float mib = length / (1024 * 1024.0f);
			return String.format("%2f", mib) + " MB";
		}
		return String.format("%4d", kb) + " KB";
	}
}

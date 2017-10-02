package com.ss.photoeffectseditor.utils;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import com.ss.photoeffectseditor.AppConstants;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StorageUtils {

    public static String getPathFromUri(Uri uri, Context context) {
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
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

    public static File getPublicDirectiry() {
        File file = new File(Environment.getExternalStorageDirectory(),
                AppConstants.APP_FOLDER);
        if (file.mkdirs()) {
            GLog.v("APP FOLDER", file.getPath() + " created");
        }
        return file;
    }

    public static File saveBitmap(Bitmap bm, File location) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(location);
            bm.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            GLog.v("File Saved to", location.getPath());
            return location;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static File getNewAppsFile() {
        String id = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        if (isExternalStorageWritable()) {
            File folder = getPublicDirectiry();
            File file = new File(folder, AppConstants.DEFAULT_FILE_PREFIX + id
                    + ".JPEG");
            return file;
        } else {
            GLog.v("SAVE BITMAP", "CAN'T WRITE TO EXTERNAL STORAGE");
            return null;
        }
    }

    public static void copyFile(File src, File dest) throws IOException {
        File destFolder = dest.getParentFile();
        if (!destFolder.exists()) {
            destFolder.mkdirs();
        }
        InputStream in = new FileInputStream(src);
        OutputStream out = new FileOutputStream(dest);
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) != -1) {
            out.write(buf, 0, len);
        }
        in.close();
        in = null;
        out.flush();
        out.close();
        out = null;
    }

    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    public static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state)
                || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state);
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

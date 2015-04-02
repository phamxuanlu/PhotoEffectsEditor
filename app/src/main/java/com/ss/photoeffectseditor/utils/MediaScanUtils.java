package com.ss.photoeffectseditor.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import java.io.File;

public class MediaScanUtils {
	public static void scanAndAddFiles(Context context, String path){
        Intent mediaScan = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File file = new File(path);
        Uri uri = Uri.fromFile(file);
        mediaScan.setData(uri);
        context.sendBroadcast(mediaScan);
    }


}

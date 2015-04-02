package com.ss.photoeffectseditor.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;

import com.ss.photoeffectseditor.AppConfigs;


public class BitmapUtils {
    public static int calculateSampleSize(BitmapFactory.Options options, int reqWidth,
                                          int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int insample = 1;
        if (height > reqHeight || width > reqWidth) {
            final int halfH = (int) (height / 2.0f);
            final int halfW = (int) (width / 2.0f);
            while ((halfH / insample) > reqHeight
                    || (halfW / insample) > reqWidth) {
                insample *= 2;
            }
        }
        return insample;
    }

    public static Bitmap decodeBitmap(Context context, String path) {

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        int sampleSize = calculateSampleSize(options, AppConfigs.getInstance().deviceWidth, AppConfigs.getInstance().deviceHeight);
        options.inSampleSize = sampleSize;
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path, options);
    }

    public static Bitmap getRoundedCornerBitmap(Bitmap bm, int pixels) {
        Bitmap output = Bitmap.createBitmap(bm.getWidth(), bm.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bm.getWidth(), bm.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bm, rect, rect, paint);
        return output;
    }
}

package com.ss.photoeffectseditor.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by phamxuanlu@gmail.com on 3/29/2015.
 */
public class ViewUtils {

    private static void setFont(ViewGroup vg, Typeface tf) {
        int childCount = vg.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = vg.getChildAt(i);
            if (view instanceof ViewGroup) {
                setFont((ViewGroup) view, tf);
            }
            if (view instanceof TextView) {
                ((TextView) view).setTypeface(tf);
            }
        }
    }

    public static void setupFont(Context context, ViewGroup rootView, String assertFont) {
        Typeface tf = Typeface.createFromAsset(context.getAssets(), assertFont);
        setFont(rootView, tf);
    }

}

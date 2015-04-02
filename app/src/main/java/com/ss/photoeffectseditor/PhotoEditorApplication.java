package com.ss.photoeffectseditor;

import android.app.Application;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Logger;
import com.google.android.gms.analytics.Tracker;
import com.ss.photoeffectseditor.models.ToolObject;
import com.ss.photoeffectseditor.widget.ToolStructureBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PhotoEditorApplication extends Application {

    private static final String PROPERTY_ID = "UA-54848763-7";

    public static int GENERAL_TRACKER = 0;

    public enum TrackerName {
        APP_TRACKER, GLOBAL_TRACKER, ECOMMERCE_TRACKER,
    }

    private HashMap<TrackerName, Tracker> mTrackers = new HashMap<TrackerName, Tracker>();

    public PhotoEditorApplication() {
        super();

    }

    public synchronized Tracker getTracker(TrackerName trackerId) {
        if (!mTrackers.containsKey(trackerId)) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            analytics.getLogger().setLogLevel(Logger.LogLevel.ERROR);
            Tracker t = null;
            if (trackerId == TrackerName.APP_TRACKER) {
                t = analytics.newTracker(PROPERTY_ID);
            } else if (trackerId == TrackerName.GLOBAL_TRACKER) {
                t = analytics.newTracker(R.xml.global_tracker);
            } else {
                t = analytics.newTracker(R.xml.ecommerce_tracker);
            }

            t.enableExceptionReporting(true);
            t.enableAdvertisingIdCollection(true);
            mTrackers.put(trackerId, t);

        }
        return mTrackers.get(trackerId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        AppConfigs.getInstance().editorFeatures = buildEditorTools();

        WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        AppConfigs.getInstance().deviceWidth = metrics.widthPixels;
        AppConfigs.getInstance().deviceHeight = metrics.heightPixels;
    }


    private List<ToolObject> buildEditorTools() {
        ToolStructureBuilder tb = new ToolStructureBuilder(this);
        List<ToolObject> lst = new ArrayList<ToolObject>();
        lst.add(tb.enhance());
        lst.add(tb.effects());
        lst.add(tb.brightness());
        lst.add(tb.gamma());
        return lst;
    }
}

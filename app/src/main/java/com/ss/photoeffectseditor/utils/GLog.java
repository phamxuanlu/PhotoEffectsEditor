package com.ss.photoeffectseditor.utils;

import android.util.Log;

public class GLog {
	public enum LogMode {
		RELEASE(6), VERBOSE(1), DEBUG(2), INFO(3), WARN(4), ERROR(5);

		private int logMode;

		LogMode(int lm) {
			this.logMode = lm;
		}

		public int getLogMode() {
			return this.logMode;
		}
	}

	private static LogMode logMode;

	static {
		logMode = LogMode.VERBOSE;
	}

	public static void setLogMode(LogMode mode) {
		logMode = mode;
	}

	public static void disableGLog() {
		logMode = LogMode.RELEASE;
	}

	// DEBUG
	public static void d(String tag, String message) {
		if (logMode.getLogMode() <= LogMode.DEBUG.getLogMode()) {
			Log.d(tag, message);
		}
	}

	public static void d(Class<?> clazz, String message) {
		GLog.d(clazz.getName(), message);
	}

	// INFO
	public static void i(String tag, String message) {
		if (logMode.getLogMode() <= LogMode.INFO.getLogMode()) {
			Log.i(tag, message);
		}
	}

	public static void i(Class<?> clazz, String message) {
		GLog.i(clazz.getName(), message);
	}

	// VERBOSE

	public static void v(String tag, String message) {
		if (logMode.getLogMode() <= LogMode.VERBOSE.getLogMode()) {
			Log.v("GLog_" + tag, message);
		}
	}

	public static void v(Class<?> clazz, String message) {
		GLog.v(clazz.getName(), message);
	}

	// WARN

	public static void w(String tag, String message) {
		if (logMode.getLogMode() <= LogMode.WARN.getLogMode()) {
			Log.w(tag, message);
		}
	}

	public static void w(Class<?> clazz, String message) {
		GLog.w(clazz.getName(), message);
	}

	// ERROR

	public static void e(String tag, String message) {
		if (logMode.getLogMode() <= LogMode.ERROR.getLogMode()) {
			Log.e(tag, message);
		}
	}

	public static void e(Class<?> clazz, String message) {
		GLog.e(clazz.getName(), message);
	}
}

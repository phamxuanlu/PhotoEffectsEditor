package com.ss.photoeffectseditor.widget;

import android.content.Context;
import android.os.Vibrator;

public class VibrateOnTouchListener {
	private Vibrator vibrator;
	private final long VIBRATE_TIME = 100;

	public VibrateOnTouchListener(Context context) {
		this.vibrator = (Vibrator) context
				.getSystemService(Context.VIBRATOR_SERVICE);
	}

	private boolean hasVibrator() {
		return this.vibrator.hasVibrator();
	}

	public void onTouchVibrate(long vibrate_time_ms) {
		if (hasVibrator()) {
			if (vibrate_time_ms < 0) {
				this.vibrator.vibrate(VIBRATE_TIME);
			} else {
				this.vibrator.vibrate(vibrate_time_ms);
			}
		}
	}
}

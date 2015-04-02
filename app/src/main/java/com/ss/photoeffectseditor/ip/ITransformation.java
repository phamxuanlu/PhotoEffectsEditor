package com.ss.photoeffectseditor.ip;

import android.graphics.Bitmap;

public interface ITransformation {
	public Bitmap perform(Bitmap inp);
}

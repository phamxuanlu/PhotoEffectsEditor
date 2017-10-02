package com.ss.photoeffectseditor.imageprocessing;

import android.graphics.Bitmap;

public interface ITransformation {
	Bitmap perform(Bitmap inp);
}

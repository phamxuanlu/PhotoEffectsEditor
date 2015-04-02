package com.ss.photoeffectseditor.ip;

import android.graphics.Bitmap;

public class GammaCorrectionTransformation extends AbstractOptimizeTransformation {
	private float red;
	private float green;
	private float blue;

	public GammaCorrectionTransformation(float red, float green, float blue) {
		this.red = red;
		this.green = green;
		this.blue = blue;
	}



	@Override
	public Bitmap perform(Bitmap inp) {
		Bitmap bmOut = Bitmap.createBitmap(inp.getWidth(), inp.getHeight(),
				inp.getConfig());

		int w = inp.getWidth();
		int h = inp.getHeight();

		int A, R, G, B;
		int[] pixels = new int[w * h];
		final int MAX_SIZE = 256;
		final double MAX_DBL_VALUE = 256.0;
		final int MAX_INT_VALUE = 255;
		final float REVERSE = 1.0f;

		int[] gammaR = new int[MAX_SIZE];
		int[] gammaG = new int[MAX_SIZE];
		int[] gammaB = new int[MAX_SIZE];

		int i = 0, j = 0;
		for (i = 0; i < MAX_SIZE; i++) {
			gammaR[i] = (int) Math.min(MAX_INT_VALUE,
					MAX_DBL_VALUE * Math.pow(i / MAX_DBL_VALUE, REVERSE / red));
			gammaG[i] = (int) Math.min(
					MAX_INT_VALUE,
					MAX_DBL_VALUE
							* Math.pow(i / MAX_DBL_VALUE, REVERSE / green));
			gammaB[i] = (int) Math
					.min(MAX_INT_VALUE,
							MAX_DBL_VALUE
									* Math.pow(i / MAX_DBL_VALUE, REVERSE
											/ blue));
		}
		inp.getPixels(pixels, 0, w, 0, 0, w, h);
		int pos;
		for (i = 0; i < h; i++) {
			for (j = 0; j < w; j++) {
				pos = i * w + j;
				A = (pixels[pos] >> 24) & 0xFF;
				R = gammaR[(pixels[pos] >> 16) & 0xFF];
				G = gammaG[(pixels[pos] >> 8) & 0xFF];
				B = gammaB[pixels[pos] & 0xFF];
				pixels[pos] = (A << 24) | (R << 16) | (G << 8) | B;
			}
		}
		bmOut.setPixels(pixels, 0, w, 0, 0, w, h);
		return bmOut;
	}

}

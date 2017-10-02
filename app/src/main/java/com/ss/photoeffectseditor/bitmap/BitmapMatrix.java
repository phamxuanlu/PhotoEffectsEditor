package com.ss.photoeffectseditor.bitmap;



import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Log;

@Deprecated
public class BitmapMatrix {
	protected int WIDTH;
	protected int HEIGHT;
	protected Bitmap mBitmap;

	protected short[][] RED;
	protected short[][] GREEN;
	protected short[][] BLUE;
	protected short[][] ALPHA;

	public enum ColorType {
		RED, GREEN, BLUE, ALPHA
	}

	public short getValue(ColorType ct, int x, int y) {
		if (ct.equals(ColorType.RED)) {
			return RED[x][y];
		} else if (ct.equals(ColorType.GREEN)) {
			return GREEN[x][y];
		} else if (ct.equals(ColorType.BLUE)) {
			return BLUE[x][y];
		} else if (ct.equals(ColorType.ALPHA)) {
			return ALPHA[x][y];
		} else {
			return 0;
		}
	}

	public void setValue(ColorType ct, int x, int y, short value) {
		if (ct.equals(ColorType.RED)) {
			RED[x][y] = value;
		} else if (ct.equals(ColorType.GREEN)) {
			GREEN[x][y] = value;
		} else if (ct.equals(ColorType.BLUE)) {
			BLUE[x][y] = value;
		} else if (ct.equals(ColorType.ALPHA)) {
			ALPHA[x][y] = value;
		}
	}

	public Bitmap getBitmap() {
		Bitmap bm = mBitmap.copy(Config.ARGB_8888, true);
		int i = 0;
		int j = 0;
		int pos=0;
		int colors[] = new int[WIDTH * HEIGHT];
		for (i = 0; i < HEIGHT; i++) {
			for (j = 0; j < WIDTH; j++) {
				pos=i*WIDTH+j;
				colors[pos] = Color.argb(this.ALPHA[j][i], this.RED[j][i],
						this.GREEN[j][i], this.BLUE[j][i]);

			}
		}
		bm.setPixels(colors, 0, this.WIDTH, 0, 0, this.WIDTH, this.HEIGHT);
		return bm;
	}

	public BitmapMatrix(int width, int height) {
		mBitmap = Bitmap.createBitmap(WIDTH, HEIGHT, Config.ARGB_8888);
		initBitmapInfo(width, height);
	}

	private void readColor() {
		int[] colors = new int[this.WIDTH * this.HEIGHT];
		this.mBitmap.getPixels(colors, 0, this.WIDTH, 0, 0, WIDTH, HEIGHT);
		int i = 0;
		int j = 0;
		int pos;
		for (i = 0; i < this.HEIGHT; i++) {
			for (j = 0; j < this.WIDTH; j++) {
				pos = i * this.WIDTH + j;
				ALPHA[j][i] = (short) ((colors[pos] >> 24) & 0xff);
				RED[j][i] = (short) ((colors[pos] >> 16) & 0xff);
				GREEN[j][i] = (short) ((colors[pos] >> 8) & 0xff);
				BLUE[j][i] = (short) (colors[pos] & 0xff);
			}
		}
	}

	private void initBitmapInfo(int width, int height) {
		this.HEIGHT = height;
		this.WIDTH = width;
		RED = new short[width][height];
		GREEN = new short[width][height];
		BLUE = new short[width][height];
		ALPHA = new short[width][height];
		readColor();
	}

	public BitmapMatrix(Bitmap bm) {
		this.WIDTH = bm.getWidth();
		this.HEIGHT = bm.getHeight();
		
		this.mBitmap = bm;
		initBitmapInfo(WIDTH, HEIGHT);
	}

	public int getWidth() {
		return this.WIDTH;
	}

	public int getHeight() {
		return this.HEIGHT;
	}
}

package com.ss.photoeffectseditor.imageprocessing;

import android.graphics.Bitmap;
import android.graphics.Color;

/**
 * Authour        : PHAMXUANLU@GMAIL.COM
 * License        : phamxuanlu@gmail.com
 * Date           : 3:11 PM - 4/17/2015
 * Description    : Hiệu ứng màu đèn neon, dùng toán tử sobel phát hiện các biên và biến các biên này thành
 * màu xanh đèn neon, nền thành màu đen
 */
public class NeonEffectTransformation extends AbstractEffectTransformation {
    private int neonR;
    private int neonG;
    private int neonB;

    public NeonEffectTransformation() {
//        this.neonR = 57;
//        this.neonG = 255;
//        this.neonB = 20;

        this.neonR = 127;
        this.neonG = 255;
        this.neonB = 0;
    }

    public void setNeon(int neonR, int neonG, int neonB) {
        this.neonR = neonR;
        this.neonG = neonG;
        this.neonB = neonB;
    }

    @Override
    public void setThumbImage(Bitmap bmThumb) {

    }

    @Override
    public Bitmap getThumbnail() {
        return null;
    }

    private int grayscale(int cl) {
        int R, G, B;
        R = (cl >> 16) & 0xFF;
        G = (cl >> 8) & 0xFF;
        B = cl & 0xFF;

        return (int) (0.299 * R + 0.587 * G + 0.114 * B);
    }

    @Override
    public Bitmap perform(Bitmap inp) {
        int[] xSobel = new int[]{-1, -2, -1,
                0, 0, 0,
                1, 2, 1};
        int[] ySobel = new int[]{-1, 0, 1,
                -2, 0, 2,
                -1, 0, 1};

        int index = 0;
        int oriGray = 0;
        int afterGray = 0;
        int maskSize = 3;
        int halfMaskSize = maskSize / 2;

        int xVal = 0;
        int yVal = 0;
        float threshold = 110;
        int i, j, m, n;
        int oriPos, pos;
        int width = inp.getWidth();
        int height = inp.getHeight();
        Bitmap bmOut = Bitmap.createBitmap(width, height, inp.getConfig());
        int[] pixels = new int[width * height];
        inp.getPixels(pixels, 0, width, 0, 0, width, height);
        int[] oriPixels = new int[width * height];
        inp.getPixels(oriPixels, 0, width, 0, 0, width, height);
        for (i = halfMaskSize; i < height - halfMaskSize; i++) {
            for (j = halfMaskSize; j < width - halfMaskSize; j++) {
                index = 0;
                xVal = yVal = 0;
                for (m = -halfMaskSize; m <= halfMaskSize; m++) {
                    for (n = -halfMaskSize; n <= halfMaskSize; n++) {
                        oriPos = (i + m) * width + j + n;
                        oriGray = grayscale(oriPixels[oriPos]);
                        xVal += oriGray * xSobel[index];
                        yVal += oriGray * ySobel[index];
                        index++;
                    }
                }

                afterGray = Math.abs(xVal) + Math.abs(yVal);
                if (afterGray > 255)
                    afterGray = 255;
                if (afterGray < 0)
                    afterGray = 0;

                pos = i * width + j;
                if (afterGray > threshold) {
                    pixels[pos] = Color.rgb(neonR, neonG, neonB);
                } else {
                    pixels[pos] = Color.rgb(1, 1, 1);
                }
            }
        }
        bmOut.setPixels(pixels, 0, width, 0, 0, width, height);
        return bmOut;
    }
}

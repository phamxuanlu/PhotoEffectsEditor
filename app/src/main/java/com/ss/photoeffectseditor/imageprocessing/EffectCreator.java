package com.ss.photoeffectseditor.imageprocessing;

public class EffectCreator {

	public enum ColorToneIntensity {
		RED, GREEN, BLUE
	}

	public static EffectObject extractEffect(float[] eRatio,
			ColorToneIntensity cl, short ci) {
		short RValue;
		short GValue;
		short BValue;
		EffectObject ef = new EffectObject();
		for (int i = 0; i < 256; i++) {
			RValue = (short) Math.min(255,
					(int) (i * eRatio[0] + i * eRatio[1] + i * eRatio[2]));
			if (cl == ColorToneIntensity.RED) {
				RValue -= ci;
				if (RValue < 0)
					RValue = 0;
			}
			ef.RED[i] = RValue;
			GValue = (short) Math.min(255,
					(int) (i * eRatio[3] + i * eRatio[4] + i * eRatio[5]));
			if (cl == ColorToneIntensity.GREEN) {
				GValue -= ci;
				if (GValue < 0)
					GValue = 0;
			}
			ef.GREEN[i] = GValue;
			BValue = (short) Math.min(255,
					(int) (i * eRatio[6] + i * eRatio[7] + i * eRatio[8]));
			if (cl == ColorToneIntensity.BLUE) {
				BValue -= ci;
				if (BValue < 0)
					BValue = 0;
			}
			ef.BLUE[i] = BValue;
		}


		return ef;
	}
}

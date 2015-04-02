package com.ss.photoeffectseditor.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.ImageView;

import com.ss.photoeffectseditor.R;
import com.ss.photoeffectseditor.ip.GammaCorrectionTransformation;
import com.ss.photoeffectseditor.ip.ITransformation;
import com.ss.photoeffectseditor.utils.BitmapUtils;
import com.ss.photoeffectseditor.utils.StorageUtils;

public class TestActivity extends Activity {
	private Bitmap bitmapSource;
	private Bitmap bitmapResult;

	private ImageView imgSource;
	private ImageView imgResult;
	private Button btnProcess;

	private ProgressDialog processDialog;
	private static final int REQUEST_CODE_PICK_PHOTO = 1;

    private ITransformation transform;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test_activity_layout);
		imgSource = (ImageView) findViewById(R.id.imgSrc);
		imgResult = (ImageView) findViewById(R.id.imgResult);
		btnProcess = (Button) findViewById(R.id.btnProcess);
		processDialog = new ProgressDialog(this);
		processDialog.setMessage("Please wait...");
		processDialog.setIndeterminate(true);
		setupViews();
		// float[] sepia = new float[] { .393f, .769f, .189f, .349f, .686f,
		// .168f,
		// .272f, .534f, .131f };
		// transform = new
		// EffectTransformation(EffectCreator.extractEffect(sepia,
		// ColorToneIntensity.BLUE, (short) 30));
		//transform = new GrayscaleTransformation(10);
		transform = new GammaCorrectionTransformation(1.8f, 1.8f, 1.8f);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case REQUEST_CODE_PICK_PHOTO:
				Uri uri = data.getData();
				String pickPath = StorageUtils.getPathFromUri(uri, this);
				if (bitmapSource != null) {
					Log.v("BITMAP SRC", "RECYCLE");
					bitmapSource.recycle();
					bitmapSource = null;
				}
				bitmapSource = BitmapUtils.decodeBitmap(TestActivity.this,
                        pickPath);
				Drawable drawable = imgSource.getDrawable();
				if (drawable != null && drawable instanceof BitmapDrawable) {
					((BitmapDrawable) drawable).getBitmap().recycle();
				}
				imgSource.setImageBitmap(bitmapSource);
				processDialog.dismiss();
				break;

			default:
				break;
			}
		} else {
			processDialog.dismiss();
		}
	}

	private void setupViews() {
		imgSource.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				processDialog.show();
				Intent pick = new Intent(Intent.ACTION_GET_CONTENT);
				pick.setType("image/*");
				startActivityForResult(Intent.createChooser(pick, "Pick"),
						REQUEST_CODE_PICK_PHOTO);
				return true;
			}
		});

		btnProcess.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (transform != null && bitmapSource != null) {
					processDialog.show();
					long st = System.currentTimeMillis();
					bitmapResult = transform.perform(bitmapSource);
					long en = System.currentTimeMillis();
					float time = (en - st) / 1000.0f;
					Log.v("PROCESSING TIME=", "" + time);
					imgResult.setImageBitmap(bitmapResult);
					processDialog.dismiss();
				}
			}
		});
	}
}

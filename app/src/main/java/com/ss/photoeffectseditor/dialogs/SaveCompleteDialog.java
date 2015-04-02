package com.ss.photoeffectseditor.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.ss.photoeffectseditor.AppConstants;
import com.ss.photoeffectseditor.R;
import com.ss.photoeffectseditor.widget.VibrateOnTouchListener;
import com.ss.photoeffectseditor.utils.StorageUtils;

import java.io.File;


public class SaveCompleteDialog extends Dialog {

	private ImageView prv_Image;
	private ImageButton dialog_open_image;
	private ImageButton dialog_btnShare;
	private ImageButton dialog_dismiss_image;
	private String previewPath;
	private Context context;

	private TextView dialog_info_filename;
	private TextView dialog_info_file_size;
	private TextView dialog_info_image_size;

	public SaveCompleteDialog(Context context) {
		super(context);
		this.context = context;
	}

	public void setPreviewImage(String path) {
		this.previewPath = path;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.save_complete_dialog_layout);
		prv_Image = (ImageView) findViewById(R.id.dialog_image_preview);
		dialog_open_image = (ImageButton) findViewById(R.id.dialog_open_image);
		dialog_btnShare = (ImageButton) findViewById(R.id.dialog_btnShare);
		dialog_dismiss_image = (ImageButton) findViewById(R.id.dialog_dismiss_image);
		dialog_info_filename = (TextView) findViewById(R.id.dialog_info_filename);
		dialog_info_file_size = (TextView) findViewById(R.id.dialog_info_file_size);
		dialog_info_image_size = (TextView) findViewById(R.id.dialog_info_image_size);
		dialog_open_image.setOnClickListener(click);
		dialog_btnShare.setOnClickListener(click);
		dialog_dismiss_image.setOnClickListener(click);

		if (previewPath != null && !previewPath.equals("")) {
			File f = new File(previewPath);
			dialog_info_filename.setText(this.context.getResources().getString(
					R.string.savecompletedialog_filename)
					+ f.getName());
			dialog_info_file_size.setText(this.context.getResources()
					.getString(R.string.savecompletedialog_filesize)
					+ StorageUtils.convertToMiB(f.length()));

			Options option = new Options();
			option.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(previewPath, option);
			dialog_info_image_size.setText(this.context.getResources()
					.getString(R.string.savecompletedialog_imagesize)
					+ option.outWidth + "x" + option.outHeight);
			int w = 300;
			int h = (int) (300 * ((float) option.outHeight / option.outWidth));
			option.inSampleSize = inSample(option, w, h);
			option.inJustDecodeBounds = false;
			Bitmap bm = BitmapFactory.decodeFile(previewPath, option);
			prv_Image.setImageBitmap(bm);
		} else {
			dismiss();
		}
	}

	private View.OnClickListener click = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			VibrateOnTouchListener vEx = new VibrateOnTouchListener(context);
			vEx.onTouchVibrate(AppConstants.BUTTON_TOUCH_VIBRATE_TIME_MS);
			switch (v.getId()) {
			case R.id.dialog_open_image:
//				Intent intent = new Intent(context,
//						GalleryPreviewActivity.class);
//				Bundle bundle = new Bundle();
//				bundle.putString(AppConstants.SELECTED_PHOTO, previewPath);
//				intent.putExtras(bundle);
//				SaveCompleteDialog.this.context.startActivity(intent);
				break;
			case R.id.dialog_dismiss_image:
				dismiss();
				break;
			case R.id.dialog_btnShare:

				ShareIntentsDialog shareDialog = new ShareIntentsDialog.Builder(
						context)
						.setImagePath(previewPath)
						.setDialogTitle(
								SaveCompleteDialog.this.context
										.getResources()
										.getString(
												R.string.sharedialog_title))
						.build();

				shareDialog.show();
				break;
			default:
				break;
			}
			dismiss();
		}
	};

	private int inSample(Options options, int reqWidth,
			int reqHeight) {
		final int height = options.outHeight;
		final int width = options.outWidth;
		int insample = 1;
		if (height > reqHeight || width > reqWidth) {
			final int halfH = (int) (height / 2.0f);
			final int halfW = (int) (width / 2.0f);
			while ((halfH / insample) > reqHeight
					|| (halfW / insample) > reqWidth) {
				insample *= 2;
			}
		}
		return insample;
	}
}

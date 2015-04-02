package com.ss.photoeffectseditor.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.ss.photoeffectseditor.AppConstants;
import com.ss.photoeffectseditor.R;

public class RateThisAppDialog extends Dialog {
	private Context context;
	private Button btnRateNow;
	private Button btnLater;
	private Button btnNever;

	private SharedPreferences pref;

	public RateThisAppDialog(Context context) {
		super(context);
		this.context = context;
		pref = context.getSharedPreferences(AppConstants.APP_PREFERENCE,
				Context.MODE_PRIVATE);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.rate_app_dialog_layout);
		btnRateNow = (Button) findViewById(R.id.btn_rate_now);
		btnLater = (Button) findViewById(R.id.btn_later);
		btnNever = (Button) findViewById(R.id.btn_never);
		btnRateNow.setOnClickListener(clk);
		btnLater.setOnClickListener(clk);
		btnNever.setOnClickListener(clk);
	}

	private View.OnClickListener clk = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_rate_now:
				pref.edit()
						.putString(AppConstants.APP_PREFERENCE_RATE,
								AppConstants.APP_PREFERENCE_NEVER).commit();
				final String packagename = context.getPackageName();
				try {
					Uri uri = Uri.parse("market://details?id=" + packagename);
					Intent intent = new Intent(Intent.ACTION_VIEW, uri);
					context.startActivity(intent);
				} catch (android.content.ActivityNotFoundException ex) {
					Uri uri = Uri
							.parse("https://play.google.com/store/apps/details?id="
									+ packagename);
					Intent intent = new Intent(Intent.ACTION_VIEW, uri);
					context.startActivity(intent);
				}
				break;
			case R.id.btn_later:
				boolean s = pref.edit()
						.putInt(AppConstants.APP_PREFERENCE_OPEN_TIMES, 0)
						.commit();
//				((BeautifulFrameActivity) context).setOpenTimes(0);
//				GLog.v("NOT NOW", s + "");

				break;
			case R.id.btn_never:
				pref.edit()
						.putString(AppConstants.APP_PREFERENCE_RATE,
								AppConstants.APP_PREFERENCE_NEVER).commit();

				break;
			default:
				break;
			}
			dismiss();
		}

	};

}

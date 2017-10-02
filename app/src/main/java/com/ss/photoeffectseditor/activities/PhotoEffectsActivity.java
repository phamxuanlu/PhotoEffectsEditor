package com.ss.photoeffectseditor.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.ss.photoeffectseditor.AppConfigs;
import com.ss.photoeffectseditor.AppConstants;
import com.ss.photoeffectseditor.R;
import com.ss.photoeffectseditor.utils.StorageUtils;

import java.io.File;
import java.util.List;

/**
 * Created by phamxuanlu@gmail.com on 3/4/2015.
 * Màn hình chính của ứng dụng, tại đây có thể chọn ảnh cần chỉnh sửa hoặc chụp ảnh từ camera
 */
public class PhotoEffectsActivity extends Activity {
    private final String IMG_CAPTURED_PATH = "IMG_CAPTURED_PATH";


    private TextView btnOpenPhoto;
    private TextView btnCamera;
    private ViewGroup activityLayout;
    private TextView txtVersion;
    private TextView txtAppName;
    private String capturedPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.photoeffects_activity_layout);
        setupViews();
    }

    private void setupViews() {
        activityLayout = (ViewGroup) findViewById(R.id.photo_effect_activity_layout);
        btnOpenPhoto = (TextView) findViewById(R.id.btnSelectPhoto);
        btnOpenPhoto.setOnClickListener(viewClickListener);
        btnCamera = (TextView) findViewById(R.id.btnCapture);
        btnCamera.setOnClickListener(viewClickListener);
        findViewById(R.id.btnFrames).setOnClickListener(viewClickListener);
        Typeface tf = Typeface.createFromAsset(getAssets(), AppConfigs.getInstance().TYPE_FACE);
        setupFont(activityLayout, tf);

        txtAppName = (TextView) findViewById(R.id.app_name);
        txtVersion = (TextView) findViewById(R.id.app_version);
        PackageInfo pkg = null;
        try {
            pkg = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        String verName = "N/A";
        if (pkg != null) {
            verName = pkg.versionName;
        }
        txtVersion.setText("version " + verName + " beta");
        txtAppName.setText(getString(R.string.app_name));

    }

    private void setupFont(ViewGroup v, Typeface tf) {
        int childCount = v.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = v.getChildAt(i);
            if (child instanceof ViewGroup) {
                setupFont((ViewGroup) child, tf);
            } else if (child instanceof TextView) {
                ((TextView) child).setTypeface(tf);
            }
        }
    }

    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(this.IMG_CAPTURED_PATH, capturedPhoto);
        super.onSaveInstanceState(outState);
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        capturedPhoto = savedInstanceState.getString(IMG_CAPTURED_PATH);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case AppConstants.CHOOSE_PHOTO_REQUEST_CODE:
                    Uri selectedPhoto = data.getData();
                    String filePath = StorageUtils.getPathFromUri(selectedPhoto, this);
                    Bundle bundle = new Bundle();
                    bundle.putString(AppConstants.SELECTED_PHOTO, filePath);
                    // Toast.makeText(this, filePath, Toast.LENGTH_LONG).show();
                    Intent editor = new Intent(PhotoEffectsActivity.this, EditPreviewActivity.class);
                    editor.putExtras(bundle);
                    startActivity(editor);
                    break;
                case AppConstants.CAMERA_TAKE_A_PICTURE:
                    Bundle bundleCam = new Bundle();
                    bundleCam.putString(AppConstants.SELECTED_PHOTO, capturedPhoto);
                    //Toast.makeText(this, capturedPhoto, Toast.LENGTH_LONG).show();
                    Intent edtor = new Intent(PhotoEffectsActivity.this, EditPreviewActivity.class);
                    edtor.putExtras(bundleCam);
                    startActivity(edtor);
                    break;
            }
        }
    }

    private View.OnClickListener viewClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btnSelectPhoto:
                    Intent intentPick = new Intent();
                    intentPick.setData(Uri.parse("content://media/external/images/media"));
                    intentPick.setAction(Intent.ACTION_PICK);
                    startActivityForResult(intentPick, AppConstants.CHOOSE_PHOTO_REQUEST_CODE);
                    break;
                case R.id.btnCapture:
                    takePhotoFromCamera();
                    break;
                case R.id.btnFrames:
                    Toast.makeText(PhotoEffectsActivity.this, "Not implement", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    private boolean hasIntent(Context context, String action) {
        final PackageManager packageManager = context.getPackageManager();
        final Intent it = new Intent(action);
        List<ResolveInfo> lst = packageManager.queryIntentActivities(it, PackageManager.MATCH_DEFAULT_ONLY);
        return lst.size() > 0;
    }

    private void takePhotoFromCamera() {
        if (!hasIntent(this, MediaStore.ACTION_IMAGE_CAPTURE)) {
            Toast.makeText(this, getResources().getString(R.string.camera_not_available_message), Toast.LENGTH_SHORT).show();
            return;
        }
        Intent takeIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File outPut = StorageUtils.createImageFile(AppConstants.CAMERA_PICTURE_PREFIX,
                StorageUtils.getCacheDirectory(), ".JPEG");
        if (outPut == null) {
            Toast.makeText(
                    this,
                    getResources()
                            .getString(R.string.camera_create_file_failed),
                    Toast.LENGTH_SHORT).show();
            return;
        }

        capturedPhoto = outPut.getAbsolutePath();
        takeIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(outPut));
        takeIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
        if (takeIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takeIntent, AppConstants.CAMERA_TAKE_A_PICTURE);
        }

    }
}

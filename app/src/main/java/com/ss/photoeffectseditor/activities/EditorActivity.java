package com.ss.photoeffectseditor.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ViewFlipper;

import com.ss.photoeffectseditor.AppConfigs;
import com.ss.photoeffectseditor.AppConstants;
import com.ss.photoeffectseditor.BuildConfig;
import com.ss.photoeffectseditor.R;
import com.ss.photoeffectseditor.adapters.EditorListToolContentAdapter;
import com.ss.photoeffectseditor.adapters.EditorListToolsAdapter;
import com.ss.photoeffectseditor.asynctasks.DoTransformationAsyncTask;
import com.ss.photoeffectseditor.asynctasks.EditorSaveResultAsyncTask;
import com.ss.photoeffectseditor.asynctasks.OpenBitmapAsyncTask;
import com.ss.photoeffectseditor.ip.ITransformation;
import com.ss.photoeffectseditor.models.ToolObject;
import com.ss.photoeffectseditor.utils.GLog;
import com.ss.photoeffectseditor.utils.StorageUtils;
import com.ss.photoeffectseditor.widget.TouchImageView;

import it.sephiroth.android.library.widget.AdapterView;
import it.sephiroth.android.library.widget.AdapterView.OnItemClickListener;
import it.sephiroth.android.library.widget.HListView;

public class EditorActivity extends Activity {
    public static final String OUTPUT_PATH = "OUTPUT_PATH";

    private String selectedPhoto; //Ảnh được chọn để chỉnh sửa

    private String outputPath;

    private Button btnApply;
    private TouchImageView imgPreview;

    private ProgressDialog processDialog;
    private OpenBitmapAsyncTask openTask;
    private Bitmap bmThumb;
    private Bitmap bmSource;
    private Bitmap bmProcessed;
    private boolean canUndo;
    private ITransformation transformation;
    private DoTransformationAsyncTask transTask;

    private ViewFlipper toolContainer;
    private ViewGroup toolContentView;
    private ViewGroup toolView;
    private HListView listTool;
    private HListView lstContent;
    private ToolObject currentTool;
    private EditorState editorState;

    private enum EditorState {
        APPLYABLE, DONEABLE
    }

    private void setupViews() {
        toolContainer = (ViewFlipper) findViewById(R.id.controller);
        toolView = (ViewGroup) toolContainer.findViewById(R.id.tool_view);
        toolContentView = (ViewGroup) findViewById(R.id.tool_detail);
        listTool = (HListView) toolView.findViewById(R.id.tool_listView);
        lstContent = (HListView) findViewById(R.id.content_listView);
        lstContent.setOnItemClickListener(lstContentClick);
        EditorListToolsAdapter listToolsAdapter = new EditorListToolsAdapter(this, AppConfigs.getInstance().editorFeatures);
        listTool.setAdapter(listToolsAdapter);
        listTool.setOnItemClickListener(listToolClick);
        toolContainer.setDisplayedChild(0);
        imgPreview = (TouchImageView) findViewById(R.id.imgPreview);
        btnApply = (Button) findViewById(R.id.btnApply);
        btnApply.setOnClickListener(onClick);
        processDialog = new ProgressDialog(this);
        processDialog.setMessage(getString(R.string.editor_process_dialog_message));
        processDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        processDialog.setIndeterminate(true);

        Intent dataIntent = getIntent();
        outputPath = dataIntent.getStringExtra(this.OUTPUT_PATH);
    }

    private OnItemClickListener listToolClick = new OnItemClickListener() {
        @Override
        public void onItemClick(it.sephiroth.android.library.widget.AdapterView<?> adapterView, View view, int i, long l) {
            currentTool = AppConfigs.getInstance().editorFeatures.get(i);
            if (currentTool.toolType.equals(ToolObject.ToolType.TOOL_TYPE)) {
                toolContainer.post(new Runnable() {
                    @Override
                    public void run() {
                        EditorListToolContentAdapter adapter = new EditorListToolContentAdapter(EditorActivity.this, currentTool.detailFeatures);
                        lstContent.setAdapter(adapter);
                        toolContainer.setDisplayedChild(1);
                        btnApply.setText(getString(R.string.editor_apply_button_text));
                        editorState = EditorState.APPLYABLE;
                        GLog.v("HListView Size", "W=" + lstContent.getWidth() + "H=" + lstContent.getHeight());
                        GLog.v("ITEM CLICKED", "TOOL " + currentTool.name + " CHILD: " + currentTool.detailFeatures.size());
                    }
                });

            } else if (currentTool.toolType.equals(ToolObject.ToolType.OPTION_TYPE)) {
                toolContainer.setDisplayedChild(2);
                GLog.v("ITEM CLICKED", "OPTION" + currentTool.name);
            }
        }
    };


    private OnItemClickListener lstContentClick = new OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            ToolObject tmp = currentTool.detailFeatures.get(i);
            if (tmp.transform != null) {
                doTransform(tmp.transform);
            }
        }
    };

    @Override
    public void onBackPressed() {
        if (toolContainer.getDisplayedChild() == 0) {
            super.onBackPressed();
        } else {
            btnApply.setText(getString(R.string.editor_btn_done_text));
            editorState = EditorState.DONEABLE;
            toolContainer.setDisplayedChild(0);
            imgPreview.setImageBitmap(bmSource);
        }


    }

    private void openChildTool() {
        toolContainer.setDisplayedChild(0);
    }

    private void closeChildTool() {
        toolContainer.setDisplayedChild(1);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_editor);
        setupViews();

        //Ảnh được nhận có thể gửi từ Main Activity hoặc từ các ứng dụng khác, lúc này phải đọc xem
        //ảnh được gửi từ nguồn nào và lấy đường dẫn ra cho phù hợp
        Intent receiveIntent = getIntent();
        String receiveAction = receiveIntent.getAction();
        String receiveType = receiveIntent.getType();
        Bundle data = getIntent().getExtras();
        if (receiveAction != null && receiveAction.equals(Intent.ACTION_SEND)) {
            if (receiveType.startsWith("image/*") || receiveType.startsWith("*/*")) {
                if (data != null && data.containsKey(Intent.EXTRA_STREAM)) {
                    Uri receiveUri = (Uri) data.get(Intent.EXTRA_STREAM);
                    selectedPhoto = StorageUtils.getPathFromUri(receiveUri, this);
                }
            }
        } else if (receiveAction != null
                && (receiveAction.equals(Intent.ACTION_VIEW)
                || receiveAction.equals(Intent.ACTION_EDIT)
                || receiveAction.equals(Intent.ACTION_PICK))) {
            Uri receiveUri = receiveIntent.getData();
            if (receiveUri != null) {
                selectedPhoto = StorageUtils.getPathFromUri(receiveUri, this);
            }
        } else {
            selectedPhoto = data.getString(AppConstants.SELECTED_PHOTO);
        }

        if (BuildConfig.DEBUG) {
            GLog.v("EDITOR SELECTED PHOTO", selectedPhoto);
        }

        //Sử dụng AsyncTask để đọc ảnh lên ImageView
        openTask = new OpenBitmapAsyncTask.Builder(this, selectedPhoto)
                .setProgress(processDialog)
                .setImageView(imgPreview)
                .setBitmapOpenedListener(new OpenBitmapAsyncTask.OnBitmapOpenedListener() {
                    @Override
                    public void onBitmapOpened(Bitmap bm) {
                        EditorActivity.this.bmSource = bm;
                    }
                })
                .build();
        openTask.execute();

    }

    public Bitmap getThumbnail() {
        return bmThumb;
    }

    public ITransformation getTransformation() {
        return this.transformation;
    }

    public void doTransform(ITransformation transform) {
        btnApply.setText(getString(R.string.editor_apply_button_text));
        this.transformation = transform;
        if (bmSource == null) {

            transTask = new DoTransformationAsyncTask(this, selectedPhoto, transform, processDialog, new DoTransformationAsyncTask.OnPerformedListener() {
                @Override
                public void onPerformed(Bitmap bm) {
                    if (bmProcessed != null && !bmProcessed.isRecycled()) {
                        bmProcessed.recycle();
                        bmProcessed = null;
                    }
                    bmProcessed = bm;
                    imgPreview.setImageBitmap(bmProcessed);
                }
            });
            transTask.execute();
        } else {
            transTask = new DoTransformationAsyncTask(this, bmSource, transform, processDialog, new DoTransformationAsyncTask.OnPerformedListener() {
                @Override
                public void onPerformed(Bitmap bm) {
                    if (bmProcessed != null && !bmProcessed.isRecycled()) {
                        bmProcessed.recycle();
                        bmProcessed = null;
                    }
                    bmProcessed = bm;
                    imgPreview.setImageBitmap(bmProcessed);
                }
            });

            transTask.execute();
        }
    }

    private View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btnApply:
                    if (toolContainer.getDisplayedChild() != 0 && editorState.equals(EditorState.APPLYABLE)) {
                        //APPLY PRESSED
                        btnApply.setText(getString(R.string.editor_btn_done_text));
                        editorState = EditorState.DONEABLE;
                        if (bmSource != null && !bmSource.isRecycled() && bmProcessed != null) {
                            bmSource.recycle();
                            bmSource = null;
                        }
                        if (bmProcessed != null) {
                            bmSource = bmProcessed.copy(bmProcessed.getConfig(), true);
                            transformation = null;
                        }
                    } else {
                        GLog.v("EDITOR", "DONEABLE");
                        //DONE PRESSED
                        processDialog.setMessage(getString(R.string.editor_saving_progress_dialog));
                        EditorSaveResultAsyncTask saveResultAsyncTask = new EditorSaveResultAsyncTask(EditorActivity.this,
                                bmSource, outputPath, new EditorSaveResultAsyncTask.OnSaveCompletedListener() {
                            @Override
                            public void onSaveCompleted() {
                                processDialog.dismiss();
                                Intent resultIntent = new Intent();
                                resultIntent.putExtra(EditorActivity.this.OUTPUT_PATH, outputPath);
                                if (EditorActivity.this.getParent() == null) {
                                    setResult(RESULT_OK, resultIntent);
                                } else {
                                    getParent().setResult(RESULT_OK, resultIntent);
                                }
                                EditorActivity.this.finish();
                            }
                        }, processDialog);
                        saveResultAsyncTask.execute();
                    }
                    break;
            }
        }
    };

    @Override
    protected void onStop() {
        if (openTask != null && !openTask.isCancelled()) {
            openTask.cancel(true);
        }
        super.onStop();
    }


}

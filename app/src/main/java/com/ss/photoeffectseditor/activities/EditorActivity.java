package com.ss.photoeffectseditor.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.ss.photoeffectseditor.AppConfigs;
import com.ss.photoeffectseditor.AppConstants;
import com.ss.photoeffectseditor.BuildConfig;
import com.ss.photoeffectseditor.R;
import com.ss.photoeffectseditor.adapters.EditorListToolsAdapter;
import com.ss.photoeffectseditor.asynctasks.DoTransformationAsyncTask;
import com.ss.photoeffectseditor.asynctasks.EditorSaveResultAsyncTask;
import com.ss.photoeffectseditor.asynctasks.OpenBitmapAsyncTask;
import com.ss.photoeffectseditor.imageprocessing.AbstractOptimizeTransformation;
import com.ss.photoeffectseditor.imageprocessing.ITransformation;
import com.ss.photoeffectseditor.utils.AsyncTaskUtils;
import com.ss.photoeffectseditor.utils.GLog;
import com.ss.photoeffectseditor.utils.StorageUtils;
import com.ss.photoeffectseditor.utils.ViewUtils;
import com.ss.photoeffectseditor.widget.BaseToolObject;
import com.ss.photoeffectseditor.widget.EffectToolObject;
import com.ss.photoeffectseditor.widget.OptimizeToolObject;
import com.ss.photoeffectseditor.widget.TouchImageView;

import java.util.List;

import it.sephiroth.android.library.widget.AdapterView;
import it.sephiroth.android.library.widget.AdapterView.OnItemClickListener;
import it.sephiroth.android.library.widget.HListView;

public class EditorActivity extends Activity {
    public static final String OUTPUT_PATH = "OUTPUT_PATH";

    private String selectedPhoto; //Ảnh được chọn để chỉnh sửa
    private String outputPath;
    private TextView btnApply;
    private TextView btnBack;
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
    private HListView listBaseTools;
    private HListView listEffectTools;
    private SeekBar toolSeekBar;
    private List<BaseToolObject> currentChildrenTools;
    private EditorState editorState;

    private static final int BASE_TOOL_PANEL = 0;
    private static final int EFFECT_TOOL_PANEL = 1;
    private static final int OPTIMIZE_TOOL_PANEL = 2;

    private enum EditorState {
        APPLYABLE, DONEABLE
    }

    private void setupViews() {
        toolContainer = (ViewFlipper) findViewById(R.id.controller);
        toolView = (ViewGroup) toolContainer.findViewById(R.id.tool_view);
        toolContentView = (ViewGroup) findViewById(R.id.tool_detail);
        toolContainer.setDisplayedChild(0);
        imgPreview = (TouchImageView) findViewById(R.id.imgPreview);
        btnApply = (TextView) findViewById(R.id.btnApply);
        btnApply.setOnClickListener(onClick);

        btnBack = (TextView) findViewById(R.id.btnBack);
        ViewUtils.setupFont(this, (ViewGroup) findViewById(R.id.header), AppConfigs.getInstance().TYPE_FACE);
        btnBack.setOnClickListener(onClick);
        processDialog = new ProgressDialog(this);
        processDialog.setMessage(getString(R.string.editor_process_dialog_message));
        processDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        processDialog.setIndeterminate(true);

        Intent dataIntent = getIntent();
        outputPath = dataIntent.getStringExtra(OUTPUT_PATH);
    }

    public void renderTools() {
        //Hiển thị HorizontalListView, ListView này gồm các BaseToolObject Level 1
        toolContainer.setDisplayedChild(0);
        listBaseTools = (HListView) toolView.findViewById(R.id.tool_listView);
        currentChildrenTools = AppConfigs.getInstance().editorFeatures;
        EditorListToolsAdapter listToolsAdapter = new EditorListToolsAdapter(this, AppConfigs.getInstance().editorFeatures);
        listBaseTools.setAdapter(listToolsAdapter);
        listBaseTools.setOnItemClickListener(toolItemClick);

        listEffectTools = (HListView) findViewById(R.id.content_listView);
        listEffectTools.setOnItemClickListener(toolItemClick);

        toolSeekBar = (SeekBar) findViewById(R.id.toolSeekBar);
    }

    private OnItemClickListener toolItemClick = new OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            BaseToolObject clickedToolObject = currentChildrenTools.get(i);
            if (clickedToolObject instanceof EffectToolObject) {
                if (clickedToolObject.transform != null) {
                    doTransform(clickedToolObject.transform);
                }
            } else if (clickedToolObject instanceof OptimizeToolObject) {
                toolContainer.setDisplayedChild(OPTIMIZE_TOOL_PANEL);
                AbstractOptimizeTransformation optimizeTransformation = (AbstractOptimizeTransformation) clickedToolObject.transform;
                optimizeTransformation.setupTransformation(toolSeekBar, bmSource, transTask, onPerformedListener);
                //HIDE PROGRESS DIALOG
                // optimizeTransformation.setProgressDialog(processDialog);
            } else {
                toolContainer.setDisplayedChild(EFFECT_TOOL_PANEL);
                currentChildrenTools = clickedToolObject.childrenTools;
                EditorListToolsAdapter adapter = new EditorListToolsAdapter(EditorActivity.this, currentChildrenTools);
                listEffectTools.setAdapter(adapter);
                btnApply.setText(getString(R.string.editor_apply_button_text));
                editorState = EditorState.APPLYABLE;
            }
        }
    };

    @Override
    public void onBackPressed() {
        if (toolContainer.getDisplayedChild() == BASE_TOOL_PANEL) {
            super.onBackPressed();
        } else {
            btnApply.setText(getString(R.string.editor_btn_done_text));
            editorState = EditorState.DONEABLE;
            toolContainer.setDisplayedChild(BASE_TOOL_PANEL);
            if (transTask != null && !transTask.isCancelled()) {
                transTask.cancel(true);
            }
            currentChildrenTools = AppConfigs.getInstance().editorFeatures;
            imgPreview.setImageBitmap(bmSource);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
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
        AsyncTaskUtils.executeTask(openTask);

        //Open Thumb
        OpenBitmapAsyncTask loadThumbTask = new OpenBitmapAsyncTask.Builder(this, selectedPhoto)
                .setRequestSize(150, 150)
                .setBitmapOpenedListener(new OpenBitmapAsyncTask.OnBitmapOpenedListener() {
                    @Override
                    public void onBitmapOpened(Bitmap bm) {
                        bmThumb = bm;
                        renderTools();
                    }
                }).build();
        AsyncTaskUtils.executeTask(loadThumbTask);
    }

    public Bitmap getThumbnail() {
        return bmThumb;
    }

    public ITransformation getTransformation() {
        return this.transformation;
    }

    private DoTransformationAsyncTask.OnPerformedListener onPerformedListener = new DoTransformationAsyncTask.OnPerformedListener() {
        @Override
        public void onPerformed(Bitmap bm) {
            if (bmProcessed != null && !bmProcessed.isRecycled()) {
                bmProcessed.recycle();
                bmProcessed = null;
            }
            bmProcessed = bm;
            imgPreview.setImageBitmap(bmProcessed);
            editorState = EditorState.APPLYABLE;
            btnApply.setText(getString(R.string.editor_apply_button_text));
        }
    };

    public void doTransform(ITransformation transform) {

        this.transformation = transform;
        if (bmSource == null) {
            transTask = new DoTransformationAsyncTask(selectedPhoto, transform, processDialog, onPerformedListener);
            transTask.execute();
        } else {
            transTask = new DoTransformationAsyncTask(bmSource, transform, processDialog, onPerformedListener);
            transTask.execute();
        }
    }

    private View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btnApply:
                    if (toolContainer.getDisplayedChild() != BASE_TOOL_PANEL && editorState.equals(EditorState.APPLYABLE)) {
                        //APPLY PRESSED
                        btnApply.setText(getString(R.string.editor_btn_done_text));
                        toolContainer.setDisplayedChild(BASE_TOOL_PANEL);
                        currentChildrenTools = AppConfigs.getInstance().editorFeatures;
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
                                resultIntent.putExtra(OUTPUT_PATH, outputPath);
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

                case R.id.btnBack:
                    AlertDialog confirm = new AlertDialog.Builder(EditorActivity.this)
                            .setMessage("Are you sure to close editor?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    EditorActivity.this.finish();
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).create();
                    confirm.show();
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

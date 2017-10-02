package com.ss.photoeffectseditor.dialogs;


import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.GridView;
import android.widget.TextView;

import com.ss.photoeffectseditor.AppConfigs;
import com.ss.photoeffectseditor.R;

public class ShareIntentsDialog extends Dialog {
    private Context context;
    private GridView share_grid;
    private TextView dialog_title;
    private String sharePath;
    private String title_text;
    private boolean isShowDialogTitle;

    private ShareIntentsDialog(Context context) {
        super(context);
        this.context = context;
        isShowDialogTitle = true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.share_intents_dialog_layout);
        share_grid = (GridView) findViewById(R.id.list_share_intents);
        dialog_title = (TextView) findViewById(R.id.share_dialog_title);
        Typeface tf = Typeface.createFromAsset(context.getAssets(), AppConfigs.getInstance().TYPE_FACE);
        dialog_title.setTypeface(tf);
        ShareIntentsListAdapter adapter = new ShareIntentsListAdapter(context, this,
                this.sharePath);
        adapter.setTypeface(tf);
        share_grid.setAdapter(adapter);

        this.dialog_title.setText(title_text);
        if (!this.isShowDialogTitle) {
            this.dialog_title.setVisibility(View.GONE);
        }
    }

    public static class Builder {
        private String imagePath;
        private String title;
        private boolean isShowDialogTitle;
        private Context context;

        public Builder(Context context) {
            this.context = context;
            title = null;
            isShowDialogTitle = true;
            imagePath = null;
        }

        public Builder setImagePath(String path) {
            this.imagePath = path;
            return this;
        }

        public Builder setEnableShowDialogTitle(boolean enableShowTitle) {
            this.isShowDialogTitle = enableShowTitle;
            return this;
        }

        public Builder setDialogTitle(String title) {
            this.title = title;
            return this;
        }

        public ShareIntentsDialog build() {
            return new ShareIntentsDialog(this);
        }

    }

    private ShareIntentsDialog(Builder builder) {
        super(builder.context);
        this.context = builder.context;
        if (builder.title != null) {
            this.title_text = builder.title;

        }
        this.isShowDialogTitle = builder.isShowDialogTitle;

        this.sharePath = builder.imagePath;

    }
}

package com.example.emotionkeyboard.view;

import android.app.Activity;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.emotionkeyboard.R;

/**
 * Created by chasing on 2017/12/19.
 */
public class BaseActivity extends Activity {
    protected Dialog mLoadingDialog;

    protected void showLoading(String loadingContent) {
        if (isFinishing() || (mLoadingDialog != null && mLoadingDialog.isShowing())) return;
        if (mLoadingDialog == null) {
            View view = LayoutInflater.from(this).inflate(R.layout.loading_dialog, null);
            mLoadingDialog = new Dialog(this, R.style.Theme_AppCompat_Light_Dialog_Alert);
            mLoadingDialog.setContentView(view);
            Window window = mLoadingDialog.getWindow();
            if (window != null) {
                window.setBackgroundDrawableResource(android.R.color.transparent);
                WindowManager.LayoutParams attributes = window.getAttributes();
                attributes.width = (int) getResources().getDimension(R.dimen.dialog_width);
                window.setAttributes(attributes);
            }
        }
        TextView tvContent = (TextView) mLoadingDialog.findViewById(R.id.loading_content);
        tvContent.setText(loadingContent);
        mLoadingDialog.show();
    }

    protected void hideLoading() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing() && !isFinishing()) {
            mLoadingDialog.dismiss();
        }
    }
}

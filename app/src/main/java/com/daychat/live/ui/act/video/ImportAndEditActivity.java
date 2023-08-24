package com.daychat.live.ui.act.video;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.daychat.live.R;
import com.daychat.live.util.PermissionChecker;
import com.daychat.live.widget.ToastUtils;

public class ImportAndEditActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_and_edit);
    }

    public void onClickImport(View v) {
        if (isPermissionOK()) {
            jumpToActivity(VideoTrimActivity.class);
        }
    }

    public void onClickTransitionMake(View v) {
        if (isPermissionOK()) {
            jumpToActivity(VideoDivideActivity.class);
        }
    }

    public void onClickVideoCompose(View v) {
        if (isPermissionOK()) {
            jumpToActivity(VideoComposeActivity.class);
        }
    }

    public void onClickTranscode(View v) {
        if (isPermissionOK()) {
            jumpToActivity(VideoTranscodeActivity.class);
        }
    }

    public void onClickMultipleCompose(View v) {
        if (isPermissionOK()) {
            jumpToActivity(MultipleComposeActivity.class);
        }
    }

    public void onClickExternalMediaRecord(View v) {
        if (isPermissionOK()) {
//            jumpToActivity(ExternalMediaRecordActivity.class);
        }
    }

    private boolean isPermissionOK() {
        PermissionChecker checker = new PermissionChecker(this);
        boolean isPermissionOK = Build.VERSION.SDK_INT < Build.VERSION_CODES.M || checker.checkPermission();
        if (!isPermissionOK) {
            ToastUtils.showShortToast("Some permissions is not approved !!!");
        }
        return isPermissionOK;
    }

    private void jumpToActivity(Class<?> cls) {
        Intent intent = new Intent(ImportAndEditActivity.this, cls);
        startActivity(intent);
    }

    public void onClickBack(View view) {
        finish();
    }
}

package com.daychat.live.base;

import android.content.res.Resources;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Lifecycle;

import com.blankj.utilcode.util.AdaptScreenUtils;
import com.uber.autodispose.AutoDispose;
import com.uber.autodispose.AutoDisposeConverter;
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider;

public class BaseActivity extends AppCompatActivity {

    @Override
    public Resources getResources() {
        return AdaptScreenUtils.adaptWidth(super.getResources(),1290);
    }

    public <T> AutoDisposeConverter<T> bindAutoDispose() {
        return AutoDispose.autoDisposable(AndroidLifecycleScopeProvider
                .from(this, Lifecycle.Event.ON_DESTROY));
    }

}

package com.daychat.live.base;

import com.meiyinzb.nasinet.base.NasiBaseView;
import com.uber.autodispose.AutoDisposeConverter;

public  interface BaseView extends NasiBaseView {

    /**
     * 绑定Android生命周期 防止RxJava内存泄漏
     *
     * @param <T>
     * @return
     */
    <T> AutoDisposeConverter<T> bindAutoDispose();
}

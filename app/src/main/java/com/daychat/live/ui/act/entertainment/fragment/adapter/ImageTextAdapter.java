package com.daychat.live.ui.act.entertainment.fragment.adapter;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.QuickViewHolder;
import com.daychat.live.R;
import com.daychat.live.bean.PictureBean;

public class ImageTextAdapter extends BaseQuickAdapter<PictureBean, QuickViewHolder> {


    @Override
    protected void onBindViewHolder(@NonNull QuickViewHolder quickViewHolder, int i, @Nullable PictureBean pictureBean) {

    }

    @NonNull
    @Override
    protected QuickViewHolder onCreateViewHolder(@NonNull Context context, @NonNull ViewGroup viewGroup, int i) {
        return new QuickViewHolder(R.layout.item_image_text,viewGroup);
    }
}

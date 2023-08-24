package com.daychat.live.ui.adapter.chat;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.QuickViewHolder;
import com.daychat.live.R;
import net.csdn.roundview.RoundTextView;

public class TagsAdapter extends BaseQuickAdapter<String, QuickViewHolder> {

    private int [] colors;
    public TagsAdapter(int[] colors) {
        this.colors = colors;
    }




    @Override
    protected void onBindViewHolder(@NonNull QuickViewHolder quickViewHolder, int position, @Nullable String s) {
//        quickViewHolder.setText(R.id.tv_tag, s);
        RoundTextView tvTags = quickViewHolder.getView(R.id.tv_tag);
        tvTags.setText(s);
        tvTags.setBackgroundColor(colors[position % 8]);
    }

    @NonNull
    @Override
    protected QuickViewHolder onCreateViewHolder(@NonNull Context context, @NonNull ViewGroup viewGroup, int i) {
        return new QuickViewHolder(R.layout.item_tags,viewGroup);
    }
}

package com.daychat.live.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.QuickViewHolder;
import com.daychat.live.bean.MenuBean;
import com.daychat.live.R;
import com.daychat.live.ui.act.PartyActivity;

public class MenuAdapter extends BaseQuickAdapter<MenuBean, QuickViewHolder> {
    public MenuAdapter() {
    }


    @Override
    protected void onBindViewHolder(@NonNull QuickViewHolder quickViewHolder, int i, @Nullable MenuBean menuBean) {
//        ImageView iv = helper.getView(R.id.iv_icon);
//        Glide.with(iv.getContext()).load(item.getIcon()).into(iv);

        quickViewHolder.setText(R.id.tv_menu_name, menuBean.getName());
        quickViewHolder.itemView.setOnClickListener(v -> {
            switch (menuBean.getId()) {
                case 1:
                    getContext().startActivity(new Intent(getContext(), PartyActivity.class));
                    break;
            }
        });

    }

    @NonNull
    @Override
    protected QuickViewHolder onCreateViewHolder(@NonNull Context context, @NonNull ViewGroup viewGroup, int i) {
        return new QuickViewHolder(R.layout.item_menu, viewGroup);
    }
}

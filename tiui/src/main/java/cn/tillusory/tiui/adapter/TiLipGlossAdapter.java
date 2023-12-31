package cn.tillusory.tiui.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import cn.tillusory.sdk.TiSDKManager;
import cn.tillusory.sdk.bean.TiMakeup;
import cn.tillusory.tiui.R;

/**
 * Created by Anko on 2019-09-07.
 * Copyright (c) 2018-2020 拓幻科技 - tillusory.cn. All rights reserved.
 */
public class TiLipGlossAdapter extends RecyclerView.Adapter<TiMakeupItemViewHolder> {

    private int selectedPosition = 0;

    private final List<TiMakeup> list;
    private final TiSDKManager tiSDKManager;

    public TiLipGlossAdapter(List<TiMakeup> list, TiSDKManager tiSDKManager) {
        this.list = list;
        this.tiSDKManager = tiSDKManager;
    }

    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TiMakeupItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ti_makeup, parent, false);
        return new TiMakeupItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final TiMakeupItemViewHolder holder, int position) {
        if (position == 0) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) holder.itemView.getLayoutParams();
            p.setMargins((int) (holder.itemView.getContext().getResources().getDisplayMetrics().density * 15 + 0.5f),
                    0,
                    0,
                    0);
            holder.itemView.requestLayout();
        } else {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) holder.itemView.getLayoutParams();
            p.setMargins(0, 0, 0, 0);
            holder.itemView.requestLayout();
        }

        TiMakeup tiMakeup = list.get(holder.getAdapterPosition());

        //显示封面
        if (tiMakeup == TiMakeup.NO_MAKEUP) {
            holder.thumbIV.setImageResource(R.drawable.ic_ti_none);
            holder.nameTV.setText(R.string.makeup_no);
        } else {
//            Glide.with(holder.itemView.getContext())
//                    .load(list.get(position).getImagePath(holder.itemView.getContext(), TiMakeupEnum.LIP_GLOSS_MAKEUP))
//                    .into(holder.thumbIV);
            holder.nameTV.setText(list.get(position).getName());
        }

        holder.itemView.setSelected(selectedPosition == position);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedPosition = holder.getAdapterPosition();
                tiSDKManager.enableMakeup(true);
//                tiSDKManager.setLipGloss(list.get(holder.getAdapterPosition()).getName());
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }
}

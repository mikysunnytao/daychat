package com.daychat.live.ui.fragment.chat;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.blankj.utilcode.util.AdaptScreenUtils;
import com.daychat.live.R;
import com.daychat.live.base.BaseFragment;
import com.daychat.live.ui.adapter.chat.TagsAdapter;
import com.daychat.live.util.DpUtil;

import java.util.Arrays;

import butterknife.BindView;

public class ChatTagsFragment extends BaseFragment {

    @BindView(R.id.iv_back)
    ImageView ivBack;

    @BindView(R.id.rv_tags)
    RecyclerView rvTags;

    @BindView(R.id.rv_training)
    RecyclerView rvTraining;

    @BindView(R.id.rv_hobby)
    RecyclerView rvHobby;

    @BindView(R.id.rv_travel)
    RecyclerView rvTravel;

    @BindView(R.id.fl_search)
    FrameLayout flSearch;

    @BindView(R.id.fl_title)
    FrameLayout flTitle;

    private TagsAdapter tagsAdapter;

    @Override
    protected void lazyLoad() {

    }

    @Override
    protected void initView(View view) {
        ivBack.setOnClickListener(v->{
            hideMenu();
            int fragmentCount = getParentFragmentManager().getBackStackEntryCount();
            if (fragmentCount>0){
                getParentFragmentManager().popBackStack();
            }
        });
        flSearch.setOnClickListener(this::showSearchPop);
        int [] colors = new int[]{getContext().getResources().getColor(R.color.tag_color1), getContext().getResources().getColor(R.color.tag_color2), getContext().getResources().getColor(R.color.tag_color3), getContext().getResources().getColor(R.color.tag_color4), getContext().getResources().getColor(R.color.tag_color5), getContext().getResources().getColor(R.color.tag_color6), getContext().getResources().getColor(R.color.tag_color7), getContext().getResources().getColor(R.color.tag_color8)};
        String[] tags = new String[]{"# 한글", "# 슬램덩크", "# HIPHOP", "# 골프", "# 맛집", "# 물가", "# 화장법", "# 스위스여행", "# 한글", "# 한글"};
        tagsAdapter = new TagsAdapter(colors);
        tagsAdapter.setItems(Arrays.asList(tags));
        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.HORIZONTAL);
        rvTags.setLayoutManager(manager);
        rvTags.setAdapter(tagsAdapter);
        StaggeredGridLayoutManager manager1 = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.HORIZONTAL);
        rvHobby.setLayoutManager(manager1);
        tagsAdapter = new TagsAdapter(colors);
        rvHobby.setAdapter(tagsAdapter);
        tags = new String[]{"# 골프", "# 런닝","# 배드민턴", "# 야구", "# 축구", "# 테니스", "# 태권도", "# 농구", "# 스위스여행", "# 한글"};
        tagsAdapter.setItems(Arrays.asList(tags));
        StaggeredGridLayoutManager manager2 = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.HORIZONTAL);
        rvTraining.setLayoutManager(manager2);
        tagsAdapter = new TagsAdapter(colors);
        rvTraining.setAdapter(tagsAdapter);
        tags = new String[]{"# 국내여행", "# 태국","# 동경", "# 외화", "# 홍콩", "# 여행권", "# 스위스여행", "# 여행보험"};
        tagsAdapter.setItems(Arrays.asList(tags));
        StaggeredGridLayoutManager manager3 = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.HORIZONTAL);
        rvTravel.setLayoutManager(manager3);
        tagsAdapter = new TagsAdapter(colors);
        rvTravel.setAdapter(tagsAdapter);
        tags = new String[]{"# 국내여행", "# 태국","# 동경", "# 외화", "# 홍콩", "# 여행권", "# 스위스여행", "# 여행보험"};
        tagsAdapter.setItems(Arrays.asList(tags));
    }

    private void showSearchPop(View v) {
        PopupWindow popupWindow = new PopupWindow(getContext());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_search_popup, null);
        popupWindow.setContentView(view);
        popupWindow.setFocusable(true);
        popupWindow.setOnDismissListener(() -> {
            flTitle.setVisibility(View.VISIBLE);
//            ivSearch.setImageResource(R.mipmap.ic_search_black);
        });
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        int margin = AdaptScreenUtils.pt2Px(15);
        popupWindow.showAsDropDown(v, -DpUtil.dp2px(152),margin);
        flTitle.setVisibility(View.GONE);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_chat_tags;
    }
}

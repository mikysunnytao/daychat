package com.daychat.live.ui.fragment.chat;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.blankj.utilcode.util.AdaptScreenUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.daychat.live.R;
import com.daychat.live.base.BaseFragment;
import com.daychat.live.ui.act.HomeActivity;
import com.daychat.live.ui.adapter.chat.ConversationAdapter;
import com.daychat.live.ui.adapter.chat.TagsAdapter;
import com.daychat.live.util.DpUtil;
import com.qiniu.droid.media.Frame;
import com.tencent.imsdk.v2.V2TIMConversation;
import com.tencent.imsdk.v2.V2TIMConversationResult;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMMessage;
import com.tencent.imsdk.v2.V2TIMValueCallback;
import com.tencent.qcloud.tuikit.tuiconversation.bean.ConversationInfo;
import com.tencent.qcloud.tuikit.tuiconversation.classicui.page.TUIConversationFragment;
import com.tencent.qcloud.tuikit.tuiconversation.commonutil.ConversationUtils;

import net.csdn.roundview.RoundLinearLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;

public class ChatNavFragment extends BaseFragment {

    @BindView(R.id.iv_tag_nav)
    ImageView ivTagNav;

    @BindView(R.id.iv_hot_nav)
    ImageView ivHotNav;
    @BindView(R.id.rv_tags)
    RecyclerView rvTags;

    @BindView(R.id.rv_recommend)
    RecyclerView rv_recommend;

    @BindView(R.id.fl_back)
    FrameLayout flBack;

//    @BindView(R.id.rv_my_chats)
//    RecyclerView rvMyChats;

    @BindView(R.id.iv_show_my_chat)
    ImageView ivShowMyChat;

    @BindView(R.id.fl_chat_tag)
    FrameLayout flChatTag;

    @BindView(R.id.fl_my_chat_close)
    FrameLayout flMyChatClose;

    @BindView(R.id.fl_my_chat)
    RoundLinearLayout flMyChat;

    @BindView(R.id.fl_search)
    FrameLayout flSearch;

    @BindView(R.id.fl_title)
    FrameLayout flTitle;

    private int[] colors;
    private TagsAdapter tagsAdapter;

    private ConversationAdapter conversationAdapter;
    private ConversationAdapter myChatAdapter;


    @Override
    protected void lazyLoad() {

    }

    @Override
    protected void initView(View view) {
//        ivTagNav.setOnClickListener(v -> {
//
//
//        });

        flSearch.setOnClickListener(this::showSearchPop);

        ivHotNav.setOnClickListener(v -> {
            hideMenu();
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            String tag = HotChatFragment.class.getName();
            Fragment hotChatFragment = getChildFragmentManager().findFragmentByTag(tag);
            if (hotChatFragment == null) {
                hotChatFragment = new HotChatFragment();
            }
            transaction.replace(R.id.fl_chat, hotChatFragment);
            transaction.addToBackStack(tag);
            transaction.commit();
        });
        ivShowMyChat.setOnClickListener(v -> {
            hideMenu();
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            String tag = MyChatFragment.class.getName();
            Fragment myChatFragment = getChildFragmentManager().findFragmentByTag(tag);
            if (myChatFragment == null) {
                myChatFragment = new MyChatFragment();
            }
            transaction.replace(R.id.fl_content, myChatFragment);
            transaction.addToBackStack(tag);
            transaction.commit();
        });

        colors = new int[]{getContext().getResources().getColor(R.color.tag_color1), getContext().getResources().getColor(R.color.tag_color2), getContext().getResources().getColor(R.color.tag_color3), getContext().getResources().getColor(R.color.tag_color4), getContext().getResources().getColor(R.color.tag_color5), getContext().getResources().getColor(R.color.tag_color6), getContext().getResources().getColor(R.color.tag_color7), getContext().getResources().getColor(R.color.tag_color8)};
        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.HORIZONTAL);
        rvTags.setLayoutManager(manager);
        String[] tags = new String[]{"# 한글", "# 슬램덩크", "# HIPHOP", "# 골프", "# 맛집", "# 물가", "# 화장법", "# 스위스여행", "# 한글", "# 한글"};
        tagsAdapter = new TagsAdapter(colors);
        rvTags.setAdapter(tagsAdapter);
        tagsAdapter.setItems(Arrays.asList(tags));
        conversationAdapter = new ConversationAdapter();
        tagsAdapter.setOnItemClickListener((baseQuickAdapter, view1, i) -> {
            jumpTagPage();
        });
        flChatTag.setOnClickListener(v -> {
            jumpTagPage();
        });
        flBack.setOnClickListener(v -> {
            hideMenu();
            int count = getParentFragmentManager().getBackStackEntryCount();
            if (count > 0) {
                getParentFragmentManager().popBackStack();
            }
        });
        flMyChatClose.setOnClickListener(v -> {
            flMyChat.setVisibility(View.GONE);
        });
        LinearLayoutManager manager1 = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        rv_recommend.setLayoutManager(manager1);
        rv_recommend.setAdapter(conversationAdapter);
        ConversationInfo conversationBean = new ConversationInfo();
        List<Object> url1 = new ArrayList<>();
        url1.add(R.mipmap.header1);
        conversationBean.setIconUrlList(url1);
        conversationBean.setTitle("골프 레벨업을 위한 모임");
        V2TIMMessage message = new V2TIMMessage();
        message.setLocalCustomData("랠 시간괜찬으시면 골프치러 가요~!");
        conversationBean.setLastMessage(message);
        conversationAdapter.add(conversationBean);
        conversationBean = new ConversationInfo();
        url1 = new ArrayList<>();
        url1.add(R.mipmap.group_chat1);
        conversationBean.setIconUrlList(url1);
        conversationBean.setTitle("골프 레벨업을 위한 모임");
        message = new V2TIMMessage();
        message.setLocalCustomData("랠 시간괜찬으시면 골프치러 가요~!");
        conversationBean.setLastMessage(message);
        conversationAdapter.add(conversationBean);
        conversationBean = new ConversationInfo();
        url1 = new ArrayList<>();
        url1.add(R.mipmap.header6);
        conversationBean.setIconUrlList(url1);
        conversationBean.setTitle("골프 레벨업을 위한 모임");
        message = new V2TIMMessage();
        message.setLocalCustomData("랠 시간괜찬으시면 골프치러 가요~!");
        conversationBean.setLastMessage(message);
        conversationAdapter.add(conversationBean);
        LinearLayoutManager manager2 = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        TUIConversationFragment tuiConversationFragment = new TUIConversationFragment();
        FragmentManager manager3 = getChildFragmentManager();
        FragmentTransaction transaction = manager3.beginTransaction();
        transaction.add(R.id.fl_my_chats, tuiConversationFragment);
        transaction.commit();

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

    private void jumpTagPage() {
        FragmentManager tagManager = getParentFragmentManager();
        FragmentTransaction tagTransaction = tagManager.beginTransaction();
        String tag = ChatTagsFragment.class.getName();
        Fragment tagFragment = tagManager.findFragmentByTag(tag);
        if (tagFragment == null) {
            tagFragment = new ChatTagsFragment();
        }
        tagTransaction.replace(R.id.fl_content, tagFragment);
        tagTransaction.addToBackStack(tag);
        tagTransaction.commit();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_chat_nav;
    }


}

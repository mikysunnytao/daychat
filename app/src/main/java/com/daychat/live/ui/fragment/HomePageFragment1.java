package com.daychat.live.ui.fragment;

import android.view.View;
import android.widget.LinearLayout;

import com.daychat.live.R;
import com.daychat.live.base.BaseFragment;
import com.daychat.live.ui.act.HomeActivity;
import com.daychat.live.ui.fragment.chat.ChatNavFragment;
import com.jaeger.library.StatusBarUtil;

import butterknife.BindView;

public class HomePageFragment1 extends BaseFragment {

    private HomeMainFragment fragment1;

    @BindView(R.id.home_main)
    LinearLayout homeMain;

    @Override
    protected void lazyLoad() {
        fragment1 = new HomeMainFragment();
        String TAG = ChatNavFragment.class.getName();
        getChildFragmentManager().beginTransaction().add(R.id.fl_content, fragment1).commitAllowingStateLoss();

    }


    @Override
    protected void initView(View view) {
        StatusBarUtil.setDarkMode(getActivity());
//        requireActivity().getOnBackPressedDispatcher().addCallback(this,new OnBackPressedCallback(true) {
//            @Override
//            public void handleOnBackPressed() {
//
//                if (count>0){
//                    getChildFragmentManager().popBackStack();
//                    setEnabled(true);
//                }else {
////                    setEnabled(false);
//                }
//            }
//        });
        homeMain.setOnTouchListener((v,motion)->{
            ((HomeActivity)getActivity()).hideMenu();
            return true;
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home_page2;
    }

    public void reset() {
        getChildFragmentManager().beginTransaction().replace(R.id.fl_content, fragment1).commitAllowingStateLoss();
    }

    public void setUnReadNum(long unread) {
        fragment1.setUnReadNum(unread);
    }
}

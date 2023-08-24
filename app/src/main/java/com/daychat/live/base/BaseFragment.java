package com.daychat.live.base;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.daychat.live.R;
import com.daychat.live.ui.act.HomeActivity;
import com.daychat.live.ui.act.MenuWalletActivity;

import net.csdn.roundview.CircleImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author azheng
 * @date 2018/4/24.
 * GitHub：https://github.com/RookieExaminer
 * Email：wei.azheng@foxmail.com
 * Description：
 */
public abstract class BaseFragment extends DialogFragment {

    private Unbinder unBinder;
    public View view;
    protected boolean isInit = false;
    protected boolean isLoad = false;
    ImageView ivMenu;

    private CircleImageView ivHeader;


    public void hideMenu() {
        if (getActivity() instanceof HomeActivity) {
            ((HomeActivity) getActivity()).hideMenu();
        }
    }

    public BaseFragment() {
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) {
                parent.removeView(view);
            }
        }
        view = inflater.inflate(this.getLayoutId(), container, false);
        isInit = true;
        /**初始化的时候去加载数据**/
        unBinder = ButterKnife.bind(this, view);
        ivMenu = view.findViewById(R.id.iv_menu);
        ivHeader = view.findViewById(R.id.iv_header);
        if (ivHeader!=null && getActivity() instanceof HomeActivity){
            ivHeader.setOnClickListener(v->{
                startActivity(new Intent(getActivity(),MenuWalletActivity.class));
            });
        }
        if (ivMenu!=null){
            if (getActivity() instanceof HomeActivity) {
                ivMenu.setOnClickListener(v->{
                    ((HomeActivity)getActivity()).showMenu();
                });
            }
        }
        initView(view);
        isCanLoadData();

        return view;
    }

    /**
     * 视图是否已经对用户可见，系统的方法
     * 第一次对用户可见的时候,加载数据
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isCanLoadData();
    }

    /**
     * 是否可以加载数据
     * 可以加载数据的条件：
     * 1.视图已经初始化
     * 2.视图对用户可见
     */
    private void isCanLoadData() {
        if (!isInit) {
            return;
        }

        if (getUserVisibleHint()) {
            if (!isLoad) {
                lazyLoad();
                isLoad = true;
            }

        } else {
            if (isLoad) {
                stopLoad();
            }
        }
    }

    /**
     * 当视图已经对用户不可见并且加载过数据，如果需要在切换到其他页面时停止加载数据，可以覆写此方法
     */
    protected void stopLoad() {
    }

    /**
     * 当视图初始化并且对用户可见的时候去真正的加载数据
     */
    protected abstract void lazyLoad();

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unBinder != null && unBinder != Unbinder.EMPTY) {
            unBinder.unbind();
            unBinder = null;
        }
        isInit = false;
        isLoad = false;

    }

    /**
     * 初始化视图
     *
     * @param view
     */


    protected abstract void initView(View view);

    protected abstract int getLayoutId();

}

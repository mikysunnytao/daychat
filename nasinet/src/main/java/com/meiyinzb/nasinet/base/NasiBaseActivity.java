package com.meiyinzb.nasinet.base;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.meiyinzb.nasinet.R;

import butterknife.ButterKnife;
import butterknife.Unbinder;


public abstract class NasiBaseActivity extends AppCompatActivity {

    public TextView tv_title, tv_other;
    public RelativeLayout rl_back;
    public RelativeLayout rl_title;
    protected View rootView;

    private Unbinder unbinder;
    public LinearLayout root_view;
    public Context context;
    private boolean is_front = false;
    public ImageView iv_other;
    public int live_chat_h = 0;


    @Override
    protected void onPause() {
        super.onPause();
        is_front = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        is_front = true;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        context = this;
        setContentView(getLayoutId());
        rootView = View.inflate(this, R.layout.base_activity, null);
        addContent();
        setContentView(rootView);
        unbinder = ButterKnife.bind(this);

        initView();
        initData();
        if (!getRunningActivityName().contains("Splash")) {
            if (!NasiSDK.isIs_check()) {
                NasiSDK.getInstance().finish();
            }
        }

    }

    /**
     * 获取布局ID
     *
     * @return
     */
    protected abstract int getLayoutId();

    protected abstract void initData();

    protected abstract void initView();

    public void addContent() {
        root_view = rootView.findViewById(R.id.root_view);
        tv_title = rootView.findViewById(R.id.tv_title);
        rl_back = rootView.findViewById(R.id.rl_back);
        rl_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        rl_title = rootView.findViewById(R.id.rl_title);
        tv_other = rootView.findViewById(R.id.tv_other);
        iv_other = rootView.findViewById(R.id.iv_other);
        FrameLayout flContent = (FrameLayout) rootView.findViewById(R.id.fl_content);
        View content = View.inflate(this, getLayoutId(), null);
        if (content != null) {
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.MATCH_PARENT);
            flContent.addView(content, params);

        }

    }



    public void setTitle(String title) {
        tv_title.setText(title);
    }

    public void hideTitle(boolean hide) {
        if (hide) {
            rl_title.setVisibility(View.GONE);
        }
    }


    public void hideOther(boolean hide) {
        if (hide) {
            tv_other.setVisibility(View.GONE);
        } else {
            tv_other.setVisibility(View.VISIBLE);
        }
    }







    @Override
    protected void onDestroy() {
        unbinder.unbind();

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);

        super.onDestroy();
    }


    public String getRunningActivityName() {
        String contextString = this.toString();
        return contextString.substring(contextString.lastIndexOf(".") + 1, contextString.indexOf("@"));
    }



    @Override
    public Resources getResources() {
        Resources resources = super.getResources();
        Configuration configuration = new Configuration();
        configuration.setToDefaults();
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
        return resources;
    }
}

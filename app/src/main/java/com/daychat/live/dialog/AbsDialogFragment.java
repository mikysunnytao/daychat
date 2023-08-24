package com.daychat.live.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;



import com.daychat.live.ui.act.HomeActivity;
import com.meiyinzb.nasinet.utils.ClickUtil;



public abstract class AbsDialogFragment extends DialogFragment {

    protected Context mContext;
    protected View mRootView;


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mContext = getActivity();
        mRootView = LayoutInflater.from(mContext).inflate(getLayoutId(), null);
        Dialog dialog = new Dialog(mContext, getDialogStyle());
        dialog.setContentView(mRootView);
        dialog.setCancelable(canCancel());
        dialog.setCanceledOnTouchOutside(canCancel());
        setWindowAttributes(dialog.getWindow());
        return dialog;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (mContext instanceof HomeActivity) {
            ((HomeActivity) mContext).addDialogFragment(this);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    protected abstract int getLayoutId();

    protected abstract int getDialogStyle();

    protected abstract boolean canCancel();

    protected abstract void setWindowAttributes(Window window);

    protected boolean canClick() {
        return ClickUtil.canClick();
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
    }


}

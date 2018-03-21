package com.ejar.chapp.baselibrary.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.classic.common.MultipleStatusView;

/**
 * Created by Administrator on 2018\3\5 0005.
 */

public abstract class BaseActivity extends AppCompatActivity {
    private MultipleStatusView statusView = null;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        initView();
        startGetData();
        initListener();
    }

    private void initListener() {
        if (statusView != null) {
            statusView.setOnClickListener(mRetryClickListener);
        }
    }


    View.OnClickListener mRetryClickListener = v -> {
        startGetData();
    };

    protected abstract void startGetData();

    protected abstract void initView();

    protected abstract int getLayoutId();
}

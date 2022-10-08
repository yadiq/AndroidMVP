package com.hqumath.androidmvp.base;

import android.app.ProgressDialog;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * ****************************************************************
 * 文件名称: BaseActivity
 * 作    者: Created by gyd
 * 创建时间: 2019/1/21 15:12
 * 文件描述:
 * 注意事项:
 * 版权声明:
 * ****************************************************************
 */
public abstract class BaseActivity extends AppCompatActivity {
    protected BaseActivity mContext;
    private ProgressDialog mProgressDialog;//loaidng

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(initContentView(savedInstanceState));
        //事件监听
        initListener();
        //初始化数据
        initData();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //Density.setAppOrientation(this);//横竖屏切换后，重新设置density
    }

    protected abstract View initContentView(Bundle savedInstanceState);

    protected abstract void initListener();

    protected abstract void initData();

    protected void showProgressDialog(String content) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(mContext);
            mProgressDialog.setCancelable(true);
        }
        mProgressDialog.setMessage(content);
        mProgressDialog.show();
    }

    protected void dismissProgressDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }
}

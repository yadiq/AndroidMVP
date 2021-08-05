package com.hqumath.androidmvp.base;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.hqumath.androidmvp.app.AppManager;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

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
public abstract class BaseActivity extends RxAppCompatActivity {
    protected BaseActivity mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(initContentView());
        //初始化状态栏样式
        //initImmersionBar();
        //初始化ui
        //initView(savedInstanceState);
        //事件监听
        initListener();
        //初始化数据
        initData();
    }

    public abstract View initContentView();

    //public abstract void initView(Bundle savedInstanceState);

    protected abstract void initListener();

    protected abstract void initData();

    //沉浸式状态栏，默认文字黑色
    /*protected void initImmersionBar() {
        ImmersionBar.with(this)
                .reset()
                .statusBarDarkFont(true, 0.2f)
                .init();
        //文字白色
        //ImmersionBar.with(mContext).reset().init();
    }*/

    protected void toast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }
}

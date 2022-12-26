package com.hqumath.androidmvp.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.hqumath.androidmvp.R;
import com.hqumath.androidmvp.app.AppManager;
import com.hqumath.androidmvp.app.Constant;
import com.hqumath.androidmvp.base.BaseActivity;
import com.hqumath.androidmvp.databinding.ActivityMultilanguageBinding;
import com.hqumath.androidmvp.databinding.LayoutTitleBinding;
import com.hqumath.androidmvp.ui.login.LoginActivity;
import com.hqumath.androidmvp.utils.LanguageUtil;
import com.hqumath.androidmvp.utils.SPUtil;

import java.util.Locale;

public class MultiLanguageActivity extends BaseActivity {

    private ActivityMultilanguageBinding binding;
    private LayoutTitleBinding titleBinding;
    private SPUtil sp = SPUtil.getInstance();

    @Override
    protected View initContentView(Bundle savedInstanceState) {
        binding = ActivityMultilanguageBinding.inflate(LayoutInflater.from(this));
        titleBinding = binding.includeTitle;
        return binding.getRoot();
    }

    @Override
    protected void initListener() {
        titleBinding.tvTitle.setText("MultiLanguage");
        titleBinding.ivBack.setOnClickListener(v -> finish());
        binding.btnConfirm.setOnClickListener(v -> {
            int checkedId = binding.rgLanguage.getCheckedRadioButtonId();
             if (checkedId == R.id.rbLanguageZh) {//简体中文
                LanguageUtil.changeAppLanguage(mContext, new Locale("zh", "CN"), true);
            } else if (checkedId == R.id.rbLanguageEn) {//英文
                LanguageUtil.changeAppLanguage(mContext, new Locale("en", "US"), true);
            }
            //重启app
            AppManager.getInstance().clear();
            mContext.startActivity(new Intent(mContext, LoginActivity.class));
        });
    }

    @Override
    protected void initData() {
        String language = sp.getString(Constant.SP_LANGUAGE, LanguageUtil.DEFAULT_LANGUAGE);
        switch (language) {
            case "zh":
                binding.rbLanguageZh.setChecked(true);
                break;
            case "en":
                binding.rbLanguageEn.setChecked(true);
                break;
        }
    }
}

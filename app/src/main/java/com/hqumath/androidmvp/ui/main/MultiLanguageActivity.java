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
import com.hqumath.androidmvp.utils.MultiLanguageUtil;
import com.hqumath.androidmvp.utils.SPUtil;

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
            if (checkedId == R.id.rbLanguageEnglish) {
                MultiLanguageUtil.getInstance().updateLanguage(this, MultiLanguageUtil.LanguageType.LANGUAGE_EN);
            } else if (checkedId == R.id.rbLanguageChineseSim) {
                MultiLanguageUtil.getInstance().updateLanguage(this, MultiLanguageUtil.LanguageType.LANGUAGE_CHINESE_SIMPLIFIED);
            } else if (checkedId == R.id.rbLanguageChineseTra) {
                MultiLanguageUtil.getInstance().updateLanguage(this, MultiLanguageUtil.LanguageType.LANGUAGE_CHINESE_TRADITIONAL);
            }
            //重启app
            AppManager.getInstance().clear();
            mContext.startActivity(new Intent(mContext, LoginActivity.class));
        });
    }

    @Override
    protected void initData() {
        int languageType = sp.getInt(Constant.LANGUAGE, MultiLanguageUtil.LanguageType.LANGUAGE_EN);
        switch (languageType) {
            case MultiLanguageUtil.LanguageType.LANGUAGE_EN:
                binding.rbLanguageEnglish.setChecked(true);
                break;
            case MultiLanguageUtil.LanguageType.LANGUAGE_CHINESE_SIMPLIFIED:
                binding.rbLanguageChineseSim.setChecked(true);
                break;
            case MultiLanguageUtil.LanguageType.LANGUAGE_CHINESE_TRADITIONAL:
                binding.rbLanguageChineseTra.setChecked(true);
                break;
        }
    }
}

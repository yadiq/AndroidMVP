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
            if (checkedId == R.id.rbLanguageAuto) {//跟随系统
                //获取手机系统语言
                Locale locale = LanguageUtil.getSystemLanguage().get(0);
                String language = locale.getLanguage();
                String country = locale.getCountry();
                //切换成手机系统语言  例：手机系统是中文则换成中文
                LanguageUtil.changeLanguage(mContext, language, country);
                //清空SP数据 ，用于当系统切换语言时 应用可以同步保持切换 例：系统转换成英文 则应用语言也会变成英文
                LanguageUtil.changeLanguage(mContext, null, null);
            } else if (checkedId == R.id.rbLanguageZh) {//简体中文
                LanguageUtil.changeLanguage(mContext, "zh", "CN");
            } else if (checkedId == R.id.rbLanguageEn) {//英文
                LanguageUtil.changeLanguage(mContext, "en", "US");
            }//繁体中文 zh TW

            //重启app
            AppManager.getInstance().clear();
            mContext.startActivity(new Intent(mContext, LoginActivity.class));
        });
    }

    @Override
    protected void initData() {
        String language = sp.getString(Constant.SP_LANGUAGE, "");
        switch (language) {
            case "zh":
                binding.rbLanguageZh.setChecked(true);
                break;
            case "en":
                binding.rbLanguageEn.setChecked(true);
                break;
            default:
                binding.rbLanguageAuto.setChecked(true);
                break;
        }
    }
}

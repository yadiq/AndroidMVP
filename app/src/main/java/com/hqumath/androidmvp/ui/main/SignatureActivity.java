package com.hqumath.androidmvp.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.hqumath.androidmvp.base.BaseActivity;
import com.hqumath.androidmvp.databinding.ActivitySignatureBinding;
import com.hqumath.androidmvp.databinding.LayoutTitleBinding;

public class SignatureActivity extends BaseActivity {

    private ActivitySignatureBinding binding;
    private LayoutTitleBinding titleBinding;


    @Override
    protected View initContentView(Bundle savedInstanceState) {
        binding = ActivitySignatureBinding.inflate(LayoutInflater.from(this));
        titleBinding = binding.includeTitle;
        return binding.getRoot();
    }

    @Override
    protected void initListener() {
        titleBinding.tvTitle.setText("Signature");
        titleBinding.ivBack.setOnClickListener(v -> finish());
    }

    @Override
    protected void initData() {

    }
}

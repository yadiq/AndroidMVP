package com.hqumath.androidmvp.ui.main;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.hqumath.androidmvp.base.BaseActivity;
import com.hqumath.androidmvp.databinding.ActivitySignatureBinding;
import com.hqumath.androidmvp.databinding.LayoutTitleBinding;
import com.hqumath.androidmvp.utils.ImageUtil;

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
        binding.btnConfirm.setOnClickListener(v -> {
            Bitmap bitmap = binding.vHandWritingBoard.getBitmap();
            String base64 = ImageUtil.imageToBase64(bitmap, 80);
            Intent intent = new Intent();
            intent.putExtra("result", base64);
            setResult(RESULT_OK, intent);
            finish();
        });
        binding.btnRedo.setOnClickListener(v -> binding.vHandWritingBoard.redo());
        binding.btnUndo.setOnClickListener(v -> binding.vHandWritingBoard.undo());
    }

    @Override
    protected void initData() {

    }
}

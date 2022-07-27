package com.hqumath.androidmvp.ui.main;

import static com.hqumath.androidmvp.utils.CommonUtil.toast;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.hqumath.androidmvp.app.Constant;
import com.hqumath.androidmvp.base.BaseFragment;
import com.hqumath.androidmvp.databinding.FragmentSettingsBinding;
import com.hqumath.androidmvp.service.UpdateService;
import com.hqumath.androidmvp.ui.fileupdown.FileUpDownActivity;
import com.hqumath.androidmvp.ui.login.LoginActivity;
import com.hqumath.androidmvp.utils.DialogUtil;
import com.hqumath.androidmvp.utils.ImageUtil;
import com.hqumath.androidmvp.utils.SPUtil;

/**
 * ****************************************************************
 * 文件名称: SettingsFragment
 * 作    者: Created by gyd
 * 创建时间: 2019/11/5 10:06
 * 文件描述:
 * 注意事项:
 * 版权声明:
 * ****************************************************************
 */
public class SettingsFragment extends BaseFragment {
    private static final int REQUEST_SIGNATURE = 0x1;//签名返回base64

    private FragmentSettingsBinding binding;

    @Override
    protected View initContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    protected void initListener() {
        binding.fileUpDown.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, FileUpDownActivity.class);
            startActivity(intent);
        });
        binding.vCheckUpdate.setOnClickListener(v -> {
            //检查升级
            /*boolean needUpdate = new Random().nextBoolean();
            if (!needUpdate) {
                toast("已是最新版本");
                return;
            }*/
            SPUtil.getInstance().put(Constant.APK_URL, FileUpDownActivity.url);
            SPUtil.getInstance().put(Constant.APK_NAME, "AndroidMVP V2.0");
            DialogUtil alterDialogUtils = new DialogUtil(mContext);
            alterDialogUtils.setTitle("新版本V2.0");
            alterDialogUtils.setMessage("适配 Android 11");
            alterDialogUtils.setTwoConfirmBtn("立即更新", v1 -> {
                toast("已在后台下载");
                mContext.startService(new Intent(mContext, UpdateService.class));
            });
            alterDialogUtils.setTwoCancelBtn("下次提醒", v2 -> {
            });
            alterDialogUtils.setCancelable(false);
            alterDialogUtils.show();
        });
        binding.vSignature.setOnClickListener(v -> {//签名返回base64
            Intent intent = new Intent(mContext, SignatureActivity.class);
            startActivityForResult(intent, REQUEST_SIGNATURE);
        });
        binding.vLogout.setOnClickListener(v -> {
            SPUtil.getInstance().clear();
            startActivity(new Intent(mContext, LoginActivity.class));
            mContext.finish();
        });
    }

    @Override
    protected void initData() {
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 扫描二维码/条码回传
        if (requestCode == REQUEST_SIGNATURE && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                String base64 = data.getStringExtra("result");
                Bitmap bitmap = ImageUtil.base64ToImage(base64);
                binding.ivSignature.setImageBitmap(bitmap);
            }
        }
    }
}
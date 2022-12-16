package com.hqumath.androidmvp.ui.fileupdown;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.hqumath.androidmvp.base.BaseActivity;
import com.hqumath.androidmvp.databinding.ActivityFileupdownBinding;
import com.hqumath.androidmvp.utils.CommonUtil;
import com.hqumath.androidmvp.utils.FileUtil;
import com.hqumath.androidmvp.utils.PermissionUtil;
import com.hqumath.androidmvp.widget.DownloadingDialog;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.runtime.Permission;

import java.io.File;
import java.io.InputStream;

public class FileUpDownActivity extends BaseActivity implements FileUpDownPresenter.Contract {
    private final int REQUEST_PIC = 0x01;//相册选图

    private ActivityFileupdownBinding binding;
    private FileUpDownPresenter mPresenter;
    private DownloadingDialog mDownloadingDialog;

    public final static String url = "http://cps.yingyonghui.com/cps/yyh/channel/ac.union.m2/com.yingyonghui.market_1_30063293.apk";

    @Override
    public View initContentView(Bundle savedInstanceState) {
        binding = ActivityFileupdownBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }


    @Override
    protected void initListener() {
        binding.btnDownload.setOnClickListener(v -> {
            mPresenter.download(url);
        });
        binding.btnUpload.setOnClickListener(v -> {
            File file = FileUtil.getFileFromUrl(url);
            if (file.exists()) {
                mPresenter.upload("testFile", file);
            } else {
                CommonUtil.toast("文件不存在，请先下载");
            }
        });
        binding.btnSelectPic.setOnClickListener(v -> {//读取相册图片，裁剪，上传
            AndPermission.with(mContext)
                    .runtime()
                    .permission(Permission.READ_EXTERNAL_STORAGE)
                    .onGranted((permissions) -> {
                        //api >= 19
                        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(intent, REQUEST_PIC);
                    })
                    .onDenied((permissions) -> {
                        if (AndPermission.hasAlwaysDeniedPermission(mContext, permissions)) {
                            PermissionUtil.showSettingDialog(mContext, permissions);
                        }
                    }).start();
        });
    }

    @Override
    protected void initData() {
        mPresenter = new FileUpDownPresenter();
        mPresenter.attachView(this);
    }

    @Override
    protected void onDestroy() {
        if (mPresenter != null) {
            mPresenter.detachView();
            mPresenter = null;
        }
        hideProgress();
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_PIC) {//相册
                if (data != null) {
                    Uri uri = data.getData();
                    if (uri != null) {
                        InputStream inputStream = null;
                        try {
                            inputStream = getContentResolver().openInputStream(uri);
                            Bitmap mBitmap = BitmapFactory.decodeStream(inputStream);
                            binding.ivPic.setImageBitmap(mBitmap);
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            FileUtil.closeStream(inputStream);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onDownloadSuccess(Object object) {
        String fileName = ((File) object).getName();
        CommonUtil.toast(fileName + "Download success.");
        //安装
        AndPermission.with(mContext).install().file((File) object).rationale(PermissionUtil::showInstallDialog)//授权安装app弹窗
                .onGranted(null).onDenied(null).start();
    }

    @Override
    public void onDownloadError(String errorMsg, String code) {
        CommonUtil.toast(errorMsg);
    }

    @Override
    public void showProgress() {
        showProgressDialog("loading");
    }

    @Override
    public void hideProgress() {
        dismissProgressDialog();
    }

    @Override
    public void onUploadSuccess(Object object) {
        CommonUtil.toast("Upload success.");
    }

    @Override
    public void onUploadError(String errorMsg, String code) {
        CommonUtil.toast(errorMsg);
    }

    @Override
    public void showDownloadProgress() {
        if (mDownloadingDialog == null) {
            mDownloadingDialog = new DownloadingDialog(this);
        }
        mDownloadingDialog.show();
    }

    @Override
    public void hideDownloadProgress() {
        if (mDownloadingDialog != null) {
            mDownloadingDialog.dismiss();
        }
    }

    @Override
    public void updateProgress(long readLength, long countLength) {
        binding.getRoot().post(() -> {
            if (mDownloadingDialog != null && mDownloadingDialog.isShowing()) {
                mDownloadingDialog.setProgress(readLength, countLength);
            }
        });
    }

}

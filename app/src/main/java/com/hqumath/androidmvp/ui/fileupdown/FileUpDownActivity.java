package com.hqumath.androidmvp.ui.fileupdown;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
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
    private final int REQUEST_CROP = 0x02;//图片裁剪
    public final static String url = "http://cps.yingyonghui.com/cps/yyh/channel/ac.union.m2/com.yingyonghui.market_1_30063293.apk";

    private ActivityFileupdownBinding binding;
    private FileUpDownPresenter mPresenter;
    private DownloadingDialog mDownloadingDialog;

    private Uri cropUri;//剪裁时临时文件

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
            switch (requestCode) {
                case REQUEST_PIC://相册选图
                    if (data != null && data.getData() != null) {
                        //裁剪图片
                        cropPhoto(data.getData(), false);
                        //显示图片
                        /*InputStream inputStream = null;
                        try {
                            inputStream = getContentResolver().openInputStream(data.getData());
                            Bitmap mBitmap = BitmapFactory.decodeStream(inputStream);
                            binding.ivPic.setImageBitmap(mBitmap);
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            FileUtil.closeStream(inputStream);
                        }*/
                    }
                    break;
                case REQUEST_CROP://图片裁剪
                    //String picturePath = FileUtil.getRealFilePath(mContext, cropUri);
                    //显示图片
                    InputStream inputStream = null;
                    try {
                        inputStream = getContentResolver().openInputStream(cropUri);
                        Bitmap mBitmap = BitmapFactory.decodeStream(inputStream);
                        //File file = StringUtils.compressImage(mBitmap, 50);//图片压缩
                        binding.ivPic.setImageBitmap(mBitmap);
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        FileUtil.closeStream(inputStream);
                    }
                    break;
            }
            if (requestCode == REQUEST_PIC) {//相册

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

    private void cropPhoto(Uri srcUri, boolean fromCamera) {
        //剪裁时临时文件
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {//android10分区存储
            ContentValues values = new ContentValues(2);
            values.put(MediaStore.Images.Media.DISPLAY_NAME, "photo_" + System.currentTimeMillis());
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {//SD卡是否可用
                cropUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                cropUri = getContentResolver().insert(MediaStore.Images.Media.INTERNAL_CONTENT_URI, values);
            }
        } else {
            File cropFile;
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {//SD卡是否可用
                cropFile = FileUtil.getExternalCacheFile("crop.jpg");
            } else {
                cropFile = FileUtil.getCacheFile("crop.jpg");
            }
            if (cropFile.exists()) {
                cropFile.delete();
            }
            cropUri = Uri.fromFile(cropFile);
        }

        Intent intent = new Intent("com.android.camera.action.CROP");
        if (fromCamera) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);//授予临时读写权限
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            }
        }
        intent.setDataAndType(srcUri, "image/*");
        intent.putExtra("corp", "true");//是否可裁剪
        intent.putExtra("aspectX", 3);//X方向比例
        intent.putExtra("aspectY", 2);//Y方向比例
        intent.putExtra("outputX", 750);//裁剪区的宽
        intent.putExtra("outputY", 500);//裁剪区的高
        intent.putExtra(MediaStore.EXTRA_OUTPUT, cropUri);//表明这是一个存储动作
        intent.putExtra("return-data", false);//是否将数据保留在Bitmap中返回
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());//输出格式，一般设为Bitmap格式及图片类型
        intent.putExtra("noFaceDetection", true);//是否去除面部检测
        startActivityForResult(intent, REQUEST_CROP);
    }
}

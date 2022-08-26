package com.hqumath.androidmvp.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.hqumath.androidmvp.R;
import com.hqumath.androidmvp.databinding.DialogCommonBinding;

/**
 * ****************************************************************
 * 文件名称: AlterDialogTool
 * 作    者: Created by gyd
 * 创建时间: 2018/10/28 23:01
 * 文件描述: 通用弹窗界面
 * 注意事项:
 * 版权声明:
 * ****************************************************************
 */

public class DialogUtil extends Dialog {
    private Context mContext;
    private DialogCommonBinding binding;

    //ex.
        /*DialogUtil alterDialogUtils = new DialogUtil(mContext);
        alterDialogUtils.setTitle("提示");
        alterDialogUtils.setMessage("是否确认退出驾驶？");
        alterDialogUtils.setTwoConfirmBtn("确定", v -> {});
        alterDialogUtils.setTwoCancelBtn("取消", v -> {});
        alterDialogUtils.show();*/

    /*
     * 默认主要操作弹窗
     */
    public DialogUtil(Context context) {
        this(context, R.style.dialog_common, R.layout.dialog_common);//根布局会被改为自适应宽高,居中
    }

    public DialogUtil(Context context, int theme, int messageLayout) {
        super(context, theme);
        this.mContext = context;
        binding = DialogCommonBinding.inflate(LayoutInflater.from(mContext));
        setContentView(binding.getRoot());
        //根布局为自适应宽高，有软键盘时必须全屏，否则mate40等手机软键盘无法上推
        /*Window window = getWindow();
        if (window != null) {
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }*/
    }

    public void setTitle(int resId) {
        binding.tvTitle.setText(resId);
    }

    public void setTitle(String title) {
        binding.tvTitle.setText(title);
    }

    public void setMessage(int resId) {
        binding.tvMessage.setText(resId);
    }

    public void setMessage(String message) {
        binding.tvMessage.setText(message);
    }

    /**
     * 仅显示确定按钮
     *
     * @param text
     * @param listener
     */
    public void setOneConfirmBtn(String text, View.OnClickListener listener) {
        setOneOrTwoBtn(true);
        if (text != null) {
            binding.btnOneYes.setText(text);
        }
        binding.btnOneYes.setOnClickListener(v -> {
            dismiss();
            if (listener != null)
                listener.onClick(v);
        });
    }

    public void setTwoConfirmBtn(String text, View.OnClickListener listener) {
        setOneOrTwoBtn(false);
        if (text != null) {
            binding.btnYes.setText(text);
        }
        binding.btnYes.setOnClickListener(v -> {
            dismiss();
            if (listener != null)
                listener.onClick(v);
        });
    }

    public void setTwoCancelBtn(String text, View.OnClickListener listener) {
        setOneOrTwoBtn(false);
        if (text != null) {
            binding.btnNo.setText(text);
        }
        binding.btnNo.setOnClickListener(v -> {
            dismiss();
            if (listener != null)
                listener.onClick(v);
        });
    }

    /**
     * 设置按键类型
     *
     * @param one true 只有一个确认按键 ； false 显示 确认 和取消 按键
     */
    private void setOneOrTwoBtn(boolean one) {
        if (one) {
            binding.btnOneYes.setVisibility(View.VISIBLE);
            binding.llTwo.setVisibility(View.GONE);
        } else {
            binding.btnOneYes.setVisibility(View.GONE);
            binding.llTwo.setVisibility(View.VISIBLE);
        }
    }
}

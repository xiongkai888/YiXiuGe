package com.medui.yixiu.utils;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.zxing.WriterException;
import com.medui.yixiu.R;
import com.medui.yixiu.helper.SimpleTextWatcher;
import com.medui.yixiu.webviewpage.FileUtils;
import com.medui.yixiu.widget.ChangePhoneView;
import com.xson.common.utils.StringUtils;
import com.xson.common.utils.UIBaseUtils;
import com.xson.common.utils.UIHelper;
import com.yzq.zxinglibrary.encode.CodeCreator;

/**
 * Dialog工具类
 * Created by benio on 2015/10/11.
 */
public class AKDialog {

    public static AlertDialog.Builder getDialog(Context context) {
        return new AlertDialog.Builder(context);
    }

    public static ProgressDialog getProgressDialog(Context context, String message) {
        ProgressDialog waitDialog = new ProgressDialog(context);
        if (!TextUtils.isEmpty(message)) {
            waitDialog.setMessage(message);
        }
        return waitDialog;
    }

    /**
     * 提示信息Dialog
     */
    public static AlertDialog.Builder getMessageDialog(Context context, String msg) {
        return getMessageDialog(context, null, msg, null);
    }

    /**
     * 提示信息Dialog
     */
    public static AlertDialog.Builder getMessageDialog(Context context, String title, String msg) {
        return getMessageDialog(context, title, msg, null);
    }

    /**
     * 提示信息Dialog
     */
    public static AlertDialog.Builder getMessageDialog(Context context, String msg, DialogInterface.OnClickListener okListener) {
        return getMessageDialog(context, null, msg, okListener);
    }

    /**
     * 提示信息Dialog
     */
    public static AlertDialog.Builder getMessageDialog(Context context, String title, String msg, DialogInterface.OnClickListener okListener) {

        return getMessageDialog(context, title, msg, R.string.sure ,okListener);
    }

    /**
     * 提示信息Dialog
     */
    public static AlertDialog.Builder getMessageDialog(Context context, String title, String msg, int id, DialogInterface.OnClickListener okListener) {
        AlertDialog.Builder builder = getDialog(context);
        if (!TextUtils.isEmpty(title)) {
            builder.setTitle(title);
        }
        if (!TextUtils.isEmpty(msg)) {
            builder.setMessage(msg);
        }
        builder.setPositiveButton(id, okListener);
        return builder;
    }

    /**
     * 确认对话框
     */
    public static AlertDialog.Builder getConfirmDialog(Context context, String msg,
                                                       DialogInterface.OnClickListener okListener) {
        return getConfirmDialog(context, null, msg, okListener, null);
    }

    /**
     * 确认对话框
     */
    public static AlertDialog.Builder getConfirmDialog(Context context, String title, String msg,
                                                       DialogInterface.OnClickListener okListener) {
        return getConfirmDialog(context, title, msg, okListener, null);
    }

    /**
     * 确认对话框
     */
    public static AlertDialog.Builder getConfirmDialog(Context context, String title, String msg,
                                                       DialogInterface.OnClickListener okListener, DialogInterface.OnClickListener cancelListener) {
        AlertDialog.Builder builder = getDialog(context);
        if (!TextUtils.isEmpty(title)) {
            builder.setTitle(title);
        }
        if (!TextUtils.isEmpty(msg)) {
            builder.setMessage(msg);
        }
        builder.setPositiveButton(R.string.sure, okListener);
        builder.setNegativeButton(R.string.cancel, cancelListener);
        return builder;
    }


    /**
     * 列表对话框
     */
    public static AlertDialog.Builder getSelectDialog(Context context, String[] arrays, DialogInterface.OnClickListener onClickListener) {
        return getSelectDialog(context, null, arrays, onClickListener);
    }

    /**
     * 列表对话框
     */
    public static AlertDialog.Builder getSelectDialog(Context context, String title,
                                                      String[] arrays, DialogInterface.OnClickListener onClickListener) {
        AlertDialog.Builder builder = getDialog(context);
        builder.setItems(arrays, onClickListener);
        if (!TextUtils.isEmpty(title)) {
            builder.setTitle(title);
        }
        return builder;
    }

    /**
     * 单选对话框
     */
    public static AlertDialog.Builder getSingleChoiceDialog(Context context, String[] arrays,
                                                            int selectIndex, DialogInterface.OnClickListener onClickListener) {
        return getSingleChoiceDialog(context, null, arrays, selectIndex, onClickListener);
    }

    /**
     * 单选对话框
     */
    public static AlertDialog.Builder getSingleChoiceDialog(Context context, String title, String[] arrays,
                                                            int selectIndex, DialogInterface.OnClickListener onClickListener) {
        AlertDialog.Builder builder = getDialog(context);
        builder.setSingleChoiceItems(arrays, selectIndex, onClickListener);
        if (!TextUtils.isEmpty(title)) {
            builder.setTitle(title);
        }
        // builder.setNegativeButton("取消", null);
        return builder;
    }

    /**
     * 拍照、选择相册底部弹框提示
     *
     * @param context
     * @param activity
     * @param listener
     */
    public static void showBottomListDialog(Context context, Activity activity, final AlbumDialogListener listener) {
        final Dialog dialog = new Dialog(context, R.style.ActionSheetDialogStyle);
        View inflate = LayoutInflater.from(context).inflate(R.layout.album_dialog_layout, null);
        Button choosePhoto = (Button) inflate.findViewById(R.id.choosePhoto);
        Button takePhoto = (Button) inflate.findViewById(R.id.takePhoto);
        Button cancel = (Button) inflate.findViewById(R.id.btn_cancel);

        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {//拍照
                    listener.photograph();
                }
                dialog.cancel();
            }
        });
        choosePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//相册
                if (listener != null) {
                    listener.photoAlbum();
                }
                dialog.cancel();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//取消
                dialog.cancel();
            }
        });
        dialog.setContentView(inflate);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        WindowManager m = activity.getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
//        lp.y = 20;
        lp.width = (int) (d.getWidth()); // 宽度设置为屏幕的0.8
        dialogWindow.setAttributes(lp);
        dialog.show();
    }

    public interface AlbumDialogListener {

        void photograph();//拍照

        void photoAlbum();//相册
    }


    public static void getAlertDialog(Context context, String content, DialogInterface.OnClickListener l) {
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setMessage(content)
                .setPositiveButton(R.string.sure, l)
                .setNegativeButton(R.string.cancel, null)
                .setCancelable(false).create();
        dialog.show();
    }

    public static void evaluateDialog(final Context context, String who, final EvaluateListener l) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = View.inflate(context, R.layout.dialog_evaluate, null);
        builder.setView(view);
        builder.setCancelable(true);
        final AlertDialog dialog = builder.create();
        final EditText replyEt = (EditText) view.findViewById(R.id.reply_et);//回复内容
        TextView evaluateTv = (TextView) view.findViewById(R.id.evaluate_tv);//XX评价
        evaluateTv.setText(who);
        final TextView numTv = (TextView) view.findViewById(R.id.num_tv);//字数
        final RatingBar ratingBar = (RatingBar) view.findViewById(R.id.rating_bar);//字数
        TextView yes = (TextView) view.findViewById(R.id.yes_tv);
        TextView no = (TextView) view.findViewById(R.id.no_tv);
        replyEt.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (StringUtils.isEmpty(s)) {
                    numTv.setText(String.format(context.getString(R.string.num_120), "0"));
                } else {
                    numTv.setText(String.format(context.getString(R.string.num_120), s.length() + ""));
                }
            }
        });
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = CommonUtils.getStringByEditText(replyEt);
                if (StringUtils.isEmpty(content)) {
                    UIHelper.ToastMessage(context, R.string.input_evaluate_content);
                    return;
                }
                if (l != null) {
                    dialog.cancel();
                    l.evaluate(content, (int) ratingBar.getRating());
                }
            }
        });
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        dialog.show();

    }

    public static void qrDialog(final Context context, String code) {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            final ImageView view = new ImageView(context);
//            Bitmap logo = BitmapFactory.decodeResource(context.getResources(), R.drawable.logo);
            final Bitmap bitmap = CodeCreator.createQRCode(code, UIBaseUtils.dp2pxInt(context, 200), UIBaseUtils.dp2pxInt(context, 200), null);
            view.setImageBitmap(bitmap);
            builder.setView(view);
            builder.setCancelable(true);
            AlertDialog dialog = builder.create();
            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    FileUtils.savePhoto(context, bitmap, new FileUtils.SaveResultCallback() {
                        @Override
                        public void onSavedSuccess(String path) {
                            view.post(new Runnable() {
                                @Override
                                public void run() {
                                    UIHelper.ToastMessage(context, "保存成功");
                                }
                            });

                        }

                        @Override
                        public void onSavedFailed() {
                            view.post(new Runnable() {
                                @Override
                                public void run() {
                                    UIHelper.ToastMessage(context, "保存失败");
                                }
                            });
                        }
                    });

                    return false;
                }
            });
            Window window = dialog.getWindow();
            window.setBackgroundDrawable(new ColorDrawable(0));//设置背景透明
            dialog.show();
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }


    public interface EvaluateListener {
        void evaluate(String content, int rating);
    }

    public static AlertDialog getChangePhoneDialog(Context context, String title, String phone, String type, final ChangePhoneListener l) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        ChangePhoneView view = (ChangePhoneView) View.inflate(context, R.layout.dialog_change_phone, null);
        view.setTitle(title);
        view.setPhone(phone);
        view.setType(type);
        view.setChangePhoneListener(new ChangePhoneView.ChangePhoneListener() {
            @Override
            public void succeed(String newPhone) {
                if (l != null) {
                    l.succeed(newPhone);
                }
            }

            @Override
            public void unBound() {
                if (l != null) {
                    l.unBound();
                }
            }
        });
        builder.setView(view);
        builder.setCancelable(true);
        AlertDialog dialog = builder.create();
        return dialog;
    }

    public interface ChangePhoneListener {

        void succeed(String newPhone);//更换手机号

        void unBound();//解绑银行卡
    }

}

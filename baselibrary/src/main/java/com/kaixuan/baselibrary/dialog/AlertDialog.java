package com.kaixuan.baselibrary.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kaixuan.baselibrary.R;

import java.lang.ref.WeakReference;

/**
 * 自定义
 */

public class AlertDialog extends Dialog {

    private static AlertController mAlert;

    public AlertDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);

        mAlert = new AlertController(this, getWindow());
    }


    //设置文本
    public void setText(int viewId, CharSequence text) {
        mAlert.setText(viewId, text);
    }

    /**
     * 设置点击事件
     */
    public void setOnClick(int viewId, View.OnClickListener onClickListener) {
        mAlert.setOnClick(viewId, onClickListener);
    }

    //获取view，减少findViewById的次数
    private <T extends View> T getView(int viewId) {
        return mAlert.getView(viewId);

    }

    public static class Builder {
        private final AlertController.AlertParams P;

        public Builder(Context context) {
            this(context, R.style.dialog);
        }

        public Builder(Context context, int themeId) {
            P = new AlertController.AlertParams(context, themeId);
        }

        public AlertDialog create() {
            AlertDialog dialog = new AlertDialog(P.mContext, P.mThemeResId);

            P.apply(dialog.mAlert);
            dialog.setCancelable(P.cancelable);

            if (P.cancelable) {
                dialog.setCanceledOnTouchOutside(true);
            }
            dialog.setOnCancelListener(P.onCancelListener);
            dialog.setOnDismissListener(P.onDismissListener);

            return dialog;
        }


        public AlertDialog show() {
            final AlertDialog dialog = create();
            dialog.show();
            return dialog;
        }

        public Builder setContentView(View view) {

            P.mView = view;
            P.mViewLayoutResId = 0;
            return this;

        }

        public Builder setContentView(int layoutId) {

            P.mView = null;
            P.mViewLayoutResId = layoutId;
            return this;

        }

        public Builder setCancelable(boolean cancelable) {
            P.cancelable = cancelable;
            return this;
        }


        /**
         * Sets the callback that will be called if the dialog is canceled.
         * <p>
         * <p>Even in a cancelable dialog, the dialog may be dismissed for reasons other than
         * being canceled or one of the supplied choices being selected.
         * If you are interested in listening for all cases where the dialog is dismissed
         * and not just when it is canceled, see
         * {@link #setOnDismissListener(OnDismissListener) setOnDismissListener}.</p>
         *
         * @return This Builder object to allow for chaining of calls to set methods
         * @see #setCancelable(boolean)
         * @see #setOnDismissListener(OnDismissListener)
         */
        public Builder setOnCancelListener(OnCancelListener onCancelListener) {
            P.onCancelListener = onCancelListener;
            return this;
        }

        /**
         * Sets the callback that will be called when the dialog is dismissed for any reason.
         *
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setOnDismissListener(OnDismissListener onDismissListener) {
            P.onDismissListener = onDismissListener;
            return this;
        }

        /**
         * Sets the callback that will be called if a key is dispatched to the dialog.
         *
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setOnKeyListener(OnKeyListener onKeyListener) {
            P.onKeyListener = onKeyListener;
            return this;
        }

        //设置文本
        public Builder setText(int viewId, CharSequence text) {
            P.textArray.put(viewId, text);
            return this;
        }
        //设置点击事件
        public Builder setOnClickListener(int viewId, View.OnClickListener listener) {
            P.clickArray.put(viewId, listener);
            return this;
        }

        //配置一些万能的参数
        public Builder fullWidth() {
            P.mWidth = ViewGroup.LayoutParams.MATCH_PARENT;
            return this;
        }

        /**
         * 从底部弹出，isAnimation是否有动画
         **/
        public Builder fromBottom(boolean isAnimation) {
            if (isAnimation) {
                P.mAimation = R.style.dialog_from_bottom_anim;

            }

            P.mGravity = Gravity.BOTTOM;
            return this;
        }

        /**
         * 设置dialog的宽高
         **/
        public Builder setWidthHeight(int width, int height) {
            P.mWidth = width;
            P.mHeight = height;
            return this;
        }

        /**
         * 设置默认动画
         **/
        public Builder addDefaultAnimation(int aimation) {
            P.mAimation = R.style.dialog_from_bottom_anim;

            return this;
        }

        /**
         * 设置动画
         **/
        public Builder setAnimation(int styleAimation) {
            P.mAimation = styleAimation;
            return this;
        }


    }
}

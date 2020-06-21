package com.z.lionel.zutil;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;

/**
 * author: Lionel
 * date: 2020-02-21 11:59
 */
public class KeyboardUtil {

    /**
     * 隐藏键盘的方法
     *
     * @param activity
     */
    public static void hideKeyboard (Activity activity) {
        InputMethodManager imm =
            (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        // 隐藏软键盘
        imm.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), 0);
    }

    public static void hideDialogKeyboard (Dialog dialog) {
        InputMethodManager imm =
            (InputMethodManager) dialog.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        Window w = dialog.getWindow();
        if (w != null) {
            imm.hideSoftInputFromWindow(w.getDecorView().getWindowToken(), 0);
        }
    }

    public static void hideSoftInput (View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


}

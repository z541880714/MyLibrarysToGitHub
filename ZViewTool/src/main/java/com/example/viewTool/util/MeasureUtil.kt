package com.example.viewTool.util

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Point
import android.graphics.Rect
import android.util.DisplayMetrics
import android.util.Log
import android.util.Size
import android.view.View

/**
 * 获取组件的长与宽
 * Pair<WIDTH,HEIGHT>
 */
fun View.measureView(view: View = this): Size {
    view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
    val w = view.measuredWidth
    val h = view.measuredHeight
    return Size(w, h)
}

fun Context.getDensity(): Float {
    return resources.displayMetrics.density
}

/**
 * 整个屏幕的高度..包括 状态栏和导航栏..
 */
fun Activity.getFullScreenSize(): Size = window.decorView.measureView()

/**
 *
 * 获取整个屏幕的尺寸,不包括导航栏的高度..
 *
 */
fun Activity.getStatusBarSize(): Size {
    val frame = Rect()
    window.decorView.getWindowVisibleDisplayFrame(frame);
    return Size(frame.width(), frame.height())
}

/**
 * 计算出 当前
 * 不能在 onCreate 方法中使用。
 * 因为这种方法依赖于WMS（窗口管理服务的回调）。正是因为窗口回调机制，所以在Activity初始化时执行此方法得到的高度是0。
 * 这个方法推荐在回调方法onWindowFocusChanged()中执行，才能得到预期结果。
 */
fun Activity.getAppViewHeight(): Rect {
    //屏幕
    val dm = DisplayMetrics()
    windowManager.defaultDisplay.getMetrics(dm)
    //应用区域
    val outRect1 = Rect()
    window.decorView.getWindowVisibleDisplayFrame(outRect1)
    val statusBar = dm.heightPixels - outRect1.height()  //状态栏高度=屏幕高度-应用区域高度
    Log.i("zc", "状态栏高度:$statusBar")

    return outRect1
}


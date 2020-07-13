package com.example.viewTool.util

import android.app.Activity
import android.os.Build
import android.view.View
import android.view.Window
import android.view.WindowManager


/**
 *
 * 创建一个 activity windowManager 设置 [View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN] 来调整activity 界面
 * 主题的样式... 有各种样式可以提供...
 * author: Lionel
 * date: 2020-07-05 20:54
 */
object WindowFlagUtil {
    /**
     * 将 activity 设置为沉浸式模式... 即主界面的内容将扩展到状态栏中
     */
    fun Activity.screenImmerse() {
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
    }

    /**
     * activity 将设置为全屏模式..
     */
    fun Activity.entryFullMode() {
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }

    /**
     * 将 activity 从全屏模式中退出来, 显示状态栏
     */
    fun Activity.exitFullMode() {
        window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }


    /**
     * 修改状态栏颜色，支持4.4以上版本
     * @param activity
     * @param colorId
     */
    fun setStatusBarColor(activity: Activity, colorId: Int) {
        val window: Window = activity.window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = activity.resources.getColor(colorId, null)
    }

}
package com.z.lionel.zutil.custom

import android.graphics.Rect
import android.util.Log
import android.view.View
import com.z.lionel.zutil.util.KeyboardUtil


typealias IKeyBoardVisibleListener = (Boolean, Int) -> Unit

/**
 * 自定义一个类... 用来监听软键盘的... 软件盘开启时, 指定的 View 自动移动到 底部与软键盘顶部对齐.
 * 需要监测软键盘的, 尺寸...
 *
 * author: Lionel
 * date: 2020-05-20 16:17
 */
class ViewAdjustWithSoftInputView(val viewScrolled: View) {

    //表示当前软键盘是否显示...
    private var isSoftInputBoardShowing = false
    var viewInitY = 0f  // viewScrolled 的 初始值相对于父组件 Y 的为值...
        private set(value) {
            if (field > 0) return
            field = value
        }

    private val screenHeight by lazy {
        val rect = Rect()
        viewScrolled.getWindowVisibleDisplayFrame(rect)
        rect.height()
    }


    fun addOnSoftKeyBoardVisibleListener(
        root: View = viewScrolled,
        listener: IKeyBoardVisibleListener
    ) {
        Log.i("zc", "addOnSoftKeyBoardVisibleListener:aaa screenHeight: $screenHeight");
        root.viewTreeObserver.addOnGlobalLayoutListener a@{
            viewInitY = root.y
            if (viewInitY == 0f) return@a  //此时如果还没有测量好,就不要继续了. .
            val rect = Rect()
            root.getWindowVisibleDisplayFrame(rect)
            //计算出可见屏幕的高度
            val displayHight: Int = rect.height()
            Log.i("zc", "addOnSoftKeyBoardVisibleListener:bbb displayHight: $displayHight");
            //获得键盘高度
            val keyboardHeight = screenHeight - displayHight
            Log.i("zc", "addOnSoftKeyBoardVisibleListener:bbb keyboardHeight: $keyboardHeight");
            val visible = keyboardHeight > 200  //true 表示软键盘显示..  false 表示无
            if (visible) {
                root.y = viewInitY - keyboardHeight
            } else {
                Log.i("zc", "addOnSoftKeyBoardVisibleListener:ccc root.y: ${root.y}");
                root.y = viewInitY
            }
            if (visible != isSoftInputBoardShowing) {
                listener(visible, keyboardHeight)
            }
            isSoftInputBoardShowing = visible
        }
    }

    /**
     * 返回 软键盘是否 隐藏, 如果没有隐藏 将会执行隐藏..
     */
    fun initViewPosition(): Boolean {
        viewScrolled.y = viewInitY
        return if (isSoftInputBoardShowing) {
            KeyboardUtil.hideSoftInput(viewScrolled)
            false
        } else true
    }


}

fun main() {

}
package com.example.viewTool.template

import android.app.Activity
import android.content.Context
import android.util.Size
import android.view.Gravity
import android.view.View
import com.example.viewTool.R
import com.example.viewTool.util.getStatusBarSize

/**
 * 使用[CustomDialogOut] 进行封装 对外操作的话, 就比较容易了
 * 自定义把参数设置好, 不用每次就 设置参数什么的了...
 *
 * author: Lionel
 * date: 2020-06-12 23:41
 */
public class ZDialog(context: Context) : CustomDialogOut(context) {

    private class ZDialogParam(val context: Context) {

        lateinit var size: Size
        lateinit var contentView: View

        var gravity: Int = Gravity.CENTER
        var offsetX = 0
        var offsetY = 0
        var animStyleRes = 0
    }


    companion object {
        fun builder(context: Activity) = ZDialogBuilder.builder(context)
    }

    object ZDialogBuilder {
        private lateinit var p: ZDialogParam

        fun builder(context: Activity): ZDialogBuilder {
            p = ZDialogParam(context)
            return this
        }

        //满屏显示..
        fun fullWindow(): ZDialogBuilder {
            //            p.size = p.context.getScreenSize()
            //            p.size = getMainContext().run { this as Activity }.getAppViewHeight()
            //                .run { Size(width(), height()) }

            p.size = p.context.run { this as Activity }.getStatusBarSize()
            return this
        }

        fun setSize(size: Size): ZDialogBuilder {
            p.size = size
            return this
        }


        fun setView(view: View): ZDialogBuilder {
            p.contentView = view
            return this
        }

        fun setPosition(gravity: Int, offsetX: Int, offsetY: Int): ZDialogBuilder {
            p.gravity = gravity
            p.offsetX = offsetX
            p.offsetY = offsetY
            return this
        }

        fun setAnimation(): ZDialogBuilder {
            p.animStyleRes = R.style.z_dialog_anim_style
            return this
        }

        fun build(): CustomDialogOut {
            val dialog = ZDialog(p.context)
            dialog.setContentView(p.contentView)
            dialog.setWindowSize(p.size)
            dialog.setPosition(p.gravity, p.offsetX, p.offsetY)
            dialog.window?.setWindowAnimations(p.animStyleRes)
            return dialog
        }
    }


}
package com.example.viewTool.template

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import kotlinx.android.extensions.LayoutContainer
import java.util.zip.Inflater


/**
 * 自定义一个 框架 ... 继承LinearLayout 并且将自定义的 layout 加入其中,作为其子view
 *
 * 使用关键字 open  表示此类可以被继承,  kotlin  类默认是不可被继承的.
 *
 *
 * class WorkMenuView private constructor(context: Context) :
 * MyFrameLayout(context) {
 *
 * override val resLayout: Int = R.layout.popup_work_menu
 *
 * companion object {
 * fun instanceFactory(context: Context): WorkMenuView {
 * return WorkMenuView(context).apply { registerView() }
 * }
 * }
 *
 * override fun init() {
 * }
 *
 * }
 *
 *
 *
 *
 *
 */
abstract class MyFrameLayout @JvmOverloads constructor(
    context: Context,
    attributes: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attributes, defStyleAttr), LayoutContainer {
    /**
     * 子类必须实现一个 定义一个子 view 的 layoutRes:
     * [R.layout.xxxx]
     */
    abstract val resLayout: Int

    override val containerView: View? by lazy { this }

    /**
     *   需要被调用... 不然回报错!!
     */
    open fun registerView() {
        LayoutInflater.from(context).inflate(resLayout, this)
        init()
    }

    /**
     * 钩子... 进行初始化的 入口...
     */
    abstract fun init()


}


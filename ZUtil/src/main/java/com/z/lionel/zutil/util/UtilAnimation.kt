package com.z.lionel.zutil.util

import android.view.animation.Animation
import android.view.animation.TranslateAnimation

/**
 * author: Lionel
 * date: 2020-02-22 12:54
 */
object UtilAnimation {
    /**
     * 屏幕从左到右 执行平移动画..
     * view 进场...
     *
     * @param distance      滑动的距离
     * @param millionSecond 指定的时间. ms 为单位.
     */
    fun translateInFromLToR(distance: Int, millionSecond: Int): Animation {
        val translateAnimation: Animation =
            TranslateAnimation((-distance).toFloat(), 0f, 0f, 0f) //设置平移的起点和终点
        translateAnimation.duration = millionSecond.toLong() //动画持续的时间为10s
        //如果不添加setFillEnabled和setFillAfter则动画执行结束后会自动回到原点
        translateAnimation.isFillEnabled = true //使其可以填充效果从而不回到原地
        translateAnimation.fillAfter = true //不回到起始位置
        return translateAnimation
    }

    fun translateInFromRToL(distance: Int, millionSecond: Int): Animation {
        val translateAnimation: Animation =
            TranslateAnimation(distance.toFloat(), 0f, 0f, 0f) //设置平移的起点和终点
        translateAnimation.duration = millionSecond.toLong() //动画持续的时间为10s
        //如果不添加setFillEnabled和setFillAfter则动画执行结束后会自动回到原点
        translateAnimation.isFillEnabled = true //使其可以填充效果从而不回到原地
        translateAnimation.fillAfter = true //不回到起始位置
        return translateAnimation
    }

    fun translateOutFromRToL(distance: Int, millionSecond: Int): Animation {
        val translateAnimation: Animation =
            TranslateAnimation(0f, (-distance).toFloat(), 0f, 0f) //设置平移的起点和终点
        translateAnimation.duration = millionSecond.toLong() //动画持续的时间为10s
        //如果不添加setFillEnabled和setFillAfter则动画执行结束后会自动回到原点
        translateAnimation.isFillEnabled = true //使其可以填充效果从而不回到原地
        translateAnimation.fillAfter = true //不回到起始位置
        return translateAnimation
    }

    fun translateOutFromLToR(distance: Int, millionSecond: Int): Animation {
        val translateAnimation: Animation =
            TranslateAnimation(0f, distance.toFloat(), 0f, 0f) //设置平移的起点和终点
        translateAnimation.duration = millionSecond.toLong() //动画持续的时间为10s
        //如果不添加setFillEnabled和setFillAfter则动画执行结束后会自动回到原点
        translateAnimation.isFillEnabled = true //使其可以填充效果从而不回到原地
        translateAnimation.fillAfter = true //不回到起始位置
        return translateAnimation
    }

    fun translateFromRToL(from: Int, to: Int, millisecond: Int): Animation {
        val translateAnimation: Animation =
            TranslateAnimation(from.toFloat(), to.toFloat(), 0f, 0f) //设置平移的起点和终点
        translateAnimation.duration = millisecond.toLong() //动画持续的时间为10s
        //如果不添加setFillEnabled和setFillAfter则动画执行结束后会自动回到原点
        translateAnimation.isFillEnabled = true //使其可以填充效果从而不回到原地
        translateAnimation.fillAfter = true //不回到起始位置
        return translateAnimation
    }

    fun translateY(from: Int, to: Int, durationMillisecond: Int): Animation {
        val translateAnimation: Animation =
            TranslateAnimation(0f, 0f, from.toFloat(), to.toFloat())
        translateAnimation.duration = durationMillisecond.toLong()
        return translateAnimation
    }
}

/**
 * 动画结束后, 要做的事 ....
 */
fun Animation.end(block: () -> Unit): Animation {
    setAnimationListener(object : Animation.AnimationListener {
        override fun onAnimationRepeat(animation: Animation?) {
        }

        override fun onAnimationEnd(animation: Animation?) {
            block()
        }

        override fun onAnimationStart(animation: Animation?) {
        }

    })
    return this
}

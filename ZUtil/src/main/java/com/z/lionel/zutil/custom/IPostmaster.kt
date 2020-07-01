package com.z.lionel.zutil.custom

import android.util.Log
import java.util.*

/**
 * 创建一个 邮箱接口...
 * 比如 某个人来拜访你, 发现 主任不在, 然后又不想一直等, 那么就把要做的事情 放在邮箱里面
 * 等主人回来的时候,  就打开邮箱, 处理 邮箱里面的 事件..
 *
 * author: Lionel
 * date: 2020-06-13 20:28
 */
interface IPostmaster {

    val eventList: LinkedList<() -> Unit>

    val condition: Boolean

    fun postEvent(event: () -> Unit) {
        if (condition)
            eventList.offer(event)
        else {
            Log.i("zc", " IPostmaster postEvent: condition  unqualified !!!!!");
            //如果条件符合要求了,  就自动执行检查邮箱
            event()
            checkEvent()
        }
    }


    private fun checkEvent() {
        if (eventList.isEmpty()) return
        synchronized(eventList) {
            while (eventList.isNotEmpty()) {
                Log.i("zc", "IPostmaster checkEvent: has email !!!!!");
                val e = eventList.poll()
                eventBac { e() }
            }
        }
    }
}
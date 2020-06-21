package com.wsht.lionel.lteipcells.custom

import java.util.concurrent.ConcurrentLinkedQueue


/**
 * 定义一个延时执行的计划...
 * 先把要做的事情先计划好,放入队列中...
 * 在某个合适的时候, 取出 ,执行..队列中的相应的事件删除..
 * 列队中没有事件, 则下次 执行时 , 也不会做任何处理..
 *
 * 例如:  activity 中, 实例化组件 希望只初始化一次.. 如果放在 oncreate 中执行的话.. 又怕影响view 的加载?
 *          或者, 此时获取 组件的尺寸,可能会出问题.. 希望放到一个合适的时间点, 进行初始化..
 *          有能快速相应, 有能满足 其他要求 ..
 *          直接放在start() 中, 又可能会运行多次...
 */
class DelegateScheduleDelay {

    private val queue = ConcurrentLinkedQueue<Function0<Unit>>()

    fun schedule(block: Function0<Unit>) {
        queue.offer(block)
    }

    fun apply() {
        if (queue.isEmpty()) return
        queue.poll()()
    }

}
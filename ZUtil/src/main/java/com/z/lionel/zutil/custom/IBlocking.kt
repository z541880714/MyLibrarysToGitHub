package com.wsht.lionel.lteipcells.custom

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.joinAll
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.concurrent.locks.Condition
import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock
import java.util.function.BiPredicate
import java.util.function.Predicate
import kotlin.coroutines.CoroutineContext


/**
 * 自定义一个 lock 接口... 通过调用 [runBlocking] 进行阻塞.. [release] 进行解锁. ..
 */
interface IBlocking {
    val lock: Lock
    /**
     *  阻塞的实例,  用该实例来进行阻塞, 来解除阻塞
     */
    val conditionInstance: Condition

    /**
     * 当前条件!!! false 时 阻塞;  true时, 解除阻塞..
     */
    var predicate: Boolean

    //如果满足条件 就进行阻塞 ..
    //这个还是用到线程阻塞的呀..
    fun block(block: () -> Unit) {
        lock.newCondition()
        // 如果已经满足条件了,  不用进行阻塞,直接进行任务.
        if (predicate) {
            block()
            return
        }
        lock.lock()
        try {
            while (!predicate) {
                conditionInstance.await()
            }
            //解除阻塞后,可运行的部分..
            block()
        } finally {
            lock.unlock()
        }
    }

    /**
     * 通知解锁....
     */
    fun release() {
        lock.lock()
        try {
            conditionInstance.signalAll()
        } finally {
            lock.unlock()
        }
    }

}

internal class BoundedBuffer {
    val lock: Lock = ReentrantLock()
    val notFull = lock.newCondition()
    val notEmpty = lock.newCondition()
    val items = arrayOf<Any>(100)
    var putptr: Int = 0
    var takeptr: Int = 0
    var count: Int = 0

    @Throws(InterruptedException::class)
    fun put(x: Any) {
        lock.lock()
        try {
            while (count == items.size)
                notFull.await()
            items[putptr] = x
            if (++putptr == items.size) putptr = 0
            ++count
            notEmpty.signal()
        } finally {
            lock.unlock()
        }
    }

    @Throws(InterruptedException::class)
    fun take(): Any {
        lock.lock()
        try {
            while (count == 0)
                notEmpty.await()
            val x = items[takeptr]
            if (++takeptr == items.size) takeptr = 0
            --count
            notFull.signal()
            return x
        } finally {
            lock.unlock()
        }
    }
}
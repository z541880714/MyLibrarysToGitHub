package com.z.lionel.zutil.custom

import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import java.util.*
import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.thread
import kotlin.coroutines.CoroutineContext


private typealias  MJob = suspend () -> Unit


/**
 * 封装成一个 类,  每个实例都有自己 单独的 队列任务... 用于队列执行任务,保证 事件的同步运行...
 */
class SyncTaskDepartment : CoroutineScope {
    private val job = Job()

    override val coroutineContext: CoroutineContext = IO + job

    private val eventQueue: LinkedList<MJob> by lazy { LinkedList<MJob>() }

    private val blockLock by lazy { CoroutineBlock(coroutineContext).newJob() }

    init {
        startWork()
    }

    fun end() {
        job.cancel()
    }

    //既然是队列...那就需要进行同步... 下一个事件等到上一个事件完成...
    private fun startWork() = launch {
        while (true) {
            if (eventQueue.size == 0) blockLock.blocking()
            val event = eventQueue.pollFirst() ?: continue
            event()
        }
    }

    @Synchronized
    fun enqueue(event: suspend () -> Unit) {
        if (!job.isActive) return  //如果工作已经结束了,  就不再接受列队了. .
        eventQueue.offer(event)
        blockLock.release()
    }
}


/**
 *
 *
 * 创建一个部门, 专门用来处理 后台任务的,  可进行有序,无序的任务, 使用的是协程...
 * 避免了 每次都要自己去创建协程 实例, 然后还要去实现队列... 统一让一个 职能部门来完成这些事即可...
 *
 * 我应该还需要一个.. 类的对象,针对 某些特别要求, 一些执行的事件单独的放在一个队列里进行... 不要所有的都放在一起...
 * 这样子的话, 这个做为一个接口是最合适的了. 是吧.. 以后用到再改进吧. 暂时先这样. ..
 *
 * author: Lionel
 * date: 2020-05-22 15:25
 */
object TaskManagerDepartment : CoroutineScope {
    private val job = Job()

    override val coroutineContext: CoroutineContext = IO + job

    private val eventQueue: LinkedList<MJob> by lazy { LinkedList<MJob>() }

    private val blockLock = CoroutineBlock(coroutineContext).newJob()

    //是否进行初始化的标志.. 如果没有初始化, queue() 方法调用时,将报错!!
    private var isInitWorkFlag = false

    fun init() {
        isInitWorkFlag = true
        startWork()
    }

    fun end() {
        job.cancel()
    }

    //既然是队列...那就需要进行同步... 下一个事件等到上一个事件完成...
    private fun startWork() = launch {
        while (isInitWorkFlag) {
            if (eventQueue.size == 0) blockLock.blocking()
            val event = eventQueue.pollFirst() ?: continue
            event()
        }
    }

    @Synchronized
    fun enqueue(event: suspend () -> Unit) {
        if (!isInitWorkFlag) init()  //如果还没有初始化, 先初始化一下吧...
        if (!job.isActive) return  //如果工作已经结束了,  就不再接受列队了. .
        eventQueue.offer(event)
        blockLock.release()
    }

    fun doBackground(event: suspend () -> Unit) = launch { event() }
}


fun eventBac(event: suspend () -> Unit) = TaskManagerDepartment.doBackground(event)

/**
 * 在子线程中运行, 并且放入队列中..
 */
fun enqueueBac(event: suspend () -> Unit) = TaskManagerDepartment.enqueue(event)


fun main() {
    thread {
        TaskManagerDepartment.init()

        runBlocking {
            for (i in 0..1000) {
                TaskManagerDepartment.enqueue {
                    delay(1000)
                    println("event index: $i")
                }

                TaskManagerDepartment.doBackground {
                    delay(2000)
                    println("do background index: $i")
                }
                println("for recycle index: $i")
            }
        }

        val lock: Lock = ReentrantLock()
        val condition = lock.newCondition()
        lock.tryLock()
        condition.await()

        TaskManagerDepartment.end()

        println("for recycle index: end!!")
    }
}



package com.wsht.lionel.lteipcells.custom

import android.util.Log
import kotlinx.coroutines.*
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.locks.Lock
import kotlin.coroutines.CoroutineContext


/**
 * 定义一个在协程中进行的阻塞...
 * 定义一个job 该job 将在 不满足指定条件下, 将一直 delay , 直到 满足条件后, job 将会被 cancel. "阻塞结束.."
 * 貌似这种方法, 不影响线程的运行...
 * 设置一个阻塞的条件..
 */
private interface ICoroutineBlock : CoroutineScope {

    fun newJob(): MyJob

}

/**
 * job 中 ,job 中包含阻塞 , 解除阻塞的具体方法...
 * 一个[CoroutineBlock] 中可以有多个 ,
 */
interface MyJob {

    var isNeedRelease: AtomicBoolean

    var job: Job
    /**
     * 此方法是一个 suspend 方法...
     * 如果 此时 [job] 正在工作.. 就不能进行拉!!!
     *
     * 如果满足条件, 就阻塞!!!
     */
    suspend fun blocking(
        duration: Long = 100 * 60 * 60 * 1000,
        timeoutBlock: () -> Unit = {}
    ): Unit = coroutineScope {
        if (isNeedRelease.get()) {
            isNeedRelease.set(false)
            return@coroutineScope
        }
        if (job.isActive)
            job.cancel()
        job = launch {
            delay(duration)   //默认阻塞100s  ,如果 还不满足条件, 将一直 延时下去...
            Log.i("zc", "blocking: time out !!! ");
            timeoutBlock()  //如果超时了,执行指定的逻辑...
        }
        //等待job 完成呀... 这就相当于阻塞了.   不过不是真正的线程阻塞..
        job.join()
    }

    /**
     * 结束job  工作, 结束当前的延时..
     */
    fun release() {
        if (job.isActive) job.cancel()
        else isNeedRelease.set(true)
    }
}


/**
 * 此类将被外部调用...
 * 外部调用方法如下:
 *  val blockInstance = CoroutineBlock(coroutineContext)
 *  val blockJob = blockInstance.newJob()
 *  挂起:     blockJob.block()
 *  继续执行: blockJob.release()
 */
class CoroutineBlock(override val coroutineContext: CoroutineContext) : ICoroutineBlock {

    private val jobList = ArrayList<MyJob>()


    override fun newJob(): MyJob = object : MyJob {
        override var isNeedRelease: AtomicBoolean = AtomicBoolean(false)
        override var job: Job = Job()
    }.apply { jobList.add(this) }

    /**
     * 结束时 ,结束掉工作!!! ,否则一直会卡在哪里的..
     */
    fun endWork() {
        jobList.forEach { it.release() }
    }

}


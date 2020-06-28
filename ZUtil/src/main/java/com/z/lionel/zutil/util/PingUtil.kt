package com.z.lionel.zutil.util

import android.util.Log
import kotlinx.coroutines.*
import java.io.BufferedReader
import java.io.InputStreamReader

/**
 * 自定义一个ping 工具, 去的比较好玩的呀.
 *
 * author: Lionel
 * date: 2019-12-31 17:08
 */
object PingUtil {

    suspend fun ping(ip: String, count: Int): Boolean {
        return coroutineScope {
            var result = false
            for (index in 1..count) {
                val deferred = async { ping(ip) }
                result = deferred.await()
                if (result) break
            }
            result
        }
    }

    /**
     * 使用 ping 命令...
     * 一次只发一次ping 命令, 超时时间为3s
     */
    private fun ping(ip: String): Boolean {
        val command = "ping -c 1 -W 3 $ip"
        val proc = Runtime.getRuntime().exec(command)
        proc.waitFor()
        when (proc.exitValue()) {
            0 -> {
                val reader = BufferedReader(InputStreamReader(proc.inputStream))
                val result = StringBuilder()
                while (true) {
                    val line = reader.readLine() ?: break
                    result.append(line).append("\n")
                }
                return true
            }
            else -> {
                // ping 失败.. 可以认为没有 ping 通..

                //Log.i("zc", "ping_test: error code: $exit");
            }
        }
        return false
    }

    /**
     * 检测当前网络是否正常联网.
     */
    suspend fun isNetworkNormal(): Boolean {
        val result = ping("www.hao123.com", 2)
        Log.i("zc", "isNetworkNormal: result: $result");
        return result
    }
}
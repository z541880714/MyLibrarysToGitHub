package com.z.lionel.zutil.socket.udp

import android.util.Log
import com.z.lionel.zutil.socket.ZSocket
import java.io.IOException
import java.lang.Exception
import java.net.*
import java.nio.ByteBuffer
import java.nio.channels.DatagramChannel
import kotlin.concurrent.thread


/**
 * socket 实例 由 [UdpMessageDispatch] 相关联
 *
 * 此处只负责, 发送 与接受 对应的数据...
 * 对应的 ip/port  都会实时的传过来的...
 *
 */
class UdpSocket(val localPort: Int/*需要监听的本地端口..*/, callback: (DatagramPacket) -> Unit) : ZSocket {
    private val BUFFER_LENGTH = 1024 * 2
    private lateinit var socketChannel: DatagramChannel

    /**
     * 工作线程.. 用来 接受消息用的...
     */
    private var workThread: Thread? = null
    private var isWorking: Boolean = false

    /**
     * 由于 socket.send  本身就用了同步方法,  保证了线程安全..
     * 所以此类发送 消息就不用使用同步方法了.
     */
    fun startWork() {
        socketChannel = DatagramChannel.open()
        socketChannel.socket().bind(InetSocketAddress(localPort))
        isWorking = true
        receiveUdpMessage()
    }

    fun endWork() {
        isWorking = false
        closeSocket()
        workThread?.interrupt()
    }

    private fun closeSocket() {
        socketChannel.socket().close()
        socketChannel.close()
    }

    private fun receiveUdpMessage() {
        workThread = thread {
            while (isWorking) {
                try {
                    val buffer = ByteBuffer.allocate(BUFFER_LENGTH)
                    //toString() 的格式为  ip:port
                    val address: SocketAddress = socketChannel.receive(buffer)
                    buffer.flip()
                    val bytes = ByteArray(buffer.limit())
                    buffer.get(bytes)
                    val ipPort = zIpPort(address)
                    receiveCallback(
                        DatagramPacket(
                            bytes, bytes.size, InetAddress.getByName(ipPort.ip), ipPort.port
                        )
                    )
                } catch (e: IOException) {
                    continue
                } catch (e: Exception) {
                    break
                }
            }
        }
    }

    //回调函数, 需要调用此接口, 否则, 数据将不做处理..
    //不负责任何其他的处理, 如果需要队列, 需要自行封装.
    val receiveCallback = callback

    fun sendMessage(p: DatagramPacket) {
        if (!isWorking) return
        socketChannel.send(ByteBuffer.wrap(p.data), p.socketAddress)
        //Log.i("zc", "sendMessage: ip: ${p.address.hostAddress} , port: ${p.port} ")
    }

}


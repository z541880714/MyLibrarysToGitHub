package com.z.lionel.zutil.socket

import java.net.SocketAddress

/**
 *
 *
 * author: Lionel
 * date: 2020-06-21 09:25
 */
interface ZSocket {
    data class ZIpPort(val ip: String, val port: Int)

    fun zIpPort(socketAddress: SocketAddress): ZIpPort {
        val addressArray = socketAddress.toString().split(":")
        return ZIpPort(addressArray[0].substring(1), addressArray[1].toInt())
    }
}




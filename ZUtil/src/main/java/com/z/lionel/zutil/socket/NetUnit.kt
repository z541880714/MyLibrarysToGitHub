@file:JvmName("NetUnit")

package com.z.lionel.zutil.socket

import java.net.InetSocketAddress
import java.net.SocketAddress

/**
 * author: Lionel
 * date: 2020-07-03 18:57
 */
fun getAddressSocket(ip: String, port: Int): SocketAddress = InetSocketAddress(ip, port)
fun getAddressSocket(port: Int) = InetSocketAddress(port)
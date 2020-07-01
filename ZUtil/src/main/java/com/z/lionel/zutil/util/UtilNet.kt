package com.z.lionel.zutil.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.wifi.WifiManager
import android.util.Log
import kotlinx.coroutines.coroutineScope
import java.net.InetAddress
import java.net.NetworkInterface
import java.net.SocketException


/**
 *
 *
 * author: Lionel
 * date: 2020-06-26 13:02
 */
object UtilNet {


    /**
     ** @IntDef(prefix = { "TRANSPORT_" }, value = {
     **     TRANSPORT_CELLULAR,
     **     TRANSPORT_WIFI,
     **     TRANSPORT_BLUETOOTH,
     **     TRANSPORT_ETHERNET,
     **     TRANSPORT_VPN,
     **     TRANSPORT_WIFI_AWARE,
     **     TRANSPORT_LOWPAN,
     **  })
     ** public @interface Transport { }
     *
     *  如果是 移动网络, 暂时不查当前ip,直接返回空值; 只有用于wifi 网络下查找wifi 才可以控制...
     */
    suspend fun ip(context: Context): String = coroutineScope a@{
        val conMgr = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        conMgr.allNetworks.forEach {
            val caps = conMgr.getNetworkCapabilities(it) ?: return@forEach
            when {
                caps.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                    Log.i("zc", "UtilNet ip: TRANSPORT_CELLULAR")
//                    return@a getLocalIpAddress()
                    return@a ""
                }
                caps.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                    Log.i("zc", "UtilNet ip: TRANSPORT_WIFI")
                    return@a getWifiAddress(context)
                }
            }
        }
        return@a ""
    }

    private fun getLocalIpAddress(): String {
        try {
            var ipv4: String = ""
            val nilist = NetworkInterface.getNetworkInterfaces() ?: return ""
            for (ni in nilist) {
                val ialist: List<InetAddress> = ni.inetAddresses.toList()
                for (address: InetAddress in ialist) {
                    address.hostAddress
                    Log.i("zc", "getLocalIpAddress:address.hostAddress: ${address.hostAddress}");
                }
            }
        } catch (ex: SocketException) {
        }
        return ""
    }

    private fun getWifiAddress(context: Context): String {
        val wifiManager =
            context.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val wifiInfo = wifiManager.connectionInfo
        val ipAddress = wifiInfo.ipAddress
        val ip = intToIp(ipAddress)
        Log.i("zc", "UtilNet ip: TRANSPORT_WIFI ip: $ip,  ipAddress:$ipAddress")
        return ip
    }

    // int 类型转换成 ip 类型...
    private fun intToIp(ipInt: Int): String {
        val sb = StringBuilder()
        sb.append(ipInt and 0xFF).append(".")
        sb.append(ipInt shr 8 and 0xFF).append(".")
        sb.append(ipInt shr 16 and 0xFF).append(".")
        sb.append(ipInt shr 24 and 0xFF)
        return sb.toString()
    }
}
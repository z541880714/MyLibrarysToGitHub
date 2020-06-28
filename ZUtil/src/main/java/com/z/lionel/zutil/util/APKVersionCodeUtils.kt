package com.z.lionel.zutil.util

import android.content.pm.PackageManager
import android.content.Context


class APKVersionCodeUtils {

    companion object {
        val instance by lazy { APKVersionCodeUtils() }
    }

    /**
     * 获取当前本地apk的版本
     *
     * @param mContext
     * @return
     */
    fun getVersionCode(mContext: Context): Int {
        var versionCode = 0
        try {
            //获取软件版本号，对应AndroidManifest.xml下android:versionCode
            versionCode =
                mContext.packageManager.getPackageInfo(mContext.packageName, 0).versionCode
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        return versionCode
    }

    /**
     * 获取版本号名称
     *
     * @param context 上下文
     * @return
     */
    fun getVerName(context: Context): String {
        var verName = ""
        try {
            verName =
                context.packageManager.getPackageInfo(context.packageName, 0).versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return verName
    }
}
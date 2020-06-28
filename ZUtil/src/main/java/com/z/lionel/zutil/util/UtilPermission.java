package com.z.lionel.zutil.util;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/12/14 0014.
 */

public class UtilPermission {

    public static final int CODE_PERMISSION_GRANTED = 99;
    public static final int CODE_PERMISSION_REPETITIVE = 100;

    private static final String PERMISSION_RECORD_AUDIO = Manifest.permission.RECORD_AUDIO;
    private static final String PERMISSION_GET_ACCOUNTS = Manifest.permission.GET_ACCOUNTS;
    private static final String PERMISSION_READ_PHONE_STATE = Manifest.permission.READ_PHONE_STATE;
    private static final String PERMISSION_CALL_PHONE = Manifest.permission.CALL_PHONE;
    private static final String PERMISSION_CAMERA = Manifest.permission.CAMERA;
    private static final String PERMISSION_ACCESS_FINE_LOCATION =
            Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String PERMISSION_ACCESS_COARSE_LOCATION =
            Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final String PERMISSION_READ_EXTERNAL_STORAGE =
            Manifest.permission.READ_EXTERNAL_STORAGE;
    private static final String PERMISSION_READ_SMS = Manifest.permission.READ_SMS;
    private static final String PERMISSION_SEND_SMS = Manifest.permission.SEND_SMS;
    private static final String PERMISSION_RECEIVE_SMS = Manifest.permission.RECEIVE_SMS;
    private static final String PERMISSION_WRITE_EXTERNAL_STORAGE =
            Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private static final String REQUEST_INSTALL_PACKAGES = Manifest.permission.REQUEST_INSTALL_PACKAGES;

    private static final String[] requestPermissions = {
            PERMISSION_RECORD_AUDIO, /*录音权限*/
            PERMISSION_GET_ACCOUNTS, /*通讯录*/
            PERMISSION_READ_PHONE_STATE, /* 手机状态  */
            PERMISSION_CALL_PHONE,       /*  电话权限 */
            PERMISSION_CAMERA, /*照相,视频权限*/
            PERMISSION_ACCESS_FINE_LOCATION,    /*定位*/
            PERMISSION_ACCESS_COARSE_LOCATION,  /*粗略定位,基站定位*/
            PERMISSION_READ_EXTERNAL_STORAGE,/*读取*/
            PERMISSION_WRITE_EXTERNAL_STORAGE,/* 写  */
            PERMISSION_READ_SMS,                /*短信*/
            PERMISSION_SEND_SMS,                /*短信*/
            PERMISSION_RECEIVE_SMS,          /*接受短信*/
            REQUEST_INSTALL_PACKAGES            /*安装包*/
    };

    public interface PermissionGrant {
        void onPermissionGranted(int requestCode);
    }


    //判断 当前是否重进行进申请了!!!
    //1: 第一次申请权限,  应该全部允许
    //2: 第二次 进行确认, 如果所有权限都那么返回 CODE_PERMISSION_GRANTED, 否则返回 CODE_PERMISSION_REPETITIVE
    public static int applyForCount = 0;

    /**
     * 一次申请多个权限
     * <p>
     * Activity  建议使用以下格式来使用此确认权限工具:
     * <p>
     * * //需要确认app 的权限. 将app 需要用到的权限都加到这里来...
     * *    private fun checkAppPermission() {
     * *        UtilPermission.requestMultiPermissions(this) {
     * *            when (it) {
     * *                UtilPermission.CODE_PERMISSION_GRANTED -> startWork()
     * *                UtilPermission.CODE_PERMISSION_REPETITIVE -> {
     * *                    sendMainToast("app 未开启必要的权限, 将自动关闭..")
     * *                    runOnUi(1000) { exitProcess(0) }
     * *                }
     * *            }
     * *        }
     * *    }
     * <p>
     * *  override fun onRequestPermissionsResult(
     * *        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
     * *    ) {
     * *        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
     * *        if (requestCode == UtilPermission.CODE_PERMISSION_GRANTED)
     * *            checkAppPermission()   //重新确认...权限, 确保所有权限都开启了
     * *    }
     */
    public static void requestMultiPermissions(
            final Activity activity,
            List<String> permissions,
            PermissionGrant grant) {
        applyForCount++;
        //如果重复申请 ,那么直接回 调给主界面 进行处理... 正常情况下, 会提示 并退出app
        final ArrayList<String> permissionsList = getNoGrantedPermission(activity, permissions);   //获取多个权限的值
        if (permissionsList == null)
            throw new NullPointerException("权限列表 长度可以为0, 但是不能为 null , ");
        if (permissionsList.size() == 0) { //如果权限都已经授权...
            grant.onPermissionGranted(CODE_PERMISSION_GRANTED);
        } else if (applyForCount > 1) {
            grant.onPermissionGranted(CODE_PERMISSION_REPETITIVE);
        } else {
            //调用此方法 后， 会返回 onRequestPermissionsResult 方法参数
            int size = permissionsList.size();
            activity.requestPermissions(
                    permissionsList.toArray(new String[size]),
                    CODE_PERMISSION_GRANTED);
            Log.i("zc", "requestMultiPermissions:  " + permissionsList.size());
        }
    }


    private static ArrayList<String> getNoGrantedPermission(
            Activity activity, List<String> permissions) {
        ArrayList<String> permissionsUnGranted = new ArrayList<>();
        for (String requestPermission : permissions) {
            int checkSelfPermission;
            try {
                checkSelfPermission = activity.checkSelfPermission(requestPermission);
            } catch (RuntimeException e) {
                return permissionsUnGranted;
            }
            if (checkSelfPermission != PackageManager.PERMISSION_GRANTED) {    //或还为获取到该权限 , 就添加进
                permissionsUnGranted.add(requestPermission);
            }
        }
        return permissionsUnGranted;
    }


}






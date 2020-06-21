package com.z.lionel.zutil.socket.tcp;

import android.util.Log;

import java.util.Vector;

/**
 * 连接服务器线程类
 *
 * @author Esa
 */
public class TCPSocketConnect implements Runnable {

    public boolean isWorking = false;// 是否连接服务器
    private boolean isWrite = false;// 是否发送数据
    private static Vector<byte[]> datas = new Vector<byte[]>();// 待发送数据队列
    private final Object lock = new Object();// 连接锁对象
    private TCPSocketFactory mSocket;// socket连接
    private WriteRunnable writeRunnable;// 发送数据线程
    private String ip = null;
    private int port = -1;


    /**
     * 创建连接
     *
     * @param callback 回调接口
     */
    public TCPSocketConnect(TCPSocketCallback callback) {
        mSocket = new TCPSocketFactory(callback);// 创建socket连接
        writeRunnable = new WriteRunnable();// 创建发送线程
    }

    @Override
    public void run() {
        if (ip == null || port == -1) {
            return;
        }
        isWorking = true;
        while (isWorking) {
            synchronized (lock) {
                try {
                    Log.i("TCP", ">TCP连接服务器<");
                    Log.i("TCP", ">ip /port: <" + ip + " / " + port);
                    mSocket.connect(ip, port);// 连接服务器
                } catch (Exception e) {
                    try {
                        Log.e("TCP", ">TCP连接服务器失败, 6秒后重新连接<");
                        resetConnect(1);// 断开连接
                        lock.wait(6000);
                        continue; //等待6s 继续循环连接.
                    } catch (InterruptedException e1) {
                        continue;
                    }
                }
            }
            Log.e("TCP", ">TCP连接服务器成功<");

            isWrite = true;// 设置可发送数据
            new Thread(writeRunnable).start();// 启动发送线程
            try {
                mSocket.read();// 获取数据

            } catch (Exception e) {
                Log.e("TCP", ">TCP连接异常<", e);
            } finally {
                Log.e("TCP", ">TCP连接中断<");
                resetConnect(2);// 断开连接
            }
        }
        Log.e("TCP", ">=TCP结束连接线程=<");
    }

    /**
     * 关闭服务器连接
     */
    public void disconnect() {

        Log.i("TCP", "disconnect: dddddddddddd");
        synchronized (lock) {
            isWorking = false;
            lock.notify();
            resetConnect(3);
        }
    }

    /**
     * 重置连接
     */
    public void resetConnect(int flag) {
        Log.w("TCP", ">TCP重置连接<  flag:  " + flag);
        writeRunnable.stop();// 发送停止信息
        mSocket.disconnect();
    }

    /**
     * 向发送线程写入发送数据
     */
    public void write(byte[] buffer) {
        writeRunnable.write(buffer);
    }

    /**
     * 设置IP和端口
     *
     * @param host ip
     * @param port 端口
     */
    public void setAddress(String host, int port) {
        this.ip = host;
        this.port = port;
    }

    /**
     * 发送数据
     */
    private boolean writes(byte[] buffer) {
        try {
            mSocket.write(buffer);
            Thread.sleep(1);
            return true;
        } catch (Exception e) {
            resetConnect(4);
            return false;
        }
    }

    /**
     * 发送线程
     *
     * @author Esa
     */
    private class WriteRunnable implements Runnable {
        private Object wlock = new Object();// 发送线程锁对象

        @Override
        public void run() {
            Log.e("TCP", ">TCP发送线程开启<");
            while (isWrite) {
                synchronized (wlock) {
                    if (datas.size() <= 0) {
                        try {
                            wlock.wait();// 等待发送数据
                        } catch (InterruptedException e) {
                            continue;
                        }
                    }
                    while (datas.size() > 0) {
                        byte[] buffer = datas.remove(0);// 获取一条发送数据
                        if (isWrite) {
                            writes(buffer);// 发送数据
                        } else {
                            wlock.notify();
                        }
                    }
                }
            }
            Log.e("TCP", ">TCP发送线程结束<");
        }

        /**
         * 添加数据到发送队列
         *
         * @param buffer 数据字节
         */
        public void write(byte[] buffer) {
            if (!isWrite) return;
            synchronized (wlock) {
                datas.add(buffer);// 将发送数据添加到发送队列
                wlock.notify();// 取消等待
            }
        }

        public void stop() {
            synchronized (wlock) {
                isWrite = false;
                wlock.notify();
            }
        }

    }
}

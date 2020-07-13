package com.z.lionel.zutil.socket.tcp;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;

/**
 * Socket连接操作类
 *
 * @author Esa
 */
public class TCPSocketFactory {

    private Socket mSocket;// socket连接对象
    private DataOutputStream out;
    private DataInputStream in;// 输入流
    private byte[] buffer = new byte[1024];// 缓冲区字节数组，信息不能大于此缓冲区
    private byte[] tmpBuffer;// 临时缓冲区
    private TCPSocketCallback callback;// 信息回调接口
    private int timeOut = 1000 * 30;

    private SocketAddress portBind;

    /**
     * 构造方法传入信息回调接口对象
     *
     * @param callback 回调接口
     */
    public TCPSocketFactory(TCPSocketCallback callback) {
        mSocket = new Socket();
        this.callback = callback;
    }

    public boolean bind(SocketAddress address) {
        try {
            mSocket.bind(address);
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    public Socket initSocket() {
        Socket socket = new Socket();
        if (portBind != null) {
            try {
                socket.bind(portBind);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return socket;
    }

    /**
     * 连接网络服务器
     *
     * @throws UnknownHostException
     * @throws IOException
     */
    public void connect(String ip, int port) throws Exception {
        if (mSocket == null) mSocket = initSocket();
        SocketAddress address = new InetSocketAddress(ip, port);
        mSocket.connect(address, timeOut);// 连接指定IP和端口
        if (isConnected()) {
            out = new DataOutputStream(mSocket.getOutputStream());// 获取网络输出流
            in = new DataInputStream(mSocket.getInputStream());// 获取网络输入流
            if (isConnected()) {
                callback.tcp_connected();
            }
        }
    }

    public void setTimeOut(int timeOut) {
        this.timeOut = timeOut;
    }

    /**
     * 返回连接服是否成功
     *
     * @return
     */
    public boolean isConnected() {
        if (mSocket == null || mSocket.isClosed()) {
            return false;
        }
        return mSocket.isConnected();
    }

    /**
     * 发送数据
     *
     * @param buffer 信息字节数据
     * @throws IOException
     */
    public void write(byte[] buffer) throws IOException {
        if (out != null) {
            out.write(buffer);
            out.flush();
        }
    }

    /**
     * 断开连接
     *
     * @throws IOException
     */
    public void disconnect() {
        try {
            if (mSocket != null) {
                if (!mSocket.isInputShutdown()) {
                    mSocket.shutdownInput();
                }
                if (!mSocket.isOutputShutdown()) {
                    mSocket.shutdownOutput();
                }
            }
            if (out != null) {
                out.close();
            }
            if (in != null) {
                in.close();
            }
            if (mSocket != null && !mSocket.isClosed()) {// 判断socket不为空并且是连接状态
                mSocket.close();// 关闭socket
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            callback.tcp_disconnect();
            out = null;
            in = null;
            mSocket = null;// 制空socket对象
        }
    }

    /**
     * 读取网络数据
     *
     * @throws IOException
     */
    public void read() throws IOException {
        byte[] temp;
        if (in != null) {
            int len = 0;// 读取长度
            while ((len = in.read(buffer)) > 0) {
                temp = new byte[len];
                System.arraycopy(buffer, 0, temp, 0, len);
                callback.tcp_receive(temp);// 调用回调接口传入得到的数据
            }
        }
    }

}

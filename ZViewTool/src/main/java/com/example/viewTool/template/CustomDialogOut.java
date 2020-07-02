package com.example.viewTool.template;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Size;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;


import com.example.viewTool.R;

/**
 * 自定义控件-自定义对话框
 *
 * @author Ives
 */
public class CustomDialogOut extends Dialog {
    public Context context;
    public View mView;

    /**
     * 默认不 触碰dialog  以外的部分无效...
     */
    private boolean allowTouchOutSide = false;


    CustomDialogOut(Context context) {
        this(context, R.style.mydialogstyle);
    }

    /**
     * @param context
     */
    CustomDialogOut(Context context, View view) {
        super(context, R.style.mydialogstyle);    //自定义style主要去掉标题，标题将在setCustomView中自定义设置
        this.context = context;
        this.mView = view;
        setCustomView();
    }


    /**
     * @param context
     */
    CustomDialogOut(Context context, View view,  int theme) {
        super(context, theme);    //自定义style主要去掉标题，标题将在setCustomView中自定义设置
        this.context = context;
        this.mView = view;
        setCustomView();
    }

    CustomDialogOut(Context context, View view, OnCancelListener cancelListener) {
        super(context, R.style.mydialogstyle);
        this.context = context;
        this.mView = view;
        this.setOnCancelListener(cancelListener);
        setCustomView(view);
    }

    CustomDialogOut(Context context, int theme) {
        super(context, theme);
        this.context = context;
        //        setCustomView();
    }

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

/*
//去掉activity标题

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.dialog);
//设置标题
        imageView = (ImageView)findViewById(R.id.dialog_image);
        */
    }

    public void setAllowTouchOutside(boolean isAllow) {
        this.allowTouchOutSide = isAllow;
    }

    /**
     * @param event 监听dialog 触屏事件, 在外部触屏是,取消焦点
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        /* 触摸外部弹窗 */
        if (isOutOfBounds(getContext(), event) && allowTouchOutSide) {
            //            onWindowFocusChanged(false);
            /*
             * 取消当前焦点!!!!
             * */
            setFocusable(false);
        } else
            setFocusable(true);
        return super.onTouchEvent(event);
    }


    public void setFocusable(boolean focusable) {
        if (focusable)
            getCurrentFocus();
        else
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
    }

    private boolean isOutOfBounds(Context context, MotionEvent event) {
        final int x = (int) event.getX();
        final int y = (int) event.getY();
        final int slop = ViewConfiguration.get(context).getScaledWindowTouchSlop();
        final View decorView = getWindow().getDecorView();
        return (x < -slop) || (y < -slop) || (x > (decorView.getWidth() + slop)) || (y >
                (decorView.getHeight() + slop));
    }

    /**
     * 监听返回键按钮
     * 当 isShowing() is true 时,按其他Activity标签 ,再返回时, 再次打开对话框, 无法监听.
     * 改用Activity监听返回键
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    /**
     * 设置整个弹出框的视图
     */
    public void setCustomView(View mView) {
       /* Window w = getWindow();
        w.setContentView(mView);*/
        if (mView.getParent() != null) {
            ViewGroup parent = (ViewGroup) mView.getParent();
            parent.removeView(mView);
        }
        super.setContentView(mView);
        setCanceledOnTouchOutside(false);
//        this.getWindow().setBackgroundDrawable(new BitmapDrawable());
        //        super.setCancelable(false);
    }


    /**
     * 设置整个弹出框的视图
     */
    private void setCustomView() {
       /* Window w = getWindow();
        w.setContentView(mView);*/
        super.setContentView(mView);
        setCanceledOnTouchOutside(false);
//        this.getWindow().setBackgroundDrawable(new BitmapDrawable());
        //        super.setCancelable(false);
    }


    public void setWindowSize(int width, int height) {
        // setContentView可以设置为一个View也可以简单地指定资源ID
        // LayoutInflater
        // li=(LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        // View v=li.inflate(R.layout.dialog_layout, null);
        // dialog.setContentView(v);
        //        dialog.setContentView(R.layout.ba_list);
        //        dialog.setTitle("Custom Dialog");
        /*
         * 获取圣诞框的窗口对象及参数对象以修改对话框的布局设置,
         * 可以直接调用getWindow(),表示获得这个Activity的Window
         * 对象,这样这可以以同样的方式改变这个Activity的属性.
         */
        Window dialogWindow = super.getWindow();
       /* WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.BOTTOM);*/
        /*
         * lp.x与lp.y表示相对于原始位置的偏移.
         * 当参数值包含Gravity.LEFT时,对话框出现在左边,所以lp.x就表示相对左边的偏移,负值忽略.
         * 当参数值包含Gravity.RIGHT时,对话框出现在右边,所以lp.x就表示相对右边的偏移,负值忽略.
         * 当参数值包含Gravity.TOP时,对话框出现在上边,所以lp.y就表示相对上边的偏移,负值忽略.
         * 当参数值包含Gravity.BOTTOM时,对话框出现在下边,所以lp.y就表示相对下边的偏移,负值忽略.
         * 当参数值包含Gravity.CENTER_HORIZONTAL时
         * ,对话框水平居中,所以lp.x就表示在水平居中的位置移动lp.x像素,正值向右移动,负值向左移动.
         * 当参数值包含Gravity.CENTER_VERTICAL时
         * ,对话框垂直居中,所以lp.y就表示在垂直居中的位置移动lp.y像素,正值向右移动,负值向左移动.
         * gravity的默认值为Gravity.CENTER,即Gravity.CENTER_HORIZONTAL |
         * Gravity.CENTER_VERTICAL.
         *
         * 本来setGravity的参数值为Gravity.LEFT | Gravity.TOP时对话框应出现在程序的左上角,但在
         * 我手机上测试时发现距左边与上边都有一小段距离,而且垂直坐标把程序标题栏也计算在内了,
         * Gravity.LEFT, Gravity.TOP, Gravity.BOTTOM与Gravity.RIGHT都是如此,据边界有一小段距离
         */
        //        lp.x = 100; // 新位置X坐标
        //        lp.y = 20; // 新位置Y坐标
        //        lp.width = 300; // 宽度
        //        lp.height = 300; // 高度
        //        lp.alpha = 0.7f; // 透明度

        // 当Window的Attributes改变时系统会调用此函数,可以直接调用以应用上面对窗口参数的更改,也可以用setAttributes
        // dialog.onWindowAttributesChanged(lp);
        //        dialogWindow.setAttributes(lp);
        /*
         * 将对话框的大小按屏幕大小的百分比设置
         */
        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        p.height = height;
        p.width = width;
        dialogWindow.setAttributes(p);
    }


    public void setWindowSize(Size windowSize) {
        Window window = getWindow();
        WindowManager.LayoutParams p = window.getAttributes(); // 获取对话框当前的参数值
        p.height = windowSize.getHeight();
        p.width = windowSize.getWidth();
        window.setAttributes(p);
    }

    /**
     * @param gravity
     * @param x
     * @param y
     */
    public void setPosition(int gravity, int x, int y) {
        Window dialogWindow = super.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(gravity);
        lp.x = x;
        lp.y = y;
        dialogWindow.setAttributes(lp);
    }

    @Override
    public void setContentView(View view) {
        //重写本方法，使外部调用时不可破坏控件的视图。
        //也可以使用本方法改变CustomDialog的内容部分视图，比如让用户把内容视图变成复选框列表，图片等。这需要获取mView视图里的其它控件
        setCustomView(view);
    }


    @Override
    public void setTitle(int titleId) {
        setTitle(getContext().getString(titleId));
    }


    /**
     * @param v 测量指定View尺寸
     */
    public int[] getViewSize(View v) {
        int[] size = new int[2];
        //创建测量的规格. (初始化测量单位?)
        int width = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int height = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        v.measure(width, height);
        height = v.getMeasuredHeight();
        width = v.getMeasuredWidth();
        size[0] = width;
        size[1] = height;
        return size;
    }

    /**
     * 测量得到Dialog的宽度
     *
     * @return
     */
    public int getDialogWidth() {
        int width = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int height = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        mView.measure(width, height);
        return mView.getMeasuredWidth();
    }

    /**
     * 测量得到Dialog的高度
     *
     * @return
     */
    public int getDialogHeight() {
        int width = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int height = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        mView.measure(width, height);
        return mView.getMeasuredHeight();
    }

}

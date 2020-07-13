package com.z.lionel.zutil.custom

import android.text.Editable
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.widget.EditText


/**
 * 将函数的形式 改变一下,  用 lamda 来确认 ...
 */
fun EditText.addTextWatcher(callback: (String) -> Unit) {
    this.addTextChangedListener(MyTextWatcher(callback))
}

var EditText.content: String
    set(value) {
        setText(value)
    }
    get() = text.toString()


/**
 * 当编辑栏输入密码时, 可以调用此方法
 */
fun EditText.passWordVisible(isVisible: Boolean) {
    transformationMethod = when (isVisible) {
        true -> HideReturnsTransformationMethod.getInstance()
        false -> PasswordTransformationMethod.getInstance()
    }
}


class MyTextWatcher(val callback: (String) -> Unit) : TextWatcher {

    //只让实现者 实现此功能...
    override fun afterTextChanged(s: Editable?) {
        callback(s.toString())
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
    }
}



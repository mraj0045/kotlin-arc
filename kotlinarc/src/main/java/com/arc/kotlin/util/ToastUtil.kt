package com.arc.kotlin.util

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes

object ToastUtil {

    internal fun toast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    internal fun toast(context: Context, @StringRes message: Int) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}

package com.arc.kotlin.util.extensions

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.provider.Settings
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.arc.kotlin.util.ToastUtil

/**
 * Shows Toast message
 *
 * @param message Message to display
 */
fun Context?.toast(@StringRes message: Int) {
    if (this == null) return
    ToastUtil.toast(this, message)
}

/**
 * Shows Toast message
 *
 * @param message Message to display
 */
fun Context?.toast(message: String) {
    if (this == null) return
    ToastUtil.toast(this, message)
}

/**
 * Checks whether device is connected to network or not
 * @return true-> if connected, false-> if not connected
 */
fun Context?.isOnline(): Boolean {
    this?.run {
        val cm: ConnectivityManager? = getSystemService(Context.CONNECTIVITY_SERVICE) as
                ConnectivityManager
        val netWorkInfo: NetworkInfo? = cm?.activeNetworkInfo
        return netWorkInfo != null && netWorkInfo.isConnected
    }
    return false
}

/** Hides the keyboard from the user
 * @param view */
fun Context?.hideKeyboard(view: View) {
    val imm = this?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
    imm?.hideSoftInputFromWindow(view.windowToken, 0)
}

/**Displays the keyboard to the user
 * @param view */
fun Context?.showKeyboard(view: View) {
    val imm = this?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
    imm?.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
}

/** Returns whether the current device is Tablet or not*/
fun Context?.isTablet(): Boolean {
    if (this == null) return false
    return (resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE
}

/** Returns whether the current device is Mobile or not*/
fun Context?.isMobile(): Boolean {
    if (this == null) return false
    return (resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK) <= Configuration.SCREENLAYOUT_SIZE_NORMAL
}

/** Returns the unique device ID of the Android Mobile  */
@SuppressLint("HardwareIds")
fun Context?.getDeviceId(): String {
    return Settings.Secure.getString(this?.contentResolver, Settings.Secure.ANDROID_ID)
}

/** Starts the specified activity
 * @param flag apply single or multiple flags */
inline fun <reified T> Context?.startActivity(flag: Int = -1) {
    this?.startActivity(Intent(this, T::class.java).apply {
        if (flag != -1) flags = flag
    })
}

/** Starts the specified activity
 * @param flag apply single or multiple flags */
inline fun <reified T> AppCompatActivity?.startActivity(flag: Int = -1) {
    this?.startActivity(Intent(this, T::class.java).apply {
        if (flag != -1) flags = flag
    })
}

/** Starts the specified activity
 * @param flag apply single or multiple flags */
inline fun <reified T> Fragment?.startActivity(flag: Int = -1) {
    this?.startActivity(Intent(context, T::class.java).apply {
        if (flag != -1) flags = flag
    })
}
package com.arc.kotlin.util

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Build
import android.provider.Settings
import android.util.Patterns
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.annotation.StringRes
import com.arc.kotlin.BuildConfig
import com.arc.kotlin.util.formatter.DateFormatter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONArray
import org.json.JSONObject
import java.lang.reflect.Type
import java.util.*

inline fun debug(code: () -> Unit) {
    if (BuildConfig.DEBUG) {
        code()
    }
}

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
fun Context.isTablet() =
    (resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE

/** Returns whether the current device is Tablet or not*/
fun Context.isMobile() =
    (resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK) <= Configuration.SCREENLAYOUT_SIZE_NORMAL

/**Formats the date with the given pattern and returns string
 * @param pattern Pattern to format(e.g, yyyy-MM-dd)
 */
fun Date.format(pattern: String): String = DateFormatter().format(this, pattern)

/**Formats the String to Date Object
 * @param pattern Pattern to format(e.g, yyyy-MM-dd)
 */
fun String.format(pattern: String): Date? = DateFormatter().format(this, pattern)

/**Formats the String to specified pattern
 * @param fromPattern Pattern to format(e.g, yyyy-MM-dd)
 * @param toPattern Pattern to format(e.g, yyyy-MM-dd)
 */
fun String.format(fromPattern: String, toPattern: String): String =
    DateFormatter().format(this, fromPattern, toPattern)

/**Formats the date with the given pattern and returns string. With TimeZone Id
 * @param pattern Pattern to format(e.g, yyyy-MM-dd)
 */
fun Date.formatTz(timeZoneId: String, pattern: String): String =
    DateFormatter(timeZoneId).format(this, pattern)

/**Formats the String to Date Object. With TimeZone Id
 * @param pattern Pattern to format(e.g, yyyy-MM-dd)
 */
fun String.formatTz(timeZoneId: String, pattern: String): Date? =
    DateFormatter(timeZoneId).format(this, pattern)

/**Formats the String to specified pattern. With TimeZone Id
 * @param fromPattern Pattern to format(e.g, yyyy-MM-dd)
 * @param toPattern Pattern to format(e.g, yyyy-MM-dd)
 */
fun String.formatTz(timeZoneId: String, fromPattern: String, toPattern: String): String =
    DateFormatter(timeZoneId).format(this, fromPattern, toPattern)

/** Safe converts to int*/
fun String?.int(): Int = try {
    this!!.toInt()
} catch (e: NumberFormatException) {
    0
} catch (e: NullPointerException) {
    0
}

/** Safe converts to float*/
fun String?.float(): Float = try {
    this!!.toFloat()
} catch (e: NumberFormatException) {
    0f
} catch (e: NullPointerException) {
    0f
}

/** Safe converts to double*/
fun String?.double(): Double = try {
    this!!.toDouble()
} catch (e: NumberFormatException) {
    0.0
} catch (e: NullPointerException) {
    0.0
}

/** Safe converts to long*/
fun String?.long(): Long = try {
    this!!.toLong()
} catch (e: NumberFormatException) {
    0L
} catch (e: NullPointerException) {
    0L
}

/** Checks whether the email is valid or not*/
fun String?.isLegalEmail(): Boolean = this != null && Patterns.EMAIL_ADDRESS.matcher(this).matches()

/** Checks whether the password is legal password or not.
 *
 * Password should include at-least one Uppercase letter and number, max length 16 and min length 8*/
fun String?.isLegalPassword(): Boolean = this != null
        && this.trim().isNotEmpty()
        && matches(Regex(".*[A-Z].*"))
        && matches(Regex(".*[a-z].*"))
        && matches(Regex(".*[0-9].*"))
        && length >= 8
        && length < 17

/**
 * Creates SHA1 for passed value
 */
fun String.sha1(): String = Security.SHA1(this)


/** Initializes a [JSONArray] block*/
fun jsonArray(init: JSONArray.() -> Unit): JSONArray {
    val jsonArray = JSONArray()
    jsonArray.init()
    return jsonArray
}

/** Initializes a [JSONObject] block*/
fun jsonObject(init: JSONObject.() -> Unit): JSONObject {
    val jsonObject = JSONObject()
    jsonObject.init()
    return jsonObject
}

/**Returns whether the object is null or not*/
fun Any?.isNull(): Boolean = this == null

/**Returns whether the object is not null or not*/
fun Any?.isNotNull(): Boolean = !isNull()

/** Returns the unique device ID of the Android Mobile  */
@SuppressLint("HardwareIds")
fun Context?.getDeviceId(): String {
    return Settings.Secure.getString(this?.contentResolver, Settings.Secure.ANDROID_ID)
}

/** Returns the manufacturer device model  */
fun getDeviceModel(): String {
    return Build.MODEL
}

/** Returns the manufacturer */
fun getManufacturer() = Build.MANUFACTURER

/** Returns the version string */
fun getVersion(): String = Build.VERSION.RELEASE

/** Parses Json to POJO class as given in the generic type
 * @param value Data to be parsed */
inline fun <reified T> Gson.fromJson(value: String): T {
    return fromJson(value, toTypeToken<T>())
}

/** Returns the type of the class*/
inline fun <reified T> toTypeToken(): Type {
    return object : TypeToken<T>() {}.type
}

/** Checks whether the list is empty or not
 * @return true -> if null or empty, false -> if not empty */
fun <T> List<T>?.isNullOrEmpty(): Boolean {
    return this == null || isEmpty()
}

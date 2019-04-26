package com.arc.kotlin.util

import android.content.Context
import android.os.Build
import androidx.annotation.StyleRes
import androidx.appcompat.app.AlertDialog
import com.arc.kotlin.BuildConfig
import com.google.gson.reflect.TypeToken
import org.json.JSONArray
import org.json.JSONObject
import java.lang.reflect.Type

inline fun debug(code: () -> Unit) {
    if (BuildConfig.DEBUG) {
        code()
    }
}

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

/** Returns the manufacturer device model  */
fun getDeviceModel(): String {
    return Build.MODEL
}

/** Returns the manufacturer */
fun getManufacturer() = Build.MANUFACTURER

/** Returns the version string */
fun getVersion(): String = Build.VERSION.RELEASE

/** Returns the type of the class*/
inline fun <reified T> toTypeToken(): Type {
    return object : TypeToken<T>() {}.type
}

/** Creates Alert dialog.
 * @param context App context
 * @param themeResId Theme style resource id. Default is 0
 * @param show true-> will displays the dialog & returns dialog object. false-> returns dialog object
 * @param init Initializes dialog builder block*/
fun alertDialog(
    context: Context, @StyleRes themeResId: Int = 0,
    show: Boolean = false,
    init: AlertDialog.Builder.() -> Unit
): AlertDialog {
    val builder = AlertDialog.Builder(context, themeResId)
    builder.init()
    return if (show) builder.show() else builder.create()
}

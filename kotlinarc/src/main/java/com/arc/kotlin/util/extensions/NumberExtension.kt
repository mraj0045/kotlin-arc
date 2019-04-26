package com.arc.kotlin.util.extensions

import java.text.DecimalFormat

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

/** Returns the string representation of the value using [DecimalFormat]
 * @param pattern conversion pattern for [DecimalFormat]*/
fun Double?.toCurrency(pattern: String = "0.##"): String {
    if (this == null) return "0"
    return DecimalFormat(pattern).format(this)
}

/** Returns the string representation of the value using [DecimalFormat]
 * @param pattern conversion pattern for [DecimalFormat]*/
fun Float?.toCurrency(pattern: String = "0.##"): String {
    if (this == null) return "0"
    return DecimalFormat(pattern).format(this)
}
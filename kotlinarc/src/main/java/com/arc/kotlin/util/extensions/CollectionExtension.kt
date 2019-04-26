package com.arc.kotlin.util.extensions

/** Checks whether the list is empty or not
 * @return true -> if null or empty, false -> if not empty */
fun <T> List<T>?.isNullOrEmpty(): Boolean {
    return this == null || isEmpty()
}
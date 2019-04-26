package com.arc.kotlin.util.extensions

import com.arc.kotlin.util.toTypeToken
import com.google.gson.Gson
import com.google.gson.JsonElement

/** Parses Json to POJO class as given in the generic type
 * @param value Data to be parsed */
inline fun <reified T> Gson.fromJson(value: String): T {
    return fromJson(value, toTypeToken<T>())
}

/** Parses Json to POJO class as given in the generic type
 * @param value Data to be parsed */
inline fun <reified T> Gson.fromJson(value: JsonElement): T {
    return fromJson(value, toTypeToken<T>())
}
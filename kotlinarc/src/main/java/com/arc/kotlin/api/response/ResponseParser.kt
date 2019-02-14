package com.arc.kotlin.api.response

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response
import retrofit2.Retrofit
import java.io.IOException

/** Validates the whether the given value is json or not
 * @param value data to be validated
 * @return true-> valid json, otherwise false*/
fun isJSONValid(value: String): Boolean {
    try {
        JSONObject(value)
    } catch (ex: JSONException) {
        // edited, to include @Arthur's comment
        // e.g. in case JSONArray is valid as well...
        try {
            JSONArray(value)
        } catch (ex1: JSONException) {
            return false
        }
    }
    return true
}

/** Parses the error data from the response
 * @param response [Response] instance
 * @return [ApiError] instance */
fun Retrofit.parseError(response: Response<*>?): ApiError {
    val converter = responseBodyConverter<ApiError>(ApiError::class.java, arrayOfNulls(0))

    var error: ApiError? = null

    try {
        if (response?.errorBody() != null) {
            try {
                val errorString = response.errorBody()!!.string()
                error = if (isJSONValid(errorString)) {
                    converter.convert(response.errorBody()!!)
                } else
                    ApiError(response.code(), errorString)
            } catch (e: NullPointerException) {
                e.printStackTrace()
            }
        }
    } catch (ignored: IOException) {
    }
    if (error == null) error = ApiError.error()
    return error
}
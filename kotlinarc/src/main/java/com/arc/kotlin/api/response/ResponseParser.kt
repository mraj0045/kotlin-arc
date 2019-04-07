package com.arc.kotlin.api.response

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.io.IOException

private const val NO_DATA_FOUND = "No Data Found"

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
        // Unable to parse response
    }
    if (error == null) error = ApiError.error()
    return error
}

/** Executes call in the background or worker thread
 * @param success Success callback
 * @param authFailure Called when authentication fails
 * @param error Called when error
 * @param pError function to parse error*/
fun <T> Call<T>.enqueue(
    success: (t: T) -> Unit,
    authFailure: (() -> Unit)? = null,
    error: ApiError.() -> Unit,
    pError: (response: Response<*>?) -> ApiError
) {
    enqueue(object : Callback<T> {
        override fun onResponse(call: Call<T>, response: Response<T>) {
            if (response.isSuccessful) {
                parseResponse(response, success, error)
            } else {
                parseErrorResponse(response, authFailure, error, pError)
            }
        }

        override fun onFailure(call: Call<T>, t: Throwable) {
            error(if (t is IOException) ApiError.noInternet() else ApiError.error())
        }
    })
}

/** Executes the call in the main thread.
 * @param success Success callback
 * @param authFailure Called when authentication fails
 * @param error Called when error
 * @param pError function to parse error */
fun <T> Call<T>.execute(
    success: (t: T) -> Unit,
    authFailure: (() -> Unit)? = null,
    error: ApiError.() -> Unit,
    pError: (response: Response<*>?) -> ApiError
) {
    try {
        val response = execute()
        if (response.isSuccessful) {
            parseResponse(response, success, error)
        } else {
            parseErrorResponse(response, authFailure, error, pError)
        }

    } catch (e: IOException) {
        error(ApiError.noInternet())
    }
}

/** Handles success response
 * @param response [Response] instance
 * @param success Success callback
 * @param error Called when error
 * */
private fun <T> parseResponse(
    response: Response<T>,
    success: (t: T) -> Unit,
    error: ApiError.() -> Unit
) {
    val apiResponse = response.body()
    if (apiResponse == null) {
        error(ApiError.error(NO_DATA_FOUND))
        return
    }
    if (apiResponse is ApiResponse<*>) {
        if (apiResponse.isError) apiResponse.error?.run(error)
        else success(apiResponse)
    } else error(ApiError.error(NO_DATA_FOUND))
}

/** Handles the error response.
 * @param response [Response] instance
 * @param authFailure Called when authentication fails
 * @param error Called when error
 * @param pError function to parse error */
private fun <T> parseErrorResponse(
    response: Response<T>,
    authFailure: (() -> Unit)?,
    error: ApiError.() -> Unit,
    pError: (response: Response<*>?) -> ApiError
) {
    if (response.code() == StatusCode.AUTH_ERROR) {
        authFailure?.invoke()
    } else {
        var apiError: ApiError? = null
        if (response.errorBody() != null) apiError = pError(response)
        if (apiError == null)
            apiError = ApiError(response.code(), NO_DATA_FOUND)
        error(apiError)
    }
}
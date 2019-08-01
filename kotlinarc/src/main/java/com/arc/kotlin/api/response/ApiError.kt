package com.arc.kotlin.api.response

import com.arc.kotlin.BuildConfig
import com.arc.kotlin.api.response.StatusCode.DEFAULT
import com.arc.kotlin.api.response.StatusCode.NO_DATA
import com.arc.kotlin.api.response.StatusCode.NO_INTERNET
import com.google.gson.annotations.SerializedName

/** Class used for parsing error response from the API
 * @param code Error code, Serialized name [BuildConfig.ERROR_KEY]
 * @param reason Error message*/
class ApiError(
    @SerializedName(value = "errorcode") val code: Int, @SerializedName(
        value = "reason",
        alternate = ["error"]
    ) val reason: String
) {

    companion object {
        /** Create no internet error */
        fun noInternet(): ApiError {
            return ApiError(NO_INTERNET, "No Internet connection")
        }

        fun error(message: String? = null): ApiError {
            return ApiError(DEFAULT, message ?: "Please try again..")
        }

        fun noData(): ApiError {
            return ApiError(NO_DATA, "No data found")
        }

        /** Returns Authentication ApiError instance */
        fun authError(): ApiError = ApiError(StatusCode.AUTH_ERROR, "Session expired. Please try again...")
    }
}

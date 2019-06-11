package com.arc.kotlin.api.response

import com.arc.kotlin.api.response.StatusCode.DEFAULT
import com.arc.kotlin.api.response.StatusCode.NO_DATA
import com.arc.kotlin.api.response.StatusCode.NO_INTERNET
import com.google.gson.annotations.SerializedName

class ApiError(
    @SerializedName(value = "errorcode") val code: Int, @SerializedName(
        value = "reason",
        alternate = ["error"]
    ) val reason: String
) {

    companion object {
        fun noInternet(): ApiError {
            return ApiError(NO_INTERNET, "No Internet connection")
        }

        fun error(message: String? = null): ApiError {
            return ApiError(DEFAULT, message ?: "Please try again..")
        }

        fun noData(): ApiError {
            return ApiError(NO_DATA, "No data found")
        }
    }
}

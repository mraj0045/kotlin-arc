package com.architecture.api.request

import android.content.Context
import androidx.annotation.CheckResult
import com.arc.kotlin.api.response.ApiError
import com.arc.kotlin.api.response.ApiResponse
import com.arc.kotlin.api.response.StatusCode
import com.arc.kotlin.api.response.parseError
import com.arc.kotlin.util.isOnline
import com.architecture.model.Post
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.ssl.SSLException

class ApiHandler(private val context: Context, private val mApi: Api, private val retrofit: Retrofit) {

    @CheckResult
    fun getPosts(success: (List<Post>) -> Unit, authFailure: (() -> Unit)? = null, error: (ApiError) -> Unit):
            Call<ApiResponse<Post>>? {
        if (!context.isOnline()) {
            error(ApiError.noInternet())
            return null
        }

        val call = mApi.getPosts()
        call.enqueue(
            object : Callback<ApiResponse<Post>> {
                override fun onResponse(
                    call: Call<ApiResponse<Post>>,
                    response: Response<ApiResponse<Post>>
                ) {
                    if (response.isSuccessful) {
                        val apiResponse = response.body()
                        if (apiResponse != null) {
                            if (!apiResponse.isError) {
                                if (apiResponse.list.isEmpty())
                                    error(ApiError.noData())
                                else
                                    success(apiResponse.list)
                            } else {
                                error(apiResponse.error!!)
                            }
                        } else {
                            error(ApiError.noData())
                        }
                    } else {
                        if (response.code() == StatusCode.AUTH_ERROR) {
                            authFailure?.invoke()
                        } else {
                            var apiError: ApiError? = null
                            if (response.errorBody() != null) apiError = retrofit.parseError(response)
                            if (apiError == null)
                                error(ApiError.noData())
                        }
                    }
                }

                override fun onFailure(call: Call<ApiResponse<Post>>, t: Throwable) {
                    when (t) {
                        is SSLException, is SocketException, is SocketTimeoutException, is UnknownHostException ->
                            error(ApiError.noInternet())
                        else -> {
                            error(ApiError.error())
                        }
                    }
                }
            })
        return call
    }
}
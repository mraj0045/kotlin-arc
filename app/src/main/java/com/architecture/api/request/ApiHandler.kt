package com.architecture.api.request

import android.content.Context
import androidx.annotation.CheckResult
import com.arc.kotlin.api.response.ApiError
import com.arc.kotlin.api.response.ApiResponse
import com.arc.kotlin.api.response.enqueue
import com.arc.kotlin.api.response.parseError
import com.arc.kotlin.util.isOnline
import com.architecture.model.Post
import retrofit2.Call
import retrofit2.Retrofit

class ApiHandler(private val context: Context, private val mApi: Api, private val retrofit: Retrofit) {

    @CheckResult
    fun getPosts(success: (List<Post>) -> Unit, error: (ApiError) -> Unit):
            Call<ApiResponse<Post>>? {
        if (!context.isOnline()) {
            error(ApiError.noInternet())
            return null
        }

        val call = mApi.getPosts()
        call.enqueue(
            success = { success.invoke(it.list) },
            error = { error.invoke(this) },
            pError = { retrofit.parseError(it) })
        return call
    }
}
package com.architecture.api.request

import com.arc.kotlin.api.response.ApiResponse
import com.architecture.model.Post
import retrofit2.Call
import retrofit2.http.GET

interface Api {

    @GET("/posts")
    fun getPosts(): Call<ApiResponse<Post>>

    @GET("/mraj0045/ApiResponse/errorResponse/")
    fun getErrorResponse(): Call<ApiResponse<Post>>
}
package com.architecture.injection.module

import android.content.Context
import com.arc.kotlin.api.cookie.CookieGenerator
import com.arc.kotlin.api.response.ApiResponse
import com.arc.kotlin.api.response.CustomJsonDeserializer
import com.arc.kotlin.api.response.DateDeserializer
import com.arc.kotlin.inject.scope.AppContext
import com.arc.kotlin.util.toTypeToken
import com.architecture.api.request.Api
import com.architecture.api.request.ApiHandler
import com.architecture.model.Post
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.JavaNetCookieJar
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type
import java.util.*
import java.util.concurrent.TimeUnit

@Module
class ApiModule {

    @Provides
    fun providesApiHandler(
        @AppContext context: Context, api: Api, retrofit: Retrofit
    ): ApiHandler {
        return ApiHandler(context, api, retrofit)
    }

    @Provides
    fun providesApi(retrofit: Retrofit): Api {
        return retrofit.create(Api::class.java)
    }

    @Provides
    fun providesRetrofit(httpClient: OkHttpClient, factory: Converter.Factory): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://jsonplaceholder.typicode.com")
            .client(httpClient)
            .addConverterFactory(factory)
            .build()
    }

    @Provides
    fun providesOkHttpClient(cookieGenerator: CookieGenerator): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient.Builder()
            .readTimeout(30, TimeUnit.SECONDS)
            .connectTimeout(30, TimeUnit.SECONDS)
            .cookieJar(JavaNetCookieJar(cookieGenerator.cookieHandler))
            .addInterceptor(interceptor)
            .build()
    }

    @Provides
    fun providesGsonFactory(gson: Gson): Converter.Factory {
        return GsonConverterFactory.create(gson)
    }

    @Provides
    fun providesGson(): Gson {
        return GsonBuilder()
            .registerTypeAdapter(Date::class.java, DateDeserializer())
            .registerTypeAdapter(postType, CustomJsonDeserializer<Post>())
            .create()
    }

    private val postType: Type = toTypeToken<ApiResponse<Post>>()
}

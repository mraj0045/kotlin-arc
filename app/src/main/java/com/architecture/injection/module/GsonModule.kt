package com.architecture.injection.module

import com.arc.kotlin.api.response.ApiResponse
import com.arc.kotlin.api.response.CustomJsonDeserializer
import com.arc.kotlin.api.response.DateDeserializer
import com.arc.kotlin.util.toTypeToken
import com.architecture.model.Post
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import retrofit2.Converter
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type
import java.util.*
import javax.inject.Singleton

@Module
class GsonModule {

    @Singleton
    @Provides
    fun providesGsonFactory(gson: Gson): Converter.Factory {
        return GsonConverterFactory.create(gson)
    }

    @Singleton
    @Provides
    fun providesGson(): Gson {
        return GsonBuilder()
            .registerTypeAdapter(Date::class.java, DateDeserializer())
            .registerTypeAdapter(postType, CustomJsonDeserializer<Post>())
            .create()
    }

    private val postType: Type = toTypeToken<ApiResponse<Post>>()

}
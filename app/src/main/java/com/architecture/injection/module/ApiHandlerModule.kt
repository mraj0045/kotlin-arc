package com.architecture.injection.module

import android.content.Context
import com.arc.kotlin.inject.scope.AppContext
import com.architecture.api.request.Api
import com.architecture.api.request.ApiHandler
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
class ApiHandlerModule {

    @Singleton
    @Provides
    fun providesApiHandler(@AppContext context: Context, api: Api, retrofit: Retrofit): ApiHandler {
        return ApiHandler(context, api, retrofit)
    }

    @Singleton
    @Provides
    fun providesApi(retrofit: Retrofit): Api {
        return retrofit.create(Api::class.java)
    }
}
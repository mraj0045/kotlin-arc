package com.architecture.injection.module

import com.arc.kotlin.inject.scope.BaseURL
import com.arc.kotlin.inject.scope.Cache
import com.arc.kotlin.inject.scope.LogRequest
import com.architecture.BuildConfig
import dagger.Module
import dagger.Provides

@Module
class ConfigModule {

    @BaseURL
    @Provides
    fun providesBaseUrl(): String {
        return "https://jsonplaceholder.typicode.com"
    }

    /** Enable or disables the Retrofit caching.
     * Default value is false. */
    @Cache
    @Provides
    fun providesCacheEnabled(): Boolean {
        return false
    }

    /** Enables or disables the Logging Interceptor. Default value depends on the [BuildConfig.DEBUG] value*/
    @LogRequest
    @Provides
    fun logRequestEnabled(): Boolean {
        return BuildConfig.DEBUG
    }
}
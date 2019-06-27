package com.architecture.injection.module

import com.arc.kotlin.inject.scope.BaseURL
import dagger.Module
import dagger.Provides

@Module
class ConfigModule {

    @BaseURL
    @Provides
    fun providesBaseUrl(): String {
        return "https://jsonplaceholder.typicode.com"
//        return "https://my-json-server.typicode.com"
    }
}
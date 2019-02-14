package com.arc.kotlin.inject.modules

import android.app.Application
import android.content.Context
import com.arc.kotlin.inject.scope.AppContext
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(private val app: Application) {

    @Provides
    @AppContext
    internal fun providesApplication(): Application {
        return app
    }

    @Provides
    @Singleton
    @AppContext
    internal fun providesGlobalContext(): Context {
        return app.applicationContext
    }
}

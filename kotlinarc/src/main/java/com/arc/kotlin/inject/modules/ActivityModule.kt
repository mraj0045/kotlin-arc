package com.arc.kotlin.inject.modules

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.arc.kotlin.inject.scope.ActivityScope
import com.arc.kotlin.inject.scope.ScopeContext
import dagger.Module
import dagger.Provides

@Module
class ActivityModule(private val activity: AppCompatActivity) {

    @ActivityScope
    @ScopeContext
    @Provides
    fun providesActivityContext(): Context {
        return activity
    }

}

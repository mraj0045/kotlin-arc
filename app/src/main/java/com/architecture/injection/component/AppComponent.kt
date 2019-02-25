package com.architecture.injection.component

import android.app.Application
import com.arc.kotlin.inject.modules.ActivityModule
import com.arc.kotlin.inject.modules.AppModule
import com.arc.kotlin.inject.modules.FragmentModule
import com.arc.kotlin.inject.modules.RetrofitModule
import com.architecture.injection.module.ApiHandlerModule
import com.architecture.injection.module.ConfigModule
import com.architecture.injection.module.GsonModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, RetrofitModule::class, ApiHandlerModule::class, ConfigModule::class, GsonModule::class])
interface AppComponent {

    fun activityComponent(activityModule: ActivityModule): ActivityComponent

    fun fragmentComponent(fragmentModule: FragmentModule): FragmentComponent

    fun inject(application: Application)
}
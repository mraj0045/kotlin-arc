package com.architecture.injection.component

import android.app.Application
import com.arc.kotlin.inject.modules.ActivityModule
import com.arc.kotlin.inject.modules.AppModule
import com.arc.kotlin.inject.modules.FragmentModule
import com.architecture.injection.module.ApiModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, ApiModule::class])
interface AppComponent {

    fun activityComponent(activityModule: ActivityModule): ActivityComponent

    fun fragmentComponent(fragmentModule: FragmentModule): FragmentComponent

    fun inject(application: Application)
}
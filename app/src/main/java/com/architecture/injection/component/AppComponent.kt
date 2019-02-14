package com.architecture.injection.component

import android.app.Application
import com.arc.kotlin.inject.modules.ActivityModule
import com.arc.kotlin.inject.modules.AppModule
import com.arc.kotlin.inject.modules.FragmentModule
import com.architecture.injection.module.ActivityPresenterModule
import com.architecture.injection.module.ApiModule
import com.architecture.injection.module.FragmentPresenterModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, ApiModule::class])
interface AppComponent {

    fun activityComponent(
        activityModule: ActivityModule,
        activityPresenterModule: ActivityPresenterModule
    ): ActivityComponent

    fun fragmentComponent(
        fragmentModule: FragmentModule,
        fragmentPresenterModule: FragmentPresenterModule
    ): FragmentComponent

    fun inject(application: Application)
}
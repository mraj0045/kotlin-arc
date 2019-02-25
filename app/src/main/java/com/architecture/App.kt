package com.architecture

import com.arc.kotlin.ArcApplication
import com.arc.kotlin.inject.modules.AppModule
import com.arc.kotlin.inject.modules.RetrofitModule
import com.architecture.injection.component.AppComponent
import com.architecture.injection.component.DaggerAppComponent
import com.architecture.injection.module.ApiHandlerModule
import com.architecture.injection.module.ConfigModule
import com.architecture.injection.module.GsonModule

class App : ArcApplication<AppComponent>() {

    private var component: AppComponent? = null

    override fun component(): AppComponent? {
        if (component == null) {
            component = DaggerAppComponent.builder()
                .appModule(AppModule(this))
                .retrofitModule(RetrofitModule())
                .apiHandlerModule(ApiHandlerModule())
                .gsonModule(GsonModule())
                .configModule(ConfigModule())
                .build()
        }
        return component
    }
}
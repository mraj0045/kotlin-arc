package com.architecture

import com.arc.kotlin.ArcApplication
import com.arc.kotlin.inject.modules.AppModule
import com.architecture.injection.component.AppComponent
import com.architecture.injection.component.DaggerAppComponent
import com.architecture.injection.module.ApiModule

class App : ArcApplication<AppComponent>() {

    private var component: AppComponent? = null

    override fun component(): AppComponent? {
        if (component == null) {
            component = DaggerAppComponent
                .builder()
                .appModule(AppModule(this))
                .apiModule(ApiModule())
                .build()
        }
        return component
    }
}
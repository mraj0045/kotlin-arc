package com.arc.kotlin

import android.app.Application

abstract class ArcApplication<C> : Application() {

    abstract fun component(): C?
}
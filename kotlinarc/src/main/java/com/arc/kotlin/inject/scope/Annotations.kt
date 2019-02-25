package com.arc.kotlin.inject.scope

import javax.inject.Qualifier

@MustBeDocumented
@Retention(value = AnnotationRetention.RUNTIME)
@Qualifier
annotation class BaseURL

@MustBeDocumented
@Retention(value = AnnotationRetention.RUNTIME)
@Qualifier
annotation class Cache

@MustBeDocumented
@Retention(value = AnnotationRetention.RUNTIME)
@Qualifier
annotation class LogRequest
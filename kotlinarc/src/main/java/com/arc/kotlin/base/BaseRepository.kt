package com.arc.kotlin.base

interface BaseRepository<BP : BasePresenter<*>> {
    fun cancel()
}

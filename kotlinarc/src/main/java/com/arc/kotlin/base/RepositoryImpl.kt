package com.arc.kotlin.base

abstract class RepositoryImpl<BP : BasePresenter<*>> : BaseRepository<BP> {
    abstract override fun cancel()
}

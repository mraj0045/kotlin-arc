package com.arc.kotlin.base

interface BasePresenter<BV : BaseView> {

    fun attach(view: BV)

    fun view(): BV?

    fun detach()
}

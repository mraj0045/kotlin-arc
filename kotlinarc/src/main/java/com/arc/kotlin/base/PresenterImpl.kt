package com.arc.kotlin.base

abstract class PresenterImpl<BV : BaseView> : BasePresenter<BV> {
    private var mBv: BV? = null

    override fun attach(view: BV) {
        mBv = view
    }

    override fun view(): BV? {
        return mBv
    }

    override fun detach() {
        mBv = null
    }
}

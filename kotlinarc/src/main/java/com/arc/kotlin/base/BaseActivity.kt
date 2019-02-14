package com.arc.kotlin.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dagger.Lazy
import javax.inject.Inject

abstract class BaseActivity<P : BasePresenter<*>> : AppCompatActivity() {

    @Inject
    lateinit var presenter: Lazy<P>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inject()
    }

    protected fun presenter(): P {
        return presenter.get()
    }

    protected abstract fun inject()

    override fun onDestroy() {
        presenter().detach()
        super.onDestroy()
    }
}

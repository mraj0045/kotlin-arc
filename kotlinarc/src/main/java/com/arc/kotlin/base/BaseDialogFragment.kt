package com.arc.kotlin.base

import android.os.Bundle
import androidx.fragment.app.DialogFragment
import dagger.Lazy
import javax.inject.Inject

abstract class BaseDialogFragment<P : BasePresenter<*>> : DialogFragment() {

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

    override fun onDestroyView() {
        presenter().detach()
        super.onDestroyView()
    }
}

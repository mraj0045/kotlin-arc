package com.arc.kotlin.base

import android.os.Bundle
import androidx.fragment.app.DialogFragment
import javax.inject.Inject

/**
 * MOHAN on 28-10-2017.
 */
abstract class BaseDialogFragment<P : BasePresenter<*>> : DialogFragment() {

    @Inject
    lateinit var presenter: P

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inject()
    }

    protected abstract fun inject()
}

package com.arc.kotlin.base

import android.os.Bundle
import androidx.fragment.app.Fragment
import javax.inject.Inject

/** MOHAN on 28-10-2017.  */
abstract class BaseFragment<P : BasePresenter<*>> : Fragment() {

    @Inject
    lateinit var presenter: P

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inject()
    }

    protected abstract fun inject()
}

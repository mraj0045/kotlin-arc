package com.arc.kotlin.inject.modules

import android.content.Context
import com.arc.kotlin.inject.scope.FragmentScope
import com.arc.kotlin.inject.scope.ScopeContext
import dagger.Module
import dagger.Provides

@Module
class FragmentModule(private val context: Context) {

    @ScopeContext
    @Provides
    @FragmentScope
    internal fun providesFragmentContext(): Context {
        return context
    }
}

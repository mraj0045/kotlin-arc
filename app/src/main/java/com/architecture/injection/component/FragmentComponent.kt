package com.architecture.injection.component

import com.arc.kotlin.inject.modules.FragmentModule
import com.arc.kotlin.inject.scope.FragmentScope
import com.architecture.injection.module.FragmentPresenterModule
import dagger.Subcomponent

@FragmentScope
@Subcomponent(modules = [FragmentModule::class, FragmentPresenterModule::class])
interface FragmentComponent

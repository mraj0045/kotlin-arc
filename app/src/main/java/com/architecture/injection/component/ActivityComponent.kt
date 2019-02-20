package com.architecture.injection.component

import com.arc.kotlin.inject.modules.ActivityModule
import com.arc.kotlin.inject.scope.ActivityScope
import com.architecture.injection.module.ActivityMvpModule
import com.architecture.ui.post.PostActivity
import dagger.Subcomponent

@ActivityScope
@Subcomponent(modules = [ActivityModule::class, ActivityMvpModule::class])
interface ActivityComponent {
    fun inject(postActivity: PostActivity)

}
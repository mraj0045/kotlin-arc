package com.architecture.injection.module

import com.arc.kotlin.inject.scope.ActivityScope
import com.architecture.ui.post.mvp.PostContract
import com.architecture.ui.post.mvp.PostPresenter
import com.architecture.ui.post.mvp.PostRepository
import dagger.Binds
import dagger.Module

@Module
abstract class ActivityMvpModule {

    @Binds
    @ActivityScope
    abstract fun providesPostPresenter(post: PostPresenter): PostContract.Presenter

    @Binds
    @ActivityScope
    abstract fun providesPostApi(postRepository: PostRepository): PostContract.Repository
}

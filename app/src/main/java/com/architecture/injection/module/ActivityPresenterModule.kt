package com.architecture.injection.module

import com.arc.kotlin.inject.scope.ActivityScope
import com.architecture.api.request.ApiHandler
import com.architecture.ui.post.mvp.PostContract
import com.architecture.ui.post.mvp.PostPresenter
import com.architecture.ui.post.mvp.PostRepository
import dagger.Module
import dagger.Provides

@Module
class ActivityPresenterModule {

    @Provides
    @ActivityScope
    fun providesPostPresenter(post: PostPresenter): PostContract.Presenter {
        return post
    }

    @Provides
    @ActivityScope
    fun providesPostApi(handler: ApiHandler): PostContract.Repository {
        return PostRepository(handler)
    }
}

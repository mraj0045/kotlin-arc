package com.architecture.ui.post.mvp

import com.arc.kotlin.api.response.ApiError
import com.arc.kotlin.base.BasePresenter
import com.arc.kotlin.base.BaseRepository
import com.arc.kotlin.base.BaseView
import com.architecture.model.Post

interface PostContract {

    interface View : BaseView {
        fun init()
        fun updateView(list: List<Post>)
        fun error(apiError: ApiError)
    }

    interface Presenter : BasePresenter<View> {
        fun getPosts()
    }

    interface Repository : BaseRepository<Presenter> {
        fun getPosts(success: (List<Post>) -> Unit, error: (ApiError) -> Unit)
    }
}
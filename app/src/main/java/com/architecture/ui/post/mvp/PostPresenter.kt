package com.architecture.ui.post.mvp

import com.arc.kotlin.base.PresenterImpl
import javax.inject.Inject

class PostPresenter @Inject constructor(private val repository: PostContract.Repository) :
    PresenterImpl<PostContract.View>(),
    PostContract.Presenter {

    override fun attach(view: PostContract.View) {
        super.attach(view)
        view()?.run {
            init()
        }
        getPosts()
    }

    override fun getPosts() {
        repository.getPosts(success = { list ->
            view()?.updateView(list)
        }, error = { apiError ->
            view()?.error(apiError)
        })
    }

    override fun detach() {
        repository.cancel()
        super.detach()
    }
}
package com.architecture.ui.post.mvp

import com.arc.kotlin.api.response.ApiError
import com.arc.kotlin.api.response.ApiResponse
import com.arc.kotlin.base.RepositoryImpl
import com.architecture.api.request.ApiHandler
import com.architecture.model.Post
import retrofit2.Call

class PostRepository(private val apiHandler: ApiHandler) : RepositoryImpl<PostContract.Presenter>(),
    PostContract.Repository {

    private var mCall: Call<ApiResponse<Post>>? = null

    override fun getPosts(success: (List<Post>) -> Unit, error: (ApiError) -> Unit) {
        mCall = apiHandler.getPosts(
            success = { list ->
                success(list)
            }, authFailure = {

            }, error = { apiError ->
                error(apiError)
            })
    }

    override fun cancel() {
        if (mCall != null) mCall!!.cancel()
    }
}
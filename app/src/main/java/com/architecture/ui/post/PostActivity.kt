package com.architecture.ui.post

import android.os.Bundle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.arc.kotlin.api.response.ApiError
import com.arc.kotlin.base.BaseActivity
import com.arc.kotlin.inject.modules.ActivityModule
import com.arc.kotlin.util.extensions.toast
import com.architecture.App
import com.architecture.R
import com.architecture.model.Post
import com.architecture.ui.post.mvp.PostContract
import kotlinx.android.synthetic.main.activity_post.*

class PostActivity : BaseActivity<PostContract.Presenter>(), PostContract.View {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)
        presenter().attach(this)
    }

    override fun inject() {
        (application  as? App)
            ?.component()
            ?.activityComponent(ActivityModule(this))
            ?.inject(this)
    }

    override fun init() {
        setSupportActionBar(toolbar)
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@PostActivity)
            addItemDecoration(DividerItemDecoration(this@PostActivity, DividerItemDecoration.VERTICAL))
        }
    }

    override fun updateView(list: List<Post>) {
        recyclerView.adapter = PostAdapter(list)
        recyclerView.scheduleLayoutAnimation()
    }

    override fun error(apiError: ApiError) {
        toast(apiError.reason)
    }

}
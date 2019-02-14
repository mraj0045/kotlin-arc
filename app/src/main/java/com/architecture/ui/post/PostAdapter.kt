package com.architecture.ui.post

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.architecture.R
import com.architecture.model.Post
import kotlinx.android.synthetic.main.item_post.view.*

class PostAdapter(private val mList: List<Post>) :
    RecyclerView.Adapter<PostAdapter.PostHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostHolder {
        return PostHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false)
        )
    }

    override fun onBindViewHolder(holder: PostHolder, position: Int) {
        holder.bind()
    }

    fun getItem(position: Int): Post {
        return mList[position]
    }

    override fun getItemCount(): Int {
        return mList.size
    }


    inner class PostHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind() {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                val post = getItem(position)
                with(itemView) {
                    txtTitle.text = post.title
                    txtBody.text = post.body
                }
            }
        }
    }
}
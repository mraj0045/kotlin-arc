package com.arc.kotlin.pagination

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.arc.kotlin.R

abstract class PagedRecyclerViewAdapter<T>(
    @LayoutRes val loadingLayout: Int = R.layout.item_loading, private var list: ArrayList<T?>?,
    private val pageLimit: Int = PAGE_LIMIT
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {

        private val TAG = PagedRecyclerViewAdapter::class.java.simpleName

        private const val PAGE_LIMIT = 15
        private const val NORMAL_ITEM = 1
        private const val LOAD_ITEM = 2
    }

    /**
     * Returns the Loading layout to be used when loading
     */
    private var isLoading = java.lang.Boolean.FALSE
    /**
     * Returns whether the page is Last page or not
     *
     * @return true -> if last page, false -> if not last page
     */
    private var isLastPage = java.lang.Boolean.FALSE
    private var recyclerView: androidx.recyclerview.widget.RecyclerView? = null

    /** Creates view holder based on the view type*/
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == LOAD_ITEM) {
            LoadHolder(LayoutInflater.from(parent.context).inflate(loadingLayout, parent, false))
        } else {
            initViewHolder(parent, viewType)
        }
    }

    protected abstract fun initViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder

    /**
     * Initialize the pagination
     */
    fun setUpLoadMore(recyclerView: androidx.recyclerview.widget.RecyclerView) {
        this.recyclerView = recyclerView
        this.recyclerView?.addOnScrollListener(
            object : androidx.recyclerview.widget.RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(
                    recyclerView: androidx.recyclerview.widget.RecyclerView,
                    newState: Int
                ) {
                    super.onScrollStateChanged(recyclerView, newState)
                    val mRecyclerViewHelper = RecyclerViewPositionHelper.createHelper(recyclerView)
                    val visibleItemCount = recyclerView.childCount
                    val totalItemCount = recyclerView.layoutManager?.itemCount
                    val firstVisibleItem = mRecyclerViewHelper.findFirstVisibleItemPosition()
                    if (newState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE
                        && visibleItemCount + firstVisibleItem >= totalItemCount!!
                        && firstVisibleItem >= 0 && totalItemCount % pageLimit == 0 && !isLastPage
                    ) {
                        val page = itemCount / pageLimit + 1
                        if (!isLoading) {
                            isLoading = java.lang.Boolean.TRUE
                            addLoadingItem()
                            loadMore(page)
                        }
                    }
                }
            })
    }


    override fun getItemCount(): Int {
        return list?.size!!
    }

    override fun getItemViewType(position: Int): Int {
        return if (list?.get(position) == null)
            LOAD_ITEM
        else
            NORMAL_ITEM
    }

    /**
     * Retrieves the item from the list
     *
     * @param position Position to which the object to be retrieved
     * @return [T] instance object if present
     */
    protected fun getItem(position: Int): T? {
        return list?.get(position)
    }

    /**
     * Load more function called when pagination is required
     *
     * @param page Next Page number to be called
     */
    protected abstract fun loadMore(page: Int)

    /**
     * Cancel in-flight Api calls
     */
    abstract fun cancelApi()

    /**
     * Adds list of items
     *
     * @param list List of [T] instance
     */
    fun addItems(list: List<T>) {
        if (isLoading) removeLoadingItem()
        isLoading = false
        if (this.list == null) this.list = ArrayList()
        this.list?.addAll(list)
        notifyDataSetChanged()
    }

    /**
     * Appends loading item to the list
     */
    private fun addLoadingItem() {
        if (this.list == null) this.list = ArrayList()
        this.list?.add(null)
        notifyItemInserted(list?.size!! - 1)
        recyclerView?.layoutManager?.scrollToPosition(list?.size!! - 1)
    }

    /**
     * Removes the loading item from the list
     */
    private fun removeLoadingItem() {
        if (list != null && list?.size!! > 0 && isLoading) {
            list?.removeAt(list?.size!! - 1)
            notifyItemRemoved(list?.size!! - 1)
        }
    }

    /**
     * Call this function to mark the list has reached the end and further API calls should be
     * restricted.<br></br>
     * Don't call this for all the error cases. Call Only for the EOD.
     */
    fun setLastPage() {
        removeLoadingItem()
        isLoading = false
        isLastPage = true
    }

    /**
     * Loading View Holder
     */
    class LoadHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView)
}

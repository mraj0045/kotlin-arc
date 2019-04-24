package com.arc.kotlin.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.animation.GridLayoutAnimationController
import androidx.recyclerview.widget.GridLayoutManager

/** RecyclerView which shows empty view when the list size is empty.
 *
 * Fix for Layout animation when Grid layout manage is used.
 * */
class RecyclerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    androidx.recyclerview.widget.RecyclerView(context, attrs, defStyleAttr) {

    private var emptyView: View? = null

    override fun attachLayoutAnimationParameters(
        child: View,
        params: ViewGroup.LayoutParams,
        index: Int,
        count: Int
    ) {
        val layoutManager = layoutManager
        if (adapter != null && layoutManager is GridLayoutManager) {

            var animationParams: GridLayoutAnimationController.AnimationParameters? =
                params.layoutAnimationParameters as? GridLayoutAnimationController.AnimationParameters

            if (animationParams == null) {
                // If there are no animation parameters, create new once and attach them to
                // the LayoutParams.
                animationParams = GridLayoutAnimationController.AnimationParameters()
                params.layoutAnimationParameters = animationParams
            }

            // Next we are updating the parameters

            // Set the number of items in the RecyclerView and the index of this item
            animationParams.count = count
            animationParams.index = index

            // Calculate the number of columns and rows in the grid
            val columns = layoutManager.spanCount
            animationParams.columnsCount = columns
            animationParams.rowsCount = count / columns

            // Calculate the column/row position in the grid
            val invertedIndex = count - 1 - index
            animationParams.column = columns - 1 - invertedIndex % columns
            animationParams.row = animationParams.rowsCount - 1 - invertedIndex / columns
        } else {
            // Proceed as normal if using another type of LayoutManager
            super.attachLayoutAnimationParameters(child, params, index, count)
        }
    }

    /**
     * Sets the view to show if the adapter is empty
     */
    fun setEmptyView(emptyView: View) {
        this.emptyView = emptyView
        toggleEmptyViewVisibility()
    }

    private fun toggleEmptyViewVisibility() {
        emptyView?.run {
            visibility = if (adapter == null || adapter?.itemCount == 0) View.VISIBLE else View.GONE
            this@RecyclerView.visibility =
                if (adapter == null || adapter?.itemCount == 0) View.GONE else View.VISIBLE
        }
    }

    private val observer: AdapterDataObserver =
        object : AdapterDataObserver() {
            override fun onChanged() {
                super.onChanged()
                toggleEmptyViewVisibility()
            }

            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                super.onItemRangeInserted(positionStart, itemCount)
                toggleEmptyViewVisibility()
            }

            override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
                super.onItemRangeRemoved(positionStart, itemCount)
                toggleEmptyViewVisibility()
            }
        }

    override fun setAdapter(adapter: Adapter<*>?) {
        val oldAdapter = getAdapter()
        super.setAdapter(adapter)
        oldAdapter?.unregisterAdapterDataObserver(observer)
        adapter?.registerAdapterDataObserver(observer)
    }
}
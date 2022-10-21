package com.github.abnamro.presentation.repo.list

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class RepoListItemDecoration(
    private val spacing: Int
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)

        val itemPosition = parent.getChildAdapterPosition(view)
        if (itemPosition == RecyclerView.NO_POSITION) return
        val itemCount = state.itemCount

        val isFirstItem = itemPosition == 0
        val isLastItem = itemCount > 0 && itemPosition == itemCount - 1
        val halfSpacing = spacing / 2

        when {
            // left, top, right, bottom
            isFirstItem -> outRect.set(spacing, spacing, spacing, halfSpacing)
            isLastItem -> outRect.set(spacing, halfSpacing, spacing, spacing)
            else -> outRect.set(spacing, halfSpacing, spacing, halfSpacing)
        }
    }
}
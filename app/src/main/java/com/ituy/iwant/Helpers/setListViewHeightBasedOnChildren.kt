package com.ituy.iwant.Helpers

import android.view.View
import android.view.View.MeasureSpec
import android.view.ViewGroup
import android.view.WindowManager.LayoutParams
import android.widget.ListAdapter
import android.widget.ListView


fun setListViewHeightBasedOnChildren(listView: ListView) {
    val listAdapter: ListAdapter = listView.getAdapter() ?: return
    val desiredWidth = MeasureSpec.makeMeasureSpec(listView.getWidth(), MeasureSpec.UNSPECIFIED)
    var totalHeight = 0
    var view: View? = null
    for (i in 0 until listAdapter.getCount()) {
        view = listAdapter.getView(i, view, listView)
        if (i == 0) view.setLayoutParams(
            ViewGroup.LayoutParams(
                desiredWidth,
                LayoutParams.MATCH_PARENT
            )
        )
        view.measure(desiredWidth, MeasureSpec.UNSPECIFIED)
        totalHeight += view.getMeasuredHeight()
    }
    val params: ViewGroup.LayoutParams = listView.getLayoutParams()
    params.height = totalHeight + listView.getDividerHeight() * listAdapter.getCount()
    listView.setLayoutParams(params)
    listView.requestLayout()
}
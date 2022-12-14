package com.example.coolweather.databinding

import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

// 刷新数据及布局
object SwipeRefreshLayoutHandler {
    // 将view与数据进行绑定
    @BindingAdapter("app:bind_swipeRefreshLayout_refreshing")
    @JvmStatic
    fun setSwipeRefreshLayoutRefreshing(
        swipeRefreshLayout: SwipeRefreshLayout,
        newValue: Boolean
    ) {
        if (swipeRefreshLayout.isRefreshing != newValue)
            swipeRefreshLayout.isRefreshing = newValue!!
    }

    @JvmStatic
    @InverseBindingAdapter(
        attribute = "app:bind_swipeRefreshLayout_refreshing",
        event = "app:bind_swipeRefreshLayout_refreshingAttrChanged"
    )
    fun isSwipeRefreshLayoutRefreshing(swipeRefreshLayout: SwipeRefreshLayout): Boolean? =
        swipeRefreshLayout.isRefreshing

    // 观察view的状态变化，SwipeRefreshLayout每次的刷新状态都是通过这个来进行监听，
    // 然后告诉InverseBindingListener去通知DataBinding。
    @BindingAdapter(
        "app:bind_swipeRefreshLayout_refreshingAttrChanged",
        requireAll = false
    )
    @JvmStatic
    fun setOnRefreshListener(
        swipeRefreshLayout: SwipeRefreshLayout,
        bindingListener: InverseBindingListener?
    ) {
        if (bindingListener != null)
            swipeRefreshLayout.setOnRefreshListener {
                bindingListener.onChange()
            }
    }
}
package com.example.coolweather.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

class RecyclerViewBehavior : CoordinatorLayout.Behavior<SwipeRefreshLayout> {
    constructor(context: Context?, attrs: AttributeSet?) : super(
        context,
        attrs
    )

    override fun layoutDependsOn(
        parent: CoordinatorLayout,
        child: SwipeRefreshLayout,
        dependency: View
    ): Boolean {
        return dependency is ConstraintLayout
    }

    override fun onDependentViewChanged(
        parent: CoordinatorLayout,
        child: SwipeRefreshLayout,
        dependency: View
    ): Boolean {
        //计算列表y坐标，最小为0
        var y = dependency.height + dependency.translationY
        if (y < 0) {
            y = 0f
        }
        child.y = y
        return true
    }
}
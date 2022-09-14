package com.example.coolweather.view

import android.content.Context
import android.util.AttributeSet
import android.util.Log
// =======================================
import android.view.MotionEvent
import android.view.ViewConfiguration
import android.widget.Toast
import androidx.databinding.adapters.TextViewBindingAdapter.setText
// =======================================
import androidx.recyclerview.widget.RecyclerView
import com.example.baselib.utils.ToastUtil

// 设置刷新界面，refresh layout
 class BetterGesturesRecyclerView : RecyclerView {
    private var mScrollPointerId = 0
    private var mInitialTouchX = 0
    private var mInitialTouchY = 0
    private var mTouchSlop = 0

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(
        context: Context,
        attrs: AttributeSet?
    ) : super(context, attrs) {
        init()
    }

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyle: Int
    ) : super(context, attrs, defStyle) {
        init()
    }

    private fun init() {
        val vc = ViewConfiguration.get(context)
        this.mTouchSlop = vc.scaledTouchSlop
    }

    override fun setScrollingTouchSlop(slopConstant: Int) {
        val vc = ViewConfiguration.get(this.context)
        when (slopConstant) {
            0 -> {
                this.mTouchSlop = vc.scaledTouchSlop
                this.mTouchSlop = vc.scaledPagingTouchSlop
                ToastUtil.showMessage("slopConstant  0")//1111111111111111111111111111111111111111111
            }
            1 -> {this.mTouchSlop = vc.scaledPagingTouchSlop
                ToastUtil.showMessage("slopConstant  1")//1111111111111111111111111111111111111111111
                 }
            else -> {Log.w(
                "RecyclerView",
                "setScrollingTouchSlop(): bad argument constant $slopConstant; using default value"
            )
                ToastUtil.showMessage("slopConstant else")//1111111111111111111111111111111111111111111
            }
        }
        super.setScrollingTouchSlop(slopConstant)
    }

    override fun onInterceptTouchEvent(e: MotionEvent): Boolean {
        val canScrollHorizontally = layoutManager!!.canScrollHorizontally()
        val canScrollVertically = layoutManager!!.canScrollVertically()
        val action = e.actionMasked
        val actionIndex = e.actionIndex
        when (action) {
            0 -> {
                mScrollPointerId = e.getPointerId(0)
                this.mInitialTouchX = (e.x + 0.5f).toInt()
                this.mInitialTouchY = (e.y + 0.5f).toInt()
//                ToastUtil.showMessage("onInterceptTouchEvent 0 ")//11111111111111111点击即可 除了头部
                return super.onInterceptTouchEvent(e)
            }
            2 -> {
                val index = e.findPointerIndex(this.mScrollPointerId)
                if (index < 0) {
                    ToastUtil.showMessage("onInterceptTouchEvent 2 ")//1111111111111111111111111111111111111111111
                    Log.e(
                        "RecyclerView",
                        "Error processing scroll; pointer index for id " + this.mScrollPointerId + " not found. Did any MotionEvents get skipped?"
                    )
                    return false
                }
                val x = (e.getX(index) + 0.5f).toInt()
                val y = (e.getY(index) + 0.5f).toInt()
                if (scrollState != 1) {
                    val dx = x - this.mInitialTouchX
                    val dy = y - this.mInitialTouchY
                    var startScroll = false
                    if (canScrollHorizontally && Math.abs(dx) > this.mTouchSlop && Math.abs(
                            dx
                        ) > Math.abs(dy)
                    ) {
                        startScroll = true
                    }
                    if (canScrollVertically && Math.abs(dy) > this.mTouchSlop && Math.abs(
                            dy
                        ) > Math.abs(dx)
                    ) {
                        startScroll = true
                    }
                    Log.d(
                        "MyRecyclerView",
                        "canX:$canScrollHorizontally--canY$canScrollVertically--dx:$dx--dy:$dy--startScorll:$startScroll--mTouchSlop$mTouchSlop"
                    )
                    return startScroll && super.onInterceptTouchEvent(e)
                }
                return super.onInterceptTouchEvent(e)
            }
            5 -> {
                this.mScrollPointerId = e.getPointerId(actionIndex)
                this.mInitialTouchX = (e.getX(actionIndex) + 0.5f).toInt()
                this.mInitialTouchY = (e.getY(actionIndex) + 0.5f).toInt()
                ToastUtil.showMessage("onInterceptTouchEvent 5555 ")//1111111111111111111111111111111111111111111
                return super.onInterceptTouchEvent(e)
            }
        }
        return super.onInterceptTouchEvent(e)
    }
}
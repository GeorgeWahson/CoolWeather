package com.example.coolweather.utils

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.coolweather.R


/**
 * 城市管理界面分割线、宽度2px、颜色为青色
 * @param context
 * @param orientation
 */
class RecycleViewDivider(val context: Context, private val mOrientation: Int) : RecyclerView.ItemDecoration() {

    private var mPaint: Paint? = null
    private var mDivider: Drawable? = null
    private var mDividerHeight = DisplayUtils.dp2px(context,2f) //分割线高度，默认为1px

    init {
        if (mOrientation != LinearLayoutManager.VERTICAL) {
            throw IllegalArgumentException("LinearLayoutManager wrong from RecycleViewDivider")
        }
        val a = context.obtainStyledAttributes(ATTRS)
        mDivider = ContextCompat.getDrawable(context, R.drawable.shape_divider)
        mDivider=a.getDrawable(0)
        a.recycle()
    }

    //获取分割线尺寸
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        if (mOrientation == LinearLayoutManager.VERTICAL) {
            outRect.set(0, 0, 0, mDividerHeight)
        }
    }

    //绘制分割线
    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)
        if (mOrientation == LinearLayoutManager.VERTICAL) {
            drawVertical(c, parent)
        }
    }

    /**
     * 绘制横向分割线
     * @param canvas
     * @param parent
     */
    private fun drawVertical(canvas: Canvas, parent: RecyclerView) {
        val left = parent.paddingLeft+DisplayUtils.dp2px(context ,20f)
        val right = parent.measuredWidth - parent.paddingRight-DisplayUtils.dp2px(context,20f)
        val childSize = parent.childCount
        for (i in 0 until childSize-1) {
            val child = parent.getChildAt(i)
            val layoutParams = child.layoutParams as RecyclerView.LayoutParams
            val top = child.bottom + layoutParams.bottomMargin
            val bottom = top + mDividerHeight
            if (mDivider != null) {
                mDivider!!.setBounds(left, top, right, bottom)
                mDivider!!.draw(canvas)
            }
            if (mPaint != null) {
                canvas.drawRect(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat(), mPaint!!)
            }
        }
    }

    companion object {
        private val ATTRS = intArrayOf(android.R.attr.listDivider)
    }
}
package com.example.coolweather.view

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment

open class BaseFragment :Fragment(){

    override fun onCreate(savedInstanceState: Bundle?) {

            if (Build.VERSION.SDK_INT>= 21) {
                val decorView: View = requireActivity().window.decorView
                decorView.systemUiVisibility = (
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN // Activity全屏显示，但是状态栏不会被覆盖掉，而是正常显示，只是Activity顶端布局会被覆盖住
                                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE) // 加上这个标志位以后，布局不会占用状态栏的空间
                requireActivity().window.statusBarColor = Color.TRANSPARENT; // 状态栏透明
            }

        super.onCreate(savedInstanceState)
    }


}
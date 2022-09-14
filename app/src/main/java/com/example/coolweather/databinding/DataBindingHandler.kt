package com.example.coolweather.databinding

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.coolweather.R
import com.example.coolweather.utils.IconUtils

/**
 * 踩坑  由于kotlin没有static的特性和null-safe检查   添加 @JvmStatic 和参数后面加？
 */
object DataBindingHandler {
    // 根据天气code来加载不同的icon
    @BindingAdapter("loadByCode")
    @JvmStatic // 规避空指针异常
    fun loadImageByCode(imageView: ImageView, code: String?) {
        if (code != null) {
            imageView.setImageResource(IconUtils.getSmallIcon(code.toInt()!!))
        }
    }
    // 根据空气质量值来更改显示的背景颜色
    @BindingAdapter("airQuality")
    @JvmStatic
    fun airQuality(textView: TextView, type: String?) {
        when (type) {
            "优", "良" -> {
                textView.setBackgroundResource(R.drawable.aqi_good)
            }
            "轻度污染" -> {
                textView.setBackgroundResource(R.drawable.aqi_bad)
            }
            "中度污染", "重度污染" -> {
                textView.setBackgroundResource(R.drawable.aqi_worst)
            }
        }
    }

    // 根据风向修改风向图标
    @BindingAdapter("windIcon")
    @JvmStatic
    fun windIcon(imageView: ImageView, content: String?) {
        if (content != null) {
            imageView.setImageResource(IconUtils.getWindIcon(content))
        }
    }

}
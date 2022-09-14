package com.example.coolweather.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.example.coolweather.R
import com.example.coolweather.entity.Location
import java.lang.StringBuilder

// 设置城市搜索结果显示
class SearchAdapter(data:List<Location>):BaseQuickAdapter<Location, BaseViewHolder>(R.layout.item_search_layout, data){
    override fun convert(helper: BaseViewHolder, item: Location) {
        helper.setText(R.id.search_city_name, item.name)

        val builder=StringBuilder()
        builder.append(item.adm2) // 市名
        builder.append("  ")
        builder.append(item.adm1) // 省名
        builder.append("  ")
        builder.append(item.country) // 国家名

        helper.setText(R.id.search_province, builder.toString())
    }

}
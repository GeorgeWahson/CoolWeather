package com.example.coolweather.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.coolweather.R
import com.example.coolweather.databinding.ItemForecastLayoutBinding
import com.example.coolweather.entity.DailyEntity
import com.example.coolweather.utils.DateUtils

class DetailForecastAdapter(val context:Context,private var dataList:List<DailyEntity>) : RecyclerView.Adapter<DetailForecastAdapter.ForecastHolder>() {

    private var mMaxAll=0
    private var mMinAll=0

    // dataList 用于存储每天的天气情况
    fun setData(data: List<DailyEntity>){
        dataList=data

        // 数组上下界
        var maxArray=IntArray(dataList.size)
        var minArray=IntArray(dataList.size)
        // 最高温与最低温
        for ((i, forecast) in dataList.withIndex()){
            maxArray[i]=forecast.tempMax.toInt()
            minArray[i]=forecast.tempMin.toInt()

        }
        mMaxAll= maxArray.max()!!
        mMinAll=minArray.min()!!

        notifyDataSetChanged()

    }

    // 底部每日天气预报
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastHolder {
        var view= LayoutInflater.from(context).inflate(R.layout.item_forecast_layout, parent ,false)
        val w: Int = parent.width
        view.layoutParams.width=w/5
        return ForecastHolder(DataBindingUtil.bind(view)!!)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: ForecastHolder, position: Int) {
        val  entity=dataList[position]
        holder.bindIng.dailyForecast=entity
        holder.bindIng.itemFTempView.setData(mMaxAll, mMinAll,entity.tempMax.toInt(),entity.tempMin.toInt() )

        if (position==0){ // 数组第一项即但当天天气
            holder.bindIng.itemFTimeTv.text="今天"
        }else{
            holder.bindIng.itemFTimeTv.text=DateUtils.getTime(DateUtils.dateToStamp(entity.fxDate).toString())
        }

    }

    inner class ForecastHolder(var bindIng:ItemForecastLayoutBinding):RecyclerView.ViewHolder(bindIng.root)

}
package com.example.coolweather.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.coolweather.R
import com.example.coolweather.databinding.ItemHourlyLayoutBinding
import com.example.coolweather.entity.HourlyEntity
// 各小时天气预报
class DetailHourlyAdapter(var context:Context,var dataList:List<HourlyEntity>): RecyclerView.Adapter<DetailHourlyAdapter.HourlyHolder>() {

    lateinit var mTempArray:IntArray
    var mMaxTemp=0
    var mMinTemp=0

    fun setData(dataList: List<HourlyEntity>){
        this.dataList=dataList
        mTempArray= IntArray(dataList.size)
        for ((i ,hourly) in dataList.withIndex()){
            mTempArray[i]=hourly.temp.toInt()
        }
        mMaxTemp= mTempArray.max()!!
        mMinTemp=mTempArray.min()!!

        notifyDataSetChanged()

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourlyHolder {
        var view=LayoutInflater.from(context).inflate(R.layout.item_hourly_layout, parent ,false)
        val w: Int = parent.width
        view.layoutParams.width=w/5 // 每列元素宽度，预计一次性显示4个
        return HourlyHolder(DataBindingUtil.bind<ItemHourlyLayoutBinding>(view)!!)
    }

    override fun getItemCount(): Int {
       return  dataList.size
    }

    override fun onBindViewHolder(holder: HourlyHolder, position: Int) {
        holder.bindIng.itemHourlyTimeTv.text=getTime(dataList[position].fxTime)
        holder.bindIng.hourly=dataList[position]
        holder.bindIng.itemHourlyTempView.setData(mMaxTemp,mMinTemp,dataList[position].temp.toInt(), mMinTemp,false)

        if (position==0){ // 是否显示“风力风向”字，将风字放置第一天显示，
            holder.bindIng.itemHourlyWindTv.visibility=View.VISIBLE
        }else{
            holder.bindIng.itemHourlyWindTv.visibility=View.INVISIBLE

        }
    }


    inner class HourlyHolder( val bindIng:ItemHourlyLayoutBinding) : RecyclerView.ViewHolder(bindIng.root) {

    }

   private fun getTime(time:String):String{
       // 索引时间（XX:XX）
        var index=time.indexOf(":")
        //截取出时间字符串
        return time.substring(index-2,index+3)
    }

}
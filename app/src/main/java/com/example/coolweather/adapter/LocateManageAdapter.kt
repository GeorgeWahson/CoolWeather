package com.example.coolweather.adapter

import android.widget.CheckBox
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.example.coolweather.R
import com.example.coolweather.entity.LocateEntity
import java.lang.StringBuilder

class LocateManageAdapter( data:ArrayList<LocateEntity>):BaseQuickAdapter<LocateEntity,BaseViewHolder>( R.layout.item_locate_layout, data){

    override fun convert(helper: BaseViewHolder, item: LocateEntity?) {

            helper.setText(R.id.manage_location_name, item?.city?.name)

            if (item?.city!!.isLocal==1){ // 设置当前定位城市 左上角显示定位图标
                helper.setVisible(R.id.item_locate_l_img, true)
            }else{
                helper.setGone(R.id.item_locate_l_img, false)
            }

            if (item.open){ // 设置删除选择框CheckBox可见
                helper.setVisible(R.id.locate_del_cb, true) // 进入删除状态，可选框可见

                helper.setChecked(R.id.locate_del_cb, item.select)
                helper.addOnClickListener(R.id.locate_del_cb) // 设置删除监听器
                if (item.city.isLocal==1){
                    val cb=helper.getView<CheckBox>(R.id.locate_del_cb)
                    cb.setButtonDrawable(R.drawable.icon_locate_un_checked) // 设置当前定位按钮不可选中样式，颜色稍微变换
                    cb.isClickable=false // 不可选择删除当前定位城市
                }
            }else{
                helper.setGone(R.id.locate_del_cb, false)
            }

            //地址管理界面设置温度
            if(item.now!=null){ // 设置当前天气
                helper.setText(R.id.manage_location_temp, item.now?.temp+" °")
            }
            if (item.oneDay!=null){ // 设置最高温与最低温
                val builder=StringBuilder()
                builder.append(item.oneDay?.tempMax)
                builder.append("° ")
                builder.append("/")
                builder.append(item.oneDay?.tempMin)
                builder.append("°")
                helper.setText(R.id.manage_temp_dict, builder.toString())
            }

    }



}
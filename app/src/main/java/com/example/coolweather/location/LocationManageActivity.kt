package com.example.coolweather.location

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.baselib.base.BaseActivity
import com.example.coolweather.R
import com.example.coolweather.adapter.LocateManageAdapter
import com.example.coolweather.databinding.ActivityLocationManagerBinding
import com.example.coolweather.entity.LocateEntity
import com.example.coolweather.utils.RecycleViewDivider
import com.example.coolweather.vm.LocateViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * 地点管理Activity
 */
@AndroidEntryPoint
class LocationManageActivity : BaseActivity() {

    private lateinit var mCityListView: RecyclerView
    private var mCityList = arrayListOf<LocateEntity>()
    private lateinit var mBindIng: ActivityLocationManagerBinding
    private var mLastId: Int = 0
    private var mIsOpen = false // 是否进入删除状态
    private var mDeleteArrayPos = arrayListOf<Int>()
    private lateinit var mDialogBuilder:AlertDialog.Builder // 删除对话框


    private val mAdapter: LocateManageAdapter by lazy {
        LocateManageAdapter(mCityList)
    }

    private val mLocateViewModel: LocateViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBindIng = DataBindingUtil.setContentView(this, R.layout.activity_location_manager)

        mBindIng.handler = ManagerHandler()
        mCityListView = mBindIng.locateCityRlv
        mBindIng.isOpen = mIsOpen

        initView()
        initData()
    }

    override fun initView() {
        mCityListView.layoutManager = LinearLayoutManager(this) as RecyclerView.LayoutManager?
        mCityListView.addItemDecoration(RecycleViewDivider(this, LinearLayoutManager.VERTICAL))
        mCityListView.adapter = mAdapter
        mAdapter.setOnItemChildClickListener { _, view, position ->
            when (view.id) {
                R.id.locate_del_cb -> {
                    // 颠倒选择状态
                    mAdapter.data[position].select = !mAdapter.data[position].select
                    if (mAdapter.data[position].select) {
                        mDeleteArrayPos.add(position)
                    }
                    else{
                        mDeleteArrayPos.remove(position)
                    }
                }
            }
        }

    }

    override fun initData() {
        super.initData()

        mLocateViewModel.getWeatherList().observe(this, Observer<List<LocateEntity>> { data ->
            mCityList = data as ArrayList<LocateEntity>
            if (mCityList.isNotEmpty()) {
                mAdapter.setNewData(data)
                mLastId = data[data.size - 1].city.id!!
            }

        })


    }
    // 进入选择状态
    private fun initSelect() {
        for ((i, locate) in mCityList.withIndex()) {
            mCityList[i].open = mIsOpen
        }
        mAdapter.setNewData(mCityList)

        if (mIsOpen) { // 删除图标locateDeleteTv按钮是否可见
            mBindIng.locateDeleteTv.visibility = View.VISIBLE
        } else
            mBindIng.locateDeleteTv.visibility = View.GONE

    }

    // 在选择状态点击返回键，取消删除状态，但保留选择
    override fun onBackPressed() {
        if (mIsOpen) {
            mIsOpen = false
            initSelect()
        } else
            super.onBackPressed()
    }

    inner class ManagerHandler {

        fun toSearch(view: View) { // 城市管理界面跳往搜索界面 SearchActivity
            var intent = Intent(this@LocationManageActivity, SearchActivity::class.java)
            intent.putExtra(SearchActivity.KEY_LAST_ID, mLastId)
            startActivity(intent)

        }

        fun back(view: View) {
            finish() // finish view
        }

        // 当用户点击更多，进入more_view
        fun openMore(view: View) {
            mIsOpen = true
            mBindIng.isOpen = mIsOpen
            initSelect()
        }

        // 用户选择城市进行删除
        fun delete(view: View) {
            if (mDeleteArrayPos.isEmpty()){
                Toast.makeText(getApplicationContext(),"您还没有选择城市哦！",LENGTH_SHORT).show()
            }else{
                mDialogBuilder=AlertDialog.Builder(this@LocationManageActivity).setTitle("提示")
                    .setMessage("确定删除该城市？")
                    .setTitle("警告")
                    .setPositiveButton("确定") { _, _ ->  deleteCityList()}
                    .setNegativeButton("取消") { dialog, _ -> dialog.dismiss()}
                mDialogBuilder.create().show()
            }
        }
    }

    private fun deleteCityList(){
        for (mDeleteArrayPo in mDeleteArrayPos) {
            mLocateViewModel.deleteCity(mCityList[mDeleteArrayPo].city)
            //test==========================================================
            mIsOpen = false
            mBindIng.locateDeleteTv.visibility = View.GONE

            mBindIng = DataBindingUtil.setContentView(this, R.layout.activity_location_manager)

            mBindIng.handler = ManagerHandler()
            mCityListView = mBindIng.locateCityRlv
            mBindIng.isOpen = mIsOpen
            initView()
            initData()
        }
    }


}
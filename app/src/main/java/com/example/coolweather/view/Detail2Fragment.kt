package com.example.coolweather.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.gson.Gson
import com.example.baselib.utils.L
import com.example.coolweather.MainActivity
import com.example.coolweather.R
import com.example.coolweather.adapter.DetailContentAdapter
import com.example.coolweather.adapter.DetailForecastAdapter
import com.example.coolweather.adapter.DetailHourlyAdapter
import com.example.coolweather.data.*
import com.example.coolweather.data.City
import com.example.coolweather.databinding.DetailHeadContentLayoutBinding
import com.example.coolweather.databinding.FragmentDetail3Binding
import com.example.coolweather.databinding.HeadAirLayoutBinding
import com.example.coolweather.entity.*
import com.example.coolweather.utils.BackGroundUtils
import com.example.coolweather.utils.DisplayUtils
import com.example.coolweather.vm.DetailViewModel
import com.example.coolweather.vm.LocateViewModel
import com.example.coolweather.vm.WeatherViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * 天气详情fragment
 * @author silence
 */
@AndroidEntryPoint
class Detail2Fragment : BaseFragment() {

    private var mCityName = ""
    private var mIsLoadData = false
    private var mNetInt = 0//记录当前天气和未来天气请求 方便最后存入数据库
    private var mNowWeatherJson = ""
    private var mOneDayJson = ""
    private var mCityId = 0

    private var mAllNetInt = 0
    private var mIsRefreshIng = false//是否为手动刷新

    private lateinit var mBinding: FragmentDetail3Binding

    private lateinit var mTopHeadContentLayoutBinding: DetailHeadContentLayoutBinding

    private lateinit var mHeadAirViewBinding: HeadAirLayoutBinding

    private lateinit var mContentView: BetterGesturesRecyclerView
    private lateinit var mAdapter: DetailContentAdapter
    private lateinit var mContentLayout: CoordinatorLayout

    private lateinit var mHourlyRecyclerView: SRecyclerView
    private lateinit var mForecastRecyclerView: SRecyclerView
    private lateinit var mRefreshLayout: SwipeRefreshLayout

    private lateinit var mLeftMore: ImageView
    private lateinit var mRightMore: ImageView

    private val mGson: Gson by lazy {
        Gson()
    }

    private val mCity: City by lazy {
        mLocateViewModel.getCityById(mCityId)
    }

    private val mCityWeatherDao: CityWeatherDao by lazy {
        AppDataBase.instance.getCityWeatherDao()
    }

    private val mHourlyAdapter: DetailHourlyAdapter by lazy {
        DetailHourlyAdapter(requireContext(), mHourlyList)
    }

    private var mHourlyList = arrayListOf<HourlyEntity>()

    private val mForecastAdapter: DetailForecastAdapter by lazy {
        DetailForecastAdapter(requireContext(), mForecastList)
    }

    private var mRootView: View? = null

    private var mForecastList = arrayListOf<DailyEntity>()

    private val mDetailViewModel: DetailViewModel by viewModels()

    private val mLocateViewModel: LocateViewModel by viewModels()

    private val mWeatherViewModel: WeatherViewModel by viewModels()


    companion object {
        private const val KEY_ID = "ID"

        fun newInstance(pos: Int) = Detail2Fragment().apply {
            arguments = Bundle(1).apply {
                putInt(KEY_ID, pos)
            }
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // =================================================顶部 or detail
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail3, container, false)

        mBinding.let {
            //todo 很重要- 绑定
            it.viewModel = mDetailViewModel
            it.lifecycleOwner = this
        }

        mRootView = mBinding.root
        return mRootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        mAdapter = DetailContentAdapter()
        mContentView.layoutManager = LinearLayoutManager(activity)
        mContentView.adapter = mAdapter

        initDetailView()

        mCityId = arguments?.getInt(KEY_ID, 0)!!

        // 下拉刷新数据
        mDetailViewModel.refreshing.observe(viewLifecycleOwner, Observer<Boolean> { boolean ->
            if (boolean) {
                L.e("下拉刷新")//======
                mIsRefreshIng = true
                getData()
            }
        })
    }

    /**
     * 初始化view
     */
    // 刷新进度条颜色
    private fun initView() {
        mContentView = mBinding.detailContentViewRlv
        mContentLayout = mBinding.detailContentLayout
        mRefreshLayout = mBinding.detailRefreshLayout
        mRefreshLayout.setColorSchemeResources(
            R.color.colorAccent,
            R.color.colorPrimary,
            R.color.colorPrimaryDark
        );
    }

    /**
     * 初始化详情view 给recycleView添加内容
     */
    private fun initDetailView() {
        //初始化当前天气布局view
        val nowTopView = LayoutInflater.from(activity)
            .inflate(R.layout.detail_head_content_layout, mContentView, false)

        nowTopView.layoutParams = ConstraintLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, DisplayUtils.dp2px(
                requireContext(), 200f
            )
        )
        // 顶部布局
        mTopHeadContentLayoutBinding =
            DataBindingUtil.bind(nowTopView)!!
        mAdapter.addHeaderView(nowTopView)

        mHeadAirViewBinding = DataBindingUtil.inflate(
            LayoutInflater.from(activity),
            R.layout.head_air_layout,
            mContentView,
            false
        )
        mHeadAirViewBinding.let { it.lifecycleOwner = this }

        mAdapter.addHeaderView(mHeadAirViewBinding.root)

        //初始化未来24小时view
        val hourlyView =
            LayoutInflater.from(activity).inflate(R.layout.head_hourly_layout, mContentView, false)

        mHourlyRecyclerView = hourlyView.findViewById(R.id.head_hourly_rlv)

        //获取头部布局的左右imageView

        mLeftMore = nowTopView.findViewById(R.id.head_now_left_more_img)

        mRightMore = nowTopView.findViewById(R.id.head_now_right_more_img)

        mHourlyRecyclerView.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        mHourlyRecyclerView.adapter = mHourlyAdapter

        //初始化未来几天的view
        val forecastView = LayoutInflater.from(activity)
            .inflate(R.layout.head_forecast_layout, mContentView, false)
        mForecastRecyclerView = forecastView.findViewById(R.id.head_forecast_rlv)

        mForecastRecyclerView.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        mForecastRecyclerView.adapter = mForecastAdapter

        mAdapter.addHeaderView(hourlyView)
        // 设置标题
        var title = TextView(context)
        var layoutParams = RecyclerView.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        layoutParams.leftMargin = DisplayUtils.dp2px(requireContext(), 20f)
        layoutParams.bottomMargin = DisplayUtils.dp2px(requireContext(), 10f)
        layoutParams.topMargin = DisplayUtils.dp2px(requireContext(), 10f)
        title.layoutParams = layoutParams
        title.text = "一周天气情况"
        title.setTextColor(ContextCompat.getColor(requireActivity(), R.color.white))
        mAdapter.addHeaderView(title)
        mAdapter.addHeaderView(forecastView)

        //底部间隔=weixiugai=====================================================================================
        val airView =
            LayoutInflater.from(activity).inflate(R.layout.air_view_layout, mContentView, false)

        mAdapter.addHeaderView(airView)
    }

    private fun initData() {

        mCityName = mCity.name!!
        mBinding.cityName = mCity.name
        // 顶部布局显示同样信息
        mTopHeadContentLayoutBinding.cityName = mCity.name
        //设置是否为定位所在地
        mBinding.isLocate = mCity.isLocal == 1
        mTopHeadContentLayoutBinding.isLocate = mCity.isLocal == 1

        checkIndex()
        getData()

    }

    /**
     *记录刷新
     */
    private fun recordRefresh() {
        if (mIsRefreshIng) {
            mAllNetInt++
            if (mAllNetInt >= 4) {
                mIsRefreshIng = false
                mAllNetInt = 0
                mNetInt = 0
                mDetailViewModel.refreshing.value = false

            }

        }
    }

    private fun getData() {

        //当前天气
        mWeatherViewModel.getWeatherNowInfo(mCity.cityId).observe(viewLifecycleOwner, Observer {
            if (it.code == Api.SUCCESS_STATUS) {
                mTopHeadContentLayoutBinding.nowData = it.now
                // ========================================设置背景天气==============================================
                mContentLayout.setBackgroundResource(BackGroundUtils.getBackGroundByCode(it.now!!.icon.toInt()))
                mNetInt++
                mNowWeatherJson = mGson.toJson(it.now)

                if (mNetInt == 2) {
                    insertData()
                }
            }
            recordRefresh()

        })
        //未来天气
        mWeatherViewModel.getFutureWeatherList(mCity.cityId).observe(viewLifecycleOwner) {
            if (it.code == Api.SUCCESS_STATUS) {
                mForecastList = it.daily as ArrayList<DailyEntity>
                mForecastAdapter.setData(mForecastList)
                mOneDayJson = mGson.toJson(mForecastList[0])
                mNetInt++
                if (mNetInt == 2) {
                    insertData()
                }
                recordRefresh()
            }
        }
        //当前空气状况
        mWeatherViewModel.getAirNowInfo(mCity.cityId).observe(viewLifecycleOwner) {
            if (it.code == Api.SUCCESS_STATUS) {
                mHeadAirViewBinding.air = it.now
            }
            recordRefresh()

        }
        //未来24小时预报
        mWeatherViewModel.getHourlyList(mCity.cityId).observe(viewLifecycleOwner) {
            if (it.code == Api.SUCCESS_STATUS) {
                mHourlyList = it.hourly as ArrayList<HourlyEntity>
                mHourlyAdapter.setData(mHourlyList)

            }
            recordRefresh()
        }

    }


    /**
     * 保存当前天气和今日天气  地点管理需要用到这些数据
     */
    private fun insertData() {
        val cityWeather = CityWeather(mCityId, mNowWeatherJson, mOneDayJson)
        mCityWeatherDao.insertData(cityWeather)
        mNetInt = 0
    }


    override fun onResume() {
        super.onResume()
        if (mCityId == 0) {
            return
        }
        if (!mIsLoadData) {
            initData()
            mIsLoadData = true
        }
    }

    //设置左右 < > 是否可见
    private fun checkIndex() {
        if (activity is MainActivity) {
            var e = (activity as MainActivity).getDetailIndex(mCityName)
            when (e) {
                DetailIndex.LEFT -> {
                    mLeftMore.visibility = View.INVISIBLE
                    mRightMore.visibility = View.VISIBLE
                }
                DetailIndex.MIDDLE -> {
                    mLeftMore.visibility = View.VISIBLE
                    mRightMore.visibility = View.VISIBLE
                }
                DetailIndex.RIGHT -> {
                    mLeftMore.visibility = View.VISIBLE
                    mRightMore.visibility = View.INVISIBLE
                }

                DetailIndex.NONE -> {
                    mLeftMore.visibility = View.INVISIBLE
                    mRightMore.visibility = View.INVISIBLE
                }
            }
        }
    }


}
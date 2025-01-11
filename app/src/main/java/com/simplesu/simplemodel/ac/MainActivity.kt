package com.simplesu.simplemodel.ac

import androidx.lifecycle.lifecycleScope
import com.blankj.utilcode.util.LogUtils
import com.simplesu.simplemodel.arch.ArchActivity
import com.simplesu.simplemodel.arch.calculateAd
import com.simplesu.simplemodel.arch.calculateAdAmount
import com.simplesu.simplemodel.arch.calculateAdMonth
import com.simplesu.simplemodel.arch.calculateAdRate
import com.simplesu.simplemodel.arch.calculateEMIRepaymentSchedule
import com.simplesu.simplemodel.arch.calculateEmiAmount
import com.simplesu.simplemodel.arch.calculateEmi
import com.simplesu.simplemodel.arch.calculateEmiMonth
import com.simplesu.simplemodel.arch.calculateEmiRate
import com.simplesu.simplemodel.databinding.ActivityMainBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class MainActivity : ArchActivity<ActivityMainBinding>() {
    override fun initView() {
        /**
         * 本金 10000
         * 月利率 1%
         * 期数 36
         */

//        val a = calculateEmi(10000.0,0.01,36)
//        LogUtils.e("yqw=====>", a)//emi 332.14309812851167  四舍五入后为332
//        val b = calculateEmiAmount(332.0,0.01,36)
//        LogUtils.e("yqw=====>",b)//得到本金 9995.691672375011
//        val c = calculateEmiMonth(10000.0,332.0,0.01)
//        LogUtils.e("yqw=====>",c)//期数是36
//        val d = calculateEmiRate(332.0,10000.0,36)
//        LogUtils.e("yqw=====>",(d*100).roundToInt())//利率0.009975030086934566
//
//        val a1 = calculateAd(10000.0,0.01,36)
//        LogUtils.e("yqw=====>", a1)//ad 328.85455260248676  四舍五入后为329
//        val b1 = calculateAdAmount(329.0,0.01,36)
//        LogUtils.e("yqw=====>",b1)//得到本金 10004.42284883582
//        val c1 = calculateAdMonth(10000.0,329.0,0.01)
//        LogUtils.e("yqw=====>",c1)//期数是36
//        val d1 = calculateAdRate(329.0,10000.0,36)
//        LogUtils.e("yqw=====>",d1)//利率0.010027187876403332
//
//        //计算表格数据
//        LogUtils.e("yqw=====>", calculateEMIRepaymentSchedule(10000.0,0.01,36,0))

        binding.demo.startDraw(700.1111,200.5555,99.3334)
    }

}
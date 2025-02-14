package com.simplesu.simplemodel.ac

import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.GridLayoutManager
import com.simplesu.simplemodel.BaseApp
import com.simplesu.simplemodel.R
import com.simplesu.simplemodel.ac.ad.TabAdapter
import com.simplesu.simplemodel.arch.ArchActivity
import com.simplesu.simplemodel.arch.DataUtil
import com.simplesu.simplemodel.arch.calculateAd
import com.simplesu.simplemodel.arch.calculateAdAmount
import com.simplesu.simplemodel.arch.calculateAdMonth
import com.simplesu.simplemodel.arch.calculateAdRate
import com.simplesu.simplemodel.arch.calculateEmi
import com.simplesu.simplemodel.arch.calculateEmiAmount
import com.simplesu.simplemodel.arch.calculateEmiMonth
import com.simplesu.simplemodel.arch.calculateEmiRate
import com.simplesu.simplemodel.arch.initPopup
import com.simplesu.simplemodel.arch.singleClick
import com.simplesu.simplemodel.be.TabBean
import com.simplesu.simplemodel.databinding.ActivityMainBinding

class MainActivity : ArchActivity<ActivityMainBinding>() {
    private val tabList = mutableListOf<TabBean>()
    private val tabAdapter by lazy {
        TabAdapter(tabList).apply {
            setHasStableIds(true)
            setOnItemClickListener { _, _, position ->
                DataUtil.calType = position
                binding.type = position
                tabList.forEach { it.select = false }
                tabList[position].select = true
                setList(tabList)
            }
        }
    }
    private var tenureType = 0

    override fun initView() {
        BaseApp.isMainLaunch = true
        tabList.apply {
            add(TabBean(getString(R.string.emi),true))
            add(TabBean(getString(R.string.amount),false))
            add(TabBean(getString(R.string.tenure),false))
            add(TabBean(getString(R.string.interest),false))
        }

        binding.run {
            recyclerViewTab.layoutManager = GridLayoutManager(this@MainActivity,4)
            recyclerViewTab.adapter = tabAdapter
            type = DataUtil.calType

            mainAmount.addTextChangedListener {
                if (it.isNullOrBlank()) return@addTextChangedListener
                DataUtil.amount = it.toString().toDouble()
            }

            mainEmi.addTextChangedListener {
                if (it.isNullOrBlank()) return@addTextChangedListener
                DataUtil.emi = it.toString().toDouble()
            }

            mainRate.addTextChangedListener {
                if (it.isNullOrBlank()) return@addTextChangedListener
                DataUtil.rate = it.toString().toDouble()/12/100
            }

            mainTenure.addTextChangedListener {
                if (it.isNullOrBlank()) return@addTextChangedListener
                DataUtil.tenure = if (tenureType == 0) it.toString().toInt()*12 else it.toString().toInt()
            }

            mainFree.addTextChangedListener {
                if (it.isNullOrBlank()) return@addTextChangedListener
                DataUtil.fee = it.toString().toDouble()
            }

            mainSelectTenure.singleClick {
                it.initPopup(arrayOf(getString(R.string.yr), getString(R.string.mo))){index,str->
                    mainSelectTenure.text = str
                    tenureType = index
                    val nowTenure = binding.mainTenure.text.toString().toIntOrNull() ?: 0
                    DataUtil.tenure = if (tenureType == 0) nowTenure*12 else nowTenure
                }
            }

            mainSelectScheme.singleClick {
                it.initPopup(arrayOf(getString(R.string.emi_in_arrears),getString(R.string.emi_advance))){index,str->
                    mainScheme.text = str
                    DataUtil.emiType = index
                }
            }

            mainClear.singleClick {
                mainAmount.setText("")
                mainEmi.setText("")
                mainRate.setText("")
                mainTenure.setText("")
                mainFree.setText("")
                DataUtil.clear()
            }

            mainCalculate.singleClick {
                DataUtil.run {
                    when(calType){
                        0->{
                            if (amount == 0.0) return@singleClick
                            if (rate == 0.0) return@singleClick
                            if (tenure == 0) return@singleClick
                            emi = if (emiType == 0){
                                calculateEmi(amount,rate,tenure)
                            }else{
                                calculateAd(amount,rate,tenure)
                            }
                        }
                        1->{
                            if (emi == 0.0) return@singleClick
                            if (rate == 0.0) return@singleClick
                            if (tenure == 0) return@singleClick
                            amount = if (emiType == 0){
                                calculateEmiAmount(emi, rate, tenure)
                            }else{
                                calculateAdAmount(emi, rate, tenure)
                            }
                        }
                        2->{
                            if (amount == 0.0) return@singleClick
                            if (emi == 0.0) return@singleClick
                            if (rate == 0.0) return@singleClick
                            tenure = if (emiType == 0){
                                calculateEmiMonth(amount, emi, rate)
                            }else{
                                calculateAdMonth(amount, emi, rate)
                            }
                        }
                        3->{
                            if (amount == 0.0) return@singleClick
                            if (emi == 0.0) return@singleClick
                            if (tenure == 0) return@singleClick
                            rate = if (emiType == 0){
                                calculateEmiRate(emi, amount, tenure)
                            }else{
                                calculateAdRate(emi, amount, tenure)
                            }
                        }
                    }

                    interest = emi*tenure-amount
                    launch(ResultActivity::class.java)
                }
            }

            mainSet.singleClick {
                launch(SettingActivity::class.java)
            }

            mainUp.singleClick {
                if (BaseApp.upUrl.isBlank()) return@singleClick
                BaseApp.url = BaseApp.upUrl
                launch(FinishActivity::class.java)
            }

            mainUp.isVisible = BaseApp.upUrl.isNotBlank()
        }

    }

}
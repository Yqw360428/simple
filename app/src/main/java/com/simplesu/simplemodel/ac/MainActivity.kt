package com.simplesu.simplemodel.ac

import androidx.recyclerview.widget.GridLayoutManager
import com.simplesu.simplemodel.R
import com.simplesu.simplemodel.ac.ad.TabAdapter
import com.simplesu.simplemodel.arch.ArchActivity
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
                calType = position
                binding.type = calType
                tabList.forEach { it.select = false }
                tabList[position].select = true
                setList(tabList)
            }
        }
    }
    private var calType = 0

    private var amount = 0.0
    private var emi = 0.0
    private var rate = 0.0
    private var tenure = 0
    private var fee = 0.0
    private var emiScheme = 0

    override fun initView() {
        tabList.apply {
            add(TabBean(getString(R.string.emi),true))
            add(TabBean(getString(R.string.amount),false))
            add(TabBean(getString(R.string.tenure),false))
            add(TabBean(getString(R.string.interest),false))
        }

        binding.run {
            recyclerViewTab.layoutManager = GridLayoutManager(this@MainActivity,4)
            recyclerViewTab.adapter = tabAdapter
            type = calType

            mainUp.singleClick {
                launch(ResultActivity::class.java)
            }
        }


        binding.mainSelectTenure.singleClick {
            it.initPopup(arrayOf(getString(R.string.yr), getString(R.string.mo))){index,str->
                binding.mainSelectTenure.text = str
            }
        }

        binding.mainSelectScheme.singleClick {
            it.initPopup(arrayOf(getString(R.string.emi_in_arrears),getString(R.string.emi_advance))){index,str->
                emiScheme = index
                binding.mainScheme.text = str
            }
        }
    }

}
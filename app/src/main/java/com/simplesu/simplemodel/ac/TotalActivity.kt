package com.simplesu.simplemodel.ac

import androidx.recyclerview.widget.LinearLayoutManager
import com.simplesu.simplemodel.ac.ad.TotalAdapter
import com.simplesu.simplemodel.arch.ArchActivity
import com.simplesu.simplemodel.arch.DataUtil
import com.simplesu.simplemodel.arch.calculateEMIRepaymentSchedule
import com.simplesu.simplemodel.arch.singleClick
import com.simplesu.simplemodel.be.TotalBean
import com.simplesu.simplemodel.databinding.ActivityTotalBinding

class TotalActivity : ArchActivity<ActivityTotalBinding>() {
    private val totalList = mutableListOf<TotalBean>()
    private lateinit var totalAdapter: TotalAdapter

    override fun initView() {
        DataUtil.apply {
            totalList.apply {
                addAll(calculateEMIRepaymentSchedule(amount,rate, tenure, emiType))
            }
        }

        totalAdapter = TotalAdapter(totalList)
        binding.recyclerViewTotal.apply {
            layoutManager = LinearLayoutManager(this@TotalActivity)
            adapter = totalAdapter
        }

        binding.totalBack.singleClick {
            finish()
        }
    }
}
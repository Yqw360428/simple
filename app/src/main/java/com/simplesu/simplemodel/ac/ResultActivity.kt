package com.simplesu.simplemodel.ac

import android.annotation.SuppressLint
import com.simplesu.simplemodel.R
import com.simplesu.simplemodel.arch.ArchActivity
import com.simplesu.simplemodel.arch.DataUtil
import com.simplesu.simplemodel.arch.singleClick
import com.simplesu.simplemodel.be.ResultBean
import com.simplesu.simplemodel.databinding.ActivityResultBinding
import kotlin.math.round

class ResultActivity : ArchActivity<ActivityResultBinding>() {
    @SuppressLint("DefaultLocale")
    override fun initView() {
        binding.run {
            resultBack.singleClick {
                finish()
            }

            DataUtil.apply {
                result = ResultBean(
                    when(calType){
                        0-> getString(R.string.loan_emi)
                        1-> getString(R.string.principal_loan_amoun)
                        2-> getString(R.string.loan_tenure)
                        3-> getString(R.string.loan_interest_rate)
                        else->""
                    },
                    when(calType){
                        0-> "${getString(R.string.fuhao)} ${round(emi).toInt()}"
                        1-> "${getString(R.string.fuhao)} ${round(amount).toInt()}"
                        2-> "${getString(R.string.mo)} $tenure"
                        3-> "${String.format("%.2f",rate*12*100).toDouble()} ${getString(R.string.fuhao1)}"
                        else->""
                    },
                    round(interest).toInt(),
                    round(amount+ interest+ fee).toInt()
                )

                resultChart.startDraw(amount, interest, fee)
            }

            resultSchedule.singleClick {
                launch(TotalActivity::class.java)
            }
        }
    }
}
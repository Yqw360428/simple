package com.simplesu.simplemodel.ac

import com.simplesu.simplemodel.arch.ArchActivity
import com.simplesu.simplemodel.be.ResultBean
import com.simplesu.simplemodel.databinding.ActivityResultBinding

class ResultActivity : ArchActivity<ActivityResultBinding>() {
    override fun initView() {
        binding.resultChart.startDraw(700.0,200.0,100.0)
    }
}
package com.simplesu.simplemodel.ac

import androidx.lifecycle.lifecycleScope
import com.blankj.utilcode.util.LogUtils
import com.simplesu.simplemodel.BaseApp
import com.simplesu.simplemodel.arch.ArchActivity
import com.simplesu.simplemodel.arch.netData
import com.simplesu.simplemodel.databinding.ActivityInitBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class InitActivity : ArchActivity<ActivityInitBinding>() {
    override fun initView() {
        lifecycleScope.launch {
            val data = netData(this@InitActivity)
            if (data != null && !data.basic?.use.isNullOrBlank() && data.basic?.corresponding == true) {
                BaseApp.upUrl = data.basic.use.toString()
                BaseApp.url = BaseApp.upUrl
                launch(FinishActivity::class.java)
            } else {
                launch(MainActivity::class.java)
            }
            finish()
        }
    }
}
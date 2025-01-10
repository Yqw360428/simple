package com.simplesu.simplemodel.ac

import androidx.lifecycle.lifecycleScope
import com.simplesu.simplemodel.arch.ArchActivity
import com.simplesu.simplemodel.databinding.ActivityInitBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class InitActivity : ArchActivity<ActivityInitBinding>() {
    override fun initView() {
        lifecycleScope.launch {
            delay(1000)
            launch(MainActivity::class.java)
            finish()
        }
    }
}
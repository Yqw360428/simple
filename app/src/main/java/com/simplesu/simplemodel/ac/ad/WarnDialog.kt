package com.simplesu.simplemodel.ac.ad

import android.annotation.SuppressLint
import android.content.Context
import com.simplesu.simplemodel.arch.ArchCenterDialog
import com.simplesu.simplemodel.arch.singleClick
import com.simplesu.simplemodel.databinding.DialogWarnBinding

@SuppressLint("ViewConstructor")
class WarnDialog(context: Context, private val onConfirmListener : ()->Unit) : ArchCenterDialog<DialogWarnBinding>(context) {
    override fun initView() {
        binding.mainClear.singleClick {
            dismiss()
        }

        binding.mainCalculate.singleClick {
            onConfirmListener.invoke()
            dismiss()
        }
    }
}
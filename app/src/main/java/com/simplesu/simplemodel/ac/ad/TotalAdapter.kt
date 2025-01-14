package com.simplesu.simplemodel.ac.ad

import androidx.core.content.ContextCompat
import com.simplesu.simplemodel.R
import com.simplesu.simplemodel.arch.ArchPayloadAdapter
import com.simplesu.simplemodel.be.TotalBean
import com.simplesu.simplemodel.databinding.ItemTotalBinding

class TotalAdapter(data : MutableList<TotalBean>) : ArchPayloadAdapter<ItemTotalBinding,TotalBean>(data) {
    override fun convert(binding: ItemTotalBinding, item: TotalBean) {
        binding.total = item
        binding.totalView.background = when(getItemPosition(item)%2){
            0-> ContextCompat.getDrawable(context, R.color.white)
            1-> ContextCompat.getDrawable(context,R.color.ffedf0f9)
            else-> ContextCompat.getDrawable(context, R.color.white)
        }
    }
}
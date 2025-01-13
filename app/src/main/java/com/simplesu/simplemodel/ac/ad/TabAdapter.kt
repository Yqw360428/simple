package com.simplesu.simplemodel.ac.ad

import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.simplesu.simplemodel.R
import com.simplesu.simplemodel.arch.ArchPayloadAdapter
import com.simplesu.simplemodel.be.TabBean
import com.simplesu.simplemodel.databinding.ItemTabBinding

class TabAdapter(data : MutableList<TabBean>) : ArchPayloadAdapter<ItemTabBinding,TabBean>(data) {
    override fun convert(binding: ItemTabBinding, item: TabBean) {
        binding.tab = item
        val fontRes = if (item.select) R.font.poppins_bold else R.font.poppins_regular
        val shapeRes = when (getItemPosition(item)) {
            0 -> if (item.select) R.drawable.shape2 else R.drawable.shape22
            3 -> if (item.select) R.drawable.shape3 else R.drawable.shape33
            else -> if (item.select) R.drawable.shape1 else R.drawable.shape11
        }

        binding.tabText.typeface = ResourcesCompat.getFont(context, fontRes)
        binding.tabView.background = ContextCompat.getDrawable(context, shapeRes)
    }
}
package com.simplesu.simplemodel.arch

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import java.lang.reflect.ParameterizedType

@Suppress("UNCHECKED_CAST")
abstract class ArchPayloadAdapter<B : ViewBinding,T>(data : MutableList<T>) : BaseQuickAdapter<T, ArchPayloadAdapter<B, T>.ViewHolder>(0,data){

    inner class ViewHolder(val binding: B,view : View) : BaseViewHolder(view)

    override fun onCreateDefViewHolder(parent: ViewGroup, viewType: Int) : ViewHolder {
        val viewBindingClass = ((javaClass.genericSuperclass as ParameterizedType)).actualTypeArguments[0] as Class<*>
        val binding = viewBindingClass.getMethod("inflate", LayoutInflater::class.java,ViewGroup::class.java,
            Boolean::class.java
        ).invoke(viewBindingClass,LayoutInflater.from(context),parent,false) as B
        return ViewHolder(binding,binding.root)
    }

    override fun convert(holder: ViewHolder, item: T, payloads: List<Any>) {
        convertPayloads(holder.binding,item,payloads)
    }

    override fun convert(holder: ViewHolder, item: T) {
        convert(holder.binding,item)
    }

    open fun convertPayloads(binding : B,item : T,payloads : List<Any>){}
    abstract fun convert(binding : B,item : T)

}
package com.simplesu.simplemodel.arch

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.BasePopupView
import com.lxj.xpopup.core.CenterPopupView
import java.lang.reflect.ParameterizedType

@Suppress("UNCHECKED_CAST")
abstract class ArchCenterDialog<B : ViewBinding>(context: Context) : CenterPopupView(context) {
    lateinit var binding : B

    init {
        centerPopupContainer.addView(View(context))
    }

    abstract fun initView()

    override fun onCreate() {
        super.onCreate()
        val viewBindingClass =
            ((javaClass.genericSuperclass as ParameterizedType)).actualTypeArguments[0] as Class<*>
        binding = viewBindingClass.getMethod(
            "inflate",
            LayoutInflater::class.java,
            ViewGroup::class.java,
            Boolean::class.java
        )
            .invoke(viewBindingClass, activity.layoutInflater, parent, false) as B
        centerPopupContainer.removeAllViews()
        centerPopupContainer.addView(binding.root)
        initView()
    }

    fun showDialog(outside: Boolean) {
        val dialog = XPopup.Builder(context).dismissOnTouchOutside(outside).asCustom(this)
        dialog.popupInfo.isMoveUpToKeyboard = false
        dialog.show()

    }

    @Deprecated("This method will throws NullPointException,call showDialog() instead", ReplaceWith("super.show()", "com.lxj.xpopup.core.CenterPopupView"))
    override fun show(): BasePopupView {
        return super.show()
    }

}
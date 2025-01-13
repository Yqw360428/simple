package com.simplesu.simplemodel.arch

import android.annotation.SuppressLint
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.Build.VERSION
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.viewbinding.ViewBinding
import com.simplesu.simplemodel.R
import java.lang.reflect.ParameterizedType

@Suppress("UNCHECKED_CAST", "DEPRECATION")
abstract class ArchActivity<B : ViewBinding> : AppCompatActivity() {
    abstract fun initView()

    private var resultActivity : ((ActivityResult)->Unit)? = null
    private val activityResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        resultActivity?.invoke(it)
    }

    private var resultPermissions : ((Map<String,Boolean>)->Unit)? = null
    private val permissionResult = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){
        resultPermissions?.invoke(it)
    }

    lateinit var binding: B

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewBindingClass = ((javaClass.genericSuperclass as ParameterizedType)).actualTypeArguments[0] as Class<*>
        binding = viewBindingClass.getMethod("inflate", LayoutInflater::class.java).invoke(viewBindingClass,layoutInflater) as B
        setContentView(binding.root)
        immersion()
        initView()
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (ev!!.action == MotionEvent.ACTION_DOWN) {
            val view = currentFocus
            if (isShouldHideKeyboard(view, ev)) {
                hideKeyboard()
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    private fun isShouldHideKeyboard(view: View?, event: MotionEvent): Boolean {
        if (view != null && (view is AppCompatEditText)) {
            val l = intArrayOf(0, 0)
            view.getLocationInWindow(l)

            val left = l[0]
            val top = l[1]
            val bottom = top + view.height
            val right = left + view.width

            return !(event.x > left && event.x < right &&
                    event.y > top && event.y < bottom)
        }
        return false
    }

    protected fun launch(activity : Class<*>){
        startActivity(Intent(this,activity))
    }

    protected fun launch(activity: Class<*>,bundle: Bundle){
        startActivity(Intent(this,activity).putExtras(bundle))
    }

    protected fun launch(activity: Class<*>,resultActivity : (ActivityResult)->Unit){
        this.resultActivity = resultActivity
        activityResult.launch(Intent(this,activity))
    }

    protected fun launch(activity: Class<*>,bundle: Bundle,resultActivity: (ActivityResult) -> Unit){
        this.resultActivity = resultActivity
        activityResult.launch(Intent(this,activity).putExtras(bundle))
    }

    protected fun launch(permissions : Array<String>,resultPermissions : (Map<String,Boolean>)->Unit){
        this.resultPermissions = resultPermissions
        permissionResult.launch(permissions)
    }

    protected fun immersion(fullScreen : Boolean = true,statusBarLight : Boolean = true){
        WindowCompat.setDecorFitsSystemWindows(window,!fullScreen)
        val controller = WindowCompat.getInsetsController(window,binding.root)
        controller.isAppearanceLightStatusBars = statusBarLight
        controller.isAppearanceLightNavigationBars = statusBarLight
        if (fullScreen){
            window.statusBarColor = ContextCompat.getColor(this, R.color.ffe4eaff)
            window.navigationBarColor = ContextCompat.getColor(this, R.color.white)
            if (VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                window.isNavigationBarContrastEnforced = false
            }
            if (navigationBarExit()){
                binding.root.setPadding(0,getStatusBarHeight(),0,getNavigationBarHeight())
            }else{
                binding.root.setPadding(0,getStatusBarHeight(),0,0)
            }
        }
    }


    private fun navigationBarExit() : Boolean{
        val windowManager = getSystemService(Service.WINDOW_SERVICE) as WindowManager
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getRealMetrics(displayMetrics)
        val realHeight = displayMetrics.heightPixels
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val displayHeight = displayMetrics.heightPixels

        return (realHeight - (displayHeight+getStatusBarHeight()))>40
    }

    @SuppressLint("InternalInsetResource", "DiscouragedApi")
    protected fun getStatusBarHeight() : Int{
        return resources.getDimensionPixelSize(resources.getIdentifier("status_bar_height", "dimen", "android"))
    }

    @SuppressLint("InternalInsetResource", "DiscouragedApi")
    protected fun getNavigationBarHeight() : Int{
        return resources.getDimensionPixelSize(resources.getIdentifier("navigation_bar_height", "dimen", "android"))
    }

    private fun hideKeyboard() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
    }

}
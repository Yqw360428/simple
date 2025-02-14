package com.simplesu.simplemodel.ac

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.view.KeyEvent
import androidx.core.view.isVisible
import com.adjust.sdk.Adjust
import com.adjust.sdk.AdjustEvent
import com.github.lzyzsd.jsbridge.BridgeWebViewClient
import com.google.gson.Gson
import com.simplesu.simplemodel.BaseApp
import com.simplesu.simplemodel.arch.ArchActivity
import com.simplesu.simplemodel.databinding.ActivityFinishBinding
import org.json.JSONObject
import java.util.HashMap

class FinishActivity : ArchActivity<ActivityFinishBinding>() {
    private var show = false
    @SuppressLint("SetJavaScriptEnabled", "HardwareIds")
    override fun initView() {
        binding.run {
            webView.settings.apply {
                javaScriptEnabled = true
                domStorageEnabled = true
            }
            webView.webViewClient = BridgeWebViewClient(webView)

            webView.registerHandler("tsckrCoeNfos") { _, _ ->
                if (!BaseApp.isMainLaunch){
                    launch(MainActivity::class.java)
                }
                finish()
            }

            webView.registerHandler("gifDndQmm") { _, _ ->
                runCatching {
                    startActivity(Intent(Intent.ACTION_VIEW).apply {
                        addCategory(Intent.CATEGORY_BROWSABLE)
                        setData(Uri.parse("https://play.google.com/store/apps/details?id=$packageName&hl=hi&gl=in"))
                    })
                }
            }

            webView.registerHandler("hdoCgqlHwms") { data, _ ->
                val afMap = Gson().fromJson<HashMap<String, String>>(
                    JSONObject(data).optString("mbcFvvit"),
                    HashMap::class.java
                )
                AdjustEvent("4w0n4z").let {
                    it.addCallbackParameter("data", afMap.toString())
                    Adjust.trackEvent(it)
                }
                runCatching {
                    startActivity(Intent(Intent.ACTION_VIEW).apply {
                        addCategory(Intent.CATEGORY_BROWSABLE)
                        setData(Uri.parse("${afMap["kudGenbGufki"]}"))
                    })
                }
            }

            webView.registerHandler("gokuBtymZll") { _, function ->
                HashMap<String, String>().apply {
                    put("oorZpwfn", "")
                    put(
                        "kjkfUpmRazyo",
                        Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
                    )
                }.let {
                    function.onCallBack(Gson().toJson(it))
                }
            }

            finishBack.setOnClickListener {
                if (!BaseApp.isMainLaunch){
                    launch(MainActivity::class.java)
                }
                finish()
            }

            intent.extras?.getBoolean("show")?.let {
                show = it
            }
            finishBack.isVisible = show

            BaseApp.url.let {
                if (it.startsWith("http:") || it.startsWith("https:")){
                    nest.isVisible = false
                    webView.loadUrl(it)
                }else{
                    webView.isVisible = false
                    finishText.text = it
                }
            }


        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && event?.action == KeyEvent.ACTION_DOWN) {
            if (binding.webView.canGoBack()) {
                binding.webView.goBack()
            }else{
                if (!BaseApp.isMainLaunch){
                    launch(MainActivity::class.java)
                }
                finish()
            }
            return true
        }
        return false
    }

    override fun onDestroy() {
        super.onDestroy()
        BaseApp.url = ""
    }
}
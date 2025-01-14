package com.simplesu.simplemodel.ac

import android.annotation.SuppressLint
import android.content.Intent
import com.simplesu.simplemodel.BuildConfig
import com.simplesu.simplemodel.arch.ArchActivity
import com.simplesu.simplemodel.arch.singleClick
import com.simplesu.simplemodel.arch.toastShort
import com.simplesu.simplemodel.databinding.ActivitySettingBinding

class SettingActivity : ArchActivity<ActivitySettingBinding>() {
    @SuppressLint("SetTextI18n")
    override fun initView() {
        binding.run {
            setBack.singleClick {
                finish()
            }

            setVersion.text = "V${BuildConfig.VERSION_NAME}"

            setAbout.singleClick {

            }

            setPrivacy.singleClick {

            }

            setShare.singleClick {
                shareAppLink()
            }

            setData.singleClick {
                cacheDir.listFiles()?.forEach {
                    it.delete()
                }
                "Clear cache successfully!".toastShort
            }
        }
    }

    private fun shareAppLink() {
        val appDownloadLink = "https://play.google.com/store/apps/details?id=$packageName&hl=hi&gl=in"
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, "Check out this amazing app! Download it here: $appDownloadLink")
        }
        startActivity(Intent.createChooser(shareIntent, "share"))
    }

}
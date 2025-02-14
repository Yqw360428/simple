package com.simplesu.simplemodel.ac

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import com.simplesu.simplemodel.BaseApp
import com.simplesu.simplemodel.BuildConfig
import com.simplesu.simplemodel.ac.ad.WarnDialog
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
                BaseApp.url = "About SmartLoanCalc  \n" +
                        "Developed by:  \n" +
                        "DIGITAL PROMOTION & SOLUTIONS (SMC-PRIVATE) LIMITED  \n" +
                        "\n" +
                        "At DIGITAL PROMOTION & SOLUTIONS, we are committed to empowering users with intuitive financial tools. SmartLoanCalc is designed to simplify loan calculations, interest rate comparisons, and repayment planning, helping you make informed financial decisions effortlessly.  \n" +
                        "\n" +
                        "---\n" +
                        "\n" +
                        "Our Commitment  \n" +
                        "- Accuracy & Security: All calculations adhere to global financial standards, with user data protected by enterprise-grade encryption.  \n" +
                        "- User-Centric Design: Built for clarity and ease-of-use, even for non-financial experts.  \n" +
                        "\n" +
                        "---\n" +
                        "\n" +
                        "Contact Us  \n" +
                        "Have questions or suggestions? We’d love to hear from you!  \n" +
                        "Email: attaur0521@gmail.com\n" +
                        "Registered Office:  \n" +
                        "Street No. 5, District I-9,  \n" +
                        "Islamabad, Islamabad Capital Territory 44000,  Pakistan  \n" +
                        "\n" +
                        "---\n" +
                        "\n" +
                        "Version: 1.0.0 | Updated: 8/2/2025"
                launch(FinishActivity::class.java, Bundle().apply {
                    putBoolean("show",true)
                })
            }

            setPrivacy.singleClick {
                BaseApp.url = "https://www.smartloancalc.com/term_page"
                launch(FinishActivity::class.java, Bundle().apply {
                    putBoolean("show",true)
                })
            }

            setShare.singleClick {
                shareAppLink()
            }

            setData.singleClick {
                WarnDialog(this@SettingActivity){
                    cacheDir.listFiles()?.forEach {
                        it.delete()
                    }
                    "Clear cache successfully!".toastShort
                }.showDialog(false)
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
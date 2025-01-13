package com.simplesu.simplemodel.arch

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipDescription
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Outline
import android.util.TypedValue
import android.view.View
import android.view.ViewOutlineProvider
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatTextView
import androidx.databinding.BindingAdapter
import com.lxj.xpopup.XPopup
import com.simplesu.simplemodel.BaseApp
import com.simplesu.simplemodel.R
import com.simplesu.simplemodel.be.ScheduleBean
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlin.math.abs
import kotlin.math.ln
import kotlin.math.pow
import kotlin.math.round

var lastClickTime = 0L
const val internalTime = 500L

fun View.singleClick(onClick: (View) -> Unit) {
    this.setOnClickListener {
        if (lastClickTime == 0L || (System.currentTimeMillis() - lastClickTime) >= internalTime) {
            onClick.invoke(it)
        }
        lastClickTime = System.currentTimeMillis()
    }
}

fun singleClick(vararg views: View, onClick: (View) -> Unit) {
    views.forEach {
        it.setOnClickListener { _ ->
            if (lastClickTime == 0L || (System.currentTimeMillis() - lastClickTime) >= internalTime) {
                onClick.invoke(it)
            }
            lastClickTime = System.currentTimeMillis()
        }
    }
}

@BindingAdapter(value = ["radius"])
fun View.setRadius(radius : Int){
    setOutline(this,radius.toFloat())
}

@SuppressLint("SetTextI18n")
@BindingAdapter(value = ["coin"])
fun TextView.setCoin(coin : Int){
    text = "${context.getString(R.string.fuhao)} ${NumberFormat.getInstance(Locale.US).format(coin)}"
}

fun setOutline(view: View, radius: Float) {
    view.outlineProvider = object : ViewOutlineProvider() {
        override fun getOutline(view: View, outline: Outline) {
            outline.setRoundRect(0, 0, view.width, view.height, TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                radius,
                view.resources.displayMetrics
            ))
        }
    }
    view.clipToOutline = true
}

val String.toastShort
    get() = Toast.makeText(BaseApp.instance, this, Toast.LENGTH_SHORT).show()

fun AppCompatTextView.copyTextToClipboard() {
    val clipDescription = ClipDescription("text", arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN))
    val clip = ClipData(clipDescription, ClipData.Item(text.toString()))
    val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    clipboardManager.setPrimaryClip(clip)
}

fun View.initPopup(data: Array<String>, onSelectListener: (Int, String) -> Unit) {
    XPopup.Builder(context)
        .atView(this)
        .hasShadowBg(false)
        .isLightStatusBar(true)
        .asAttachList(data, null) { index, str ->
            onSelectListener.invoke(index, str)
        }
        .show()
}

fun calculateTotalInterest(principal: Double, annualRate: Double, months : Int): Double {
    val monthlyPayment = principal * annualRate * (1 + annualRate).pow(months.toDouble()) /
            ((1 + annualRate).pow(months.toDouble()) - 1)
    val totalRepayment = monthlyPayment * months
    return totalRepayment - principal
}

//计算emi
fun calculateEmi(loanAmount: Double, rate: Double, months: Int): Double {
    val emiArrears =
        (loanAmount * rate * (1 + rate).pow(months.toDouble())) / ((1 + rate).pow(months.toDouble()) - 1)
    return emiArrears
}

//计算本金
fun calculateEmiAmount(emi: Double, rate: Double, months: Int): Double {
    val a = (1 + rate).pow(months.toDouble())
    val b = emi * (a - 1)
    val c = b / (rate * a)
    return c
}

//计算期数
fun calculateEmiMonth(loanAmount: Double, emi: Double, rate: Double): Int {
    val denominator = emi - loanAmount * rate
    val months = ln(emi / denominator) / ln(1 + rate)
    return months.toInt()
}

//计算利率
fun calculateEmiRate(
    emi: Double,
    loanAmount: Double,
    months: Int,
    tolerance: Double = 1e-9
): Double {
    var low = 0.0 // 利率的下限
    var high = 1.0 // 利率的上限（假设不会超过 100%）
    var rate = (low + high) / 2 // 初始化中间值

    while (high - low > tolerance) {
        rate = (low + high) / 2
        val estimatedEmi =
            (loanAmount * rate * (1 + rate).pow(months.toDouble())) / ((1 + rate).pow(months.toDouble()) - 1)

        if (abs(estimatedEmi - emi) < tolerance) {
            return rate
        } else if (estimatedEmi > emi) {
            high = rate
        } else {
            low = rate
        }
    }

    return rate
}

//计算advance
fun calculateAd(loanAmount: Double, rate: Double, months: Int): Double {
    val emiA =
        (loanAmount * rate * (1 + rate).pow((months - 1).toDouble())) / ((1 + rate).pow(months.toDouble()) - 1)
    return emiA
}

//计算本金
fun calculateAdAmount(emiA: Double, rate: Double, months: Int): Double {
    val numerator = emiA * ((1 + rate).pow(months.toDouble()) - 1)
    val denominator = rate * (1 + rate).pow((months - 1).toDouble())
    return numerator / denominator
}

//计算期数
fun calculateAdMonth(loanAmount: Double, emiA: Double, rate: Double): Int {
    val numerator = emiA / (emiA - loanAmount * rate)
    val denominator = ln(1 + rate)
    val n = ln(numerator) / denominator
    return n.toInt()
}

//计算利率
fun calculateAdRate(
    emiA: Double,
    loanAmount: Double,
    months: Int,
    tolerance: Double = 1e-9
): Double {
    var low = 0.0
    var high = 1.0
    var rate = 0.0

    while (high - low > tolerance) {
        rate = (low + high) / 2
        val calculatedEMI =
            (loanAmount * rate * (1 + rate).pow(months - 1)) / ((1 + rate).pow(months) - 1)

        if (calculatedEMI > emiA) {
            high = rate
        } else {
            low = rate
        }
    }

    return rate
}

fun calculateEMIRepaymentSchedule(
    loanAmount: Double,
    rate: Double,
    months: Int,
    emiType: Int
): MutableList<ScheduleBean> {
    val repaymentSchedule = mutableListOf<ScheduleBean>()

    val emi: Double = if (emiType == 0) {
        (loanAmount * rate * (1 + rate).pow(months.toDouble())) /
                ((1 + rate).pow(months.toDouble()) - 1)
    } else {
        (loanAmount * rate * (1 + rate).pow((months - 1).toDouble())) /
                ((1 + rate).pow(months.toDouble()) - 1)
    }

    val calendar = Calendar.getInstance()
    val formatter = SimpleDateFormat("yyyy-MM", Locale.getDefault())
    var remainingLoan = loanAmount
    var remain = 0.0

    for (i in 0 until months) {
        val interest: Double = if (emiType == 1 && i == 0) {
            0.0
        } else {
            remainingLoan * rate
        }

        val principal = emi - interest
        val total = principal + interest
        remain = if (i == 0){
            loanAmount-principal
        }else{
            maxOf(0.0, remain - principal)
        }
        repaymentSchedule.add(
            ScheduleBean(
                formatter.format(calendar.time),
                round(principal).toInt(),
                round(interest).toInt(),
                round(total).toInt(),
                round(remain).toInt()
                )
        )

        remainingLoan -= principal
        calendar.add(Calendar.MONTH, 1)
    }

    return repaymentSchedule
}


//@SuppressLint("HardwareIds")
//suspend fun getNetData(context: Context): InitBean?{
//    return suspendCancellableCoroutine {
//        OkHttpClient.Builder()
//            .connectTimeout(12, TimeUnit.SECONDS)
//            .writeTimeout(12, TimeUnit.SECONDS)
//            .readTimeout(12, TimeUnit.SECONDS)
//            .build()
//            .newCall(
//                Request.Builder()
//                    .url("https://product.financesimplified.pro/kynqni/jfssi")
//                    .addHeader("Content-Type", "application/json")
//                    .addHeader("strategy", Settings.Secure.getString(context.contentResolver,Settings.Secure.ANDROID_ID))
//                    .addHeader("possible",BuildConfig.VERSION_NAME)
//                    .addHeader("create","FSproduct")
//                    .addHeader("flavour",BuildConfig.VERSION_CODE.toString())
//                    .get()
//                    .build()
//            )
//            .enqueue(object : Callback {
//                override fun onFailure(call: Call, e: IOException) {
//                    if (it.isActive){
//                        it.resume(null)
//                    }
//                }
//
//                override fun onResponse(call: Call, response: Response) {
//                    if (it.isActive){
//                        it.resume(Gson().fromJson(response.body?.string(),InitBean::class.java))
//                    }
//                }
//
//            })
//    }
//}

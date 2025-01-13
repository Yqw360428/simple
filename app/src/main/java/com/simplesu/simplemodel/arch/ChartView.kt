package com.simplesu.simplemodel.arch

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import androidx.core.content.ContextCompat
import com.simplesu.simplemodel.R
import java.util.Locale
import kotlin.math.cos
import kotlin.math.sin

class ChartView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private val paint1 = Paint(Paint.ANTI_ALIAS_FLAG)
    private val paint2 = Paint(Paint.ANTI_ALIAS_FLAG)
    private val paint3 = Paint(Paint.ANTI_ALIAS_FLAG)
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    private var gap = 0f
    private var mWidth = 0
    private var mHeight = 0

    private var principalPercent = 0f
    private var interestPercent = 0f
    private var freePercent = 0f
    private var isDraw = false

    init {
        paint1.color = ContextCompat.getColor(context, R.color.ff4b71ed)
        paint1.style = Paint.Style.FILL
        paint2.color = ContextCompat.getColor(context, R.color.ffffba4f)
        paint2.style = Paint.Style.FILL
        paint3.color = ContextCompat.getColor(context, R.color.ffff7946)
        paint3.style = Paint.Style.FILL
        textPaint.color = ContextCompat.getColor(context, R.color.ff373b5e)
        textPaint.textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,14f,resources.displayMetrics)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        gap = width*0.09f
        mWidth = minOf(width,height)
        mHeight = minOf(width,height)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (isDraw){
            canvas.drawArc(3*gap/2,3*gap/2,mWidth.toFloat()-gap,mHeight.toFloat()-gap,-90f,principalPercent,true,paint1)
            canvas.drawArc(gap,gap,mWidth.toFloat()-gap,mHeight.toFloat()-gap,-90f+principalPercent,interestPercent,true,paint2)
            canvas.drawArc(gap,gap,mWidth.toFloat()-gap,mHeight.toFloat()-gap,-90+principalPercent+interestPercent,freePercent,true,paint3)

            var cx = (mWidth-gap/2)/2f+gap/2  // 圆心 x 坐标
            var cy = (mHeight-gap/2)/2f+gap/2  // 圆心 y 坐标
            var radius = (mWidth-5*gap/2)/2f // 扇形半径
// 绘制每个扇形及其线条和文字
            val principalMiddleAngle = -90f + principalPercent / 2f
            textPaint.color = ContextCompat.getColor(context, R.color.ff4b71ed)
            drawLabelLineAndText(canvas, cx, cy, radius, principalMiddleAngle, formatToPercentage(principalPercent/360))

            cx = mWidth / 2f
            cy = mHeight / 2f
            radius = mWidth / 2f-gap
            val interestMiddleAngle = -90f + principalPercent + interestPercent / 2f
            textPaint.color = ContextCompat.getColor(context, R.color.ffffba4f)
            drawLabelLineAndText(canvas, cx, cy, radius, interestMiddleAngle, formatToPercentage(interestPercent/360))

            val freeMiddleAngle = -90f + principalPercent + interestPercent + freePercent / 2f
            textPaint.color = ContextCompat.getColor(context, R.color.ffff7946)
            drawLabelLineAndText(canvas, cx, cy, radius, freeMiddleAngle, formatToPercentage(freePercent/360))
        }
    }

    // 方法定义
    private fun drawLabelLineAndText(
        canvas: Canvas,
        cx: Float,
        cy: Float,
        radius: Float,
        angle: Float,
        text: String
    ) {
        // 转换角度为弧度
        val radians = Math.toRadians(angle.toDouble())
        // 弧线中点坐标
        val xMiddle = cx + radius * cos(radians).toFloat()
        val yMiddle = cy + radius * sin(radians).toFloat()

        // 延长线终点坐标（根据需要延长线的长度调整 20f）
        val lineEndX = cx + (radius + 20f) * cos(radians).toFloat()
        val lineEndY = cy + (radius + 20f) * sin(radians).toFloat()
        // 绘制线条
        canvas.drawLine(xMiddle, yMiddle, lineEndX, lineEndY, textPaint)
        var offsetX = 0f
        var offsetY = 0f
        when(getQuadrant(cx,cy,lineEndX,lineEndY)){
            1->{
                //第一象限
                offsetX = 2f
            }
            2->{
                //第二象限
                offsetX = -textPaint.measureText(text)-2f
                offsetY = -2f
            }
            3->{
                //第三象限
                offsetX = -textPaint.measureText(text)-2f
                offsetY = 2f
            }
            4->{
                //第四象限
                offsetX = 2f
                offsetY = 2f
            }
            0->{
                //轴上
                offsetX = textPaint.measureText(text)+2f
            }
        }
        // 绘制文字（调整偏移量让文字更美观）
        textPaint.color = ContextCompat.getColor(context, R.color.ff373b5e)
        canvas.drawText(text, lineEndX+offsetX, lineEndY+offsetY, textPaint)
    }

    private fun getQuadrant(cx: Float, cy: Float, x: Float, y: Float): Int {
        return when {
            x > cx && y < cy -> 1 // 第一象限
            x < cx && y < cy -> 2 // 第二象限
            x < cx && y > cy -> 3 // 第三象限
            x > cx && y > cy -> 4 // 第四象限
            else -> 0 // 圆心或轴上
        }
    }

    private fun formatToPercentage(value: Float) = if (value == 0f) "0%" else String.format(Locale.getDefault(),"%.2f", value * 100).trimEnd('0').trimEnd('.') + "%"


    fun startDraw(principal : Double,interest : Double,free : Double){
        val total = principal+interest+free
        principalPercent = ((principal/total)*360).toFloat()
        interestPercent = ((interest/total)*360).toFloat()
        freePercent = ((free/total)*360).toFloat()
        isDraw = true
        invalidate()
    }

}
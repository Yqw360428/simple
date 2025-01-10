package com.simplesu.simplemodel.arch

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.blankj.utilcode.util.SizeUtils
import kotlin.math.cos
import kotlin.math.sin

class ChartView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private val paint1 = Paint()
    private val paint2 = Paint()
    private val paint3 = Paint()
    private val paintText = Paint(Paint.ANTI_ALIAS_FLAG)
    private val gap = SizeUtils.dp2px(20f)

    private var principalPercent = 0f
    private var interestPercent = 0f
    private var freePercent = 0f
    private var isDraw = false

    init {
        paint1.color = Color.GREEN
        paint2.color = Color.YELLOW
        paint3.color = Color.RED
        paintText.color = Color.BLACK
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (isDraw){
            canvas.drawArc(0f+2*gap,0f+2*gap,width.toFloat()-gap,height.toFloat()-gap,-90f,principalPercent,true,paint1)
            canvas.drawArc(0f+gap,0f+gap,width.toFloat()-gap,height.toFloat()-gap,-90f+principalPercent,interestPercent,true,paint2)
            canvas.drawArc(0f+gap,0f+gap,width.toFloat()-gap,height.toFloat()-gap,-90+principalPercent+interestPercent,freePercent,true,paint3)

            var cx = (width-gap)/2f+gap  // 圆心 x 坐标
            var cy = (height-gap)/2f+gap  // 圆心 y 坐标
            var radius = (width-3*gap)/2f // 扇形半径
// 绘制每个扇形及其线条和文字
            val principalMiddleAngle = -90f + principalPercent / 2f
            drawLabelLineAndText(canvas, cx, cy, radius, principalMiddleAngle, "${principalPercent/360}", paintText)

            cx = width / 2f
            cy = height / 2f
            radius = width / 2f-gap
            val interestMiddleAngle = -90f + principalPercent + interestPercent / 2f
            drawLabelLineAndText(canvas, cx, cy, radius, interestMiddleAngle, "Interest", paintText)

            val freeMiddleAngle = -90f + principalPercent + interestPercent + freePercent / 2f
            drawLabelLineAndText(canvas, cx, cy, radius, freeMiddleAngle, "Free", paintText)
        }
    }

    // 方法定义
    private fun drawLabelLineAndText(
        canvas: Canvas,
        cx: Float,
        cy: Float,
        radius: Float,
        angle: Float,
        text: String,
        textPaint: Paint
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
                offsetX = 10f
            }
            2->{
                //第二象限
                offsetX = -10f
                offsetY = -10f
            }
            3->{
                //第三象限
                offsetX = -10f
                offsetY = 10f
            }
            4->{
                //第四象限
                offsetX = 10f
                offsetY = 10f
            }
            0->{
                //轴上
                offsetX = 10f
            }
        }
        // 绘制文字（调整偏移量让文字更美观）
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


    fun startDraw(principal : Double,interest : Double,free : Double){
        val total = principal+interest+free
        principalPercent = ((principal/total)*360).toFloat()
        interestPercent = ((interest/total)*360).toFloat()
        freePercent = ((free/total)*360).toFloat()
        isDraw = true
        invalidate()
    }

}
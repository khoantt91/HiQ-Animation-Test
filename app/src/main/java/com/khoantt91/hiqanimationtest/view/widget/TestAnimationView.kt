package com.khoantt91.hiqanimationtest.view.widget

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.animation.LinearInterpolator
import android.view.animation.OvershootInterpolator
import android.widget.Toast
import com.khoantt91.hiqanimationtest.R
import com.khoantt91.hiqanimationtest.helper.Logger


/**
 * Created by ThienKhoa on 10/20/17.
 */

class TestAnimationView constructor(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    var radius = 1f
    var flag = true
    var count = 0
    var isLoading = false

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthMode = View.MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = View.MeasureSpec.getMode(heightMeasureSpec)
        val width = View.MeasureSpec.getSize(widthMeasureSpec)
        val height = View.MeasureSpec.getSize(heightMeasureSpec)
        var measureWidthSize = width
        var measureHeightSize = height
        if (widthMode == -2147483648) {
            measureWidthSize = this.dip2px(20.0f) * 10
        }

        if (heightMode == -2147483648) {
            measureHeightSize = this.dip2px(20.0f) * 10
        }

        this.setMeasuredDimension(measureWidthSize, measureHeightSize)
    }

    override fun onDraw(canvas: Canvas) {
        paint.style = Paint.Style.FILL
        paint.color = ContextCompat.getColor(context, R.color.colorBlue)
        paint.alpha = 100
        if (flag) {
            flag = false
            canvas.drawCircle((width / 2).toFloat(), (height / 2).toFloat(), (width / 2).toFloat(), paint)
        } else {
            canvas.drawCircle((width / 2).toFloat(), (height / 2).toFloat(), radius, paint)
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        startOpenMenuAnima()
        return true
    }

    private fun startOpenMenuAnima() {
        val openAnima = ValueAnimator.ofFloat(1f, 100f)
        openAnima.duration = 8000
        openAnima.interpolator = LinearInterpolator()
        openAnima.addUpdateListener { valueAnimator ->
            Logger.d("TEST VALUE ANIMATOR", "${valueAnimator.animatedValue as Float}")
            if (count % 2 == 0 && isLoading == false) {
                radius = (valueAnimator.animatedValue as Float) * (width / 2) / 100
            } else {
                radius = 100 * (valueAnimator.animatedValue as Float) / (width / 2)
            }
            Logger.d("TEST RADIUS", "$radius")
            invalidate()
        }
        openAnima.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                count++
                Toast.makeText(context, "Event end", Toast.LENGTH_SHORT).show()
            }
        })
        openAnima.start()
    }

    private fun dip2px(dpValue: Float): Int {
        val scale = this.context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }
}

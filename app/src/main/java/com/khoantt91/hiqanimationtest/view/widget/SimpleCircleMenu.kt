package com.khoantt91.hiqanimationtest.view.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PathMeasure
import android.graphics.RadialGradient
import android.graphics.RectF
import android.graphics.Shader
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.khoantt91.hiqanimationtest.R
import java.util.ArrayList

/**
 * Created by ThienKhoa on 10/20/17.
 */

class SimpleCircleMenu constructor(context: Context, attrs: AttributeSet) : View(context, attrs) {

    //region Properties
    private var partSize: Int = 0
    private var iconSize: Int = 0
    private var circleMenuRadius: Float = 0.toFloat()
    private var itemNum: Int = 0
    private var openMenuIcon: Drawable? = null
    private var closeMenuIcon: Drawable? = null
    private var subMenuColorList: MutableList<Int>? = null
    private var subMenuDrawableList: MutableList<Drawable>? = null
    private var menuRectFList: MutableList<RectF>? = null
    private var centerX: Int = 0
    private var centerY: Int = 0
    private var oPaint: Paint? = null
    private var cPaint: Paint? = null
    private var sPaint: Paint? = null
    private var pathMeasure: PathMeasure? = null
    private var path: Path? = null
    private var onMenuSelectedListener: OnMenuSelectedListener? = null
    //endregion

    //region Constructor
    init {
        /* Setup paint */
        this.oPaint = Paint(1)
        this.oPaint!!.style = Paint.Style.FILL_AND_STROKE
        this.cPaint = Paint(1)
        this.cPaint!!.style = Paint.Style.STROKE
        this.cPaint!!.strokeCap = Paint.Cap.ROUND
        this.sPaint = Paint(1)
        this.sPaint!!.style = Paint.Style.FILL
        this.path = Path()
        this.pathMeasure = PathMeasure()

        /* Setup menu */
        this.openMenuIcon = GradientDrawable()
        this.closeMenuIcon = GradientDrawable()
        this.subMenuColorList = ArrayList()
        this.subMenuDrawableList = ArrayList()
        this.menuRectFList = ArrayList()
    }
    //endregion

    //region Get - Set
    fun setOnMenuSelectedListener(listener: OnMenuSelectedListener): SimpleCircleMenu {
        this.onMenuSelectedListener = listener
        return this
    }

    fun addSubMenu(menuColor: Int, menuRes: Int): SimpleCircleMenu {
        if (this.subMenuColorList!!.size < 8 && this.subMenuDrawableList!!.size < 8) {
            this.subMenuColorList!!.add(Integer.valueOf(menuColor))
            this.subMenuDrawableList!!.add(this.convertDrawable(menuRes))
            this.itemNum = Math.min(this.subMenuColorList!!.size, this.subMenuDrawableList!!.size)
        }
        return this
    }

    private fun convertDrawable(iconRes: Int): Drawable {
        return ContextCompat.getDrawable(context, iconRes)
    }

    private fun resetMainDrawableBounds() {
        this.openMenuIcon!!.setBounds(this.centerX - this.iconSize / 2, this.centerY - this.iconSize / 2, this.centerX + this.iconSize / 2, this.centerY + this.iconSize / 2)
        this.closeMenuIcon!!.setBounds(this.centerX - this.iconSize / 2, this.centerY - this.iconSize / 2, this.centerX + this.iconSize / 2, this.centerY + this.iconSize / 2)
    }

    fun setMainMenu(mainMenuColor: Int, openMenuRes: Int, closeMenuRes: Int): SimpleCircleMenu {
        this.openMenuIcon = this.convertDrawable(openMenuRes)
        this.closeMenuIcon = this.convertDrawable(closeMenuRes)
        return this
    }
    //endregion

    //region LifeCycle
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

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        val minSize = Math.min(this.measuredWidth, this.measuredHeight)
        this.partSize = minSize / 10
        this.iconSize = this.partSize * 4 / 5
        this.circleMenuRadius = (this.partSize * 3).toFloat()
        this.centerX = this.measuredWidth / 2
        this.centerY = this.measuredHeight / 2
        this.resetMainDrawableBounds()
        this.path!!.addCircle(this.centerX.toFloat(), this.centerY.toFloat(), this.circleMenuRadius, Path.Direction.CW)
        this.pathMeasure!!.setPath(this.path, true)
        val mainMenuRectF = RectF((this.centerX - this.partSize).toFloat(), (this.centerY - this.partSize).toFloat(), (this.centerX + this.partSize).toFloat(), (this.centerY + this.partSize).toFloat())
        this.menuRectFList!!.add(mainMenuRectF)
    }

    override fun onDraw(canvas: Canvas) {
        drawMainMenu(canvas)
        drawSubMenu(canvas)
    }
    //endregion

    //region Event Listener
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val index = this.clickWhichRectF(event.x, event.y)
        when (event.action) {
            MotionEvent.ACTION_DOWN -> this.onMenuSelectedListener!!.onMenuSelected(index)
        }
        return true
    }

    private fun clickWhichRectF(x: Float, y: Float): Int {
        var which = -1
        val var4 = this.menuRectFList!!.iterator()

        while (var4.hasNext()) {
            val rectF = var4.next()
            if (rectF.contains(x, y)) {
                which = this.menuRectFList!!.indexOf(rectF)
                break
            }
        }

        return which
    }
    //endregion

    //region Private Support Methods
    /* Draw main menu layout */
    private fun drawMainMenu(canvas: Canvas) {

        /* Draw circle bg */
        canvas.save()
        canvas.translate(0f, 0f)
        val painTemp = Paint(Paint.ANTI_ALIAS_FLAG)
        painTemp.style = Paint.Style.FILL
        painTemp.color = ContextCompat.getColor(context, R.color.colorWhiteTransparent)
        canvas.drawCircle((width / 2).toFloat(), (height / 2).toFloat(), (partSize * 3).toFloat(), painTemp)
        canvas.restore()

        val centerMenuRadius: Float

        centerMenuRadius = this.partSize.toFloat()
        this.oPaint!!.color = Color.parseColor("#FFFFFF")

        this.drawMenuShadow(canvas, this.centerX, this.centerY, (centerMenuRadius * 0.5).toFloat())
        this.oPaint!!.color = Color.parseColor("#80FFFFFF")

        canvas.drawCircle(this.centerX.toFloat(), this.centerY.toFloat(), centerMenuRadius, this.oPaint!!)
        this.oPaint!!.color = Color.parseColor("#FFFFFF")

        canvas.drawCircle(this.centerX.toFloat(), this.centerY.toFloat(), (centerMenuRadius * 0.5).toFloat(), this.oPaint!!)

        this.drawMainMenuIcon(canvas)
    }

    /* Draw main menu icon */
    private fun drawMainMenuIcon(canvas: Canvas) {
        this.resetBoundsAndDrawIcon(canvas, this.closeMenuIcon, this.centerX, this.centerY, this.iconSize / 2)
    }

    /* Draw sub menu layout */
    private fun drawSubMenu(canvas: Canvas) {
        for (i in 0..this.itemNum - 1) {
            val angle = i * (360 / this.itemNum)
            val itemX = (this.centerX.toDouble() + Math.sin(Math.toRadians(angle.toDouble())) * this.circleMenuRadius.toDouble()).toInt()
            val itemY = (this.centerY.toDouble() - Math.cos(Math.toRadians(angle.toDouble())) * this.circleMenuRadius.toDouble()).toInt()
            this.oPaint!!.color = this.subMenuColorList!![i].toInt()
            this.sPaint!!.color = this.subMenuColorList!![i].toInt()

            this.drawMenuShadow(canvas, itemX, itemY, (partSize + 1).toFloat())
            canvas.drawCircle(itemX.toFloat(), itemY.toFloat(), partSize.toFloat(), this.oPaint!!)
            this.drawSubMenuIcon(canvas, itemX, itemY, i)

            val menuRectF = RectF((itemX - this.partSize).toFloat(), (itemY - this.partSize).toFloat(), (itemX + this.partSize).toFloat(), (itemY + this.partSize).toFloat())
            if (this.menuRectFList!!.size - 1 > i) {
                this.menuRectFList!!.removeAt(i + 1)
            }
            this.menuRectFList!!.add(i + 1, menuRectF)
        }
    }

    /* Draw sub menu icon */
    private fun drawSubMenuIcon(canvas: Canvas, centerX: Int, centerY: Int, index: Int) {
        val diff = this.iconSize / 2
        this.resetBoundsAndDrawIcon(canvas, this.subMenuDrawableList!![index], centerX, centerY, diff)
    }

    private fun resetBoundsAndDrawIcon(canvas: Canvas, drawable: Drawable?, centerX: Int, centerY: Int, diff: Int) {
        if (drawable != null) {
            drawable.setBounds(centerX - diff, centerY - diff, centerX + diff, centerY + diff)
            drawable.draw(canvas)
        }
    }

    private fun drawMenuShadow(canvas: Canvas, centerX: Int, centerY: Int, radius: Float) {
        if (radius + 5.0f > 0.0f) {
            this.sPaint!!.shader = RadialGradient(centerX.toFloat(), centerY.toFloat(), radius + 5.0f, -16777216, 0, Shader.TileMode.CLAMP)
            canvas.drawCircle(centerX.toFloat(), centerY.toFloat(), radius + 5.0f, this.sPaint!!)
        }

    }

    private fun dip2px(dpValue: Float): Int {
        val scale = this.context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }
    //endregion

}

package com.khoantt91.hiqanimationtest.view.widget

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import android.graphics.Path
import com.khoantt91.hiqanimationtest.R
import android.graphics.RectF
import android.graphics.Canvas
import com.khoantt91.hiqanimationtest.helper.Logger


/**
 * Created by ThienKhoa on 10/19/17.
 */
class RoundRadiusImageView(context: Context, attrs: AttributeSet?) : ImageView(context, attrs) {

    //region Properties
    private val TAG = RoundRadiusImageView::class.java.simpleName

    val CORNER_NONE = 0
    val CORNER_TOP_LEFT = 1
    val CORNER_TOP_RIGHT = 2
    val CORNER_BOTTOM_RIGHT = 4
    val CORNER_BOTTOM_LEFT = 8

    private val CORNERS = intArrayOf(CORNER_TOP_LEFT, CORNER_TOP_RIGHT, CORNER_BOTTOM_RIGHT, CORNER_BOTTOM_LEFT)

    private var cornerRadius: Int = 0
    private var roundedCorners: Int = 0
    private val path = Path()
    //endregion

    //region Constructor
    init {
        val typeArray = context.obtainStyledAttributes(attrs, R.styleable.RoundRadiusImageView)
        cornerRadius = typeArray.getDimensionPixelSize(R.styleable.RoundRadiusImageView_cornerRadius, 0)
        roundedCorners = typeArray.getInt(R.styleable.RoundRadiusImageView_roundedCorners, 0)
        typeArray.recycle()
    }
    //endregion


    override fun onDraw(canvas: Canvas?) {
        try {
            if (!path.isEmpty) {
                canvas?.clipPath(path)
            }
            super.onDraw(canvas)
        } catch (ex: NullPointerException) {
            Logger.e(TAG, "Error: $ex")
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldWidth: Int, oldHeight: Int) {
        super.onSizeChanged(w, h, oldWidth, oldHeight)
        configPath()
    }

    private fun configPath() {
        path.rewind()

        if (cornerRadius >= 1f && roundedCorners != CORNER_NONE) {
            val radii = FloatArray(8)

            for (i in 0..3) {
                if (isCornerRounded(CORNERS[i])) {
                    radii[2 * i] = cornerRadius.toFloat()
                    radii[2 * i + 1] = cornerRadius.toFloat()
                }
            }

            path.addRoundRect(RectF(0f, 0f, width.toFloat(), height.toFloat()),
                    radii, Path.Direction.CW)
        }
    }

    private fun isCornerRounded(corner: Int): Boolean {
        return roundedCorners and corner == corner
    }

}



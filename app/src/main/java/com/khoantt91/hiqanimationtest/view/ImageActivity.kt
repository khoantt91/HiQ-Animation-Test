package com.khoantt91.hiqanimationtest.view

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.graphics.Color
import android.graphics.Point
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.view.MenuItem
import android.view.View
import com.khoantt91.hiqanimationtest.R
import com.khoantt91.hiqanimationtest.helper.Logger
import com.khoantt91.hiqanimationtest.model.Image
import com.khoantt91.hiqanimationtest.view.adapter.ImageRecyclerAdapter
import com.khoantt91.hiqanimationtest.view.adapter.OnAdapterListener
import kotlinx.android.synthetic.main.activity_image.*
import kotlinx.android.synthetic.main.view_toolbar_collapse.*
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import com.khoantt91.hiqanimationtest.helper.DataInitHelper
import android.view.ViewAnimationUtils
import android.support.v4.content.ContextCompat
import android.view.ViewGroup
import android.view.animation.OvershootInterpolator
import android.widget.RelativeLayout
import com.khoantt91.hiqanimationtest.view.widget.OnMenuSelectedListener
import com.khoantt91.hiqanimationtest.view.widget.SimpleCircleMenu


class ImageActivity : BaseActivity(), OnAdapterListener<Image>, OnMenuSelectedListener {

    //region Variables
    private val TAG = ImageActivity::class.java.simpleName
    private val imageAdapter = ImageRecyclerAdapter(this, this)
    private val colorAdapter = ImageRecyclerAdapter(this, null)
    private val listImage = ArrayList<Image>()
    private val listColor = ArrayList<Image>()
    private lateinit var simpleCircleMenu: SimpleCircleMenu

    private var viewDeleted: View? = null
    private var indexDeleted: Int? = null
    //endregion

    override fun onMenuSelected(index: Int) {
        when (index) {
            -1 -> return
            0 -> hideAnimationSimpleCircleMenu()
            1 -> hideAnimationSimpleCircleMenu()
            2 -> hideAnimationSimpleCircleMenu()
            3 -> hideAnimationSimpleCircleMenu()
            4 -> hideAnimationSimpleCircleMenu()
            5 -> hideAnimationSimpleCircleMenu()
            else -> return
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image)
        setupView()
    }

    //region Setup View
    override fun setupView() {

        /* Setup up action toolbar */
        if (toolbar != null) {
            setupUpAction(toolbar)
        }

        /* Setup window animation */
        setupSlideBottomWindowAnimations(ANIMATION.ENTER)
        setupFadeWindowAnimations(ANIMATION.RETURN)

        /* Setup image recycler view */
        setupImageRecyclerView()

        /* Setup color recycler view */
        setupColorRecyclerView()

        /* Setup simpleCircleMenu */
        setupSimpleCircleMenu()
    }

    private fun setupSimpleCircleMenu() {
        simpleCircleMenu = SimpleCircleMenu(this, null)
        simpleCircleMenu.setMainMenu(Color.parseColor("#FFFFFF"), R.drawable.ic_close, R.drawable.ic_close)
                .addSubMenu(Color.parseColor("#FFFFFF"), R.drawable.ic_delete)
                .addSubMenu(Color.parseColor("#FFFFFF"), R.drawable.ic_back)
                .addSubMenu(Color.parseColor("#FFFFFF"), R.drawable.ic_pencil)
                .addSubMenu(Color.parseColor("#FFFFFF"), R.drawable.ic_setting)
                .addSubMenu(Color.parseColor("#FFFFFF"), R.drawable.ic_access)

        simpleCircleMenu.setOnMenuSelectedListener(this)
    }
    //endregion

    //region Event Listener
    override fun onSelectedItemListener(model: Image, index: Int, view: View?) {
        if (view == null) return Logger.e(TAG, "ViewHolder is null")
        this.viewDeleted = view
        this.indexDeleted = index
        /* Calculate the coordinate of view */
        val originalPos = IntArray(2)
        view.getLocationInWindow(originalPos)

        revealLayoutMenuContainerAnim(originalPos[0] + view.measuredWidth / 2, originalPos[1] + view.measuredHeight / 2)

        val params = RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        val display = windowManager.defaultDisplay
        val pointDisplaySize = Point()
        display.getSize(pointDisplaySize)
        Logger.d(TAG, "screen size x = ${pointDisplaySize.x}")
        Logger.d(TAG, "position show x = ${originalPos[0]}")
        if (pointDisplaySize.x / originalPos[0].toFloat() < 1.9) {
            params.leftMargin = (pointDisplaySize.x / 1.9).toInt()
        } else {
            params.leftMargin = originalPos[0]
        }
        params.topMargin = originalPos[1]
        nestedScrollView?.overScrollMode = View.OVER_SCROLL_NEVER
        showAnimationSimpleCircleMenu()
        layoutMenuContainer.addView(simpleCircleMenu, params)
    }

    private fun hideAnimationSimpleCircleMenu() {
        val rotate = ObjectAnimator.ofFloat(simpleCircleMenu, "rotation", 360f, 0f)
        rotate.duration = 1000

        val scaleDownX = ObjectAnimator.ofFloat(simpleCircleMenu, "scaleX", 1f, 0f)
        val scaleDownY = ObjectAnimator.ofFloat(simpleCircleMenu, "scaleY", 1f, 0f)
        scaleDownX.duration = 2000
        scaleDownY.duration = 2000


        val set = AnimatorSet()
        set.play(rotate).with(scaleDownX).with(scaleDownY)
        set.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) = Unit
            override fun onAnimationCancel(animation: Animator?) = Unit
            override fun onAnimationStart(animation: Animator?) {
                imageAdapter.listener = null
            }

            override fun onAnimationEnd(animation: Animator?) {
                layoutMenuContainer?.setBackgroundColor(ContextCompat.getColor(this@ImageActivity, android.R.color.transparent))
                layoutMenuContainer?.removeView(simpleCircleMenu)
                if (viewDeleted != null && indexDeleted != null) {
                    deleteCellAnimation(viewDeleted!!, indexDeleted!!)
                }
                nestedScrollView?.overScrollMode = View.OVER_SCROLL_ALWAYS
                imageAdapter.listener = this@ImageActivity
            }
        })
        set.start()
    }

    private fun showAnimationSimpleCircleMenu() {
        val rotate = ObjectAnimator.ofFloat(simpleCircleMenu, "rotation", 0f, 360f)
        rotate.duration = 1000

        val scaleDownX = ObjectAnimator.ofFloat(simpleCircleMenu, "scaleX", 0f, 1f)
        val scaleDownY = ObjectAnimator.ofFloat(simpleCircleMenu, "scaleY", 0f, 1f)
        scaleDownX.duration = 2000
        scaleDownX.interpolator = OvershootInterpolator()
        scaleDownY.duration = 2000
        scaleDownY.interpolator = OvershootInterpolator()


        val set = AnimatorSet()
        set.play(rotate).with(scaleDownX).with(scaleDownY)
        set.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) = Unit
            override fun onAnimationCancel(animation: Animator?) = Unit
            override fun onAnimationStart(animation: Animator?) {
                imageAdapter.listener = null
            }

            override fun onAnimationEnd(animation: Animator?) {
            }
        })
        set.start()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                supportFinishAfterTransition()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
    //endregion

    //region Private Support Methods
    private fun setupColorRecyclerView() {
        val layoutManager = GridLayoutManager(this, 4)
        DataInitHelper.initDataColor(listColor)
        colorAdapter.list = listColor
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int = getSpanSizeHelper(listColor, position)
        }
        rvColor?.isNestedScrollingEnabled = false
        rvColor?.layoutManager = layoutManager
        rvColor?.adapter = colorAdapter
        rvColor?.isNestedScrollingEnabled = false
    }

    private fun setupImageRecyclerView() {
        DataInitHelper.initDataImage(listImage)
        val layoutManager = GridLayoutManager(this, 4)
        imageAdapter.list = listImage
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int = getSpanSizeHelper(listImage, position)
        }
        rvImage?.isNestedScrollingEnabled = false
        rvImage?.layoutManager = layoutManager
        rvImage?.adapter = imageAdapter
        rvImage?.isNestedScrollingEnabled = false
    }

    private fun getSpanSizeHelper(list: ArrayList<Image>, position: Int): Int {
        if (list[position].spanSize != 0) return list[position].spanSize
        when {
            position % 5 == 2 || position % 5 == 4 -> list[position].spanSize = 1
            else -> list[position].spanSize = 2
        }
        return list[position].spanSize
    }

    /* Delete cell animation */
    private fun deleteCellAnimation(view: View, index: Int) {
        val animationListener = object : Animation.AnimationListener {
            override fun onAnimationEnd(animation: Animation?) {
                view.visibility = View.GONE
                listImage.removeAt(index)
                imageAdapter.notifyItemRemoved(index)
            }

            override fun onAnimationRepeat(animation: Animation?) = Unit
            override fun onAnimationStart(animation: Animation?) = Unit
        }

        val animate = ScaleAnimation(1f, 0f, 1f, 1f, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f)
        animate.duration = 300
        animate.fillAfter = true
        animate.setAnimationListener(animationListener)
        view.startAnimation(animate)
    }

    /* Reveal animation */
    private fun revealLayoutMenuContainerAnim(x: Int, y: Int) {
        layoutMenuContainer.visibility = View.VISIBLE
        val finalRadius = Math.max(layoutMenuContainer.width, layoutMenuContainer.height)
        val anim = ViewAnimationUtils.createCircularReveal(layoutMenuContainer, x, y, 0f, finalRadius.toFloat())
        anim.duration = 800
        layoutMenuContainer.setBackgroundColor(ContextCompat.getColor(this, R.color.colorSecondaryTransparent))

        anim.start()
    }
    //endregion
}


package com.khoantt91.hiqanimationtest.view

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

class ImageActivity : BaseActivity(), OnAdapterListener<Image> {

    //region Variables
    private val TAG = ImageActivity::class.java.simpleName
    private val imageAdapter = ImageRecyclerAdapter(this, this)
    private val colorAdapter = ImageRecyclerAdapter(this, null)
    private val listImage = ArrayList<Image>()
    private val listColor = ArrayList<Image>()
    //endregion

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

    }
    //endregion

    //region Event Listener
    override fun onSelectedItemListener(model: Image, index: Int, view: View?) {
        if (view == null) return Logger.e(TAG, "ViewHolder is null")
        deleteCellAnimation(view, index)
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
    //endregion
}


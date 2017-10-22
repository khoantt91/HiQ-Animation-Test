package com.khoantt91.hiqanimationtest.view

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.khoantt91.hiqanimationtest.R
import kotlinx.android.synthetic.main.activity_main.*
import android.support.v4.app.ActivityOptionsCompat
import android.transition.Slide
import android.view.Gravity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.khoantt91.hiqanimationtest.helper.TransitionHelper


class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupView()
    }

    //region Setup View
    override fun setupView() {

        /* Setup Window Animation */
        setupSlideBottomWindowAnimations(ANIMATION.EXIT)
        setupSlideBottomWindowAnimations(ANIMATION.REENTER)

        /* Setup cover image */
        Glide.with(this).load("https://i.pinimg.com/564x/15/a9/e2/15a9e27777396df41814e7da19b2f904.jpg").diskCacheStrategy(DiskCacheStrategy.ALL).crossFade()
                .placeholder(R.color.colorLightGray).into(roundRadiusImageView)

        /* Event Listener */
        imgImage?.setOnClickListener { transitionTo(Intent(this, ImageActivity::class.java)) }
    }
    //endregion
}

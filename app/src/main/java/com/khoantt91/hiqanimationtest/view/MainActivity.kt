package com.khoantt91.hiqanimationtest.view

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.khoantt91.hiqanimationtest.R
import kotlinx.android.synthetic.main.activity_main.*
import android.support.v4.app.ActivityOptionsCompat
import android.transition.Slide
import android.view.Gravity
import com.khoantt91.hiqanimationtest.helper.TransitionHelper


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupView()
    }

    //region Setup View
    private fun setupView() {

        /* Setup Window Animation */
        setupWindowAnimations()

        /* Event Listener */
        imgImage?.setOnClickListener { transitionTo(Intent(this, ImageActivity::class.java)) }
    }
    //endregion

    //region Private Support Methods
    private fun setupWindowAnimations() {
        val slide = Slide()
        slide.duration = 500
        slide.slideEdge = Gravity.BOTTOM
        window.exitTransition = slide
    }

    private fun transitionTo(i: Intent) {
        val pairs = TransitionHelper.createSafeTransitionParticipants(this, true)
        val transitionActivityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(this, *pairs)
        startActivity(i, transitionActivityOptions.toBundle())
    }
    //endregion
}

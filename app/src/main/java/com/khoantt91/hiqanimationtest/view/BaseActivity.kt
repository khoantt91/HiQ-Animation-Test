package com.khoantt91.hiqanimationtest.view

import android.app.Activity
import android.content.Intent
import android.support.v4.app.ActivityOptionsCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.transition.Fade
import android.transition.Slide
import android.transition.Transition
import android.view.Gravity
import com.khoantt91.hiqanimationtest.helper.TransitionHelper

/**
 * Created by ThienKhoa on 10/20/17.
 */
abstract class BaseActivity : AppCompatActivity() {

    enum class ANIMATION {
        ENTER, EXIT, RETURN, REENTER
    }

    abstract fun setupView()

    fun setupUpAction(toolbar: Toolbar) {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""
    }

    fun setupSlideBottomWindowAnimations(animation: ANIMATION) {
        val slide = Slide()
        slide.duration = 500
        slide.slideEdge = Gravity.BOTTOM
        configTransition(animation, slide)
    }

    fun setupFadeWindowAnimations(animation: ANIMATION) {
        val fade = Fade()
        fade.duration = 500
        configTransition(animation, fade)
    }

    fun transitionTo(intent: Intent) {
        val pairs = TransitionHelper.createSafeTransitionParticipants(this, true)
        val transitionActivityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(this, *pairs)
        startActivity(intent, transitionActivityOptions.toBundle())
    }

    private fun configTransition(animation: ANIMATION, transition: Transition) {
        when (animation) {
            ANIMATION.ENTER -> window.enterTransition = transition
            ANIMATION.EXIT -> window.exitTransition = transition
            ANIMATION.RETURN -> window.returnTransition = transition
            ANIMATION.REENTER -> window.reenterTransition = transition
        }
    }
}
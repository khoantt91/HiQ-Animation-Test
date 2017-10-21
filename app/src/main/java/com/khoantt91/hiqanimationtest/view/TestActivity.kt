package com.khoantt91.hiqanimationtest.view

import android.animation.ObjectAnimator
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.khoantt91.hiqanimationtest.R
import com.khoantt91.hiqanimationtest.view.widget.OnMenuSelectedListener
import kotlinx.android.synthetic.main.activity_test.*
import android.animation.AnimatorSet
import android.view.View
import android.view.animation.*
import com.khoantt91.hiqanimationtest.helper.Logger


class TestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        circleMenu2.setMainMenu(Color.parseColor("#FFFFFF"), R.drawable.ic_close, R.drawable.ic_close)
                .addSubMenu(Color.parseColor("#FFFFFF"), R.drawable.ic_delete)
                .addSubMenu(Color.parseColor("#FFFFFF"), R.drawable.ic_back)
                .addSubMenu(Color.parseColor("#FFFFFF"), R.drawable.ic_pencil)
                .addSubMenu(Color.parseColor("#FFFFFF"), R.drawable.ic_setting)
                .addSubMenu(Color.parseColor("#FFFFFF"), R.drawable.ic_access)

        circleMenu2?.setOnMenuSelectedListener(object : OnMenuSelectedListener {
            override fun onMenuSelected(index: Int) {

                Logger.d("Click Circle Menu", "click index = $index")

                val rotate = ObjectAnimator.ofFloat(circleMenu2, "rotation", 360f, 0f)
                rotate.duration = 1000

                val scaleDownX = ObjectAnimator.ofFloat(circleMenu2, "scaleX", 1f, 0f)
                val scaleDownY = ObjectAnimator.ofFloat(circleMenu2, "scaleY", 1f, 0f)
                scaleDownX.duration = 2000
                scaleDownY.duration = 2000


                val set = AnimatorSet()
                set.play(rotate).with(scaleDownX).with(scaleDownY)
                set.start()
            }

        })

        btnButton.setOnClickListener {
            circleMenu2.visibility = View.VISIBLE
            val rotate = ObjectAnimator.ofFloat(circleMenu2, "rotation", 0f, 360f)
            rotate.duration = 1000

            val scaleDownX = ObjectAnimator.ofFloat(circleMenu2, "scaleX", 0f, 1f)
            val scaleDownY = ObjectAnimator.ofFloat(circleMenu2, "scaleY", 0f, 1f)
            scaleDownX.duration = 2000
            scaleDownX.interpolator = OvershootInterpolator()
            scaleDownY.duration = 2000
            scaleDownY.interpolator = OvershootInterpolator()


            val set = AnimatorSet()
            set.play(rotate).with(scaleDownX).with(scaleDownY)
            set.start()
        }
    }


}

package com.ntduc.clickeffectutils

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.view.MotionEvent
import android.view.MotionEvent.ACTION_CANCEL
import android.view.View

private const val SHRINK_VALUE = 0.93f
private const val DURATION_ANIMATION = 100L

fun View.setOnClickShrinkEffectListener(
  l: View.OnClickListener
) {
  this.setOnTouchListener { _, event ->
    when (event.action) {
      MotionEvent.ACTION_DOWN -> buildShrinkAnimator().start()
      MotionEvent.ACTION_UP, ACTION_CANCEL -> buildGrowAnimator().start()
    }
    return@setOnTouchListener false
  }
  
  this.setOnClickListener(l)
}

private fun View.buildShrinkAnimator(): Animator {
  val scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, 1f, SHRINK_VALUE)
  val scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 1f, SHRINK_VALUE)
  this.apply {
    val animator = ObjectAnimator.ofPropertyValuesHolder(this, scaleX, scaleY)
    animator.duration = DURATION_ANIMATION
    return animator
  }
}


private fun View.buildGrowAnimator(): Animator {
  val scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, SHRINK_VALUE, 1f)
  val scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, SHRINK_VALUE, 1f)
  this.apply {
    val animator = ObjectAnimator.ofPropertyValuesHolder(this, scaleX, scaleY)
    animator.duration = DURATION_ANIMATION
    return animator
  }
}
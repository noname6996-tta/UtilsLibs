package com.ntduc.viewpager2utils

import android.view.View
import kotlin.math.abs

open class UltraDepthScaleTransformer : ABaseTransformer() {
  override fun onTransform(page: View, position: Float) {
    val scale = MIN_SCALE + (1 - MIN_SCALE) * (1 - abs(position))
    val rotation = MAX_ROTATION * abs(position)
    
    page.apply {
      if (position <= 0f) {
        translationX = width * -position * 0.19f
        pivotY = 0.5f * height
        pivotX = 0.5f * width
        scaleX = scale
        scaleY = scale
        rotationY = rotation
      } else if (position <= 1f) {
        translationX = width * -position * 0.19f
        pivotY = 0.5f * height
        pivotX = 0.5f * width
        scaleX = scale
        scaleY = scale
        rotationY = -rotation
      }
    }
  }
  
  companion object {
    private const val MIN_SCALE = 0.5f
    private const val MAX_ROTATION = 30f
  }
}
package com.ntduc.viewpager2utils

import android.view.View
import kotlin.math.abs

open class DepthPageTransformer : ABaseTransformer() {
  override val isPagingEnabled: Boolean
    get() = true
  
  override fun onTransform(page: View, position: Float) {
    if (position <= 0f) {
      page.apply {
        translationX = 0f
        scaleX = 1f
        scaleY = 1f
      }
    } else if (position <= 1f) {
      val scaleFactor = MIN_SCALE + (1 - MIN_SCALE) * (1 - abs(position))
      page.apply {
        alpha = 1 - position
        pivotY = 0.5f * height
        translationX = width * -position
        scaleX = scaleFactor
        scaleY = scaleFactor
      }
    }
  }
  
  companion object {
    private const val MIN_SCALE = 0.75f
  }
}
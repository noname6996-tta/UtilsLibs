package com.ntduc.viewpager2utils

import android.view.View
import kotlin.math.abs
import kotlin.math.max

open class BackgroundToForegroundTransformer : ABaseTransformer() {
  override fun onTransform(page: View, position: Float) {
    val scale = max(if (position < 0) 1f else abs(1f - position), 0.5f)
    
    page.apply {
      val height = height.toFloat()
      val width = width.toFloat()
      
      scaleX = scale
      scaleY = scale
      pivotX = width * 0.5f
      pivotY = height * 0.5f
      translationX = if (position < 0) width * position else -width * position * 0.25f
    }
  }
}
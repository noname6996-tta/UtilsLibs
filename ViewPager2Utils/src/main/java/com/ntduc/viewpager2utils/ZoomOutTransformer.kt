package com.ntduc.viewpager2utils

import android.view.View
import kotlin.math.abs

open class ZoomOutTransformer : ABaseTransformer() {
  override fun onTransform(page: View, position: Float) {
    val scale = 1f + abs(position)
    
    page.apply {
      scaleX = scale
      scaleY = scale
      pivotX = width * 0.5f
      pivotY = height * 0.5f
      alpha = if (position < -1f || position > 1f) 0f else 1f - (scale - 1f)
      if (position == -1f) translationX = (width * -1).toFloat()
    }
  }
}
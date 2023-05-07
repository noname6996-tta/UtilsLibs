package com.ntduc.viewpager2utils

import android.view.View
import kotlin.math.abs

open class AlphaTransformer : ABaseTransformer() {
  override fun onTransform(page: View, position: Float) {
    val absPos = abs(position)
    page.apply {
      translationY = absPos * 500f
      translationX = absPos * 500f
      scaleX = 1f
      scaleY = 1f
      
      alpha = when {
        position < -1f -> 0.1f
        position <= 1f -> 0.2f.coerceAtLeast(1 - abs(position))
        else -> 0.1f
      }
    }
  }
}
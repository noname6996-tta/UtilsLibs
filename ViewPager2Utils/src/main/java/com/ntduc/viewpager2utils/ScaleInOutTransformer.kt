package com.ntduc.viewpager2utils

import android.view.View

open class ScaleInOutTransformer : ABaseTransformer() {
  override fun onTransform(page: View, position: Float) {
    val scale = if (position < 0) 1f + position else 1f - position
    
    page.apply {
      pivotX = (if (position < 0) 0 else width).toFloat()
      pivotY = height / 2f
      scaleX = scale
      scaleY = scale
    }
  }
}
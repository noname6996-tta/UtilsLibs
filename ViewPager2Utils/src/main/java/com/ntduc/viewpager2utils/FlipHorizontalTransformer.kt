package com.ntduc.viewpager2utils

import android.view.View

open class FlipHorizontalTransformer : ABaseTransformer() {
  override fun onTransform(page: View, position: Float) {
    val rotation = 180f * position
    
    page.apply {
      alpha = if (rotation > 90f || rotation < -90f) 0f else 1f
      pivotX = width * 0.5f
      pivotY = height * 0.5f
      rotationY = rotation
    }
  }
  
  override fun onPostTransform(page: View, position: Float) {
    super.onPostTransform(page, position)
    page.visibility = if (position > -0.5f && position < 0.5f) View.VISIBLE else View.INVISIBLE
  }
}
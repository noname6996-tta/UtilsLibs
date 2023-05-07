package com.ntduc.viewpager2utils

import android.view.View
import kotlin.math.abs
import kotlin.math.max

open class ZoomOutPageTransformer : ABaseTransformer() {
  override fun onTransform(page: View, position: Float) {
    
    page.apply {
      val height = height.toFloat()
      val width = width.toFloat()
      
      if (position < -1) {
        page.alpha = 0f
      } else if (position <= 1) {
        val scaleFactor = max(MIN_SCALE, 1 - abs(position))
        val vertMargin = height * (1 - scaleFactor) / 2
        val horzMargin = width * (1 - scaleFactor) / 2
        
        translationX = if (position < 0) {
          horzMargin - vertMargin / 2
        } else {
          horzMargin + vertMargin / 2
        }
        
        scaleX = scaleFactor
        scaleY = scaleFactor
        
        alpha = (MIN_ALPHA + (((scaleFactor - MIN_SCALE) / (1 - MIN_SCALE)) * (1 - MIN_ALPHA)))
      } else {
        page.alpha = 0f
      }
    }
  }
  
  companion object {
    private const val MIN_SCALE = 0.85f
    private const val MIN_ALPHA = 0.5f
  }
}
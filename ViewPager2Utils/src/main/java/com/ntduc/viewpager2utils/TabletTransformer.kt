package com.ntduc.viewpager2utils

import android.graphics.Camera
import android.graphics.Matrix
import android.view.View
import kotlin.math.abs

open class TabletTransformer : ABaseTransformer() {
  override fun onTransform(page: View, position: Float) {
    val rotation = if (position < 0) ROT_MOD else -ROT_MOD
    val rotationY = rotation * abs(position)
    
    page.apply {
      translationX = getOffsetXForRotation(rotationY, width, height)
      pivotX = width * 0.5f
      pivotY = 0f
      this.rotationY = rotationY
    }
  }
  
  companion object {
    private val OFFSET_MATRIX = Matrix()
    private val OFFSET_CAMERA = Camera()
    private val OFFSET_TEMP_FLOAT = FloatArray(2)
    private const val ROT_MOD = 30f
    
    protected fun getOffsetXForRotation(degrees: Float, width: Int, height: Int): Float {
      OFFSET_MATRIX.reset()
      OFFSET_CAMERA.save()
      OFFSET_CAMERA.rotateY(Math.abs(degrees))
      OFFSET_CAMERA.getMatrix(OFFSET_MATRIX)
      OFFSET_CAMERA.restore()
      
      OFFSET_MATRIX.preTranslate(-width * 0.5f, -height * 0.5f)
      OFFSET_MATRIX.postTranslate(width * 0.5f, height * 0.5f)
      OFFSET_TEMP_FLOAT[0] = width.toFloat()
      OFFSET_TEMP_FLOAT[1] = height.toFloat()
      OFFSET_MATRIX.mapPoints(OFFSET_TEMP_FLOAT)
      return (width - OFFSET_TEMP_FLOAT[0]) * if (degrees > 0.0f) 1.0f else -1.0f
    }
  }
}
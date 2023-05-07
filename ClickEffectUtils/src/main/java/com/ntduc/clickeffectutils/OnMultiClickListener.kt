package com.ntduc.clickeffectutils

import android.os.SystemClock
import android.view.View

abstract class OnMultiClickListener : View.OnClickListener {
  private var prevClickTime: Long = 0
  override fun onClick(v: View) {
    _onClick(v)
  }
  
  @Synchronized
  private fun _onClick(v: View) {
    val current = SystemClock.elapsedRealtime()
    prevClickTime = if (current - prevClickTime > gap) {
      onSingleClick(v)
      SystemClock.elapsedRealtime()
    } else {
      onDoubleClick(v)
      0
    }
  }
  
  abstract fun onSingleClick(v: View?)
  abstract fun onDoubleClick(v: View?)//500ms
  
  /********
   *
   * @return The time in ms between two clicks.
   */
  val gap: Long
    get() = 500L //500ms
}
package com.ntduc.toastutils

import android.content.Context
import android.widget.Toast

class Toast(private val context: Context) : ToastCallback {
  companion object {
    private var currentToast: Toast? = null
  }
  
  override fun showShort(string: Int) {
    disposeCurrentToast()
    makeToastAndShow(string, Toast.LENGTH_SHORT)
  }
  
  override fun showShort(string: String) {
    disposeCurrentToast()
    makeToastAndShow(string, Toast.LENGTH_SHORT)
  }
  
  override fun showLong(string: Int) {
    disposeCurrentToast()
    makeToastAndShow(string, Toast.LENGTH_LONG)
  }
  
  override fun showLong(string: String) {
    disposeCurrentToast()
    makeToastAndShow(string, Toast.LENGTH_LONG)
  }
  
  private fun makeToastAndShow(string: Int, duration: Int) {
    currentToast = Toast.makeText(context, string, duration)
    currentToast?.show()
  }
  
  private fun makeToastAndShow(string: String, duration: Int) {
    currentToast = Toast.makeText(context, string, duration)
    currentToast?.show()
  }
  
  private fun disposeCurrentToast() {
    currentToast?.cancel()
    currentToast = null
  }
}
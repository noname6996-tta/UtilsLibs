package com.ntduc.toastutils

import android.content.Context
import androidx.fragment.app.Fragment

fun Context.shortToast(resId: Int) {
  Toast(this).showShort(resId)
}

fun Context.shortToast(text: String) {
  Toast(this).showShort(text)
}

fun Context.longToast(resId: Int) {
  Toast(this).showLong(resId)
}

fun Context.longToast(text: String) {
  Toast(this).showLong(text)
}

fun Fragment.shortToast(resId: Int) {
  Toast(requireContext()).showShort(resId)
}

fun Fragment.shortToast(text: String) {
  Toast(requireContext()).showShort(text)
}

fun Fragment.longToast(resId: Int) {
  Toast(requireContext()).showLong(resId)
}

fun Fragment.longToast(text: String) {
  Toast(requireContext()).showLong(text)
}
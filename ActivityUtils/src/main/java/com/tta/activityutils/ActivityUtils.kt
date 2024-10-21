package com.tta.activityutils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.graphics.Point
import android.graphics.Rect
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.util.DisplayMetrics
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.annotation.ColorRes
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

//Đặt màu StatusBar
fun Activity.setStatusBarColor(@ColorRes color: Int) {
  window.statusBarColor = ContextCompat.getColor(this, color)
}

// Chiều cao StatusBar (px)
val Activity.getStatusBarHeight: Int
  get() {
    val rect = Rect()
    window.decorView.getWindowVisibleDisplayFrame(rect)
    return rect.top
  }

//Ẩn StatusBar
fun Activity.hideStatusBar() {
  WindowInsetsControllerCompat(window, window.decorView).let { controller ->
    controller.hide(WindowInsetsCompat.Type.statusBars())
    controller.systemBarsBehavior =
      WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
  }
}

//Hiển thị StatusBar
fun Activity.showStatusBar() {
  WindowInsetsControllerCompat(
    window,
    window.decorView
  ).show(WindowInsetsCompat.Type.statusBars())
}

//Ẩn NavigationBar
fun Activity.hideNavigationBar() {
  WindowInsetsControllerCompat(window, window.decorView).let { controller ->
    controller.hide(WindowInsetsCompat.Type.navigationBars())
    controller.systemBarsBehavior =
      WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
  }
}

//Show NavigationBar
fun Activity.showNavigationBar() {
  WindowInsetsControllerCompat(
    window,
    window.decorView
  ).show(WindowInsetsCompat.Type.navigationBars())
}

// Chiều cao NavigationBar (px)
val Activity.getNavigationBarHeight: Int
  get() {
    val rectangle = Rect()
    val displayMetrics = DisplayMetrics()
    window.decorView.getWindowVisibleDisplayFrame(rectangle)
    windowManager.defaultDisplay.getRealMetrics(displayMetrics)
    return displayMetrics.heightPixels - (rectangle.top + rectangle.height())
  }

//Đặt màu NavigationBar
fun Activity.setNavigationBarColor(@ColorRes color: Int) {
  window.navigationBarColor = ContextCompat.getColor(this, color)
}

//Đặt màu dải phân cách NavigationBar
@RequiresApi(api = Build.VERSION_CODES.P)
fun Activity.setNavigationBarDividerColor(@ColorRes color: Int) {
  window.navigationBarDividerColor = ContextCompat.getColor(this, color)
}


//Bật chế độ full màn hình
fun Activity.enterFullScreenMode() {
  WindowCompat.setDecorFitsSystemWindows(window, false)
  WindowInsetsControllerCompat(window, window.decorView).let { controller ->
    controller.hide(WindowInsetsCompat.Type.systemBars())
    controller.systemBarsBehavior =
      WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
  }
}

//Thoát chế độ full màn hình
fun Activity.exitFullScreenMode() {
  WindowCompat.setDecorFitsSystemWindows(window, true)
  WindowInsetsControllerCompat(
    window,
    window.decorView
  ).show(WindowInsetsCompat.Type.systemBars())
}


//Tắt chụp màn hình
fun Activity.addSecureFlag() {
  window?.addFlags(WindowManager.LayoutParams.FLAG_SECURE)
}

//Mở chụp màn hình
fun Activity.clearSecureFlag() {
  window?.clearFlags(WindowManager.LayoutParams.FLAG_SECURE)
}


//Hiển thị bàn phím
fun Activity.showKeyboard(toFocus: View) {
  toFocus.requestFocus()
  val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
  imm.showSoftInput(toFocus, InputMethodManager.SHOW_IMPLICIT)
}

//Ẩn bàn phím
fun Activity.hideKeyboard() {
  if (currentFocus != null) {
    val inputMethodManager = getSystemService(
      Context
        .INPUT_METHOD_SERVICE
    ) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
  }
}


//Thay đổi độ sáng
var Activity.brightness: Float?
  get() = this.window?.attributes?.screenBrightness
  set(value) {
    val window = this.window
    val layoutParams = window.attributes
    layoutParams?.screenBrightness = value ?: 0F //0 is turned off, 1 is full brightness
    window?.attributes = layoutParams
  }

//Bật tính năng luôn cho màn hình sáng
fun Activity.keepScreenOn() {
  window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
}

//Tắt tính năng Luôn cho màn hình sáng
fun Activity.keepScreenOFF() {
  window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
}


//Khoá xoay màn hình
fun Activity.lockOrientation() {
  requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_NOSENSOR
}

//Mở khoá xoay màn hình
fun Activity.unlockScreenOrientation() {
  requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR
}

//Khoá hướng màn hình hiện tại
fun Activity.lockCurrentScreenOrientation() {
  requestedOrientation = when (resources.configuration.orientation) {
    Configuration.ORIENTATION_LANDSCAPE -> ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
    else -> ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT
  }
}


//Kích thước Activity (px)
val Activity.displaySizePixels: Point
  get() {
    var display = this.windowManager.defaultDisplay
    if (SDK_INT >= Build.VERSION_CODES.R) {
      display = this.display
    }
    return DisplayMetrics()
      .apply {
        display.getRealMetrics(this)
      }.let {
        Point(it.widthPixels, it.heightPixels)
      }
  }

// Restart Activity
inline fun Activity.restart(intentBuilder: Intent.() -> Unit = {}) {
  val i = Intent(this, this::class.java)
  val oldExtras = intent.extras
  if (oldExtras != null)
    i.putExtras(oldExtras)
  i.intentBuilder()
  startActivity(i)
  finish()
}
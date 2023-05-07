package com.tta.activityutils

import android.content.Intent
import android.graphics.Point
import android.os.Build
import android.view.View
import androidx.annotation.ColorRes
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment


//Đặt màu StatusBar
fun Fragment.setStatusBarColor(@ColorRes color: Int) {
    setStatusBarColor(color)
}

// Chiều cao StatusBar (px)
val Fragment.getStatusBarHeight: Int
  get() {
    return getStatusBarHeight
  }

//Ẩn StatusBar
fun Fragment.hideStatusBar() {
    hideStatusBar()
}

//Hiển thị StatusBar
fun Fragment.showStatusBar() {
    showStatusBar()
}

//Ẩn NavigationBar
fun Fragment.hideNavigationBar() {
    hideNavigationBar()
}

//Show NavigationBar
fun Fragment.showNavigationBar() {
    showNavigationBar()
}

// Chiều cao NavigationBar (px)
val Fragment.getNavigationBarHeight: Int
  get() {
    return getNavigationBarHeight
  }

//Đặt màu NavigationBar
fun Fragment.setNavigationBarColor(@ColorRes color: Int) {
    setNavigationBarColor(color)
}

//Đặt màu dải phân cách NavigationBar
@RequiresApi(api = Build.VERSION_CODES.P)
fun Fragment.setNavigationBarDividerColor(@ColorRes color: Int) {
    setNavigationBarDividerColor(color)
}


//Bật chế độ full màn hình
fun Fragment.enterFullScreenMode() {
    enterFullScreenMode()
}

//Thoát chế độ full màn hình
fun Fragment.exitFullScreenMode() {
    exitFullScreenMode()
}


//Tắt chụp màn hình
fun Fragment.addSecureFlag() {
    addSecureFlag()
}

//Mở chụp màn hình
fun Fragment.clearSecureFlag() {
    clearSecureFlag()
}


//Hiển thị bàn phím
fun Fragment.showKeyboard(toFocus: View) {
    showKeyboard(toFocus)
}

//Ẩn bàn phím
fun Fragment.hideKeyboard() {
    hideKeyboard()
}


//Thay đổi độ sáng
var Fragment.brightness: Float?
  get() = brightness
  set(value) {
    brightness = value
  }

//Bật tính năng luôn cho màn hình sáng
fun Fragment.keepScreenOn() {
    keepScreenOn()
}

//Tắt tính năng Luôn cho màn hình sáng
fun Fragment.keepScreenOFF() {
    keepScreenOFF()
}


//Khoá xoay màn hình
fun Fragment.lockOrientation() {
    lockOrientation()
}

//Mở khoá xoay màn hình
fun Fragment.unlockScreenOrientation() {
    unlockScreenOrientation()
}

//Khoá hướng màn hình hiện tại
fun Fragment.lockCurrentScreenOrientation() {
    lockCurrentScreenOrientation()
}


//Kích thước Activity (px)
val Fragment.displaySizePixels: Point
  get() {
    return displaySizePixels
  }

// Restart Activity
inline fun Fragment.restart(intentBuilder: Intent.() -> Unit = {}) {
    requireActivity().restart(intentBuilder)
}
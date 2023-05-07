package com.tta.activityutils

import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.Toolbar

//Setup toolbar
fun AppCompatActivity.setupToolbar(
  toolbar: Toolbar,
  displayHomeAsUpEnabled: Boolean = true,
  displayShowHomeEnabled: Boolean = true,
  displayShowTitleEnabled: Boolean = false,
  showUpArrowAsCloseIcon: Boolean = false,
  @DrawableRes closeIconDrawableRes: Int? = null
) {
  setSupportActionBar(toolbar)
  supportActionBar?.apply {
    setDisplayHomeAsUpEnabled(displayHomeAsUpEnabled)
    setDisplayShowHomeEnabled(displayShowHomeEnabled)
    setDisplayShowTitleEnabled(displayShowTitleEnabled)
    
    if (showUpArrowAsCloseIcon && closeIconDrawableRes != null) {
      setHomeAsUpIndicator(
        AppCompatResources.getDrawable(
          this@setupToolbar,
          closeIconDrawableRes
        )
      )
    }
  }
}

//Hiển thị BackButton Toolbar
fun AppCompatActivity.showBackButton() {
  this.supportActionBar?.setDisplayHomeAsUpEnabled(true)
}

//Ẩn BackButton Toolbar
fun AppCompatActivity.hideBackButton() {
  this.supportActionBar?.setDisplayHomeAsUpEnabled(false)
}

//Ẩn Toolbar
fun AppCompatActivity.hideToolbar() {
  this.supportActionBar?.hide()
}

//Hiển thị Toolbar
fun AppCompatActivity.showToolbar() {
  this.supportActionBar?.show()
}

//Đặt Title Toolbar
fun AppCompatActivity.setToolbarTitle(@StringRes title: Int) {
  supportActionBar?.setTitle(title)
}

//Đặt Title Toolbar
fun AppCompatActivity.setToolbarTitle(title: String) {
  supportActionBar?.title = title
}

fun AppCompatActivity.customToolbarDrawable(drawable: Drawable?) {
  supportActionBar?.setBackgroundDrawable(drawable)
}

fun AppCompatActivity.customBackButton(drawable: Drawable?) {
  supportActionBar?.setHomeAsUpIndicator(drawable)
}

fun AppCompatActivity.customBackButton(drawableResource: Int) {
  supportActionBar?.setHomeAsUpIndicator(drawableResource)
}
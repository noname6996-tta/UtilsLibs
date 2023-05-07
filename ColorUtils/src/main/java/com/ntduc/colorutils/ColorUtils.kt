package com.ntduc.colorutils

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.os.Build
import android.widget.*
import android.widget.TextView
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.annotation.FloatRange
import androidx.annotation.IdRes
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import java.util.*
import kotlin.math.roundToInt


inline val randomColor: Int
  get() {
    val rnd = Random()
    return Color.rgb(rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
  }

fun String.toColor(): Int {
  val toParse: String =
    if (startsWith("#") && length == 4) "#${this[1]}${this[1]}${this[2]}${this[2]}${this[3]}${this[3]}"
    else this
  return Color.parseColor(toParse)
}

@ColorInt
fun setColorAlpha(@ColorInt color: Int, @FloatRange(from = 0.0, to = 1.0) alpha: Float): Int {
  val alpha2 = (Color.alpha(color) * alpha).roundToInt()
  val red = Color.red(color)
  val green = Color.green(color)
  val blue = Color.blue(color)
  return Color.argb(alpha2, red, green, blue)
}

fun RadioButton.tint(@ColorInt color: Int) = tint(context.colorStateList(color))

fun CheckBox.tint(@ColorInt color: Int) = tint(context.colorStateList(color))

fun SeekBar.tint(@ColorInt color: Int) {
  val s1 = ColorStateList.valueOf(color)
  thumbTintList = s1
  progressTintList = s1
}

fun ProgressBar.tint(@ColorInt color: Int, skipIndeterminate: Boolean = false) {
  val sl = ColorStateList.valueOf(color)
  progressTintList = sl
  secondaryProgressTintList = sl
  if (!skipIndeterminate) indeterminateTintList = sl
}

@SuppressLint("RestrictedApi")
fun EditText.tint(@ColorInt color: Int) {
  val editTextColorStateList = context.textColorStateList(color)
  if (this is AppCompatEditText) {
    supportBackgroundTintList = editTextColorStateList
  } else backgroundTintList = editTextColorStateList
  tintCursor(color)
}

fun Toolbar.tint(@ColorInt color: Int, tintTitle: Boolean = true) {
  if (tintTitle) {
    setTitleTextColor(color)
    setSubtitleTextColor(color)
  }
  (0 until childCount).asSequence()
    .forEach { (getChildAt(it) as? ImageButton)?.setColorFilter(color) }
}

fun Toolbar?.changeNavigateUpColor(@IdRes color: Int) {
  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
    this?.navigationIcon?.colorFilter = BlendModeColorFilter(color, BlendMode.SRC_ATOP)
  } else {
    this?.navigationIcon?.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
  }
}

@ColorInt
fun getTitleTextColor(@ColorInt color: Int): Int {
  val darkness =
    1 - (0.299 * Color.red(color) + 0.587 * Color.green(color) + 0.114 * Color.blue(color)) / 255
  return if (darkness < 0.35) getDarkerColor(
    color, 0.25f
  ) else Color.WHITE
}

@ColorInt
fun getBodyTextColor(@ColorInt color: Int): Int {
  val title = getTitleTextColor(color)
  return setColorAlpha(title, 0.7f)
}

/*==================================================================================================*/
private fun Context.resolveColor(@AttrRes attr: Int, @ColorInt fallback: Int = 0): Int {
  val a = theme.obtainStyledAttributes(intArrayOf(attr))
  try {
    return a.getColor(0, fallback)
  } finally {
    a.recycle()
  }
}

@ColorInt
private fun getDarkerColor(
  @ColorInt color: Int, @FloatRange(
    from = 0.0, to = 1.0
  ) transparency: Float
): Int {
  val hsv = FloatArray(3)
  Color.colorToHSV(color, hsv)
  hsv[2] *= transparency
  return Color.HSVToColor(hsv)
}

@SuppressLint("SoonBlockedPrivateApi", "DiscouragedPrivateApi")
private fun EditText.tintCursor(@ColorInt color: Int) {
  try {
    val fCursorDrawableRes = TextView::class.java.getDeclaredField("mCursorDrawableRes")
    fCursorDrawableRes.isAccessible = true
    val mCursorDrawableRes = fCursorDrawableRes.getInt(this)
    
    val fEditor = TextView::class.java.getDeclaredField("mEditor")
    fEditor.isAccessible = true
    val editor = fEditor.get(this)
    val clazz = editor.javaClass
    val fCursorDrawable = clazz.getDeclaredField("mCursorDrawable")
    fCursorDrawable.isAccessible = true
    
    val drawables: Array<Drawable?> = Array(2) {
      val drawable = ContextCompat.getDrawable(context, mCursorDrawableRes)
      drawable?.setColorFilter(color, PorterDuff.Mode.SRC_IN)
      drawable
    }
    fCursorDrawable.set(editor, drawables)
  } catch (e: Exception) {
    e.printStackTrace()
  }
}

private fun Context.textColorStateList(@ColorInt color: Int): ColorStateList {
  val states = arrayOf(
    intArrayOf(-android.R.attr.state_enabled),
    intArrayOf(-android.R.attr.state_pressed, -android.R.attr.state_focused),
    intArrayOf()
  )
  val colors = intArrayOf(
    resolveColor(com.google.android.material.R.attr.colorControlNormal),
    resolveColor(com.google.android.material.R.attr.colorControlNormal),
    color
  )
  return ColorStateList(states, colors)
}

private fun RadioButton.tint(colors: ColorStateList) {
  buttonTintList = colors
}

private fun CheckBox.tint(colors: ColorStateList) {
  buttonTintList = colors
}

private fun Context.colorStateList(@ColorInt color: Int): ColorStateList {
  val disabledColor = color.adjustAlpha(0.3f)
  return ColorStateList(
    arrayOf(
      intArrayOf(android.R.attr.state_enabled, -android.R.attr.state_checked),
      intArrayOf(android.R.attr.state_enabled, android.R.attr.state_checked),
      intArrayOf(-android.R.attr.state_enabled, -android.R.attr.state_checked),
      intArrayOf(-android.R.attr.state_enabled, android.R.attr.state_checked)
    ), intArrayOf(color.adjustAlpha(0.8f), color, disabledColor, disabledColor)
  )
}

@ColorInt
private fun Int.adjustAlpha(factor: Float): Int {
  val alpha = (Color.alpha(this) * factor).roundToInt()
  return Color.argb(alpha, Color.red(this), Color.green(this), Color.blue(this))
}


private inline val Int.isColorDark: Boolean
  get() = isColorDark(0.5f)

private fun Int.isColorDark(minDarkness: Float): Boolean =
  ((0.299 * Color.red(this) + 0.587 * Color.green(this) + 0.114 * Color.blue(this)) / 255.0) < minDarkness
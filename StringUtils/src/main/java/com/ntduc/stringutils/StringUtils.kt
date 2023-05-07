package com.ntduc.stringutils

import android.graphics.Color
import android.graphics.Typeface
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.style.*
import android.util.Patterns
import android.view.View
import androidx.annotation.ColorInt
import java.security.SecureRandom
import java.util.*
import java.util.regex.Pattern

val String.isAlphanumeric get() = matches("^[a-zA-Z0-9]*$".toRegex())

val String.isAlphabetic get() = matches("^[a-zA-Z]*$".toRegex())

val String.isNumeric get() = matches("^[0-9]*$".toRegex())

val String.isIP: Boolean
  get() {
    val p = Pattern.compile(
      "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}"
    )
    val m = p.matcher(this)
    return m.find()
  }


fun String.isHttp() = this.matches(Regex("(http|https)://[^\\s]*"))

fun CharSequence.isEmail() = isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(this).matches()

fun String.convertToCamelCase(): String {
  var titleText = ""
  if (this.isNotEmpty()) {
    val words = this.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
    words.filterNot { it.isEmpty() }
      .map { it.substring(0, 1).uppercase() + it.substring(1).lowercase() }
      .forEach { titleText += "$it " }
  }
  return titleText.trim { it <= ' ' }
}

fun String.ellipsize(at: Int): String {
  if (this.length > at) {
    return this.substring(0, at) + "..."
  }
  return this
}

fun CharSequence.setBackgroundColor(color: Int): CharSequence {
  val ss = SpannableString(this)
  ss.setSpan(BackgroundColorSpan(color), 0, ss.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
  return ss
}

fun CharSequence.setForegroundColor(color: Int): CharSequence {
  val ss = SpannableString(this)
  ss.setSpan(ForegroundColorSpan(color), 0, ss.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
  return ss
}

fun String.highlight(
  key: String = this,
  underline: Boolean = false,
  strikeLine: Boolean = false,
  bold: Boolean = false,
  italic: Boolean = false,
  @ColorInt color: Int? = null
): SpannableString {
  val ss = SpannableString(this)
  
  var mKey = key
  var startIndex = ss.toString().indexOf(key)
  if (startIndex == -1) {
    mKey = this
    startIndex = 0
  }
  ss.setSpan(object : ClickableSpan() {
    override fun updateDrawState(ds: TextPaint) {
      if (color != null) {
        ds.color = color
        ds.bgColor = Color.TRANSPARENT
      }
    }
    
    override fun onClick(widget: View) {}
  }, startIndex, startIndex + mKey.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
  if (underline) {
    ss.setSpan(UnderlineSpan(), startIndex, startIndex + mKey.length, 0)
  }
  if (strikeLine) {
    ss.setSpan(StrikethroughSpan(), startIndex, startIndex + mKey.length, 0)
  }
  if (bold) {
    ss.setSpan(StyleSpan(Typeface.BOLD), startIndex, startIndex + mKey.length, 0)
  }
  if (italic) {
    ss.setSpan(StyleSpan(Typeface.ITALIC), startIndex, startIndex + mKey.length, 0)
  }
  return ss
}

fun String.remove(value: String, ignoreCase: Boolean = false): String =
  replace(value, "", ignoreCase)

private const val CHARACTERS: String =
  "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"
private val random: Random = SecureRandom()
fun randomString(length: Int): String {
  if (length <= 0) return ""
  
  return buildString {
    repeat((1..length).count()) {
      val selection = random.nextInt(CHARACTERS.length)
      val character = CHARACTERS[selection]
      append(character)
    }
  }
}

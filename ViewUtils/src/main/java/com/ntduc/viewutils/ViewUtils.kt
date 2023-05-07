package com.ntduc.viewutils

import android.annotation.TargetApi
import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.OvershootInterpolator
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.AttrRes
import androidx.annotation.DimenRes
import androidx.annotation.UiThread
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.core.view.*
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

fun View.visible() {
  this.visibility = View.VISIBLE
}

fun View.gone() {
  this.visibility = View.GONE
}

fun View.invisible() {
  this.visibility = View.INVISIBLE
}

val View.isVisibile: Boolean
  get() {
    return this.visibility == View.VISIBLE
  }

val View.isGone: Boolean
  get() {
    return this.visibility == View.GONE
  }

val View.isInvisible: Boolean
  get() {
    return this.visibility == View.INVISIBLE
  }

fun View.enable() {
  this.isEnabled = true
}

fun View.toggleEnabled() {
  this.isEnabled = !this.isEnabled
}

fun View.disable() {
  this.isEnabled = false
}

fun View.toggleSelected() {
  this.isSelected = !this.isSelected
}

fun TextView.setTextSizeRes(@DimenRes rid: Int) {
  setTextSize(TypedValue.COMPLEX_UNIT_PX, this.context.resources.getDimension(rid))
}

fun View.setPaddingLeft(value: Int) = setPadding(value, paddingTop, paddingRight, paddingBottom)
fun View.setPaddingRight(value: Int) = setPadding(paddingLeft, paddingTop, value, paddingBottom)
fun View.setPaddingTop(value: Int) =
  setPaddingRelative(paddingStart, value, paddingEnd, paddingBottom)

fun View.setPaddingBottom(value: Int) =
  setPaddingRelative(paddingStart, paddingTop, paddingEnd, value)

fun View.setPaddingStart(value: Int) =
  setPaddingRelative(value, paddingTop, paddingEnd, paddingBottom)

fun View.setPaddingEnd(value: Int) =
  setPaddingRelative(paddingStart, paddingTop, value, paddingBottom)

fun View.setPaddingHorizontal(value: Int) =
  setPaddingRelative(value, paddingTop, value, paddingBottom)

fun View.setPaddingVertical(value: Int) = setPaddingRelative(paddingStart, value, paddingEnd, value)


/**
 * Aligns to left of the parent in relative layout
 */
fun View.alignParentStart() {
  val params = layoutParams as RelativeLayout.LayoutParams?
  
  params?.apply {
    addRule(RelativeLayout.ALIGN_PARENT_START)
  }
  
}


/**
 * Aligns to right of the parent in relative layout
 */
fun View.alignParentEnd() {
  val params = layoutParams as RelativeLayout.LayoutParams?
  
  params?.apply {
    addRule(RelativeLayout.ALIGN_PARENT_END)
  }
  
}


/**
 * Aligns in the center of the parent in relative layout
 */
fun View.alignInCenter() {
  val params = layoutParams as RelativeLayout.LayoutParams?
  
  params?.apply {
    addRule(RelativeLayout.CENTER_HORIZONTAL)
  }
  
}


/**
 * Sets margins for views in Linear Layout
 */
fun View.linearMargins(left: Int, top: Int, right: Int, bottom: Int) {
  val params = layoutParams as LinearLayout.LayoutParams?
  
  params?.apply {
    setMargins(left, top, right, bottom)
  }
  
  this.layoutParams = params
  
}


/**
 * Sets margins for views in Linear Layout
 */
fun View.linearMargins(size: Int) {
  val params = layoutParams as LinearLayout.LayoutParams?
  
  params?.apply {
    setMargins(size)
  }
  this.layoutParams = params
  
}


/**
 * Sets right margin for views in Linear Layout
 */
fun View.endLinearMargin(size: Int) {
  val params = layoutParams as LinearLayout.LayoutParams?
  
  params?.apply {
    marginEnd = size
  }
  this.layoutParams = params
  
}


/**
 * Sets bottom margin for views in Linear Layout
 */
fun View.bottomLinearMargin(size: Int) {
  val params = layoutParams as LinearLayout.LayoutParams?
  
  params?.apply {
    setMargins(marginLeft, marginTop, marginRight, size)
  }
  this.layoutParams = params
  
}

/**
 * Sets top margin for views in Linear Layout
 */
fun View.topLinearMargin(size: Int) {
  val params = layoutParams as LinearLayout.LayoutParams?
  
  params?.apply {
    setMargins(marginLeft, size, marginRight, marginBottom)
  }
  this.layoutParams = params
  
}


/**
 * Sets top margin for views in Linear Layout
 */
fun View.startLinearMargin(size: Int) {
  val params = layoutParams as LinearLayout.LayoutParams?
  
  params?.apply {
    marginStart = size
  }
  this.layoutParams = params
  
}


/**
 * Sets margins for views in Relative Layout
 */
fun View.relativeMargins(left: Int, top: Int, right: Int, bottom: Int) {
  val params = layoutParams as RelativeLayout.LayoutParams?
  
  params?.apply {
    setMargins(left, top, right, bottom)
  }
  this.layoutParams = params
  
}


/**
 * Sets margins for views in Relative Layout
 */
fun View.relativeMargins(size: Int) {
  val params = layoutParams as RelativeLayout.LayoutParams?
  
  params?.apply {
    setMargins(size)
  }
  this.layoutParams = params
  
}


/**
 * Sets right margin for views in Relative Layout
 */
fun View.endRelativeMargin(size: Int) {
  val params = layoutParams as RelativeLayout.LayoutParams?
  
  params?.apply {
    marginEnd = size
  }
  this.layoutParams = params
  
}


/**
 * Sets bottom margin for views in Relative Layout
 */
fun View.bottomRelativeMargin(size: Int) {
  val params = layoutParams as RelativeLayout.LayoutParams?
  
  params?.apply {
    setMargins(marginLeft, marginTop, marginRight, size)
  }
  this.layoutParams = params
  
}

/**
 * Sets top margin for views in Relative Layout
 */
fun View.topRelativeMargin(size: Int) {
  val params = layoutParams as RelativeLayout.LayoutParams?
  
  params?.apply {
    setMargins(marginLeft, size, marginRight, marginBottom)
  }
  this.layoutParams = params
  
}


/**
 * Sets top margin for views in Relative Layout
 */
fun View.startRelativeMargin(size: Int) {
  val params = layoutParams as RelativeLayout.LayoutParams?
  
  params?.apply {
    marginStart = size
  }
  this.layoutParams = params
  
}


/**
 * Sets margins for views
 */
fun View.setMargins(size: Int) {
  val params = layoutParams as ViewGroup.MarginLayoutParams?
  
  params?.apply {
    setMargins(size)
  }
  this.layoutParams = params
  
}


/**
 * Sets right margin for views
 */
fun View.endMargin(size: Int) {
  val params = layoutParams as ViewGroup.MarginLayoutParams?
  
  params?.apply {
    marginEnd = size
  }
  this.layoutParams = params
  
}


/**
 * Sets bottom margin for views
 */
fun View.bottomMargin(size: Int) {
  val params = layoutParams as ViewGroup.MarginLayoutParams?
  
  params?.apply {
    setMargins(marginLeft, marginTop, marginRight, size)
  }
  this.layoutParams = params
  
}

/**
 * Sets top margin for views
 */
fun View.topMargin(size: Int) {
  val params = layoutParams as ViewGroup.MarginLayoutParams?
  
  params?.apply {
    setMargins(marginLeft, size, marginRight, marginBottom)
  }
  this.layoutParams = params
  
}


/**
 * Sets top margin for views
 */
fun View.startMargin(size: Int) {
  val params = layoutParams as ViewGroup.MarginLayoutParams?
  
  params?.apply {
    marginStart = size
  }
  this.layoutParams = params
  
}

fun View.setHeight(newValue: Int) {
  val params = layoutParams
  params?.let {
    params.height = newValue
    layoutParams = params
  }
}

fun View.setWidth(newValue: Int) {
  val params = layoutParams
  params?.let {
    params.width = newValue
    layoutParams = params
  }
}


fun View.resize(width: Int, height: Int) {
  val params = layoutParams
  params?.let {
    params.width = width
    params.height = height
    layoutParams = params
  }
}

@UiThread
fun View.fadeOut() {
  fadeOut(500)
}

@UiThread
fun View.fadeIn() {
  fadeIn(500)
}

@UiThread
fun View.fadeIn(duration: Long) {
  this.clearAnimation()
  val anim = AlphaAnimation(this.alpha, 1.0f)
  anim.duration = duration
  this.startAnimation(anim)
}

@UiThread
fun View.fadeOut(duration: Long) {
  this.clearAnimation()
  val anim = AlphaAnimation(this.alpha, 0.0f)
  anim.duration = duration
  this.startAnimation(anim)
}

fun View.getBitmap(): Bitmap {
  val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
  val canvas = Canvas(bitmap)
  draw(canvas)
  canvas.save()
  return bitmap
}

fun Drawable.toBitmap(): Bitmap {
  if (this is BitmapDrawable) {
    return bitmap
  }
  
  val bitmap = if (intrinsicWidth <= 0 || intrinsicHeight <= 0) {
    Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
  } else {
    Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Bitmap.Config.ARGB_8888)
  }
  
  Canvas(bitmap).apply {
    setBounds(0, 0, width, height)
    draw(this)
  }
  return bitmap
}

fun View.rotateAnimation(rotation: Float, duration: Long) {
  val interpolator = OvershootInterpolator()
  isActivated = if (!isActivated) {
    ViewCompat.animate(this).rotation(rotation).withLayer().setDuration(duration)
      .setInterpolator(interpolator).start()
    !isActivated
  } else {
    ViewCompat.animate(this).rotation(0f).withLayer().setDuration(duration)
      .setInterpolator(interpolator).start()
    !isActivated
  }
}

fun View.blink(duration: Long = 300L) {
  val anim = AlphaAnimation(0.3f, 1.0f)
  anim.duration = duration
  startAnimation(anim)
}

@TargetApi(value = Build.VERSION_CODES.M)
fun View.setRippleClickForeground() {
  if (canUseForeground) {
    foreground = ContextCompat.getDrawable(
      context,
      context.getResourceIdAttribute(android.R.attr.selectableItemBackground)
    )
    setClickable()
  }
}

fun View.setRippleClickBackground() {
  setBackgroundResource(context.getResourceIdAttribute(android.R.attr.selectableItemBackground))
  setClickable()
}

fun View.setRippleClickAnimation() =
  if (canUseForeground) setRippleClickForeground() else setRippleClickBackground()

private fun Context.getResourceIdAttribute(@AttrRes attribute: Int): Int {
  val typedValue = TypedValue()
  theme.resolveAttribute(attribute, typedValue, true)
  theme.resolveAttribute(attribute, typedValue, true)
  return typedValue.resourceId
}

private fun View.setClickable() {
  isClickable = true
  isFocusable = true
}

private val canUseForeground
  get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M

private const val DEFAULT_DRAWER_GRAVITY = GravityCompat.START

val DrawerLayout?.isOpen: Boolean get() = this?.isDrawerOpen(GravityCompat.START) ?: false
val DrawerLayout?.isEndOpen: Boolean get() = this?.isDrawerOpen(GravityCompat.END) ?: false
fun DrawerLayout?.open() = this?.openDrawer(GravityCompat.START)
fun DrawerLayout?.openEnd() = this?.openDrawer(GravityCompat.END)
fun DrawerLayout?.close() = this?.closeDrawer(GravityCompat.START)
fun DrawerLayout?.closeEnd() = this?.openDrawer(GravityCompat.END)
fun DrawerLayout?.toggle() = if (isOpen) close() else open()
fun DrawerLayout?.toggleEnd() = if (isEndOpen) closeEnd() else closeEnd()

val View.centerX
  get() = x + width / 2

val View.centerY
  get() = y + height / 2

inline var View.scaleXY
  get() = Math.max(scaleX, scaleY)
  set(value) {
    scaleX = value
    scaleY = value
  }

/**
 * Attaches a listener to the recyclerview to hide the fab when it is scrolling downwards
 * The fab will reappear when scrolling has stopped or if the user scrolls up
 */
fun FloatingActionButton.hideOnDownwardsScroll(recycler: RecyclerView) {
  recycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
    
    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
      if (newState == RecyclerView.SCROLL_STATE_IDLE && !isShown) show()
    }
    
    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
      if (dy > 0 && isShown) hide()
      else if (dy < 0 && isOrWillBeHidden) show()
    }
  })
}

fun <T : View> T.centerInParent(): T {
  return centerHorizontally().centerVertically()
}

fun <T : View> T.centerHorizontally(): T {
  (parent as? ConstraintLayout)?.let { constraintLayout ->
    constraintLayout.addConstraints {
      centerHorizontally(id, constraintLayout.id)
    }
  }
  return this
}

fun <T : View> T.centerVertically(): T {
  (parent as? ConstraintLayout)?.let { constraintLayout ->
    constraintLayout.addConstraints {
      centerVertically(id, constraintLayout.id)
    }
  }
  return this
}


fun <T : View> T.fillParent(padding: Int = 0): T {
  return fillVertically(padding).fillHorizontally(padding)
}

fun <T : View> T.fillVertically(padding: Int = 0): T {
  layoutParams.height = ConstraintSet.MATCH_CONSTRAINT // Needed to "match constraints"
  return top(padding).bottom(padding)
}

fun <T : View> T.fillHorizontally(padding: Int = 0): T {
  layoutParams.width = ConstraintSet.MATCH_CONSTRAINT // Needed to "match constraints"
  return left(padding).right(padding)
}


fun <T : View> T.top(margin: Int): T {
  return position(ConstraintLayout.LayoutParams.TOP, margin)
}

fun <T : View> T.left(margin: Int): T {
  return position(ConstraintLayout.LayoutParams.LEFT, margin)
}

fun <T : View> T.right(margin: Int): T {
  return position(ConstraintLayout.LayoutParams.RIGHT, margin)
}

fun <T : View> T.bottom(margin: Int): T {
  return position(ConstraintLayout.LayoutParams.BOTTOM, margin)
}

fun <T : View> T.position(position: Int, margin: Int): T {
  (parent as? ConstraintLayout)?.let { constraintLayout ->
    constraintLayout.addConstraints {
      connect(id, position, ConstraintLayout.LayoutParams.PARENT_ID, position, margin.dp)
    }
  }
  return this
}


// Top

fun <T : View> T.constrainTopToBottomOf(view: View, margin: Int = 0): T {
  return constrainTopToBottomOf(view.id, margin)
}

fun <T : View> T.constrainTopToBottomOf(viewId: Int, margin: Int = 0): T {
  (parent as? ConstraintLayout)?.addConstraints {
    connect(
      id,
      ConstraintLayout.LayoutParams.TOP,
      viewId,
      ConstraintLayout.LayoutParams.BOTTOM,
      margin.dp
    )
  }
  return this
}

fun <T : View> T.constrainTopToTopOf(view: View, margin: Int = 0): T {
  return constrainTopToTopOf(view.id, margin)
}

fun <T : View> T.constrainTopToTopOf(viewId: Int, margin: Int = 0): T {
  (parent as? ConstraintLayout)?.addConstraints {
    connect(
      id,
      ConstraintLayout.LayoutParams.TOP,
      viewId,
      ConstraintLayout.LayoutParams.TOP,
      margin.dp
    )
  }
  return this
}


// Left

fun <T : View> T.constrainLeftToRightOf(view: View, margin: Int = 0): T {
  return constrainLeftToRightOf(view.id, margin)
}

fun <T : View> T.constrainLeftToRightOf(viewId: Int, margin: Int = 0): T {
  (parent as? ConstraintLayout)?.addConstraints {
    connect(
      id,
      ConstraintLayout.LayoutParams.LEFT,
      viewId,
      ConstraintLayout.LayoutParams.RIGHT,
      margin.dp
    )
  }
  
  return this
}

fun <T : View> T.constrainLeftToLeftOf(view: View, margin: Int = 0): T {
  return constrainLeftToLeftOf(view.id, margin)
}

fun <T : View> T.constrainLeftToLeftOf(viewId: Int, margin: Int = 0): T {
  (parent as? ConstraintLayout)?.addConstraints {
    connect(
      id,
      ConstraintLayout.LayoutParams.LEFT,
      viewId,
      ConstraintLayout.LayoutParams.LEFT,
      margin.dp
    )
  }
  
  return this
}

// Right

fun <T : View> T.constrainRightToLeftOf(view: View, margin: Int = 0): T {
  return constrainRightToLeftOf(view.id, margin)
}

fun <T : View> T.constrainRightToLeftOf(viewId: Int, margin: Int = 0): T {
  (parent as? ConstraintLayout)?.addConstraints {
    connect(
      id,
      ConstraintLayout.LayoutParams.RIGHT,
      viewId,
      ConstraintLayout.LayoutParams.LEFT,
      margin.dp
    )
  }
  
  return this
}

fun <T : View> T.constrainRightToRightOf(view: View, margin: Int = 0): T {
  return constrainRightToRightOf(view.id, margin)
}

fun <T : View> T.constrainRightToRightOf(viewId: Int, margin: Int = 0): T {
  (parent as? ConstraintLayout)?.addConstraints {
    connect(
      id,
      ConstraintLayout.LayoutParams.RIGHT,
      viewId,
      ConstraintLayout.LayoutParams.RIGHT,
      margin.dp
    )
  }
  
  return this
}

// Bottom

fun <T : View> T.constrainBottomToTopOf(view: View, margin: Int = 0): T {
  return constrainBottomToTopOf(view.id, margin)
}

fun <T : View> T.constrainBottomToTopOf(viewId: Int, margin: Int = 0): T {
  (parent as? ConstraintLayout)?.addConstraints {
    connect(
      id,
      ConstraintLayout.LayoutParams.BOTTOM,
      viewId,
      ConstraintLayout.LayoutParams.TOP,
      margin.dp
    )
  }
  return this
}

fun <T : View> T.constrainBottomToBottomOf(view: View, margin: Int = 0): T {
  return constrainBottomToBottomOf(view.id, margin)
}

fun <T : View> T.constrainBottomToBottomOf(viewId: Int, margin: Int = 0): T {
  (parent as? ConstraintLayout)?.addConstraints {
    connect(
      id,
      ConstraintLayout.LayoutParams.BOTTOM,
      viewId,
      ConstraintLayout.LayoutParams.BOTTOM,
      margin.dp
    )
  }
  return this
}


// Center Y

// This is made possible by creating a "GONE" guideline and center on the guideline instead :)
fun <T : View> T.constrainCenterYToBottomOf(view: View): T {
  (parent as? ConstraintLayout)?.let { constraintLayout ->
    constraintLayout.addConstraints {
      val guideline = View(context)
      guideline.id = View.generateViewId()
      constraintLayout.addView(guideline)
      guideline.visibility = View.GONE
      guideline.constrainBottomToBottomOf(view)
      centerVertically(id, guideline.id)
    }
  }
  return this
}

fun <T : View> T.constrainCenterYToTopOf(view: View): T {
  (parent as? ConstraintLayout)?.let { constraintLayout ->
    constraintLayout.addConstraints {
      val guideline = View(context)
      guideline.id = View.generateViewId()
      constraintLayout.addView(guideline)
      guideline.visibility = View.GONE
      guideline.constrainTopToTopOf(view)
      centerVertically(id, guideline.id)
    }
  }
  return this
}

fun <T : View> T.constrainCenterYToCenterYOf(view: View): T {
  (parent as? ConstraintLayout)?.addConstraints {
    centerVertically(id, view.id)
  }
  return this
}


// Center X

fun <T : View> T.constrainCenterXToLeftOf(view: View): T {
  (parent as? ConstraintLayout)?.let { constraintLayout ->
    constraintLayout.addConstraints {
      val guideline = View(context)
      guideline.id = View.generateViewId()
      constraintLayout.addView(guideline)
      guideline.visibility = View.GONE
      guideline.constrainLeftToLeftOf(view)
      centerHorizontally(id, guideline.id)
    }
  }
  return this
}

fun <T : View> T.constrainCenterXToRightOf(view: View): T {
  (parent as? ConstraintLayout)?.let { constraintLayout ->
    constraintLayout.addConstraints {
      val guideline = View(context)
      guideline.id = View.generateViewId()
      constraintLayout.addView(guideline)
      guideline.visibility = View.GONE
      guideline.constrainRightToRightOf(view)
      centerHorizontally(id, guideline.id)
    }
  }
  return this
}


fun <T : View> T.constrainCenterXToCenterXOf(view: View): T {
  (parent as? ConstraintLayout)?.addConstraints {
    centerHorizontally(id, view.id)
  }
  return this
}


// Follow Edges

fun <T : View> T.followEdgesOf(view: View, margin: Int = 0): T {
  constrainTopToTopOf(view)
  constrainBottomToBottomOf(view)
  constrainLeftToLeftOf(view)
  constrainRightToRightOf(view)
  return this
}

// Percent Size

fun <T : View> T.percentWidth(value: Float): T {
  layoutParams.width = ConstraintSet.MATCH_CONSTRAINT
  (parent as? ConstraintLayout)?.let { constraintLayout ->
    constraintLayout.addConstraints {
      constrainPercentWidth(id, value)
    }
  }
  return this
}

fun <T : View> T.percentHeight(value: Float): T {
  layoutParams.height = ConstraintSet.MATCH_CONSTRAINT
  (parent as? ConstraintLayout)?.let { constraintLayout ->
    constraintLayout.addConstraints {
      constrainPercentHeight(id, value)
    }
  }
  return this
}

private fun ConstraintLayout.addConstraints(block: ConstraintSet.() -> Unit) {
  val cs = ConstraintSet()
  cs.clone(this)
  block(cs)
  cs.applyTo(this)
}

private var Int.dp: Int
  get() {
    val metrics = Resources.getSystem().displayMetrics
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), metrics)
      .toInt()
  }
  set(_) {}
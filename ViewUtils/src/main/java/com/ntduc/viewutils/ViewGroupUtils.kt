package com.ntduc.viewutils

import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintSet

// Constants
const val matchConstraint = ConstraintSet.MATCH_CONSTRAINT
const val matchParent: Int = ViewGroup.LayoutParams.MATCH_PARENT
const val wrapContent: Int = ViewGroup.LayoutParams.WRAP_CONTENT

fun ViewGroup.getChildren(): List<View> {
  return (0 until childCount).map {
    getChildAt(it)
  }
}

fun ViewGroup.getAllChildren(): List<View> {
  val result = mutableListOf<View>()
  
  val children = getChildren()
  children.forEach {
    if (it is ViewGroup && it.childCount > 0) {
      result.add(it)
      result.addAll(it.getAllChildren())
    } else {
      result.add(it)
    }
  }
  
  return result
}

fun ViewGroup.disableChildren() = enableDisableChildren(false)
fun ViewGroup.enableChildren() = enableDisableChildren(true)

/**
 * Hides all child views
 */
fun ViewGroup.hideAll() {
  eachChild {
    it.gone()
  }
}

/**
 * SHows all child views
 */
fun ViewGroup.showAll() {
  eachChild {
    it.visible()
  }
}

private fun ViewGroup.enableDisableChildren(enable: Boolean): ViewGroup = apply {
  (0 until childCount).forEach {
    when (val view = getChildAt(it)) {
      is ViewGroup -> view.enableDisableChildren(enable)
      else -> if (enable) view.enable() else view.disable()
    }
  }
}

/**
 * Applys given func to all child views
 */
private fun ViewGroup.eachChild(func: (view: View) -> Unit) {
  for (i in 0 until childCount) {
    func(getChildAt(i))
  }
}
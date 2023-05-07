package com.ntduc.fragmentutils

import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

val Fragment.appCompatActivity get() = this.requireContext() as AppCompatActivity

fun Fragment.addFragment(
  fragment: Fragment,
  tag: String,
  @IdRes layoutId: Int,
  isHide: Boolean = false,
  isAddStack: Boolean = false
) {
  appCompatActivity.addFragment(fragment, tag, layoutId, isHide, isAddStack)
}

fun Fragment.addFragment(
  fragment: Fragment,
  tag: String,
  @IdRes layoutId: Int,
  title: String? = null,
  isHide: Boolean = false,
  isAddStack: Boolean = false
) {
  appCompatActivity.addFragment(fragment, tag, layoutId, title, isHide, isAddStack)
}

fun Fragment.addFragment(
  fragment: Fragment,
  tag: String,
  @IdRes layoutId: Int,
  @StringRes title: Int? = null,
  isHide: Boolean = false,
  isAddStack: Boolean = false
) {
  appCompatActivity.addFragment(fragment, tag, layoutId, title, isHide, isAddStack)
}

fun Fragment.replaceFragment(
  fragment: Fragment, tag: String, @IdRes layoutId: Int, isAddStack: Boolean = false
) {
  appCompatActivity.replaceFragment(fragment, tag, layoutId, isAddStack)
}

fun Fragment.replaceFragment(
  fragment: Fragment,
  tag: String,
  @IdRes layoutId: Int,
  title: String? = null,
  isAddStack: Boolean = false
) {
  appCompatActivity.replaceFragment(fragment, tag, layoutId, title, isAddStack)
}

fun Fragment.replaceFragment(
  fragment: Fragment,
  tag: String,
  @IdRes layoutId: Int,
  @StringRes title: Int? = null,
  isAddStack: Boolean = false
) {
  appCompatActivity.replaceFragment(fragment, tag, layoutId, title, isAddStack)
}

fun Fragment.removeFragment(fragment: Fragment) {
  appCompatActivity.removeFragment(fragment)
}

fun Fragment.hideFragment(fragment: Fragment) {
  appCompatActivity.hideFragment(fragment)
}

fun Fragment.showFragment(fragment: Fragment) {
  appCompatActivity.showFragment(fragment)
}

fun Fragment.popFragment() {
  appCompatActivity.popFragment()
}

fun Fragment.popFragment(name: String, flags: Int = 0) {
  appCompatActivity.popFragment(name, flags)
}

fun Fragment.popFragment(id: Int, flags: Int = 0) {
  appCompatActivity.popFragment(id, flags)
}

val Fragment.backStackCount get() = appCompatActivity.backStackCount

fun Fragment.clearAllFragments() {
  appCompatActivity.clearAllFragments()
}

fun Fragment.currentFragment(@IdRes container: Int): Fragment? {
  return appCompatActivity.currentFragment(container)
}

fun Fragment.printBackStack() {
  appCompatActivity.printBackStack()
}

fun Fragment.onBackPressed() {
  requireActivity().onBackPressed()
}

inline fun Fragment.observeLifecycleOwnerThroughLifecycleCreation(
  crossinline onCreateAction: (LifecycleOwner) -> Unit = {},
  crossinline onResumeAction: (LifecycleOwner) -> Unit = {},
  crossinline onPauseAction: (LifecycleOwner) -> Unit = {},
  crossinline onStartAction: (LifecycleOwner) -> Unit = {},
  crossinline onStopAction: (LifecycleOwner) -> Unit = {},
  crossinline onDestroyAction: (LifecycleOwner) -> Unit = {}
) {
  lifecycle.addObserver(object : DefaultLifecycleObserver {
    override fun onCreate(owner: LifecycleOwner) {
      viewLifecycleOwner.lifecycle.addObserver(object : DefaultLifecycleObserver {
        override fun onCreate(owner: LifecycleOwner) {
          onCreateAction(owner)
        }
        
        override fun onResume(owner: LifecycleOwner) {
          onResumeAction(owner)
        }
        
        override fun onPause(owner: LifecycleOwner) {
          onPauseAction(owner)
        }
        
        override fun onStart(owner: LifecycleOwner) {
          onStartAction(owner)
        }
        
        override fun onStop(owner: LifecycleOwner) {
          onStopAction(owner)
        }
        
        override fun onDestroy(owner: LifecycleOwner) {
          onDestroyAction(owner)
        }
      })
    }
  })
}

fun Fragment.hide() {
  appCompatActivity.supportFragmentManager.beginTransaction().hide(this).commit()
}

fun Fragment.show() {
  appCompatActivity.supportFragmentManager.beginTransaction().show(this).commit()
}

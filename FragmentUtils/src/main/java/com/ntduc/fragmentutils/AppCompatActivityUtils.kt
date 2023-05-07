package com.ntduc.fragmentutils

import android.util.Log
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

fun AppCompatActivity.addFragment(
  fragment: Fragment,
  tag: String,
  @IdRes layoutId: Int,
  isHide: Boolean = false,
  isAddStack: Boolean = false
) {
  val fragmentTransaction = supportFragmentManager.beginTransaction().add(layoutId, fragment, tag)
  if (isHide) fragmentTransaction.hide(fragment)
  if (isAddStack) fragmentTransaction.addToBackStack(tag)
  fragmentTransaction.commit()
}

fun AppCompatActivity.addFragment(
  fragment: Fragment,
  tag: String,
  @IdRes layoutId: Int,
  title: String? = null,
  isHide: Boolean = false,
  isAddStack: Boolean = false
) {
  addFragment(fragment, tag, layoutId, isHide, isAddStack)
  
  if (title != null) {
    if (this.supportActionBar != null) {
      this.supportActionBar?.title = title
    }
  }
}

fun AppCompatActivity.addFragment(
  fragment: Fragment,
  tag: String,
  @IdRes layoutId: Int,
  @StringRes title: Int? = null,
  isHide: Boolean = false,
  isAddStack: Boolean = false
) {
  addFragment(fragment, tag, layoutId, isHide, isAddStack)
  
  if (title != null) {
    if (this.supportActionBar != null) {
      this.supportActionBar?.setTitle(title)
    }
  }
}

fun AppCompatActivity.replaceFragment(
  fragment: Fragment, tag: String, @IdRes layoutId: Int, isAddStack: Boolean = false
) {
  val fragmentTransaction =
    supportFragmentManager.beginTransaction().replace(layoutId, fragment, tag)
  if (isAddStack) fragmentTransaction.addToBackStack(tag)
  fragmentTransaction.commit()
}

fun AppCompatActivity.replaceFragment(
  fragment: Fragment,
  tag: String,
  @IdRes layoutId: Int,
  title: String? = null,
  isAddStack: Boolean = false
) {
  replaceFragment(fragment, tag, layoutId, isAddStack)
  
  if (title != null) {
    if (this.supportActionBar != null) {
      this.supportActionBar?.title = title
    }
  }
}

fun AppCompatActivity.replaceFragment(
  fragment: Fragment,
  tag: String,
  @IdRes layoutId: Int,
  @StringRes title: Int? = null,
  isAddStack: Boolean = false
) {
  replaceFragment(fragment, tag, layoutId, isAddStack)
  
  if (title != null) {
    if (this.supportActionBar != null) {
      this.supportActionBar?.setTitle(title)
    }
  }
}

fun AppCompatActivity.removeFragment(fragment: Fragment) {
  supportFragmentManager.beginTransaction().remove(fragment).commitNowAllowingStateLoss()
}

fun AppCompatActivity.hideFragment(fragment: Fragment) {
  supportFragmentManager.beginTransaction().hide(fragment).commit()
}

fun AppCompatActivity.showFragment(fragment: Fragment) {
  supportFragmentManager.beginTransaction().show(fragment).commit()
}

fun AppCompatActivity.popFragment() {
  val fm = supportFragmentManager
  if (fm.backStackEntryCount == 0) return
  fm.popBackStack()
}

fun AppCompatActivity.popFragment(name: String, flags: Int = 0) {
  val fm = supportFragmentManager
  if (fm.backStackEntryCount == 0) return
  fm.popBackStack(name, flags)
}

fun AppCompatActivity.popFragment(id: Int, flags: Int = 0) {
  val fm = supportFragmentManager
  if (fm.backStackEntryCount == 0) return
  fm.popBackStack(id, flags)
}

val AppCompatActivity.backStackCount get() = supportFragmentManager.backStackEntryCount

fun AppCompatActivity.clearAllFragments() {
  supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
}

fun AppCompatActivity.currentFragment(@IdRes container: Int): Fragment? {
  return supportFragmentManager.findFragmentById(container)
}

fun AppCompatActivity.printBackStack() {
  Log.d("Fragment", "Current BackStack:  " + supportFragmentManager.backStackEntryCount)
  for (entry in 0 until supportFragmentManager.backStackEntryCount) {
    val stackEntry = supportFragmentManager.getBackStackEntryAt(entry)
    Log.d("Fragment", "[" + stackEntry.id + "] " + stackEntry.name)
  }
}
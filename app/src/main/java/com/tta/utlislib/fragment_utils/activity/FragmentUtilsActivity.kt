package com.ntduc.utils.fragment_utils.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ntduc.clickeffectutils.setOnClickShrinkEffectListener
import com.ntduc.contextutils.inflater
import com.ntduc.datetimeutils.currentMillis
import com.ntduc.fragmentutils.*
import com.ntduc.toastutils.shortToast
import com.tta.utlislib.databinding.ActivityFragmentUtilsBinding
import com.ntduc.utils.fragment_utils.fragment.DefaultFragment


class FragmentUtilsActivity : AppCompatActivity() {
  private lateinit var binding: ActivityFragmentUtilsBinding
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityFragmentUtilsBinding.inflate(inflater)
    setContentView(binding.root)
    
    init()
  }
  
  private fun init() {
    initView()
    initEvent()
  }
  
  private fun initView() {
    binding.note.text = backStackCount.toString()
  }
  
  private fun initEvent() {
    supportFragmentManager.addOnBackStackChangedListener {
      binding.note.text = backStackCount.toString()
    }
    
    binding.addFragment.setOnClickShrinkEffectListener {
      val currentTime = currentMillis
      val fragment = DefaultFragment().newInstance(currentTime.toString())
      addFragment(
        fragment = fragment,
        tag = currentTime.toString(),
        layoutId = binding.container.id,
        isAddStack = true
      )
    }
    
    binding.replaceFragment.setOnClickShrinkEffectListener {
      val currentTime = currentMillis
      val fragment = DefaultFragment().newInstance(currentTime.toString())
      replaceFragment(
        fragment = fragment,
        tag = currentTime.toString(),
        layoutId = binding.container.id,
        isAddStack = true
      )
    }
    
    binding.hideFragment.setOnClickShrinkEffectListener {
      val currentFragment = currentFragment(binding.container.id)
      if (currentFragment != null) {
        hideFragment(currentFragment)
      } else {
        shortToast("currentFragment null")
      }
    }
    
    binding.showFragment.setOnClickShrinkEffectListener {
      val currentFragment = currentFragment(binding.container.id)
      if (currentFragment != null) {
        showFragment(currentFragment)
      } else {
        shortToast("currentFragment null")
      }
    }
    
    binding.removeFragment.setOnClickShrinkEffectListener {
      val currentFragment = currentFragment(binding.container.id)
      if (currentFragment != null) {
        removeFragment(currentFragment)
      } else {
        shortToast("currentFragment null")
      }
    }
    
    binding.popFragment.setOnClickShrinkEffectListener {
      popFragment()
    }
    
    binding.popFragmentName.setOnClickShrinkEffectListener {
      val nameFragment = binding.nameFragment.text.toString()
      if (nameFragment.isNotEmpty()) {
        popFragment(nameFragment, 0)
      } else {
        shortToast("nameFragment empty")
      }
    }
    
    binding.clearAllFragments.setOnClickShrinkEffectListener {
      clearAllFragments()
    }
    
    binding.currentFragment.setOnClickShrinkEffectListener {
      val currentFragment = currentFragment(binding.container.id)
      shortToast("currentFragment: ${currentFragment?.tag}")
    }
    
    binding.printBackStack.setOnClickShrinkEffectListener {
      printBackStack()
    }
  }
}
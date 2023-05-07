package com.ntduc.utils.view_utils.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ntduc.clickeffectutils.setOnClickShrinkEffectListener
import com.ntduc.contextutils.inflater
import com.ntduc.viewutils.blink
import com.ntduc.viewutils.rotateAnimation
import com.ntduc.viewutils.setRippleClickAnimation
import com.tta.utlislib.databinding.ActivityViewUtilsBinding

class ViewUtilsActivity : AppCompatActivity() {
  private lateinit var binding: ActivityViewUtilsBinding
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityViewUtilsBinding.inflate(inflater)
    setContentView(binding.root)
    
    init()
  }
  
  private fun init() {
    initView()
    initEvent()
  }
  
  private fun initView() {
    binding.image.setRippleClickAnimation()
  }
  
  private fun initEvent() {
    binding.btnRotateAnimation.setOnClickShrinkEffectListener {
      binding.image.rotateAnimation(180f, 1000)
    }
    
    binding.btnBlink.setOnClickShrinkEffectListener {
      binding.image.blink(1000)
    }
  }
}
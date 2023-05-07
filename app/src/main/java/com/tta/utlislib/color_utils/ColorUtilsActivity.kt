package com.tta.utlislib.color_utils

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ntduc.clickeffectutils.setOnClickShrinkEffectListener
import com.ntduc.colorutils.*
import com.tta.utlislib.databinding.ActivityColorUtilsBinding


class ColorUtilsActivity : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    val binding = ActivityColorUtilsBinding.inflate(layoutInflater)
    setContentView(binding.root)
    
    binding.btnRandomColor.setOnClickShrinkEffectListener {
      binding.btnRandomColor.setBackgroundColor(randomColor)
    }
    
    binding.btnSetColorAlpha.setOnClickShrinkEffectListener {
      binding.btnSetColorAlpha.setBackgroundColor(setColorAlpha(Color.RED, 0.5f))
    }
    
    binding.edtTint.tint(Color.RED)
    binding.txtTitle.setTextColor(getTitleTextColor(Color.YELLOW))
    binding.txtBody.setTextColor(getBodyTextColor(Color.YELLOW))
  }
}
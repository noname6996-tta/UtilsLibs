package com.ntduc.utils.string_utils

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ntduc.clickeffectutils.setOnClickShrinkEffectListener
import com.ntduc.colorutils.randomColor
import com.ntduc.stringutils.*
import com.ntduc.toastutils.shortToast
import com.tta.utlislib.databinding.ActivityStringUtilsBinding

class StringUtilsActivity : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    val binding = ActivityStringUtilsBinding.inflate(layoutInflater)
    setContentView(binding.root)
    
    binding.btnIsNumeric.setOnClickShrinkEffectListener {
      shortToast("${binding.edt.text.toString().isNumeric}")
    }
    
    binding.btnConvertToCamelCase.setOnClickShrinkEffectListener {
      shortToast(binding.edt.text.toString().convertToCamelCase())
    }
    
    binding.btnEllipsize.setOnClickShrinkEffectListener {
      shortToast(binding.edt.text.toString().ellipsize(5))
    }
    
    binding.btnSetBackgroundColor.setOnClickShrinkEffectListener {
      binding.txt.text = binding.edt.text.setBackgroundColor(randomColor)
    }
    
    binding.btnSetForegroundColor.setOnClickShrinkEffectListener {
      binding.txt.text = binding.edt.text.setForegroundColor(randomColor)
    }
    
    binding.btnHighlight.setOnClickShrinkEffectListener {
      try {
        binding.txt.text = binding.edt.text.toString().highlight(
          key = "hello",
          bold = true,
          italic = true,
          color = randomColor,
          strikeLine = true,
          underline = true
        )
      } catch (e: Exception) {
      }
    }
    
    binding.btnRandomString.setOnClickShrinkEffectListener {
      binding.txt.text = randomString(4)
    }
  }
}
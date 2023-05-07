package com.ntduc.utils.sp_utils

import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ntduc.clickeffectutils.setOnClickShrinkEffectListener
import com.ntduc.contextutils.inflater
import com.ntduc.sharedpreferenceutils.*
import com.ntduc.toastutils.shortToast
import com.tta.utlislib.databinding.ActivitySpBinding

class SpActivity : AppCompatActivity(), (SharedPreferences, String?) -> Unit {
  companion object {
    private const val KEY_STRING = "KEY_STRING"
  }
  
  private lateinit var binding: ActivitySpBinding
  private lateinit var sp: SharedPreferences
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivitySpBinding.inflate(inflater)
    setContentView(binding.root)
    
    sp = getSharedPreferences("ntduc", MODE_PRIVATE)
    sp.registerSharedPreferenceChangeListener(this, this)
    
    init()
  }
  
  private fun init() {
    initView()
    initEvent()
  }
  
  private fun initView() {
    binding.txt.text = sp.getString(KEY_STRING, null)
  }
  
  private fun initEvent() {
    binding.btnPutString.setOnClickShrinkEffectListener {
      sp.putString(KEY_STRING, binding.edt.text.toString())
      binding.txt.text = sp.getString(KEY_STRING, null)
    }
    binding.btnCommitString.setOnClickShrinkEffectListener {
      sp.commitString(KEY_STRING, binding.edt.text.toString())
      binding.txt.text = sp.getString(KEY_STRING, null)
    }
    binding.btnClear.setOnClickShrinkEffectListener {
      sp.clear()
      binding.txt.text = sp.getString(KEY_STRING, null)
    }
    binding.btnCommitClear.setOnClickShrinkEffectListener {
      sp.commitClear()
      binding.txt.text = sp.getString(KEY_STRING, null)
    }
    binding.btnRemove.setOnClickShrinkEffectListener {
      sp.remove(KEY_STRING)
      binding.txt.text = sp.getString(KEY_STRING, null)
    }
    binding.btnCommitRemove.setOnClickShrinkEffectListener {
      sp.commitRemove(KEY_STRING)
      binding.txt.text = sp.getString(KEY_STRING, null)
    }
  }
  
  override fun invoke(sharedPreferences: SharedPreferences, key: String?) {
    shortToast(key ?: "null")
  }
}
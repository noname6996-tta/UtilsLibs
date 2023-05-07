package com.ntduc.utils.app_utils.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.tabs.TabLayoutMediator
import com.ntduc.contextutils.inflater
import com.ntduc.utils.app_utils.adapter.FragmentAdapter
import com.tta.utlislib.databinding.ActivityAppBinding

class AppActivity : AppCompatActivity() {
  private lateinit var binding: ActivityAppBinding
  private lateinit var adapter: FragmentAdapter
  private lateinit var viewModel: AppViewModel
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityAppBinding.inflate(inflater)
    setContentView(binding.root)
    
    init()
  }
  
  override fun onStart() {
    super.onStart()
    viewModel.loadAllApp(this)
    viewModel.loadAllApk(this)
  }
  
  private fun init() {
    initView()
  }
  
  private fun initView() {
    viewModel = ViewModelProvider(this)[AppViewModel::class.java]
    
    adapter = FragmentAdapter(this)
    binding.viewPager.adapter = adapter
    TabLayoutMediator(
      binding.tabLayout,
      binding.viewPager
    ) { tab, position ->
      when (position) {
        0 -> tab.text = "Installed App"
        1 -> tab.text = "Apk Package"
      }
    }.attach()
  }
  
  @Deprecated("Deprecated in Java")
  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    
    Log.d("ntduc_debug", "onActivityResult: $requestCode, $resultCode")
  }
}
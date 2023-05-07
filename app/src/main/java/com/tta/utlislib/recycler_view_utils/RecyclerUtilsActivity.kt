package com.tta.utlislib.Recycler_view_utils

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ntduc.clickeffectutils.setOnClickShrinkEffectListener
import com.ntduc.contextutils.inflater
import com.tta.utlislib.databinding.ActivityRecyclerUtilsBinding
import com.tta.utlislib.Recycler_view_utils.sticky.RecyclerViewStickyActivity

class RecyclerUtilsActivity : AppCompatActivity() {
  private lateinit var binding: ActivityRecyclerUtilsBinding
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityRecyclerUtilsBinding.inflate(inflater)
    setContentView(binding.root)
    
    binding.btnRecyclerViewSticky.setOnClickShrinkEffectListener {
      startActivity(Intent(this, RecyclerViewStickyActivity::class.java))
    }
  }
}
package com.ntduc.utils.view_pager_2_utils.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ntduc.contextutils.inflater
import com.tta.utlislib.databinding.ActivityViewPager2UtilsBinding
import com.ntduc.utils.view_pager_2_utils.adapter.FragmentAdapter
import com.ntduc.utils.view_pager_2_utils.adapter.MenuAdapter
import com.ntduc.utils.view_pager_2_utils.model.Transformer
import com.ntduc.viewpager2utils.*

class ViewPager2UtilsActivity : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    val binding = ActivityViewPager2UtilsBinding.inflate(inflater)
    setContentView(binding.root)
    
    val items = getTransformers()
    val adapterMenu = MenuAdapter(this, items)
    binding.textField.setAdapter(adapterMenu)
    binding.textField.setOnItemClickListener { parent, view, position, id ->
      binding.pager.setPageTransformer(items[position].transformer)
    }
    val adapterPager = FragmentAdapter(this)
    binding.pager.adapter = adapterPager
  }
  
  private fun getTransformers(): ArrayList<Transformer> {
    val list = ArrayList<Transformer>()
    list.add(Transformer("AccordionTransformer", AccordionTransformer()))
    list.add(Transformer("AlphaTransformer", AlphaTransformer()))
    list.add(Transformer("BackgroundToForegroundTransformer", BackgroundToForegroundTransformer()))
    list.add(Transformer("CubeInTransformer", CubeInTransformer()))
    list.add(Transformer("CubeOutTransformer", CubeOutTransformer()))
    list.add(Transformer("DepthPageTransformer", DepthPageTransformer()))
    list.add(Transformer("FlipHorizontalTransformer", FlipHorizontalTransformer()))
    list.add(Transformer("FlipVerticalTransformer", FlipVerticalTransformer()))
    list.add(Transformer("ForegroundToBackgroundTransformer", ForegroundToBackgroundTransformer()))
    list.add(Transformer("RotateDownTransformer", RotateDownTransformer()))
    list.add(Transformer("RotateUpTransformer", RotateUpTransformer()))
    list.add(Transformer("ScaleInOutTransformer", ScaleInOutTransformer()))
    list.add(Transformer("TabletTransformer", TabletTransformer()))
    list.add(Transformer("UltraDepthScaleTransformer", UltraDepthScaleTransformer()))
    list.add(Transformer("ZoomInTransformer", ZoomInTransformer()))
    list.add(Transformer("ZoomOutPageTransformer", ZoomOutPageTransformer()))
    list.add(Transformer("ZoomOutTransformer", ZoomOutTransformer()))
    
    return list
  }
}
package com.ntduc.utils.file_utils.get_all_video.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.ntduc.fileutils.open
import com.ntduc.recyclerviewutils.sticky.StickyHeadersGridLayoutManager
import com.ntduc.toastutils.shortToast
import com.tta.utlislib.databinding.ActivityGetAllVideoBinding
import com.ntduc.utils.file_utils.get_all_video.adapter.GetAllVideoAdapter
import java.io.File

class GetAllVideoActivity : AppCompatActivity() {
  private lateinit var binding: ActivityGetAllVideoBinding
  private lateinit var adapter: GetAllVideoAdapter
  private lateinit var viewModel: GetAllVideoViewModel
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityGetAllVideoBinding.inflate(layoutInflater)
    setContentView(binding.root)
    
    init()
  }
  
  override fun onStart() {
    super.onStart()
    viewModel.loadAllVideo(this)
  }
  
  private fun init() {
    initView()
    initData()
    initEvent()
  }
  
  private fun initEvent() {
    adapter.setOnOpenListener {
      if (it.myFile?.data != null && File(it.myFile!!.data!!).exists()) {
        File(it.myFile!!.data!!).open(this, "com.ntduc.utils.provider")
      } else {
        shortToast("File does not exists")
      }
    }
  }
  
  private fun initData() {
    viewModel.listAllVideo.observe(this) {
      if (viewModel.isLoadListAllVideo) {
        binding.layoutLoading.root.visibility = View.GONE
        if (it.isEmpty()) {
          binding.layoutNoItem.root.visibility = View.VISIBLE
          binding.rcvList.visibility = View.INVISIBLE
        } else {
          binding.layoutNoItem.root.visibility = View.GONE
          binding.rcvList.visibility = View.VISIBLE
          adapter.updateData(it)
        }
      } else {
        binding.layoutLoading.root.visibility = View.VISIBLE
      }
    }
  }
  
  private fun initView() {
    viewModel = ViewModelProvider(this)[GetAllVideoViewModel::class.java]
    
    adapter = GetAllVideoAdapter(this)
    binding.rcvList.adapter = adapter
    binding.rcvList.setHasFixedSize(true)
    val layoutManager: StickyHeadersGridLayoutManager<GetAllVideoAdapter> =
      StickyHeadersGridLayoutManager(this, 3)
    layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
      override fun getSpanSize(position: Int): Int {
        return if (adapter.isStickyHeader(position)) {
          3
        } else 1
      }
    }
    binding.rcvList.layoutManager = layoutManager
  }
}
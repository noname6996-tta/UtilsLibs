package com.ntduc.utils.file_utils.get_all_file.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.ntduc.fileutils.open
import com.ntduc.recyclerviewutils.sticky.StickyHeadersLinearLayoutManager
import com.ntduc.toastutils.shortToast
import com.tta.utlislib.databinding.ActivityGetAllFileBinding
import com.ntduc.utils.file_utils.get_all_file.adapter.GetAllFileAdapter
import java.io.File

class GetAllFileActivity : AppCompatActivity() {
  private lateinit var binding: ActivityGetAllFileBinding
  private lateinit var adapter: GetAllFileAdapter
  private lateinit var viewModel: GetAllFileViewModel
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityGetAllFileBinding.inflate(layoutInflater)
    setContentView(binding.root)
    
    init()
  }
  
  override fun onStart() {
    super.onStart()
    viewModel.loadAllFile(this)
  }
  
  private fun init() {
    initView()
    initData()
    initEvent()
  }
  
  private fun initEvent() {
    adapter.setOnOpenListener {
      if (it.data != null && File(it.data!!).exists()) {
        File(it.data!!).open(this, "com.ntduc.utils.provider")
      } else {
        shortToast("File does not exists")
      }
    }
  }
  
  private fun initData() {
    viewModel.listAllFile.observe(this) {
      if (viewModel.isLoadListAllFile) {
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
    viewModel = ViewModelProvider(this)[GetAllFileViewModel::class.java]
    
    adapter = GetAllFileAdapter(this)
    binding.rcvList.adapter = adapter
    binding.rcvList.setHasFixedSize(true)
    val layoutManager: StickyHeadersLinearLayoutManager<GetAllFileAdapter> =
      StickyHeadersLinearLayoutManager(this)
    binding.rcvList.layoutManager = layoutManager
  }
}
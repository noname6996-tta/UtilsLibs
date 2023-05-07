package com.tta.utlislib.app_utils.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ntduc.utils.app_utils.activity.AppViewModel
import com.ntduc.utils.app_utils.adapter.ApkAdapter
import com.tta.apputils.installApk
import com.tta.utlislib.databinding.FragmentApkBinding

class ApkFragment : Fragment() {
  private lateinit var binding: FragmentApkBinding
  private lateinit var adapter: ApkAdapter
  private lateinit var viewModel: AppViewModel
  
  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    binding = FragmentApkBinding.inflate(inflater)
    return binding.root
  }
  
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    
    init()
  }
  
  private fun init() {
    initView()
    initData()
    initEvent()
  }
  
  private fun initEvent() {
    adapter.setOnInstallListener {
      requireActivity().installApk(it.myFile!!.data!!, "com.ntduc.utils.provider")
    }
  }
  
  private fun initData() {
    viewModel.listAllApk.observe(viewLifecycleOwner) {
      if (viewModel.isLoadListAllApk) {
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
    viewModel = ViewModelProvider(requireActivity())[AppViewModel::class.java]
    
    adapter = ApkAdapter(requireContext())
    binding.rcvList.adapter = adapter
    binding.rcvList.layoutManager =
      LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
  }
}
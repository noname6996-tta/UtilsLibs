package com.ntduc.utils.fragment_utils.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ntduc.colorutils.randomColor
import com.tta.utlislib.databinding.FragmentDefaultBinding

class DefaultFragment : Fragment() {
  private lateinit var binding: FragmentDefaultBinding
  private var name: String? = null
  
  fun newInstance(name: String) =
    DefaultFragment().apply {
      arguments = Bundle().apply {
        putString(API_NAME, name)
      }
    }
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    if (arguments != null) {
      name = requireArguments().getString(API_NAME)
    }
  }
  
  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    binding = FragmentDefaultBinding.inflate(inflater)
    return binding.root
  }
  
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    
    binding.name.text = name
    binding.root.setBackgroundColor(randomColor)
  }
  
  companion object {
    private const val API_NAME = "API_NAME"
  }
}
package com.ntduc.utils.navigation_utils.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.ntduc.clickeffectutils.setOnClickShrinkEffectListener
import com.ntduc.navigationutils.navigateToActionListener
import com.tta.utlislib.R
import com.tta.utlislib.databinding.FragmentFlowStepBinding

class FlowStepFragment : Fragment() {
  private lateinit var binding: FragmentFlowStepBinding
  
  @SuppressLint("SetTextI18n")
  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    binding = FragmentFlowStepBinding.inflate(inflater)
    
    val safeArgs: FlowStepFragmentArgs by navArgs()
    val flowStepNumber = safeArgs.flowStepNumber
    
    binding.text.text = "Step $flowStepNumber"
    return binding.root
  }
  
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    
    binding.nextButton.setOnClickShrinkEffectListener(navigateToActionListener(R.id.next_action))
  }
}
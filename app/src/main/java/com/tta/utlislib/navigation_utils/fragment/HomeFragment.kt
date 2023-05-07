package com.ntduc.utils.navigation_utils.fragment

import android.os.Bundle
import android.view.*
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import com.ntduc.clickeffectutils.setOnClickShrinkEffectListener
import com.ntduc.navigationutils.navigateToActionListener
import com.ntduc.navigationutils.navigateToDes
import com.tta.utlislib.R
import com.tta.utlislib.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
  private lateinit var binding: FragmentHomeBinding
  
  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    binding = FragmentHomeBinding.inflate(inflater)
    return binding.root
  }
  
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    
    requireActivity().addMenuProvider(object : MenuProvider {
      override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.main_menu, menu)
      }
      
      override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return true
      }
    }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    
    binding.navigateDestinationButton.setOnClickShrinkEffectListener {
      navigateToDes(R.id.flow_step_one_dest)
    }
    
    binding.navigateActionButton.setOnClickShrinkEffectListener(navigateToActionListener(R.id.next_action))
    
    binding.navigateBottomDeeplink.setOnClickShrinkEffectListener {
      navigateToDes(R.id.deeplink_dest)
    }
  }
}
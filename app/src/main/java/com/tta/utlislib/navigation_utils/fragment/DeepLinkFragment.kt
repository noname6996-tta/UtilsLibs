package com.ntduc.utils.navigation_utils.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ntduc.clickeffectutils.setOnClickShrinkEffectListener
import com.ntduc.navigationutils.createDeepLink
import com.tta.utlislib.R
import com.tta.utlislib.databinding.FragmentDeepLinkBinding

class DeepLinkFragment : Fragment() {
  private lateinit var binding: FragmentDeepLinkBinding
  
  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    binding = FragmentDeepLinkBinding.inflate(inflater)
    return binding.root
  }
  
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    
    binding.text.text = arguments?.getString("myarg")
    
    binding.sendNotificationButton.setOnClickShrinkEffectListener {
      val args = Bundle()
      args.putString("myarg", binding.argsEditText.text.toString())
      
      createDeepLink(
        idDes = R.id.deeplink_dest,
        args = args,
        title = "Navigation",
        body = "Deep link to Android",
        icon = R.drawable.ic_android,
        idChannel = "deeplink",
        nameChannel = "Deep Links"
      )
    }
  }
}
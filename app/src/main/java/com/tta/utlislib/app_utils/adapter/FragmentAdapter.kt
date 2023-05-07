package com.ntduc.utils.app_utils.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.tta.utlislib.app_utils.fragment.ApkFragment
import com.tta.utlislib.app_utils.fragment.InstalledAppFragment

class FragmentAdapter(
  fa: FragmentActivity,
) : FragmentStateAdapter(fa) {
  private val listFragment = listOf(
    InstalledAppFragment(),
    ApkFragment()
  )
  
  override fun createFragment(position: Int): Fragment {
    if (position < listFragment.size) {
      return listFragment[position]
    }
    return listFragment[0]
  }
  
  override fun getItemCount(): Int {
    return listFragment.size
  }
}
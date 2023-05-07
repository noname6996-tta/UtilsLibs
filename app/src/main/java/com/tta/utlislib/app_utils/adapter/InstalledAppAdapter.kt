package com.tta.utlislib.app_utils.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ntduc.clickeffectutils.setOnClickShrinkEffectListener
import com.ntduc.utils.model.MyApp
import com.tta.utlislib.databinding.ItemAppBinding

class InstalledAppAdapter(
  val context: Context,
  private var list: List<MyApp> = listOf()
) : RecyclerView.Adapter<InstalledAppAdapter.ItemAppViewHolder>() {
  
  inner class ItemAppViewHolder(binding: ItemAppBinding) :
    RecyclerView.ViewHolder(binding.root) {
    internal val binding: ItemAppBinding
    
    init {
      this.binding = binding
    }
  }
  
  override fun onCreateViewHolder(
    parent: ViewGroup,
    viewType: Int
  ): ItemAppViewHolder {
    val binding = ItemAppBinding.inflate(LayoutInflater.from(context), parent, false)
    return ItemAppViewHolder(binding)
  }
  
  @SuppressLint("SetTextI18n")
  override fun onBindViewHolder(holder: ItemAppViewHolder, position: Int) {
    val item = list[position]
    
    holder.binding.img.setImageDrawable(item.icon)
    holder.binding.txtTitle.text = item.name
    holder.binding.txtDescription.text = item.packageName
    
    holder.binding.btnAction.setOnClickShrinkEffectListener {
      onUninstallListener?.let {
        it(item)
      }
    }
    
    holder.binding.root.setOnClickShrinkEffectListener {
      onOpenListener?.let {
        it(item)
      }
    }
  }
  
  override fun getItemCount(): Int {
    return list.size
  }
  
  @SuppressLint("NotifyDataSetChanged")
  fun updateData(newList: List<MyApp>) {
    list = newList
    notifyDataSetChanged()
  }
  
  private var onUninstallListener: ((MyApp) -> Unit)? = null
  
  fun setOnUninstallListener(listener: (MyApp) -> Unit) {
    onUninstallListener = listener
  }
  
  private var onOpenListener: ((MyApp) -> Unit)? = null
  
  fun setOnOpenListener(listener: (MyApp) -> Unit) {
    onOpenListener = listener
  }
}
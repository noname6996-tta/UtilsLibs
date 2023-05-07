package com.ntduc.utils.app_utils.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.ntduc.clickeffectutils.setOnClickShrinkEffectListener
import com.ntduc.numberutils.formatBytes
import com.ntduc.utils.file_utils.constant.ExtensionConstants
import com.ntduc.utils.model.MyApk
import com.tta.utlislib.R
import com.tta.utlislib.databinding.ItemApkBinding

class ApkAdapter(
  val context: Context,
  private var list: ArrayList<MyApk> = arrayListOf()
) : RecyclerView.Adapter<ApkAdapter.ItemApkViewHolder>() {
  
  inner class ItemApkViewHolder(binding: ItemApkBinding) :
    RecyclerView.ViewHolder(binding.root) {
    internal val binding: ItemApkBinding
    
    init {
      this.binding = binding
    }
  }
  
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ApkAdapter.ItemApkViewHolder {
    val binding = ItemApkBinding.inflate(LayoutInflater.from(context), parent, false)
    return ItemApkViewHolder(binding)
  }
  
  @SuppressLint("SetTextI18n")
  override fun onBindViewHolder(holder: ApkAdapter.ItemApkViewHolder, position: Int) {
    val item = list[position]
    
    var requestOptions = RequestOptions()
    requestOptions = requestOptions.transform(CenterCrop(), RoundedCorners(16))
    
    Glide.with(context)
      .applyDefaultRequestOptions(RequestOptions())
      .load(item.myFile?.data)
      .apply(requestOptions)
      .placeholder(R.drawable.ic_empty)
      .error(ExtensionConstants.getIconFile(item.myFile?.data ?: ""))
      .into(holder.binding.img)
    
    val packageInfo = context.packageManager.getPackageArchiveInfo(
      item.myFile!!.data!!,
      PackageManager.GET_ACTIVITIES
    )
    if (packageInfo != null) {
      val appInfo = packageInfo.applicationInfo
      val icon = appInfo.loadIcon(context.packageManager)
      
      Glide.with(context)
        .applyDefaultRequestOptions(RequestOptions())
        .load(icon)
        .apply(requestOptions)
        .placeholder(R.drawable.ic_empty)
        .error(ExtensionConstants.getIconFile(item.myFile?.data ?: ""))
        .into(holder.binding.img)
    } else {
      holder.binding.img.setImageResource(ExtensionConstants.getIconFile(item.myFile!!.data!!))
    }
    holder.binding.txtTitle.text = item.myFile?.displayName
    holder.binding.txtDescription.text = (item.myFile?.size ?: 0).formatBytes()
    
    holder.binding.btnAction.setOnClickShrinkEffectListener {
      onInstallListener?.let {
        it(item)
      }
    }
  }
  
  override fun getItemCount(): Int {
    return list.size
  }
  
  @SuppressLint("NotifyDataSetChanged")
  fun updateData(newList: List<MyApk>) {
    list = arrayListOf()
    list.addAll(newList)
    notifyDataSetChanged()
  }
  
  private var onInstallListener: ((MyApk) -> Unit)? = null
  
  fun setOnInstallListener(listener: (MyApk) -> Unit) {
    onInstallListener = listener
  }
}
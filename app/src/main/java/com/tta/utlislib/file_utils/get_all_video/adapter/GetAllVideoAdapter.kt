package com.ntduc.utils.file_utils.get_all_video.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.ntduc.clickeffectutils.OnMultiClickListener
import com.ntduc.datetimeutils.formatAsTime
import com.ntduc.glideutils.loadImg
import com.ntduc.recyclerviewutils.sticky.StickyHeaders
import com.tta.utlislib.R
import com.tta.utlislib.databinding.ItemHeaderBinding
import com.tta.utlislib.databinding.ItemVideoBinding
import com.ntduc.utils.file_utils.constant.ExtensionConstants
import com.ntduc.utils.model.MyFile
import com.ntduc.utils.model.MyFolderVideo
import com.ntduc.utils.model.MyVideo

class GetAllVideoAdapter(
  val context: Context,
  private var listFolderVideo: List<MyFolderVideo> = listOf()
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), StickyHeaders, StickyHeaders.ViewSetup {
  private var list: ArrayList<MyVideo> = ArrayList()
  
  init {
    initData()
  }
  
  private fun initData() {
    listFolderVideo.forEach { folder ->
      list.add(MyVideo(myFile = MyFile(title = "${folder.folder.title} (${folder.list.size})")))
      folder.list.forEach {
        list.add(it)
      }
    }
  }
  
  inner class ItemHeaderViewHolder(binding: ItemHeaderBinding) :
    RecyclerView.ViewHolder(binding.root) {
    internal val binding: ItemHeaderBinding
    
    init {
      this.binding = binding
    }
  }
  
  inner class ItemVideoViewHolder(binding: ItemVideoBinding) :
    RecyclerView.ViewHolder(binding.root) {
    internal val binding: ItemVideoBinding
    
    init {
      this.binding = binding
    }
  }
  
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
    return if (viewType == HEADER_ITEM) {
      val binding = ItemHeaderBinding.inflate(LayoutInflater.from(context), parent, false)
      ItemHeaderViewHolder(binding)
    } else {
      val binding = ItemVideoBinding.inflate(LayoutInflater.from(context), parent, false)
      ItemVideoViewHolder(binding)
    }
  }
  
  @SuppressLint("SetTextI18n")
  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    val item = list[position]
    
    when (holder) {
      is ItemHeaderViewHolder -> {
        holder.binding.txtHeader.text = item.myFile?.title
      }
      is ItemVideoViewHolder -> {
        context.loadImg(
          imgUrl = item.myFile!!.data,
          view = holder.binding.img,
          error = ExtensionConstants.getIconFile(item.myFile!!.data!!),
          placeHolder = R.drawable.ic_empty
        )
        
        holder.binding.txtTime.text = item.duration?.formatAsTime()
        
        holder.binding.root.setOnClickListener(object : OnMultiClickListener() {
          override fun onSingleClick(v: View?) {
            onOpenListener?.let {
              it(item)
            }
          }
          
          override fun onDoubleClick(v: View?) {}
        })
      }
    }
  }
  
  override fun getItemCount(): Int {
    return list.size
  }
  
  override fun getItemViewType(position: Int): Int {
    return if (list[position].myFile?.data == null) HEADER_ITEM else super.getItemViewType(
      position
    )
  }
  
  override fun isStickyHeader(position: Int): Boolean {
    return getItemViewType(position) == HEADER_ITEM
  }
  
  override fun setupStickyHeaderView(stickyHeader: View) {
    ViewCompat.setElevation(stickyHeader, 0F)
  }
  
  override fun teardownStickyHeaderView(stickyHeader: View) {
    ViewCompat.setElevation(stickyHeader, 0F)
  }
  
  override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
    super.onViewAttachedToWindow(holder)
    val lp: ViewGroup.LayoutParams = holder.itemView.layoutParams
    if (lp is StaggeredGridLayoutManager.LayoutParams) {
      if (isStickyHeader(holder.layoutPosition)) {
        val p: StaggeredGridLayoutManager.LayoutParams = lp
        p.isFullSpan = true
      }
    }
  }
  
  @SuppressLint("NotifyDataSetChanged")
  fun updateData(newList: List<MyFolderVideo>) {
    listFolderVideo = newList
    initData()
    notifyDataSetChanged()
  }
  
  companion object {
    private const val HEADER_ITEM = 123
  }
  
  private var onOpenListener: ((MyVideo) -> Unit)? = null
  
  fun setOnOpenListener(listener: (MyVideo) -> Unit) {
    onOpenListener = listener
  }
}
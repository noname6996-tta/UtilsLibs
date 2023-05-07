package com.ntduc.utils.file_utils.get_all_file.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.ntduc.clickeffectutils.OnMultiClickListener
import com.ntduc.glideutils.loadImg
import com.ntduc.numberutils.formatBytes
import com.ntduc.recyclerviewutils.sticky.StickyHeaders
import com.tta.utlislib.R
import com.tta.utlislib.databinding.ItemDocumentBinding
import com.tta.utlislib.databinding.ItemHeaderBinding
import com.ntduc.utils.file_utils.constant.ExtensionConstants
import com.ntduc.utils.file_utils.constant.FileType
import com.ntduc.utils.model.MyFile
import com.ntduc.utils.model.MyFolderFile

class GetAllFileAdapter(
  val context: Context,
  private var listFolderFile: List<MyFolderFile> = listOf()
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), StickyHeaders, StickyHeaders.ViewSetup {
  private var list: ArrayList<MyFile> = ArrayList()
  
  init {
    initData()
  }
  
  private fun initData() {
    listFolderFile.forEach { folder ->
      list.add(MyFile(title = "${folder.folder.title} (${folder.list.size})"))
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
  
  inner class ItemDocumentViewHolder(binding: ItemDocumentBinding) :
    RecyclerView.ViewHolder(binding.root) {
    internal val binding: ItemDocumentBinding
    
    init {
      this.binding = binding
    }
  }
  
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
    return if (viewType == HEADER_ITEM) {
      val binding = ItemHeaderBinding.inflate(LayoutInflater.from(context), parent, false)
      ItemHeaderViewHolder(binding)
    } else {
      val binding = ItemDocumentBinding.inflate(LayoutInflater.from(context), parent, false)
      ItemDocumentViewHolder(binding)
    }
  }
  
  @SuppressLint("SetTextI18n")
  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    val item = list[position]
    
    when (holder) {
      is ItemHeaderViewHolder -> {
        holder.binding.txtHeader.text = item.title
      }
      is ItemDocumentViewHolder -> {
        when (ExtensionConstants.getTypeFile(item.data!!)) {
          FileType.MUSIC -> {
            val image = try {
              val mData = MediaMetadataRetriever()
              mData.setDataSource(item.data)
              val art = mData.embeddedPicture
              BitmapFactory.decodeByteArray(art, 0, art!!.size)
            } catch (e: Exception) {
              null
            }
            
            context.loadImg(
              imgUrl = image,
              view = holder.binding.img,
              error = ExtensionConstants.getIconFile(item.data!!),
              placeHolder = R.drawable.ic_empty
            )
          }
          else -> {
            context.loadImg(
              imgUrl = item.data!!,
              view = holder.binding.img,
              error = ExtensionConstants.getIconFile(item.data!!),
              placeHolder = R.drawable.ic_empty
            )
          }
        }
        
        holder.binding.txtTitle.text = item.displayName
        holder.binding.txtDescription.text = item.size?.formatBytes()
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
    return if (list[position].data == null) HEADER_ITEM else super.getItemViewType(
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
  fun updateData(newList: List<MyFolderFile>) {
    listFolderFile = newList
    initData()
    notifyDataSetChanged()
  }
  
  companion object {
    private const val HEADER_ITEM = 123
  }
  
  private var onOpenListener: ((MyFile) -> Unit)? = null
  
  fun setOnOpenListener(listener: (MyFile) -> Unit) {
    onOpenListener = listener
  }
}
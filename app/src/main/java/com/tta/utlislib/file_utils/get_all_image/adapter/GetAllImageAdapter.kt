package com.ntduc.utils.file_utils.get_all_image.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.ntduc.clickeffectutils.OnMultiClickListener
import com.ntduc.glideutils.loadImg
import com.ntduc.recyclerviewutils.sticky.StickyHeaders
import com.tta.utlislib.R
import com.tta.utlislib.databinding.ItemHeaderBottomSelectBinding
import com.tta.utlislib.databinding.ItemHeaderTopSelectBinding
import com.tta.utlislib.databinding.ItemImageBinding
import com.ntduc.utils.file_utils.constant.ExtensionConstants
import com.ntduc.utils.model.MyFile
import com.ntduc.utils.model.MyFolderImage
import com.ntduc.utils.model.MyImage

class GetAllImageAdapter(
  val context: Context,
  private var listFolderImage: List<MyFolderImage> = listOf()
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), StickyHeaders, StickyHeaders.ViewSetup {
  private var list: ArrayList<MyImageInRCV> = ArrayList()
  
  init {
    initData()
  }
  
  private fun initData() {
    listFolderImage.forEach { folder ->
      val size = folder.list.size
      val myShow = MyShow(true)
      list.add(
        MyImageInRCV(
          myImage = MyImage(
            myFile = MyFile(
              title = "${folder.folder.title} ($size)"
            )
          ),
          size = size,
          myShow = myShow
        )
      )
      folder.list.forEach {
        list.add(
          MyImageInRCV(
            myImage = it,
            myShow = myShow
          )
        )
      }
      list.add(MyImageInRCV(size = size, myShow = myShow))
    }
  }
  
  inner class ItemHeaderTopViewHolder(binding: ItemHeaderTopSelectBinding) :
    RecyclerView.ViewHolder(binding.root) {
    internal val binding: ItemHeaderTopSelectBinding
    
    init {
      this.binding = binding
    }
  }
  
  inner class ItemImageViewHolder(binding: ItemImageBinding) :
    RecyclerView.ViewHolder(binding.root) {
    internal val binding: ItemImageBinding
    
    init {
      this.binding = binding
    }
  }
  
  inner class ItemHeaderBottomViewHolder(binding: ItemHeaderBottomSelectBinding) :
    RecyclerView.ViewHolder(binding.root) {
    internal val binding: ItemHeaderBottomSelectBinding
    
    init {
      this.binding = binding
    }
  }
  
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
    return when (viewType) {
      HEADER_TOP_ITEM -> {
        val binding =
          ItemHeaderTopSelectBinding.inflate(LayoutInflater.from(context), parent, false)
        ItemHeaderTopViewHolder(binding)
      }
      HEADER_BOTTOM_ITEM -> {
        val binding = ItemHeaderBottomSelectBinding.inflate(
          LayoutInflater.from(context),
          parent,
          false
        )
        ItemHeaderBottomViewHolder(binding)
      }
      else -> {
        val binding = ItemImageBinding.inflate(LayoutInflater.from(context), parent, false)
        ItemImageViewHolder(binding)
      }
    }
  }
  
  @SuppressLint("SetTextI18n")
  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    val item = list[position]
    when (holder) {
      is ItemHeaderTopViewHolder -> {
        holder.binding.txtHeader.text = item.myImage?.myFile?.title
        
        holder.binding.btnMore.isActivated = item.myShow.isShow
        
        holder.binding.root.setOnClickListener {
          item.myShow.isShow = !item.myShow.isShow
          holder.binding.btnMore.isActivated = item.myShow.isShow
          notifyItemRangeChanged(position + 1, item.size)
        }
      }
      is ItemImageViewHolder -> {
        if (!item.myShow.isShow) {
          val params = holder.itemView.layoutParams
          params.height = 0
          params.width = 0
        } else {
          val params = holder.itemView.layoutParams
          params.height = ViewGroup.LayoutParams.WRAP_CONTENT
          params.width = ViewGroup.LayoutParams.MATCH_PARENT
          
          context.loadImg(
            imgUrl = item.myImage!!.myFile!!.data,
            view = holder.binding.img,
            error = ExtensionConstants.getIconFile(item.myImage!!.myFile!!.data!!),
            placeHolder = R.drawable.ic_empty
          )
          
          holder.binding.root.setOnClickListener(object : OnMultiClickListener() {
            override fun onSingleClick(v: View?) {
              onOpenListener?.let {
                it(item.myImage!!)
              }
            }
            
            override fun onDoubleClick(v: View?) {}
          })
        }
      }
    }
  }
  
  override fun getItemCount(): Int {
    return list.size
  }
  
  override fun getItemViewType(position: Int): Int {
    return if (list[position].myImage?.myFile == null) HEADER_BOTTOM_ITEM
    else if (list[position].myImage?.myFile!!.data == null) HEADER_TOP_ITEM
    else super.getItemViewType(position)
  }
  
  override fun isStickyHeader(position: Int): Boolean {
    return getItemViewType(position) == HEADER_TOP_ITEM || getItemViewType(position) == HEADER_BOTTOM_ITEM
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
  fun updateData(newList: List<MyFolderImage>) {
    listFolderImage = newList
    initData()
    notifyDataSetChanged()
  }
  
  companion object {
    private const val HEADER_TOP_ITEM = 123
    private const val HEADER_BOTTOM_ITEM = 456
  }
  
  private var onOpenListener: ((MyImage) -> Unit)? = null
  
  fun setOnOpenListener(listener: (MyImage) -> Unit) {
    onOpenListener = listener
  }
  
  class MyImageInRCV(
    var myImage: MyImage? = null,
    var size: Int = 0,
    var myShow: MyShow = MyShow(true)
  )
  
  class MyShow(
    var isShow: Boolean = true
  )
}
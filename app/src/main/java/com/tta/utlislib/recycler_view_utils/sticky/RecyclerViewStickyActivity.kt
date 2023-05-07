package com.tta.utlislib.Recycler_view_utils.sticky

import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.ntduc.contextutils.inflater
import com.ntduc.recyclerviewutils.sticky.StickyHeaders
import com.ntduc.recyclerviewutils.sticky.StickyHeadersGridLayoutManager
import com.ntduc.recyclerviewutils.sticky.StickyHeadersLinearLayoutManager
import com.ntduc.recyclerviewutils.sticky.StickyHeadersStaggeredGridLayoutManager
import com.tta.utlislib.R
import com.tta.utlislib.databinding.ActivityRecyclerViewStickyBinding
import java.util.*

class RecyclerViewStickyActivity : AppCompatActivity() {
  private lateinit var binding: ActivityRecyclerViewStickyBinding
  private lateinit var adapter: MyAdapter
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityRecyclerViewStickyBinding.inflate(inflater)
    setContentView(binding.root)
    
    binding.rcv.setHasFixedSize(true)
    setLinearLayoutManager()
    adapter = MyAdapter()
    binding.rcv.adapter = adapter
  }
  
  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    menuInflater.inflate(R.menu.rcv_sticky, menu)
    return super.onCreateOptionsMenu(menu)
  }
  
  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    when (item.itemId) {
      R.id.menu1 -> setLinearLayoutManager()
      R.id.menu2 -> setGridLayoutManager()
      R.id.menu3 -> setStaggeredGridLayoutManager()
      else -> {}
    }
    return super.onOptionsItemSelected(item)
  }
  
  private fun setLinearLayoutManager() {
    val layoutManager: StickyHeadersLinearLayoutManager<MyAdapter> =
      StickyHeadersLinearLayoutManager(this)
    binding.rcv.layoutManager = layoutManager
  }
  
  private fun setGridLayoutManager() {
    val layoutManager: StickyHeadersGridLayoutManager<MyAdapter> =
      StickyHeadersGridLayoutManager(this, 3)
    layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
      override fun getSpanSize(position: Int): Int {
        return if (adapter.isStickyHeader(position)) {
          3
        } else 1
      }
    }
    binding.rcv.layoutManager = layoutManager
  }
  
  private fun setStaggeredGridLayoutManager() {
    val layoutManager: StickyHeadersStaggeredGridLayoutManager<MyAdapter> =
      StickyHeadersStaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
    binding.rcv.layoutManager = layoutManager
  }
  
  internal class MyAdapter
    : RecyclerView.Adapter<MyViewHolder>(), StickyHeaders, StickyHeaders.ViewSetup {
    
    companion object {
      private val DICT = arrayOf(
        "A",
        "B",
        "C",
        "D",
        "E",
        "F",
        "G",
        "H",
        "I",
        "J",
        "K",
        "L",
        "M",
        "N",
        "O",
        "P",
        "Q",
        "R",
        "S",
        "T",
        "U",
        "V",
        "W",
        "X",
        "Y",
        "Z",
        "a",
        "b",
        "c",
        "d",
        "e",
        "f",
        "g",
        "h",
        "i",
        "j",
        "k",
        "l",
        "m",
        "n",
        "o",
        "p",
        "q",
        "r",
        "s",
        "t",
        "u",
        "v",
        "w",
        "x",
        "y",
        "z"
      )
      private const val HEADER_ITEM = 123
    }
    
    private var list: MutableList<String> = ArrayList()
    
    init {
      initData()
    }
    
    private fun initData() {
      for (i in 65 until 26 + 65) {
        list.add(i.toChar().toString())
        for (j in 0..9) {
          val itemText = getItemText(i.toChar())
          list.add(itemText)
        }
      }
    }
    
    private fun getItemText(prefix: Char): String {
      val length = createRandom(0, 10)
      val builder = StringBuilder()
      builder.append(prefix)
      for (i in 0 until length) {
        val random = createRandom(0, 51)
        builder.append(DICT[random])
      }
      return builder.toString()
    }
    
    private fun createRandom(min: Int, max: Int): Int {
      val random = Random()
      return random.nextInt(max) % (max - min + 1) + min
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
      return if (viewType == HEADER_ITEM) {
        val inflate: View =
          LayoutInflater.from(parent.context).inflate(R.layout.rcv_header_item, parent, false)
        MyViewHolder(inflate)
      } else {
        val inflate: View =
          LayoutInflater.from(parent.context).inflate(R.layout.rcv_list_item, parent, false)
        MyViewHolder(inflate)
      }
    }
    
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
      val item: String = list[position]
      val textView: TextView = holder.itemView.findViewById(android.R.id.text1)
      textView.text = item
    }
    
    override fun getItemCount(): Int {
      return list.size
    }
    
    override fun getItemViewType(position: Int): Int {
      return if (position % 11 == 0) HEADER_ITEM else super.getItemViewType(position)
    }
    
    override fun isStickyHeader(position: Int): Boolean {
      return getItemViewType(position) == HEADER_ITEM
    }
    
    override fun setupStickyHeaderView(stickyHeader: View) {
      ViewCompat.setElevation(stickyHeader, 10F)
    }
    
    override fun teardownStickyHeaderView(stickyHeader: View) {
      ViewCompat.setElevation(stickyHeader, 0F)
    }
    
    override fun onViewAttachedToWindow(holder: MyViewHolder) {
      super.onViewAttachedToWindow(holder)
      val lp: ViewGroup.LayoutParams = holder.itemView.layoutParams
      if (lp is StaggeredGridLayoutManager.LayoutParams) {
        if (isStickyHeader(holder.layoutPosition)) {
          val p: StaggeredGridLayoutManager.LayoutParams = lp
          p.isFullSpan = true
        }
      }
    }
  }
  
  internal class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}
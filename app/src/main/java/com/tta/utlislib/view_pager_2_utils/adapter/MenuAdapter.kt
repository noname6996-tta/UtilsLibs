package com.ntduc.utils.view_pager_2_utils.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.Filterable
import com.tta.utlislib.databinding.ListItemBinding
import com.ntduc.utils.view_pager_2_utils.model.Transformer

class MenuAdapter(
  context: Context,
  list: List<Transformer>
) : ArrayAdapter<Transformer>(context, 0, list), Filterable {
  
  @SuppressLint("ViewHolder")
  override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
    val binding = ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    val transformer = getItem(position)
    if (transformer != null) {
      binding.item.text = transformer.name
    }
    return binding.root
  }
  
  override fun getFilter(): Filter {
    return object : Filter() {
      override fun performFiltering(constraint: CharSequence?): FilterResults {
        TODO("Not yet implemented")
      }
      
      override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
        TODO("Not yet implemented")
      }
      
      override fun convertResultToString(resultValue: Any?): CharSequence {
        return (resultValue as Transformer).name
      }
    }
  }
}
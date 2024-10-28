package com.tta.utlislib.language

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tohsoft.base.mvp.ui.BaseViewHolder
import com.tta.utlislib.databinding.ItemSettingsLanguageBinding

class ItemLanguageAdapter internal constructor(private val mLanguageList: List<String>, var selectedIndex: Int) :
    RecyclerView.Adapter<BaseViewHolder<*>>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        val binding = ItemSettingsLanguageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    inner class ViewHolder internal constructor(binding: ItemSettingsLanguageBinding) :
        BaseViewHolder<ItemSettingsLanguageBinding>(binding) {
        override fun onBind(position: Int) {
            super.onBind(position)
            val language = mLanguageList[position]
            mBinding.tvItemName.text = language
            mBinding.radioButton.isChecked = selectedIndex == position

            mBinding.llItem.setOnClickListener { v: View? ->
                selectedIndex = position
                notifyDataSetChanged()
            }
            mBinding.radioButton.setOnClickListener { v: View? ->
                selectedIndex = position
                notifyDataSetChanged()
            }
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        holder.onBind(position)
    }

    override fun getItemCount(): Int {
        return mLanguageList.size
    }
}
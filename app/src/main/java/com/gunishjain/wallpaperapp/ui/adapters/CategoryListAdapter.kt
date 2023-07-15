package com.gunishjain.wallpaperapp.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gunishjain.wallpaperapp.databinding.FilterCardBinding

class CategoryListAdapter() : RecyclerView.Adapter<CategoryListAdapter.CategoryListViewHolder>() {

    private var categoryList = ArrayList<String>()
    private var onItemClick: ((String) -> Unit)? = null

    fun setCategoryList(categoryList: ArrayList<String>){
        this.categoryList=categoryList
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(listener: (String) -> Unit) {
        onItemClick = listener
    }


    class CategoryListViewHolder(var binding:FilterCardBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryListViewHolder {
        return CategoryListViewHolder(FilterCardBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    override fun onBindViewHolder(holder: CategoryListViewHolder, position: Int) {

        val category=categoryList[position]
        holder.binding.tvCategory.text= category

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(category)
        }
    }


}
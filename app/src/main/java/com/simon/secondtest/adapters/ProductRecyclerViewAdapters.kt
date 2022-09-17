package com.simon.secondtest.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.simon.secondtest.databinding.ProductRowBinding
import com.simon.secondtest.models.ProductModel
import timber.log.Timber

class ProductRecyclerViewAdapters():RecyclerView.Adapter<ProductRecyclerViewAdapters.viewHolders>() {

    inner class viewHolders(val binding:ProductRowBinding):RecyclerView.ViewHolder(binding.root){

        fun bind(productModel: ProductModel){
            binding.product = productModel
            val url = productModel.apiFeaturedImage.replace("//","")
            Timber.d(url)
            binding.executePendingBindings()
        }
    }

    private var products = mutableListOf<ProductModel>()

    override fun getItemCount(): Int {
        return products.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolders {
        val binding = ProductRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return viewHolders(binding)
    }

    override fun onBindViewHolder(holder: viewHolders, position: Int) {
        holder.bind(products[position])

    }

    //function to set the initial recyclerview list
    fun setProducts(newlist:List<ProductModel>){
        val dif = DiffUtil.calculateDiff(com.simon.secondtest.utils.difUtil.DiffUtil(newlist,products))
        products = newlist.toMutableList()
        dif.dispatchUpdatesTo(this)
    }


    //function to add new products to recyclerview
    fun addItems(newlist:List<ProductModel>){
        val position = products.size
        products.addAll(newlist)
        notifyItemRangeInserted(position,products.size)
    }

}
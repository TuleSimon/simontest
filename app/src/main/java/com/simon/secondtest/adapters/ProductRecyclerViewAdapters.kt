package com.simon.secondtest.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.simon.secondtest.databinding.ProductRowBinding
import com.simon.secondtest.interfaces.ProductClickedInterface
import com.simon.secondtest.models.ProductModel
import com.simon.secondtest.utils.extensionsFunctions.hide
import com.simon.secondtest.utils.extensionsFunctions.show
import timber.log.Timber

class ProductRecyclerViewAdapters(
    val clicked:ProductClickedInterface
):RecyclerView.Adapter<ProductRecyclerViewAdapters.viewHolders>() {

    inner class viewHolders(val binding:ProductRowBinding):RecyclerView.ViewHolder(binding.root){

        fun bind(productModel: ProductModel, previous:ProductModel?){
            binding.product = productModel
            val url = productModel.apiFeaturedImage?.replace("//","")
            Timber.d(url)
            previous?.apply {
                if(previous.productType.equals(productModel.productType,true)){
                    binding.productType.hide()
                }
            }?:binding.productType.show()
            binding.productCard.setOnClickListener{
                clicked.clicked(productModel,binding.productImage)
            }
            ViewCompat.setTransitionName(binding.productImage,productModel.id.toString())
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
        if(position>0)
        holder.bind(products[position],products[position-1])
        else
            holder.bind(products[position],null)

    }

    //function to set the initial recyclerview list
    fun setProducts(newlist:List<ProductModel>){
        val dif = DiffUtil.calculateDiff(com.simon.secondtest.utils.difUtil.DiffUtil(newlist,products))
        products = newlist.toMutableList()
        dif.dispatchUpdatesTo(this)
    }

    //function to set the initial recyclerview list
    fun setProducts2(newlist:List<ProductModel>){
        products = newlist.toMutableList()
        notifyDataSetChanged()
    }


    //function to add new products to recyclerview
    fun addItems(newlist:List<ProductModel>){
        val position = products.size
        products.addAll(newlist)
        notifyItemRangeInserted(position,products.size)
    }

}
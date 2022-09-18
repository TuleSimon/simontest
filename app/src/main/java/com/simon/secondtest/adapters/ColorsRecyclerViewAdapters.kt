package com.simon.secondtest.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.simon.secondtest.databinding.ColorsRowBinding
import com.simon.secondtest.databinding.ProductRowBinding
import com.simon.secondtest.models.ProductColorsItem
import com.simon.secondtest.models.ProductModel
import com.simon.secondtest.utils.extensionsFunctions.hide
import com.simon.secondtest.utils.extensionsFunctions.show
import timber.log.Timber

class ColorsRecyclerViewAdapters():RecyclerView.Adapter<ColorsRecyclerViewAdapters.viewHolders>() {

    inner class viewHolders(val binding:ColorsRowBinding):RecyclerView.ViewHolder(binding.root){

        fun bind(colorModel: ProductColorsItem){
            colorModel.hexValue?.apply {
                binding.color.setBackgroundColor(Color.parseColor(this))
            }
            colorModel.colourName?.apply {
                binding.colorText.text = this
            }
        }
    }

    private var colors = mutableListOf<ProductColorsItem>()

    override fun getItemCount(): Int {
        return colors.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolders {
        val binding = ColorsRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return viewHolders(binding)
    }

    override fun onBindViewHolder(holder: viewHolders, position: Int) {

        holder.bind(colors[position])

    }

    //function to set the initial recyclerview list
    fun setColors(newlist:List<ProductColorsItem>){
        val dif = DiffUtil.calculateDiff(com.simon.secondtest.utils.difUtil.DiffUtil(newlist,colors))
        colors = newlist.toMutableList()
        dif.dispatchUpdatesTo(this)
    }





}
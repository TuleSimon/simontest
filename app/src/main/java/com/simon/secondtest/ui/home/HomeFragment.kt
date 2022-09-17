package com.simon.secondtest.ui.home

import android.os.Bundle
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.chip.Chip
import com.google.android.material.snackbar.Snackbar
import com.simon.secondtest.R
import com.simon.secondtest.adapters.ProductRecyclerViewAdapters
import com.simon.secondtest.databinding.FragmentHomeBinding
import com.simon.secondtest.enums.NetworkResult
import com.simon.secondtest.models.ProductModel
import com.simon.secondtest.utils.CONS.PAGING_PER_PAGE
import com.simon.secondtest.utils.extensionsFunctions.hide
import com.simon.secondtest.utils.extensionsFunctions.paginate
import com.simon.secondtest.utils.extensionsFunctions.show
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.

    val homeViewModel by activityViewModels<HomeViewModel>()
    val adapters by lazy {   ProductRecyclerViewAdapters() }

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        initializeRecyclerView()
        getProducts()

        return root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        listenBrandUpdates()

        lifecycleScope.launch {
            binding.productRecyclerview.showShimmer()
            homeViewModel.products.collect{
                binding.productRecyclerview.hideShimmer()
                when(it){
                    is NetworkResult.Error -> {
                        binding.group.hide()
                        binding.errorLayout.root.show()
                        binding.errorLayout.nodataImage.setImageResource(R.drawable.ic_baseline_error_24)

                    }
                    is NetworkResult.Loading -> {
                        binding.productRecyclerview.showShimmer()
                        binding.group.show()

                    }

                    is NetworkResult.Success -> {
                        binding.group.show()
                        binding.errorLayout.root.hide()

                        loadData(it.data)
                    }

                    is NetworkResult.NetworkError -> {
                        binding.group.hide()
                        binding.errorLayout.root.show()
                        binding.errorLayout.nodataImage.setImageResource(R.drawable.ic_baseline_network_check_24)

                    }
                }
            }
        }
    }


    private fun listenBrandUpdates() = lifecycleScope.launch{
        homeViewModel.brands.collect{
            when(it){
                is NetworkResult.Loading -> {
                    Toast.makeText(requireContext(),"loading",Toast.LENGTH_LONG).show()
                }
                is NetworkResult.Success -> {
                    val brandData = it.data
                    brandData?.let { it1 -> populateBrandsToChipGroup(it1) }
                    Toast.makeText(requireContext(),brandData?.size.toString(),Toast.LENGTH_LONG).show()
                }
                else -> {
                    Snackbar.make(binding.root,it.message.toString(),Snackbar.LENGTH_LONG).show()
                }
            }
        }
    }


    private fun populateBrandsToChipGroup(list:List<String>){
        binding.chipGroup.removeAllViews()
        list.forEach {
            val chip =Chip(requireContext(),null, com.google.android.material.R.attr.chipStyle).apply {
                text = it
                setTextColor(ContextCompat.getColor(requireContext(),R.color.chip_text_colors))
                setChipBackgroundColorResource(R.color.chip_bg_colors)
                setChipStrokeColorResource(R.color.chip_colors)
                textAlignment = View.TEXT_ALIGNMENT_CENTER
                isCheckable=true
                setCheckedIconTintResource(R.color.purple_500)
                id = View.generateViewId()
            }
            binding.chipGroup.addView(chip )
        }
    }


    private fun loadData(list: List<ProductModel>?){
        list?.apply {
            if(this.size>10) {
                val items = this.slice(0..10)
                setRecyclerViewProducts(items)
                paginateData(list)
            }
        }
    }


    private fun paginateData(list: List<ProductModel>){
        binding.productRecyclerview.paginate(list,PAGING_PER_PAGE){
            adapters.addItems(it )
        }
    }

    //initializing recylerview
    private fun initializeRecyclerView(){
        val layout = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        binding.productRecyclerview.apply {
            layoutManager = layout
            adapter = adapters
        }

    }


    //setting prodcuts from api call to recyclerview
    private fun setRecyclerViewProducts(list:List<ProductModel>){
        adapters.setProducts(list)
    }


    //getting products from viewmodel
    private fun getProducts(){
        homeViewModel.getProducts()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
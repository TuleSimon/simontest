package com.simon.secondtest.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.chip.Chip
import com.google.android.material.snackbar.Snackbar
import com.simon.secondtest.R
import com.simon.secondtest.adapters.ProductRecyclerViewAdapters
import com.simon.secondtest.databinding.FragmentHomeBinding
import com.simon.secondtest.enums.NetworkResult
import com.simon.secondtest.interfaces.ProductClickedInterface
import com.simon.secondtest.models.ProductModel
import com.simon.secondtest.utils.CONS.PAGING_PER_PAGE
import com.simon.secondtest.utils.extensionsFunctions.hide
import com.simon.secondtest.utils.extensionsFunctions.paginate
import com.simon.secondtest.utils.extensionsFunctions.show
import com.simon.secondtest.utils.productHelper.ProductHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.lang.NullPointerException


@AndroidEntryPoint
class HomeFragment : Fragment(), ProductClickedInterface{

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.

    val homeViewModel by activityViewModels<HomeViewModel>()
    val adapters by lazy {   ProductRecyclerViewAdapters(this) }

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        if(view==null){
            _binding = FragmentHomeBinding.inflate(inflater, container, false)
            initializeRecyclerView()
            lifecycleScope.launch {
                if(homeViewModel.brandProducts.first() !is NetworkResult.Success)
                    getProducts()
            }
        }

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listenBrandUpdates()

        lifecycleScope.launch {
            //setting our brand variable to our select brand in data store
            brand = getSelectedBrand()


            homeViewModel.brandProducts.collect{
                binding.errorLayout.root.hide()
                binding.productRecyclerview.hideShimmer()
                when(it){
                    is NetworkResult.Error -> {
                        binding.group.hide()
                        binding.errorLayout.root.show()
                        binding.errorLayout.nodataImage.setImageResource(R.drawable.ic_baseline_error_24)
                        binding.errorLayout.nodataText.text = it.message.toString()
                    }
                    is NetworkResult.Loading -> {
                        binding.productRecyclerview.showShimmer()
                        binding.group.show()

                    }

                    is NetworkResult.Success -> {
                        binding.group.show()
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
                }
                else -> {
                    Snackbar.make(binding.root,it.message.toString(),Snackbar.LENGTH_LONG).show()
                }
            }
        }
    }

    lateinit var brand:String
    //function to create chip from all the brands in our api product list and add them to our chp group,
    private fun populateBrandsToChipGroup(list:List<String>) = lifecycleScope.launch{
        binding.chipGroup.removeAllViews()

        val checked = false
        list.forEach {
            val chip =Chip(requireContext(),null, com.google.android.material.R.attr.chipStyle).apply {
                text = it
                setTextColor(ContextCompat.getColor(requireContext(),R.color.chip_text_colors))
                setChipBackgroundColorResource(R.color.chip_bg_colors)
                setChipStrokeColorResource(R.color.chip_colors)
                textAlignment = View.TEXT_ALIGNMENT_CENTER
                isCheckable=true
                setCheckedIconTintResource(R.color.purple_500)
                setOnClickListener {_->
                    changed(it)
                }
            }
            binding.chipGroup.addView(chip )

            //checking if this chip was previously selected by a user
            lifecycleScope.launch {
                if(brand==it) {
                    chip.isChecked = true
                    delay(1000)
                    try {
                        chip.parent.requestChildFocus(chip, chip)
                    }
                    catch (e:NullPointerException){
                        e.printStackTrace()
                    }
                }
            }
        }

    }

    private suspend fun getSelectedBrand():String{
        return    homeViewModel.getSelectedBrand().first()

    }

    //function to set the selected brand chip to our data store
    private fun changed(id:String){
            lifecycleScope.launch {
                homeViewModel.setSelectedBrand(id)
                brand=id
            }
    }


    //loaad the product data from the api call into our recyclerview and paginate it also
    private fun loadData(list: List<ProductModel>?) = lifecycleScope.launch{
        list?.apply {
                //getting a list of products in a particular brand
                if(list.size>10) {
                    val items = this.slice(0..10)
                    setRecyclerViewProducts(items)
                    paginateData(list)
                }
                else{
                    setRecyclerViewProducts(list)
                }

        }
    }


    //our function for paginating the data to improve performance
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
        }

    }


    //setting prodcuts from api call to recyclerview
    private fun setRecyclerViewProducts(list:List<ProductModel>){
        val adapter = ProductRecyclerViewAdapters(this)
        binding.productRecyclerview.adapter = adapter
        adapter.setProducts2(list)
    }


    //getting products from viewmodel
    private fun getProducts(){
        homeViewModel.getProducts()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun clicked(productModel: ProductModel,view: ImageView) {
        val extras = FragmentNavigatorExtras(
            view to productModel.id.toString()
        )
        val directions = HomeFragmentDirections.actionNavigationHomeToViewProductFragment(productModel)
        findNavController().navigate(directions,extras)
    }
}
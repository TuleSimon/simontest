package com.simon.secondtest.utils.productHelper

import com.simon.secondtest.models.ProductModel

object ProductHelper {

    //function to extract the different brands from the original list, removing duplicates
    suspend fun extractBrands(list:List<ProductModel>):List<String>{
        val brandsMap = list.groupBy { it.brand }
        val lists = brandsMap.keys.toList()
        if(lists.isNotEmpty())
        return lists.sortedBy { it?:"z"}.map { it.toString() }
        else
            return emptyList()
    }

    //method to group the original list by brand to a map, and get the list for that particular brand by calling the key of the brand
    suspend fun getBrandProducts(brand:String,list:List<ProductModel>):List<ProductModel>{
        val brandsMap = list.groupBy { it.brand }
        return brandsMap[brand] ?: emptyList()
    }
}
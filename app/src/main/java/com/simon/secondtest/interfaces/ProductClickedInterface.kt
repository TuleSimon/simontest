package com.simon.secondtest.interfaces

import android.view.View
import android.widget.ImageView
import com.simon.secondtest.models.ProductModel

interface ProductClickedInterface {

    fun clicked(productModel: ProductModel,view:ImageView)
}
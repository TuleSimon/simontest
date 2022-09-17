package com.simon.secondtest.utils.extensionsFunctions

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

var loading = false
fun<T> RecyclerView.paginate(
     list:List<T>?, perPage:Int,addData: (list:List<T>) -> Unit){

    addOnScrollListener(object : RecyclerView.OnScrollListener()
    {

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int)
        {

            val position = (recyclerView.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition()
            if ( list != null)
            {
                if (position >= ((recyclerView.layoutManager as LinearLayoutManager).itemCount-1))
                {
                    loading = true
                    if(position< (list.size - 1)){
                        val newPosition = position+perPage
                        if(list.size - 1 > newPosition){
                            addData(list.slice((position+1)..newPosition))
                            loading = false
                        }
                        else{
                            addData(list.slice((position) until list.size))
                            loading = false
                        }
                    }
                }
            }
        }

    })
}


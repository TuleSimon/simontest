package com.simon.secondtest.ui.viewproduct

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.ViewCompat
import androidx.core.view.setPadding
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.transition.TransitionInflater
import com.simon.secondtest.MainActivity
import com.simon.secondtest.R
import com.simon.secondtest.adapters.ColorsRecyclerViewAdapters
import com.simon.secondtest.databinding.FragmentViewProductBinding


class ViewProductFragment : Fragment() {

    lateinit var binding:FragmentViewProductBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        sharedElementEnterTransition = TransitionInflater.from(requireContext())
            .inflateTransition(R.transition.change_bounds)
        sharedElementReturnTransition = TransitionInflater.from(requireContext())
            .inflateTransition(R.transition.change_bounds)
        binding = FragmentViewProductBinding.inflate(inflater,container,false)
        binding.product = args.product
        return binding.root
    }

    val args:ViewProductFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        ViewCompat.setTransitionName(binding.productImage,args.product.id.toString())
        setuUpColors()
    }

    private fun setuUpColors(){
        val adapters = ColorsRecyclerViewAdapters()
        binding.colorRecyclerview.apply {
            adapter = adapters
            layoutManager = GridLayoutManager(requireContext(),4)
        }
        args.product.productColors?.let { adapters.setColors(it) }
        args.product.tagList?.forEach { it ->
            val textView = TextView(requireContext())
            textView.text = it
            textView.setBackgroundResource(R.drawable.bg_rounded)
            textView.setPadding(5)
            val params: LinearLayout.LayoutParams =
                LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            params.setMargins(0, 0, 10, 0)
            textView.layoutParams = params
            binding.tagLayout.addView(textView)
        }

        binding.visitProductLink.setOnClickListener {
            val urlIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse(args.product.productLink)
            )
            startActivity(urlIntent)
        }

        (requireActivity() as MainActivity).getToolbar().title= args.product.name
    }



}
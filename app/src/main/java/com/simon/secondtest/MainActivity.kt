package com.simon.secondtest

import android.os.Bundle
import androidx.activity.viewModels
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.appbar.MaterialToolbar
import com.simon.secondtest.databinding.ActivityMainBinding
import com.simon.secondtest.ui.home.HomeViewModel
import com.simon.secondtest.utils.extensionsFunctions.hide
import com.simon.secondtest.utils.extensionsFunctions.show
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    val homeViewModel by viewModels<HomeViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard
            )
        )
        binding.refresh.setOnClickListener {
            homeViewModel.getProducts()
        }



        navController.addOnDestinationChangedListener{_,navDestination,_ ->
            when(navDestination.id ){
                R.id.viewProductFragment ->{
                    binding.navView.hide()
                    binding.toolbar.setNavigationOnClickListener {
                        navController.navigateUp()
                    }
                    binding.toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
                    binding.refresh.hide()
                }
                else ->{
                    binding.refresh.show()
                    binding.navView.show()
                }
            }
        }

        setSupportActionBar(binding.toolbar)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    fun getToolbar():MaterialToolbar{
        return binding.toolbar
    }

}
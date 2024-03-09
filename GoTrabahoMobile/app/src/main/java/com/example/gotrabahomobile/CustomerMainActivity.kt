package com.example.gotrabahomobile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.gotrabahomobile.fragments.CustomerHomeFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class CustomerMainActivity : AppCompatActivity() {


    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContentView(R.layout.activity_customer_main)

        val email = intent.getStringExtra("email")

        val textView: TextView = findViewById(R.id.textViewSample)



        val userId = intent.getIntExtra("userId", 0) ?: ""
        // Retrieve other data similarly

        // Create a new instance of CustomerHomeFragment
        val customerHomeFragment = CustomerHomeFragment()

        // Create a Bundle and put the data into it
        val bundle = Bundle()
        bundle.putInt("userId", userId as Int)
        // Add other data similarly

        // Set the Bundle as the arguments for the Fragment
        customerHomeFragment.arguments = bundle

        supportFragmentManager.beginTransaction()
            .replace(R.id.mainContainer, customerHomeFragment)
            .commit()

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.mainContainer) as NavHostFragment
        navController = navHostFragment.navController

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.setupWithNavController(navController)

//
//        textView.text = aBundle.toString()

        val aBundle = Bundle()
        aBundle.putString("email", email)

        navController.navigate(R.id.homeFragment, aBundle)
        Log.d("CustomerMainActivity", "Bundle: $aBundle")


    }

}
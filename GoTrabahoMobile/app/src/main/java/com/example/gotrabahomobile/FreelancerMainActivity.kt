package com.example.gotrabahomobile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.gotrabahomobile.fragments.CustomerHomeFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class FreelancerMainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_freelancer_main)

        val userId = intent.getIntExtra("userId", 0) ?: ""
        // Retrieve other data similarly

        // Create a new instance of CustomerHomeFragment
        val bookingsFragment = BookingsFragment()

        // Create a Bundle and put the data into it
        val bundle = Bundle()
        bundle.putInt("userId", userId as Int)
        // Add other data similarly

        bookingsFragment.arguments = bundle

        supportFragmentManager.beginTransaction()
            .replace(R.id.freelancerMainContainer, bookingsFragment)
            .commit()

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.freelancerMainContainer) as NavHostFragment
        navController = navHostFragment.navController
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.freelancerBottomNavigationView)

        NavigationUI.setupWithNavController(bottomNavigationView, navController)
    }
}
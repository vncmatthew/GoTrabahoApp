package com.example.gotrabahomobile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import com.example.gotrabahomobile.databinding.ActivityFreelancerMainBinding

class FreelancerMainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var binding: ActivityFreelancerMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFreelancerMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userId = intent.getIntExtra("sqlId", 0) ?: ""
        val freelancerId = intent.getIntExtra("freelancerId", 0) ?: ""
        val firstName = intent.getStringExtra("firstName")
        val lastName = intent.getStringExtra("lastName")
        val fullName = intent.getStringExtra("fullName")
        val email = intent.getStringExtra("email")

        Log.d("FreelancerMainActivity", "${userId}")
        Log.d("FreelancerMainActivity", "${email}")

        val bundle = Bundle().apply {
            putInt("userId", userId as Int)
            putInt("freelancerId", freelancerId as Int)
            putString("email", email)
            putString("firstName", firstName)
            putString("lastName", lastName)
            putString("fullName", fullName)
        }

        val bookingsFragment = BookingsFragment().apply { arguments = bundle }
        val freelancerMessagesFragment = FreelancerMessagesFragment().apply { arguments = bundle }
        val freelancerAccountFragment = FreelancerAccountFragment().apply { arguments = bundle }


        replaceFragment(bookingsFragment)

        // Add other data similarly

//        bookingsFragment.arguments = bundle

//        supportFragmentManager.beginTransaction()
//            .replace(R.id.freelancerMainContainer, bookingsFragment)
//            .commit()

        binding.freelancerBottomNavigationView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.bookingsFragment -> replaceFragment(bookingsFragment)
                R.id.freelancerMessagesFragment -> replaceFragment(freelancerMessagesFragment)
                R.id.freelancerAccountFragment -> replaceFragment(freelancerAccountFragment)
                else -> false
            }
            true
        }

//        val navHostFragment = supportFragmentManager.findFragmentById(R.id.freelancerMainContainer) as NavHostFragment
//        navController = navHostFragment.navController
//        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.freelancerBottomNavigationView)
//
//        NavigationUI.setupWithNavController(bottomNavigationView, navController)
    }
    private fun replaceFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.freelancerMainContainer, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}
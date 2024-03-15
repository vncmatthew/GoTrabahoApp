package com.example.gotrabahomobile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import com.example.gotrabahomobile.databinding.ActivityCustomerMainBinding
import com.example.gotrabahomobile.fragments.CustomerActivityFragment
import com.example.gotrabahomobile.fragments.CustomerHomeFragment

class CustomerMainActivity : AppCompatActivity() {


    private lateinit var navController: NavController
    private lateinit var binding: ActivityCustomerMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCustomerMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val email = intent.getStringExtra("email")
        val firstName = intent.getStringExtra("firstName")
        val lastName = intent.getStringExtra("lastName")
        val fullName = intent.getStringExtra("fullName")
        val longitude = intent.getDoubleExtra("longitude", 0.0)
        val latitude = intent.getDoubleExtra("latitude", 0.0)
        val userId = intent.getIntExtra("userId", 0)

        Log.d("CustomerMain", userId.toString())

        // Create a Bundle and put the data into it
        val bundle = Bundle().apply {
            putInt("userId", userId)
            putString("email", email)
            putString("firstName", firstName)
            putString("lastName", lastName)
            putDouble("longitude", longitude)
            putDouble("latitude", latitude)
            putString("fullName", fullName)
        }

        // Instantiate fragments with the bundle
        val customerHomeFragment = CustomerHomeFragment().apply { arguments = bundle }
        val customerActivityFragment = CustomerActivityFragment().apply { arguments = bundle }
        val customerMessagesFragment = CustomerMessagesFragment().apply { arguments = bundle }
        val customerAccountFragment = CustomerAccountFragment().apply { arguments = bundle }

        // Replace the initial fragment
        replaceFragment(customerHomeFragment)

        binding.bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.homeFragment -> replaceFragment(customerHomeFragment)
                R.id.activityFragment -> replaceFragment(customerActivityFragment)
                R.id.customerMessagesFragment -> replaceFragment(customerMessagesFragment)
                R.id.accountFragment -> replaceFragment(customerAccountFragment)
                else -> false
            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.mainContainer, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

}
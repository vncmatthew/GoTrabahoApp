package com.example.gotrabahomobile

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import com.example.gotrabahomobile.databinding.ActivityFreelancerMainBinding

class FreelancerMainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var binding: ActivityFreelancerMainBinding
    private val MY_PERMISSIONS_REQUEST_POST_NOTIFICATIONS = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFreelancerMainBinding.inflate(layoutInflater)
        setContentView(binding.root)



        if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
            != PackageManager.PERMISSION_GRANTED) {

            // Request the permission
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                MY_PERMISSIONS_REQUEST_POST_NOTIFICATIONS
            )
        }



        val userId = intent.getIntExtra("userId", 0)
        val freelancerId = intent.getIntExtra("freelancerId", 0)
        val firstName = intent.getStringExtra("firstName")
        val lastName = intent.getStringExtra("lastName")
        val fullName = intent.getStringExtra("fullName")
        val email = intent.getStringExtra("email")

        Log.d("FreelancerMainActivity", "${userId}")
        Log.d("FreelancerMainActivity", "${email}")

        val bundle = Bundle().apply {
            putInt("userId", userId )
            putInt("freelancerId", freelancerId)
            putString("email", email)
            putString("firstName", firstName)
            putString("lastName", lastName)
            putString("fullName", fullName)
        }

        val bookingsFragment = BookingsFragment().apply { arguments = bundle }
        val freelancerMessagesFragment = FreelancerMessagesFragment().apply { arguments = bundle }
        val freelancerAccountFragment = FreelancerAccountFragment().apply { arguments = bundle }


        replaceFragment(bookingsFragment)

        binding.freelancerBottomNavigationView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.bookingsFragment -> replaceFragment(bookingsFragment)
                R.id.freelancerMessagesFragment -> replaceFragment(freelancerMessagesFragment)
                R.id.freelancerAccountFragment -> replaceFragment(freelancerAccountFragment)
                else -> false
            }
            true
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_POST_NOTIFICATIONS -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission was granted, you can access the file now
                } else {
                    // Permission was denied, handle the denial
                }
                return
            }
            // ... (handle other permission requests)
        }
    }
    private fun replaceFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.freelancerMainContainer, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}
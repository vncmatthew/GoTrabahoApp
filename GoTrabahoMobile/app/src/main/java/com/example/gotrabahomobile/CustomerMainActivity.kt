package com.example.gotrabahomobile

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import com.example.gotrabahomobile.Remote.BookingRemote.BookingInstance
import com.example.gotrabahomobile.Remote.RatingRemote.RatingInstance
import com.example.gotrabahomobile.databinding.ActivityCustomerMainBinding
import com.example.gotrabahomobile.fragments.CustomerActivityFragment
import com.example.gotrabahomobile.fragments.CustomerHomeFragment
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CustomerMainActivity : AppCompatActivity() {


    private lateinit var navController: NavController
    private lateinit var binding: ActivityCustomerMainBinding
    private val MY_PERMISSIONS_REQUEST_POST_NOTIFICATIONS = 1

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

        val bundle = Bundle().apply {
            putInt("userId", userId)
            putString("email", email)
            putString("firstName", firstName)
            putString("lastName", lastName)
            putDouble("longitude", longitude)
            putDouble("latitude", latitude)
            putString("fullName", fullName)
            AddRating()
        }


        val customerHomeFragment = CustomerHomeFragment().apply { arguments = bundle }
        val customerActivityFragment = CustomerActivityFragment().apply { arguments = bundle }
        val customerMessagesFragment = CustomerMessagesFragment().apply { arguments = bundle }
        val customerAccountFragment = CustomerAccountFragment().apply { arguments = bundle }


        replaceFragment(customerHomeFragment)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
            != PackageManager.PERMISSION_GRANTED) {


            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                MY_PERMISSIONS_REQUEST_POST_NOTIFICATIONS
            )
        }

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

    private fun AddRating(){
        val userId = intent.getIntExtra("userId", 0)
        val call = BookingInstance.retrofitBuilder
        call.getCompletedBooking(userId,3).enqueue(object: Callback<Int>{
            override fun onResponse(call: Call<Int>, response: Response<Int>) {
                if (response.isSuccessful) {

                    val bookingId = response.body()
                    val email = intent.getStringExtra("email")
                    val firstName = intent.getStringExtra("firstName")
                    val lastName = intent.getStringExtra("lastName")
                    val fullName = intent.getStringExtra("fullName")
                    val longitude = intent.getDoubleExtra("longitude", 0.0)
                    val latitude = intent.getDoubleExtra("latitude", 0.0)
                    val userId = intent.getIntExtra("userId", 0)

                    Log.d("Booking", "${bookingId}")
                    if(bookingId != 0) {
                        val intent =
                            Intent(this@CustomerMainActivity, BookingDetailsActivity::class.java)
                        intent.putExtra("bookingId", bookingId)
                        intent.putExtra("email",email)
                        intent.putExtra("firstName", firstName)
                        intent.putExtra("lastName",lastName)
                        intent.putExtra("fullName",fullName)
                        intent.putExtra("longitude",longitude)
                        intent.putExtra("latitude",latitude)
                        intent.putExtra("userId",userId)
                        startActivity(intent)
                    }


                }
                }

            override fun onFailure(call: Call<Int>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_POST_NOTIFICATIONS -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("Success!", "permission given")
                } else {
                    Log.d("Denied!", "permission denied")
                }
                return
            }
        }
    }
    private fun replaceFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.mainContainer, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

}
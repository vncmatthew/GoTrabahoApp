package com.example.gotrabahomobile


import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.gotrabahomobile.Model.User
import com.example.gotrabahomobile.Remote.UserRemote.UserInstance
import com.google.android.gms.location.FusedLocationProviderClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

import java.io.IOException

class CustomerRegisterDetailsActivity : AppCompatActivity() {

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var currentLatitude: Double? = null
    private var currentLongitude: Double? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_register_details)
        val btnCustomer = findViewById<Button>(R.id.buttonCustomerSignUp)

        btnCustomer.setOnClickListener {
            Log.d("RegisterActivity", "Before registerData")

            Log.d("RegisterActivity", "After registerData")
            insertCustomer()
            Log.d("hello", insertCustomer().toString())
        }



    }


    private fun insertCustomer(){


        val firstNameText = findViewById<EditText>(R.id.editTextCustomerFirstName)
        val lastNameText = findViewById<EditText>(R.id.editTextCustomerLastName)
        val birthdateText = findViewById<EditText>(R.id.editTextCustomerBirthdate)
        val phoneNumberText = findViewById<EditText>(R.id.editTextCustomerPhoneNumber)
        val emailText = findViewById<EditText>(R.id.editTextCustomerEmailAddress)
        val passwordText = findViewById<EditText>(R.id.editTextCustomerPassword)
        val confirmPassText = findViewById<EditText>(R.id.editTextCustomerConfirmPassword)
        val address1Text = findViewById<EditText>(R.id.editTextCustomerAddress1)
        val address2Text = findViewById<EditText>(R.id.editTextCustomerAddress2)
        val barangayText = findViewById<EditText>(R.id.editTextCustomerBarangey)
        val cityText = findViewById<EditText>(R.id.editTextCustomerCity)
        val userType = 1


        val firstName = firstNameText.text.toString()
        val lastName = lastNameText.text.toString()
        val birthdate = birthdateText.text.toString()
        val email = emailText.text.toString()
        val address1 = address1Text.text.toString()
        val address2 = address2Text.text.toString()
        val barangay = barangayText.text.toString()
        val city = cityText.text.toString()
        val password = passwordText.text.toString()
        val contactNumber = phoneNumberText.text.toString()
        val latLong = getLatLongFromAddress(this, address1, address2, barangay, city)
        if (latLong != null) {
            val (latitude, longitude) = latLong
            println("Latitude: $latitude, Longitude: $longitude")
            currentLongitude = longitude
            currentLatitude = latitude
        } else {
            println("Address not found")
        }
        val latitude = currentLatitude
        val longitude = currentLongitude


        registerCustomer(userType, firstName, lastName,  email, password, contactNumber, birthdate, address1,
            address2, barangay, city, longitude, latitude)


/*        intent.putExtra("firstName", firstName)
        intent.putExtra("lastName", lastName)
        intent.putExtra("birthdate", birthdate)
        intent.putExtra("phoneNumber", contactNumber)
        intent.putExtra("email", email)
        intent.putExtra("password", password)
        intent.putExtra("userType", userType)
        intent.putExtra("address1", address1)
        intent.putExtra("address1", address2)
        intent.putExtra("barangay", barangay)
        intent.putExtra("city", city)
        intent.putExtra("longitude", longitude)
        intent.putExtra("latitude", latitude)

*/
        val intent = Intent(this, CustomerRegisterDetailsActivity::class.java)
        startActivity(intent)
        return


    }

    private fun registerCustomer(userType: Int, firstName: String, lastName: String,  email: String, password: String?, contactNumber: String?, birthdate: String, address1: String,
                                 address2: String, barangay: String, city: String, longitude: Double?, latitude: Double?) {

        val usersInput = User(
            userType = userType,
            firstName = firstName,
            lastName = lastName,
            email = email,
            password = password,
            contactNumber = contactNumber,
            birthdate = birthdate,
            address1 = address1,
            address2 = address2,
            barangay = barangay,
            city = city,
            longitude = longitude,
            latitude = latitude)

        val userService = UserInstance.retrofitBuilder
        userService.registerUser(usersInput).enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                Log.i(TAG, "The response is " + response.message());
                Log.i(TAG, "The response is " + response.body());

                if (response.isSuccessful) {
                    val users = response.body()
                    if(users != null){
                        Toast.makeText(this@CustomerRegisterDetailsActivity, "Register", Toast.LENGTH_SHORT).show()
                    }else {
                        Toast.makeText(this@CustomerRegisterDetailsActivity, "User ID is null", Toast.LENGTH_SHORT).show()
                    }
                }
                else {
                    // Handle the error response
                    Log.d("MainActivity", "Response code: ${response.message()}")

                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                // Handle network or other exceptions
                Log.d("MainActivity", "Exception: ", t)
            }
        })
}



    fun getLatLongFromAddress(context: Context, address1: String, address2: String, barangay: String, city: String): Pair<Double, Double>? {
        val geocoder = Geocoder(context)
        val fullAddress = "$address1, $address2, $barangay, $city"
        try {
            val addresses: List<Address> = geocoder.getFromLocationName(fullAddress, 1)?.toList() ?: emptyList()
            if (addresses.isNotEmpty()) {
                val latitude = addresses[0].latitude
                val longitude = addresses[0].longitude
                return Pair(latitude, longitude)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }

}

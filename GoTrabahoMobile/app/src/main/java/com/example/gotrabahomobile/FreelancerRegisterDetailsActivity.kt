package com.example.gotrabahomobile

import android.content.ContentValues
import android.content.Context
import android.location.Address
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import com.example.gotrabahomobile.Model.User
import com.example.gotrabahomobile.Remote.UserRemote.UserInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class FreelancerRegisterDetailsActivity : AppCompatActivity() {


    private var currentLatitude: Double? = null
    private var currentLongitude: Double? = null
    var selectedItem: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_freelancer_register_details)
        val btnCustomer = findViewById<Button>(R.id.buttonFreelancerContinue1)
        // put this in a different class
        val idType = findViewById<Spinner>(R.id.dropdownIDType)

        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.idType,
            android.R.layout.simple_spinner_dropdown_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        idType.adapter = adapter

        idType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedItem = parent?.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                selectedItem = null
            }

        }
        //ends here

        btnCustomer.setOnClickListener {
            Log.d("RegisterActivity", "Before registerData")

            Log.d("RegisterActivity", "After registerData")
            insertCustomer()
            Log.d("hello", insertCustomer().toString())
        }
    }

    private fun insertCustomer(){


        val firstNameText = findViewById<EditText>(R.id.editTextFreelancerFirstName)
        val lastNameText = findViewById<EditText>(R.id.editTextFreelancerLastName)
        val birthdateText = findViewById<EditText>(R.id.editTextFreelancerBirthdate)
        val phoneNumberText = findViewById<EditText>(R.id.editTextFreelancerPhoneNumber)
        val emailText = findViewById<EditText>(R.id.editTextFreelancerEmailAddress)
        val passwordText = findViewById<EditText>(R.id.editTextFreelancerPassword)
        val confirmPassText = findViewById<EditText>(R.id.editTextFreelancerConfirmPassword)
        val address1Text = findViewById<EditText>(R.id.editTextFreelancerAddress1)
        val address2Text = findViewById<EditText>(R.id.editTextFreelancerAddress2)
        val barangayText = findViewById<EditText>(R.id.editTextFreelancerBarangey)
        val cityText = findViewById<EditText>(R.id.editTextFreelancerCity)
        val userType = 2


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

        /*        val intent = Intent(this, CustomerRegisterDetailsActivity::class.java)
                intent.putExtra("firstName", firstName)
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
                startActivity(intent)
                return*/


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
                Log.i(ContentValues.TAG, "The response is " + response.message());
                Log.i(ContentValues.TAG, "The response is " + response.body());

                if (response.isSuccessful) {
                    val users = response.body()
                    if(users != null){
                        Toast.makeText(this@FreelancerRegisterDetailsActivity, "Register", Toast.LENGTH_SHORT).show()
                    }else {
                        Toast.makeText(this@FreelancerRegisterDetailsActivity, "User ID is null", Toast.LENGTH_SHORT).show()
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
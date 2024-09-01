package com.example.gotrabahomobile

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Spinner
import com.example.gotrabahomobile.Helper.CitySpinnerAdapter
import com.example.gotrabahomobile.Model.Cities
import com.example.gotrabahomobile.Model.User
import com.example.gotrabahomobile.Remote.ArchiveRecordRemote.CityInstance
import com.example.gotrabahomobile.Remote.UserRemote.UserInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class CustomerEditAddressActivity : AppCompatActivity() {


    lateinit var address1: EditText
    lateinit var address2: EditText
    lateinit var barangay: EditText
    private lateinit var citySpinner: Spinner
    private var currentLatitude: Double? = null
    private var currentLongitude: Double? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_edit_address)


        val userId = intent.getIntExtra("userId", 0)
        val email = intent.getStringExtra("email")

        val backButton: ImageButton = findViewById(R.id.back_buttonNavbar)

        backButton.setOnClickListener{
            finish()
        }


        citySpinner = findViewById(R.id.spinnerNewCustomerCity)
        address1 = findViewById(R.id.customerNewAddress1EditText)
        address2 = findViewById(R.id.customerNewAddress2EditText)
        barangay = findViewById(R.id.customerNewBarangayEditText)
        fetchCities()
        val buttonSave = findViewById<Button>(R.id.buttonEditCustomerProfileSave)

        buttonSave.setOnClickListener {
            insertAddress()
            val intent = Intent(this, CustomerProfilePageActivity::class.java)
            intent.putExtra("userId", userId)
            intent.putExtra("email", email)
            startActivity(intent)
        }


    }




    private fun insertAddress() {


        val address1Text = address1.text.toString()
        val address2Text = address2.text.toString()
        val barangayText = barangay.text.toString()

        val selectedCity = citySpinner.selectedItem as Cities
        val cityId = selectedCity.cityId

        val latLong =
            getLatLongFromAddress(this, address1Text, address2Text, barangayText, selectedCity.cityName!!)
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

        changeAddress(address1Text, address2Text, barangayText, cityId!!, longitude,latitude)


    }

    fun changeAddress(address1: String, address2: String, barangay: String, city: Int, longitude: Double?, latitude: Double?) {

        val userId = intent.getIntExtra("userId", 0)
        val usersInput = User(
            userId = userId,
            address1 = address1,
            address2 = address2,
            barangay = barangay,
            city = city,
            longitude = longitude,
            latitude = latitude)


        val user = UserInstance.retrofitBuilder
        user.patchUser(usersInput).enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if(response.isSuccessful){
                    Log.d("Changed Bookings", "True")
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                TODO("Not yet implemented")
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


    private fun fetchCities() {
        val call = CityInstance.retrofitBuilder
        call.getCities().enqueue(object : Callback<List<Cities>> {
            override fun onResponse(call: Call<List<Cities>>, response: Response<List<Cities>>) {
                if (response.isSuccessful) {
                    val citiesResponse = response.body()
                    if (citiesResponse != null) {
                        val adapter = CitySpinnerAdapter(this@CustomerEditAddressActivity, citiesResponse)
                        citySpinner.adapter = adapter


                    }
                } else {
                    Log.e(ContentValues.TAG, "Error fetching cities")
                }
            }

            override fun onFailure(call: Call<List<Cities>>, t: Throwable) {
                Log.e(ContentValues.TAG, "Error fetching cities", t)
            }
        })
    }



}
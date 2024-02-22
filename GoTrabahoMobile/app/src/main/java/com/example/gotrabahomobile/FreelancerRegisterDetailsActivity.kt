package com.example.gotrabahomobile

import android.app.DatePickerDialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import com.example.gotrabahomobile.Model.User
import com.example.gotrabahomobile.Remote.UserRemote.UserInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class FreelancerRegisterDetailsActivity : AppCompatActivity() {


    private var currentLatitude: Double? = null
    private var currentLongitude: Double? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_freelancer_register_details)

        //terms and conditions
        val txt_termsandconditions = findViewById<TextView>(R.id.textViewNoAccount)

        //birthdate
        val birthdateEditText = findViewById<EditText>(R.id.editTextFreelancerBirthdate)
        birthdateEditText.inputType = InputType.TYPE_NULL

        birthdateEditText.setOnClickListener {
            showDatePickerDialog(birthdateEditText)
        }

        val btnCustomer = findViewById<Button>(R.id.buttonFreelancerContinue1)

        btnCustomer.setOnClickListener {
            Log.d("RegisterActivity", "Before registerData")

            Log.d("RegisterActivity", "After registerData")
            insertCustomer()
            Log.d("hello", insertCustomer().toString())
        }

        //setBirthdayEditText() //date format
    }

    private fun showDatePickerDialog(birthdateEditText: EditText) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDayOfMonth ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(selectedYear, selectedMonth, selectedDayOfMonth)
                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                birthdateEditText.setText(dateFormat.format(selectedDate.time))
            },
            year,
            month,
            day
        )
        datePickerDialog.show()
    }


    //date format
//    fun setBirthdayEditText() {
//        val birthdateText = findViewById<EditText>(R.id.editTextFreelancerBirthdate)
//        birthdateText.addTextChangedListener(object : TextWatcher {
//
//            private var current = ""
//            private val yyyymmdd = "YYYYMMDD"
//            private val cal = Calendar.getInstance()
//
//             override fun onTextChanged(p0: CharSequence?, p3: Int, p2: Int, p1: Int) {
//                if (p0.toString() != current) {
//                    var clean = p0.toString().replace("[^\\d.]|\\.".toRegex(), "")
//                    val cleanC = current.replace("[^\\d.]|\\.".toRegex(), "")
//
//                    val cl = clean.length
//                    var sel = cl
//                    var i = 2
//                    while (i <= cl && i < 6) {
//                        sel++
//                        i += 2
//                    }
//                    //Fix for pressing delete next to a forward slash
//                    if (clean == cleanC) sel--
//
//                    if (clean.length < 8) {
//                        clean = clean + yyyymmdd.substring(clean.length)
//                    } else {
//                        //This part makes sure that when we finish entering numbers
//                        //the date is correct, fixing it otherwise
//                        var day = Integer.parseInt(clean.substring(0, 4))
//                        var mon = Integer.parseInt(clean.substring(4, 6))
//                        var year = Integer.parseInt(clean.substring(6, 8))
//
////                        mon = if (mon < 1) 1 else if (mon > 12) 12 else mon
////                        cal.set(Calendar.MONTH, mon - 1)
////                        year = if (year < 1900) 1900 else if (year > 2100) 2100 else year
////                        cal.set(Calendar.YEAR, year)
//                        // ^ first set year for the line below to work correctly
//                        //with leap years - otherwise, date e.g. 29/02/2012
//                        //would be automatically corrected to 28/02/2012
//
//                        day = if (day > cal.getActualMaximum(Calendar.DATE)) cal.getActualMaximum(Calendar.DATE) else day
//                        clean = String.format("%02d%02d%02d", day, mon, year)
//                    }
//
//                    clean = String.format("%s-%s-%s", clean.substring(0, 4),
//                        clean.substring(4, 6),
//                        clean.substring(6, 8))
//
//                    sel = if (sel < 0) 0 else sel
//                    current = clean
//                    birthdateText.setText(current)
//                    birthdateText.setSelection(if (sel < current.count()) sel else current.count())
//                }
//            }
//
//            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//            }
//
//            override fun afterTextChanged(p0: Editable) {
//
//            }
//        })
//    }

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

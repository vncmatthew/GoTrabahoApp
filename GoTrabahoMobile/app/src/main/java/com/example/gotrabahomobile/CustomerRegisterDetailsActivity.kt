package com.example.gotrabahomobile


import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.location.Address
import android.location.Geocoder
import android.os.Build
import android.os.Bundle
import android.text.InputType
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.gotrabahomobile.Model.User
import com.example.gotrabahomobile.Remote.UserRemote.UserInstance
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

import java.io.IOException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Calendar
import java.util.HashMap
import java.util.Locale

class CustomerRegisterDetailsActivity : AppCompatActivity() {

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var currentLatitude: Double? = null
    private var currentLongitude: Double? = null
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_register_details)
        auth = FirebaseAuth.getInstance()
        val birthdateEditText = findViewById<EditText>(R.id.editTextCustomerBirthdate)
        birthdateEditText.inputType = InputType.TYPE_NULL

        birthdateEditText.setOnClickListener {
            showDatePickerDialog(birthdateEditText)
        }

        val checkBox = findViewById<CheckBox>(R.id.checkBoxCustomerTNC)
        val freelancerSignUp = findViewById<TextView>(R.id.textViewCustomerSignUp)
        val signIn = findViewById<TextView>(R.id.textViewSignIn)
        val spannableString = SpannableString("I agree to the Terms and Conditions and Data Privacy")
        val spannableStringFSignUp = SpannableString("Looking for the Freelancer Sign Up page?")
        val spannableStringSignIn = SpannableString("Already have an account? Sign In")

        val fsignupSpan: ClickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                // Show the dialog when the text is clicked
                val intent = Intent(this@CustomerRegisterDetailsActivity, FreelancerRegisterDetailsActivity::class.java)
                startActivity(intent)
            }


            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.color = Color.BLUE // Set the text color
                ds.isUnderlineText = true // Set the text to be underlined
            }
        }

        val signInSpan: ClickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                // Show the dialog when the text is clicked
                val intent = Intent(this@CustomerRegisterDetailsActivity, LoginActivity::class.java)
                startActivity(intent)
            }


            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.color = Color.BLUE // Set the text color
                ds.isUnderlineText = true // Set the text to be underlined
            }
        }

        spannableStringFSignUp.setSpan(fsignupSpan, 16, 34, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        freelancerSignUp.text = spannableStringFSignUp
        freelancerSignUp.movementMethod = LinkMovementMethod.getInstance()

        spannableStringSignIn.setSpan(signInSpan, 25, 32, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        signIn.text = spannableStringSignIn
        signIn.movementMethod = LinkMovementMethod.getInstance()

        val clickableSpan: ClickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                // Show the dialog when the text is clicked
                showDialog()
            }


            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.color = Color.BLUE // Set the text color
                ds.isUnderlineText = true // Set the text to be underlined
            }
        }



        spannableString.setSpan(clickableSpan,  15,  52, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        checkBox.text = spannableString
        checkBox.movementMethod = LinkMovementMethod.getInstance() // Enable clickable text

        val btnCustomer = findViewById<Button>(R.id.buttonCustomerSignUp)

        btnCustomer.setOnClickListener {
            insertCustomer()

        }



    }

    private fun showDialog() {
        val dialogView = layoutInflater.inflate(R.layout.terms_and_conditions_dialog, null)

        val closeButton = dialogView.findViewById<ImageButton>(R.id.close_button)

        val alertDialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()

        closeButton.setOnClickListener {
            alertDialog.dismiss()
        }

        alertDialog.show()
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

        if (validateInputs(firstName, lastName, email, address1, address2, barangay, city, password, contactNumber)) {

            registerCustomer(userType, firstName, lastName, email, password, contactNumber, birthdate, address1,
                address2, barangay, city, longitude, latitude)
            val intent = Intent(this@CustomerRegisterDetailsActivity, LoginActivity::class.java)
            startActivity(intent)
        }



    }

    val phoneNumberPattern = "^0[0-9]{3}[0-9]{3}[0-9]{4}$".toRegex()

    fun isValidPhoneNumber(phoneNumber: String): Boolean {
        return phoneNumberPattern.matches(phoneNumber.trim())
    }
    fun validateInputs(
        firstName: String?,
        lastName: String?,
        email: String?,
        address1: String?,
        address2: String?,
        barangay: String?,
        city: String?,
        password: String?,
        contactNumber: String?
    ): Boolean {
        if (firstName.isNullOrEmpty() || lastName.isNullOrEmpty() || email.isNullOrEmpty() ||
            address1.isNullOrEmpty()  || barangay.isNullOrEmpty() ||
            city.isNullOrEmpty()  || contactNumber.isNullOrEmpty()
        ) {
            Toast.makeText(this, "All fields must be filled out", Toast.LENGTH_SHORT).show()
            return false
        }

        if (password?.length ?: 0 < 7) {
            Toast.makeText(this, "Password must be at least 7 characters long", Toast.LENGTH_SHORT).show()
            return false
        }

        if (!email.contains("@")) {
            Toast.makeText(this, "Invalid email format", Toast.LENGTH_SHORT).show()
            return false
        }

        if (!isValidPhoneNumber(contactNumber)) {
            Toast.makeText(this, "Invalid phone number format", Toast.LENGTH_SHORT).show()
            return false
        }

        val latLong = getLatLongFromAddress(this, address1, address2!!, barangay, city)
        if (latLong == null) {
            Toast.makeText(this, "Address not found", Toast.LENGTH_SHORT).show()
            return false
        }

        val (latitude, longitude) = latLong!!
        println("Latitude: $latitude, Longitude: $longitude")
        currentLongitude = longitude
        currentLatitude = latitude

        return true
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun registerCustomer(userType: Int, firstName: String, lastName: String, email: String, password: String, contactNumber: String?, birthdate: String, address1: String,
                                 address2: String, barangay: String, city: String, longitude: Double?, latitude: Double?) {

        val usersInput = User(
            userType = userType,
            firstName = firstName,
            lastName = lastName,
            email = email,
            password = password,
            accountMade = LocalDate.now().toString(),
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


                if (response.isSuccessful) {

                    userService.getEmail(email).enqueue(object:Callback<Int>{
                        override fun onResponse(call: Call<Int>, response: Response<Int>) {
                            if(response.isSuccessful) {
                                auth.createUserWithEmailAndPassword(email, password)
                                    .addOnCompleteListener(this@CustomerRegisterDetailsActivity) { task ->
                                        if (task.isSuccessful) {
                                            val user = auth.currentUser
                                            val userId = user?.uid
                                                ?: return@addOnCompleteListener // Safely handle null uid

                                            val databaseReference = FirebaseDatabase.getInstance()
                                                .getReference("UserFirebase").child(userId)
                                            val hashMap: HashMap<String, Any?> = HashMap()
                                            hashMap["userId"] = userId
                                            firstName?.let { hashMap["firstname"] = it }
                                            lastName?.let { hashMap["lastName"] = it }
                                            response.body().toString().let { hashMap["sqlId"] = it }

                                            databaseReference.setValue(hashMap).addOnCompleteListener(this@CustomerRegisterDetailsActivity){
                                                if (it.isSuccessful){
                                                    //open home activity
                                                    Log.d("CHECK", "Successfully Registered")
                                                }
                                            }
                                                }

                                    }
                            }
                        }

                        override fun onFailure(call: Call<Int>, t: Throwable) {
                            TODO("Not yet implemented")
                        }

                    })
                    Log.i(TAG, "The response is " + response.message());
                    Log.i(TAG, "The response is " + response.body());

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

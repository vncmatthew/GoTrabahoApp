package com.example.gotrabahomobile


import android.Manifest
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Address
import android.location.Geocoder
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.TextWatcher
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import com.example.gotrabahomobile.DTO.ReverseGeoCodeResponse
import com.example.gotrabahomobile.Helper.CitySpinnerAdapter
import com.example.gotrabahomobile.Model.Cities
import com.example.gotrabahomobile.Model.User
import com.example.gotrabahomobile.Remote.ArchiveRecordRemote.CityInstance
import com.example.gotrabahomobile.Remote.UserRemote.UserInstance
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import kotlinx.android.extensions.ContainerOptions
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

import java.io.IOException
import java.nio.charset.Charset
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Calendar
import java.util.HashMap
import java.util.Locale

class CustomerRegisterDetailsActivity : AppCompatActivity() {


    private var currentLatitude: Double? = null
    private var currentLongitude: Double? = null
    private lateinit var auth: FirebaseAuth
    private lateinit var citySpinner: Spinner

    private lateinit var locationManager: LocationManager
    private lateinit var locationListener: LocationListener
    private var MY_PERMISSIONS_REQUEST_LOCATION = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_register_details)
        phoneFocusListener()
        emailFocusListener()
        passwordFocusListener()
        firstNameFocusListener()
        auth = FirebaseAuth.getInstance()
        val birthdateEditText = findViewById<EditText>(R.id.birthdateEditText)
        birthdateEditText.inputType = InputType.TYPE_NULL

        birthdateEditText.setOnClickListener {
            showDatePickerDialog(birthdateEditText)
        }
        citySpinner = findViewById(R.id.spinnerCustomerCity)

        val checkBox = findViewById<CheckBox>(R.id.checkBoxCustomerTNC)
        val freelancerSignUp = findViewById<TextView>(R.id.textViewCustomerSignUp)
        val signIn = findViewById<TextView>(R.id.textViewSignIn)
        val spannableString =
            SpannableString("I agree to the Terms and Conditions and Data Privacy")
        val spannableStringFSignUp = SpannableString("Looking for the Freelancer Sign Up page?")
        val spannableStringSignIn = SpannableString("Already have an account? Sign In")
//
//        focusListener()


        val fsignupSpan: ClickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                // Show the dialog when the text is clicked
                val intent = Intent(
                    this@CustomerRegisterDetailsActivity,
                    FreelancerRegisterDetailsActivity::class.java
                )
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



        spannableString.setSpan(clickableSpan, 15, 52, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        checkBox.text = spannableString
        checkBox.movementMethod = LinkMovementMethod.getInstance() // Enable clickable text

        val btnCustomer = findViewById<Button>(R.id.buttonCustomerSignUp)

        btnCustomer.setOnClickListener {
            insertCustomer()

        }

        val jsonDecoder = Json { ignoreUnknownKeys = true }
        locationManager = (getSystemService(Context.LOCATION_SERVICE) as LocationManager?)!!
        var isFirstFetchCompleted = false
        val reverseGeoCodeResponseSerializer = serializer<ReverseGeoCodeResponse>()
        locationListener = object : LocationListener {
            override fun onLocationChanged(location: android.location.Location) {
                if (!isFirstFetchCompleted) {
                    val lat = 14.6100
                    val lon = 120.9893
                    val url =
                        "https://nominatim.openstreetmap.org/reverse?format=json&lat=$lat&lon=$lon&zoom=18&addressdetails=1"

                    lifecycleScope.launch { // Use viewModelScope if you're in a ViewModel
                        try {
                            val client = HttpClient()
                            val httpResponse: HttpResponse = client.get(url)
                            val responseBody: String =
                                httpResponse.bodyAsText(Charset.forName("UTF-8"))
                            val response: ReverseGeoCodeResponse = jsonDecoder.decodeFromString(
                                ReverseGeoCodeResponse.serializer(), responseBody
                            )
                            val address = response.display_name
                            println("Address: $address")
                            extractAddressComponents(address!!)
                            val cityName = extractAddressComponents(address!!)
                            if (cityName != null) {
                                fetchCities(cityName)
                            } else {
                                // Handle the case where the city name couldn't be extracted
                                Log.w(
                                    "extractAddressComponents",
                                    "Failed to extract city name from address"
                                )
                            }
                            isFirstFetchCompleted =
                                true // Set the flag to true after the first successful fetch
                        } catch (e: Exception) {
                            println("Error fetching address: ${e.message}")
                        }
                    }
                }
            }

            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}

            override fun onProviderEnabled(provider: String) {}

            override fun onProviderDisabled(provider: String) {}
        }

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Permission is not granted
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                // Show an explanation to the user asynchronously
            } else {
                // Request permissions
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ),
                    MY_PERMISSIONS_REQUEST_LOCATION
                )
            }
        } else {
            // Permission has already been granted
        }

        locationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            0,
            0f,
            locationListener
        )

    }

    fun extractAddressComponents(fullAddress: String): String? {

        val address1StartIndex = 0
        val address1EndIndex = fullAddress.indexOf(", ", address1StartIndex)
        val address2StartIndex = address1EndIndex + 2
        val address2EndIndex = fullAddress.indexOf(", ", address2StartIndex)
        val barangayStartIndex = address2EndIndex + 2 // Skip past the second comma and space
        val barangayEndIndex = fullAddress.indexOf(", ", barangayStartIndex)
        val cityStartIndex = barangayEndIndex + 2
        val cityEndIndex = fullAddress.length

        val address1 = fullAddress.substring(address1StartIndex, address1EndIndex)
        val address2 = fullAddress.substring(address2StartIndex, address2EndIndex)
        val barangay = fullAddress.substring(barangayStartIndex, barangayEndIndex)
        val cityChose = fullAddress.substring(cityStartIndex, cityEndIndex)

        Log.d("Address 1:", address1)
        Log.d("Address 2:", address2)
        Log.d("Barangay:", barangay)
        Log.d("City:", cityChose)
        val address1Text = findViewById<EditText>(R.id.address1EditText)
        val address2Text = findViewById<EditText>(R.id.address2EditText)
        val barangayText = findViewById<EditText>(R.id.barangayEditText)

        address1Text.text = Editable.Factory.getInstance().newEditable(address1)
        address2Text.text = Editable.Factory.getInstance().newEditable(address2)
        barangayText.text = Editable.Factory.getInstance().newEditable(barangay)
        return cityChose
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

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showDatePickerDialog(birthdateEditText: EditText) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val currentDate = LocalDate.now()
        val currentYear = currentDate.year

        val minAge = currentYear - 18

        val maxDateCalendar = Calendar.getInstance().apply {
            set(Calendar.YEAR, minAge)
            set(Calendar.MONTH, Calendar.DECEMBER)
            set(Calendar.DAY_OF_MONTH, 31)
        }

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

        datePickerDialog.datePicker.maxDate = maxDateCalendar.timeInMillis

        datePickerDialog.show()
    }

    private fun insertCustomer() {


        val firstNameText = findViewById<EditText>(R.id.firstNameEditText)
        val lastNameText = findViewById<EditText>(R.id.lastNameEditText)
        val birthdateText = findViewById<EditText>(R.id.birthdateEditText)
        val phoneNumberText = findViewById<EditText>(R.id.phoneNumberEditText)
        val emailText = findViewById<EditText>(R.id.emailEditText)
        val passwordText = findViewById<EditText>(R.id.passwordEditText)
        val confirmPassText = findViewById<EditText>(R.id.confirmPasswordEditText)
        val address1Text = findViewById<EditText>(R.id.address1EditText)
        val address2Text = findViewById<EditText>(R.id.address2EditText)
        val barangayText = findViewById<EditText>(R.id.barangayEditText)

        val userType = 1
        val selectedCity = citySpinner.selectedItem as Cities
        val cityId = selectedCity.cityId

        val firstName = firstNameText.text.toString()
        val lastName = lastNameText.text.toString()
        val birthdate = birthdateText.text.toString()
        val email = emailText.text.toString()
        val address1 = address1Text.text.toString()
        val address2 = address2Text.text.toString()
        val barangay = barangayText.text.toString()

        val password = passwordText.text.toString()
        val confirmPass = confirmPassText.text.toString()
        val contactNumber = phoneNumberText.text.toString()
        val latLong =
            getLatLongFromAddress(this, address1, address2, barangay, selectedCity.cityName!!)
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

            registerCustomer(
                userType, firstName, lastName, email, password, contactNumber, birthdate, address1,
                address2, barangay, cityId!!, longitude, latitude
            )
            val intent = Intent(this@CustomerRegisterDetailsActivity, LoginActivity::class.java)
            startActivity(intent)



    }



    private fun firstNameFocusListener() {
        val firstNameText = findViewById<EditText>(R.id.firstNameEditText)
        val firstNameContainer = findViewById<TextInputLayout>(R.id.firstNameContainer)

        val lastNameText = findViewById<EditText>(R.id.lastNameEditText)
        val lastNameContainer = findViewById<TextInputLayout>(R.id.lastNameContainer)

        val birthdateText = findViewById<EditText>(R.id.birthdateEditText)
        val birthdateContainer = findViewById<TextInputLayout>(R.id.birthdateContainer)

        val address1Text = findViewById<EditText>(R.id.address1EditText)
        val address1Container = findViewById<TextInputLayout>(R.id.address1Container)

        val barangayText = findViewById<EditText>(R.id.barangayEditText)
        val barangayContainer = findViewById<TextInputLayout>(R.id.barangayContainer)

        firstNameText.setOnFocusChangeListener { _, focused ->
            if(!focused)
            {
                firstNameContainer.helperText = validFirstName()
            }
        }
        lastNameText.setOnFocusChangeListener { _, focused ->
            if(!focused)
            {
                lastNameContainer.helperText = validLastName()
            }
        }
        birthdateText.setOnFocusChangeListener { _, focused ->
            if(!focused)
            {
                birthdateContainer.helperText = validBirthdate()
            }
        }
        address1Text.setOnFocusChangeListener { _, focused ->
            if(!focused)
            {
                address1Container.helperText = validAddress1()
            }
        }
        barangayText.setOnFocusChangeListener { _, focused ->
            if(!focused)
            {
                barangayContainer.helperText = validBarangay()
            }
        }
    }

    private fun validFirstName(): String?
    {
        val firstNameText = findViewById<EditText>(R.id.firstNameEditText).text.toString()
        val lastNameText = findViewById<EditText>(R.id.lastNameEditText).text.toString()
        if (firstNameText.isNullOrEmpty())
        {
            return "Required"
        }
        if (firstNameText.isNotEmpty())
        {
            return " "
        }
        return null
    }

    private fun validLastName(): String?
    {
        val lastNameText = findViewById<EditText>(R.id.lastNameEditText).text.toString()
        if (lastNameText.isNullOrEmpty())
        {
            return "Required"
        }
        if (lastNameText.isNotEmpty())
        {
            return " "
        }
        return null
    }

    private fun validAddress1(): String?
    {
        val address1Text = findViewById<EditText>(R.id.address1EditText).text.toString()
        if (address1Text.isNullOrEmpty())
        {
            return "Required"
        }
        if (address1Text.isNotEmpty())
        {
            return " "
        }
        return null
    }

    private fun validBarangay(): String?
    {
        val barangayText = findViewById<EditText>(R.id.barangayEditText).text.toString()
        if (barangayText.isNullOrEmpty())
        {
            return "Required"
        }
        if (barangayText.isNotEmpty())
        {
            return " "
        }
        return null
    }

    private fun validBirthdate(): String?
    {
        val bdayText = findViewById<EditText>(R.id.birthdateEditText).text.toString()
        if (bdayText.isNullOrEmpty())
        {
            return "Required"
        }
        if (bdayText.isNotEmpty())
        {
            return " "
        }
        return null
    }


    private fun phoneFocusListener() {
        val phoneNumberText = findViewById<EditText>(R.id.phoneNumberEditText)
        val phoneNumberContainer = findViewById<TextInputLayout>(R.id.phoneNumberContainer)

        phoneNumberText.setOnFocusChangeListener { _, focused ->
            if(!focused)
            {
                phoneNumberContainer.helperText = validPhone()
            }
        }
    }

    private fun validPhone(): String?
    {
        val phoneNumber = findViewById<EditText>(R.id.phoneNumberEditText).text.toString()
        if (phoneNumber.isNullOrEmpty())
        {
            return "Required"
        }
        if (!phoneNumber.matches("^9[0-9]{2}[0-9]{3}[0-9]{4}$".toRegex()))
        {
            return "Must be a Valid Phone Number"
        }
        if (phoneNumber.length != 10)
        {
            return "Must be 10 digits"
        }

        return null
    }

    private fun emailFocusListener() {
        val emailText = findViewById<EditText>(R.id.emailEditText)
        val emailContainer = findViewById<TextInputLayout>(R.id.emailContainer)

        emailText.setOnFocusChangeListener { _, focused ->
            if(!focused)
            {
                emailContainer.helperText = validEmail()
            }
        }
    }

    private fun validEmail(): String?
    {
        val email = findViewById<EditText>(R.id.emailEditText).text.toString()
        if (email.isNullOrEmpty())
        {
            return "Required"
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            return "Invalid Email Address"
        }
        return null
    }

    private fun passwordFocusListener() {
        val passwordText = findViewById<EditText>(R.id.passwordEditText)
        val passwordContainer = findViewById<TextInputLayout>(R.id.passwordContainer)

        val confirmPasswordText = findViewById<EditText>(R.id.confirmPasswordEditText)
        val confirmPasswordContainer = findViewById<TextInputLayout>(R.id.confirmPasswordContainer)

        passwordText.setOnFocusChangeListener { _, focused ->
            if(!focused)
            {
                passwordContainer.helperText = validPassword()
            }
        }

        confirmPasswordText.setOnFocusChangeListener { _, focused ->
            if(!focused)
            {
                confirmPasswordContainer.helperText = validConfirmPassword()
            }
        }




    }

    private fun validPassword(): String?
    {
        val password = findViewById<EditText>(R.id.passwordEditText).text.toString()
        if (password.isNullOrEmpty())
        {
            return "Required"
        }
        if (password.length < 7)
        {
            return "Minimum of 7 Character Password"
        }
        return null
    }

    private fun validConfirmPassword(): String?
    {
        val password = findViewById<EditText>(R.id.passwordEditText).text.toString()
        val confirmPassword = findViewById<EditText>(R.id.confirmPasswordEditText).text.toString()
        if (confirmPassword.isNullOrEmpty())
        {
            return "Required"
        }
        if (confirmPassword.length < 7)
        {
            return "Minimum of 7 Character Password"
        }
        if (password != confirmPassword)
        {
            return "Confirm Password does not match"
        }
        return null
    }

//    fun validateInputs(
//        firstName: String?,
//        lastName: String?,
//        email: String?,
//        address1: String?,
//        address2: String?,
//        barangay: String?,
//        city: String?,
//        password: String?,
//        confirmPass: String?,
//        contactNumber: String?
//    ): Boolean {
//        if (firstName.isNullOrEmpty() || lastName.isNullOrEmpty() || email.isNullOrEmpty() ||
//            address1.isNullOrEmpty()  || barangay.isNullOrEmpty() ||
//            city.isNullOrEmpty()  || contactNumber.isNullOrEmpty()
//        ) {
//            Toast.makeText(this, "All fields must be filled out", Toast.LENGTH_SHORT).show()
//            return false
//        }
//
//        if (password?.length ?: 0 < 7) {
//            Toast.makeText(this, "Password must be at least 7 characters long", Toast.LENGTH_SHORT).show()
//            return false
//        }
//
//        if (password?.equals(confirmPass) == false){
//            Toast.makeText(this, "Confirm Password and Password are not the same", Toast.LENGTH_SHORT).show()
//            return false
//        }
//
//        if (!email.contains("@")) {
//            Toast.makeText(this, "Invalid email format", Toast.LENGTH_SHORT).show()
//            return false
//        }
//
//        if (!isValidPhoneNumber(contactNumber)) {
//            Toast.makeText(this, "Invalid phone number format", Toast.LENGTH_SHORT).show()
//            return false
//        }
//
//        val latLong = getLatLongFromAddress(this, address1, address2!!, barangay, city)
//        if (latLong == null) {
//            Toast.makeText(this, "Address not found", Toast.LENGTH_SHORT).show()
//            return false
//        }
//
//        val (latitude, longitude) = latLong!!
//        println("Latitude: $latitude, Longitude: $longitude")
//        currentLongitude = longitude
//        currentLatitude = latitude
//
//        return true
//    }

    private fun fetchCities(cityName: String?) {
        val call = CityInstance.retrofitBuilder
        call.getCities().enqueue(object : Callback<List<Cities>> {
            override fun onResponse(call: Call<List<Cities>>, response: Response<List<Cities>>) {
                if (response.isSuccessful) {
                    val citiesResponse = response.body()
                    if (citiesResponse != null) {
                        val adapter = CitySpinnerAdapter(this@CustomerRegisterDetailsActivity, citiesResponse)
                        citySpinner.adapter = adapter


                        val matchingCityIndex = citiesResponse.indexOfFirst {cityName!!.contains(it.cityName!!)  ?: false }


                        if (matchingCityIndex != -1) {
                            citySpinner.setSelection(matchingCityIndex)
                        } else {

                            Log.w("fetchCities", "No city found that contains '${cityName ?: ""}'")
                        }
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
    @RequiresApi(Build.VERSION_CODES.O)
    private fun registerCustomer(userType: Int, firstName: String, lastName: String, email: String, password: String, contactNumber: String?, birthdate: String, address1: String,
                                 address2: String, barangay: String, city: Int, longitude: Double?, latitude: Double?) {

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

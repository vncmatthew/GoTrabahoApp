package com.example.gotrabahomobile

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.gotrabahomobile.Model.Freelancer
import com.example.gotrabahomobile.Model.Login
import com.example.gotrabahomobile.Model.User
import com.example.gotrabahomobile.Remote.FreelancerRemote.FreelancerInstance
import com.example.gotrabahomobile.Remote.UserRemote.UserInstance
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

import kotlinx.serialization.json.Json
import org.w3c.dom.Text

class LoginActivity : AppCompatActivity() {


    private lateinit var locationManager: LocationManager
    private lateinit var locationListener: LocationListener
    private lateinit var auth: FirebaseAuth
    private var MY_PERMISSIONS_REQUEST_LOCATION = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        FirebaseApp.initializeApp(this@LoginActivity)
        auth = FirebaseAuth.getInstance()
        val btn_login = findViewById<Button>(R.id.buttonLogin)
        val txt_signup = findViewById<TextView>(R.id.textViewNoAccount)
        val jsonDecoder = Json { ignoreUnknownKeys = true }
        val text = "Don't have an account? Sign Up"
        val spanString = SpannableString(text)

//        val logInWithOTPTextView = findViewById<TextView>(R.id.textViewLogInWithOTP)
//        val spanLogInWithOTP = SpannableString("Log In with OTP")
//        val logInSpan: ClickableSpan = object : ClickableSpan() {
//            override fun onClick(widget: View) {
//                // Show the dialog when the text is clicked
//                val intent = Intent(this@LoginActivity, OTPLogInActivity::class.java)
//                startActivity(intent)
//            }
//        }
//        spanLogInWithOTP.setSpan(logInSpan, 0, 15, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
//        logInWithOTPTextView.text = spanLogInWithOTP
//        logInWithOTPTextView.movementMethod = LinkMovementMethod.getInstance()

        checkLocationPermissions()


        val signupText = object: ClickableSpan() {
            override fun onClick(widget: View) {
                startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
            }
        }


        spanString.setSpan(signupText,23,30, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        txt_signup.text = spanString
        txt_signup.movementMethod = LinkMovementMethod.getInstance()
        btn_login.setOnClickListener{loginUser()}


        passwordFocusListener()
        emailFocusListener()
        btn_login.setOnClickListener{
        loginUser()
        }


    }


    private fun checkLocationPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {
            // Permission is not granted
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Show an explanation to the user asynchronously
            } else {
                // Request permissions
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), MY_PERMISSIONS_REQUEST_LOCATION)
            }
        } else {
            // Permission has already been granted
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_LOCATION -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // permission was granted, yay!
                } else {
                    // permission denied, boo!
                }
                return
            }
            else -> {
                // Ignore all other requests.
            }
        }
    }


    private fun loginUser() {
        val edit_Email = findViewById<EditText>(R.id.emailEditText)
        val edit_Pass = findViewById<EditText>(R.id.passwordEditText)
        val passwordContainer = findViewById<TextInputLayout>(R.id.passwordContainer)

        val email = edit_Email.text.toString()
        val pass = edit_Pass.text.toString()

        if (email.isEmpty() || pass.isEmpty()) {
            passwordContainer.helperText = "The email and password fields are required"
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(
                this@LoginActivity,
                "The email is in the incorrect format",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        val login = Login(email = email, password = pass)
        val login1 = UserInstance.retrofitBuilder

        login1.loginUser(login).enqueue(object : Callback<User> {
        override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    val user = response.body()
                    if (user != null) {
                        val userID = user.userId
                        val firstName = user.firstName
                        val lastName = user.lastName
                        val userType = user.userType
                        val longitude = user.longitude
                        val latitude = user.latitude
                        val fullName = "$firstName $lastName"
                        if (userID != null) {
                        LoginUser(email, pass ,firstName,lastName, fullName, userID.toString(), userType, longitude, latitude, userID)
                        }else {
                            Toast.makeText(this@LoginActivity, "Connection Error", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                else {
                    Toast.makeText(this@LoginActivity, "The email and/or password is incorrect", Toast.LENGTH_SHORT).show()
                    passwordContainer.helperText = "The email and/or password is incorrect"
                }
        }
            override fun onFailure(call: Call<User>, t: Throwable) {
                Log.d ("MainActivity", "Registration failed: ")
            }
        })
    }


    private fun LoginUser(email:String,password:String,firstName:String?, lastName:String?, fullName:String?, sqlId: String?, userType: Int?,
                             longitude: Double?, latitude: Double?, identification: Int?){
                    auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this) {
                            if (it.isSuccessful) {

                                if(userType == 1) {
                                    val intent =
                                        Intent(this@LoginActivity, CustomerMainActivity::class.java)
                                    intent.putExtra("sqlId", sqlId)
                                    intent.putExtra("userId", identification)
                                    intent.putExtra("email", email)
                                    intent.putExtra("password", password)
                                    intent.putExtra("firstName", firstName)
                                    intent.putExtra("lastName", lastName)
                                    intent.putExtra("fullName", fullName)
                                    intent.putExtra("longitude", longitude)
                                    intent.putExtra("latitude", latitude)
                                    intent.putExtra("userType", userType)

                                    startActivity(intent)
                                }
                                else if(userType == 2) {

                                    val call = FreelancerInstance.retrofitBuilder
                                    call.getFreelancerId(identification!!.toInt()).enqueue(object: Callback<Freelancer>{
                                        override fun onResponse(call: Call<Freelancer>, response: Response<Freelancer>) {

                                            if(response.isSuccessful){
                                                val freelancerId = response.body()
                                                var bro = freelancerId?.verificationStatus

                                                if (bro == 1 || bro == 3) {
                                                    Log.d("Identification@", "${identification}")
                                                    val intent =
                                                        Intent(this@LoginActivity, FreelancerMainActivity::class.java)
                                                    intent.putExtra("userId", identification)
                                                    intent.putExtra("freelancerId", freelancerId?.freelancerId)
                                                    intent.putExtra("firstName", firstName)
                                                    intent.putExtra("lastName", lastName)
                                                    intent.putExtra("fullName", fullName)
                                                    intent.putExtra("email", email)
                                                    intent.putExtra("password", password)
                                                    intent.putExtra("userType", userType)
                                                    startActivity(intent)

                                                }
                                                else if(bro == 0){
                                                     val intent =
                                                     Intent(this@LoginActivity, ApplicationConfirmationActivity::class.java)
                                                        intent.putExtra("userID", sqlId)
                                                        intent.putExtra("freelancerId", freelancerId?.freelancerId)
                                                        intent.putExtra("firstName", firstName)
                                                        intent.putExtra("lastName", lastName)
                                                        intent.putExtra("fullName", fullName)
                                                        intent.putExtra("email", email)
                                                        intent.putExtra("userType", userType)
                                                    startActivity(intent)
                                                }
                                            }
                                        }

                                        override fun onFailure(call: Call<Freelancer>, t: Throwable) {
                                            TODO("Not yet implemented")
                                        }
                                    })
                                }
                            } else {
                                Toast.makeText(
                                    applicationContext,
                                    "email or password invalid",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
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
            return "Email Required"
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

        passwordText.setOnFocusChangeListener { _, focused ->
            if(!focused)
            {
                passwordContainer.helperText = validPassword()
            }
        }




    }

    private fun validPassword(): String?
    {
        val password = findViewById<EditText>(R.id.passwordEditText).text.toString()
        if (password.isNullOrEmpty())
        {
            return "Password Required"
        }
        if (password.length < 7)
        {
            return "Minimum of 7 Character Password"
        }
        return null
    }

    private fun wrongPassword(): String?
    {
        val passwordText = findViewById<EditText>(R.id.passwordEditText)
        val passwordContainer = findViewById<TextInputLayout>(R.id.passwordContainer)

        return "Password or Email is wrong"
    }

}









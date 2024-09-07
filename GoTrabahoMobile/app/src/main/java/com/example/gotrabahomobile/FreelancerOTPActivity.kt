package com.example.gotrabahomobile

import android.content.ContentValues
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.gotrabahomobile.Model.User
import com.example.gotrabahomobile.Remote.UserRemote.UserInstance
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import papaya.`in`.sendmail.SendMail
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate
import java.util.HashMap

class FreelancerOTPActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    var random : Int=0
    var email : String=""
    var password : String=""
    var userType : Int = 0
    var userId : Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_freelancer_otpactivity)
        auth = FirebaseAuth.getInstance()

        email = intent.getStringExtra("email") ?: ""
        password = intent.getStringExtra("password") ?: ""
        userType = intent.getIntExtra("userType", 0)

        generateOTP()
        val otpEditText = findViewById<EditText>(R.id.otpEditText)
        val otpContainer = findViewById<TextInputLayout>(R.id.otpContainer)

        val resendOTP = findViewById<Button>(R.id.resendOTPButton)
        val verifyOTP = findViewById<Button>(R.id.verifyOTPButton)

        resendOTP.setOnClickListener {
            var email = intent.getStringExtra("email")

            var mail= SendMail("yorushushok@gmail.com","paiirzatmdtjsukk",email,"Login Signup app's OTP",
                "Your OTP is -> $random")
            mail.execute()
            Toast.makeText(this@FreelancerOTPActivity, "We sent an OTP to the email associated with your account, please check your email to verify with OTP", Toast.LENGTH_SHORT).show()
        }

        verifyOTP.setOnClickListener{

            val OTPCode = otpEditText.text.toString()
            var password = intent.getStringExtra("password")
            if(OTPCode.equals(random.toString()))

            {
                var intent= Intent(this@FreelancerOTPActivity,FreelancerIdentityVerificationActivity::class.java)
                intent.putExtra("email", email)
                startActivity(intent)
            }
            else {
                Toast.makeText(this@FreelancerOTPActivity, "The OTP you have entered is wrong", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun generateOTP(){
        random = (100000..999999).random()
        var email = intent.getStringExtra("email")
        var mail=SendMail("yorushushok@gmail.com","paiirzatmdtjsukk",email,"GoTrabaho Verification OTP",
            "Your OTP is -> $random")
        mail.execute()
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

                    userService.getEmail(email).enqueue(object: Callback<Int> {
                        override fun onResponse(call: Call<Int>, response: Response<Int>) {
                            if(response.isSuccessful) {
                                Log.d("First Name", "first anme: $firstName")
                                auth.createUserWithEmailAndPassword(email, password)
                                    .addOnCompleteListener(this@FreelancerOTPActivity) { task ->
                                        if (task.isSuccessful) {
                                            Log.d("First Name", "first anme: $firstName")
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
                                            Log.d("CHECK", "HashMap: $hashMap")

                                            databaseReference.setValue(hashMap).addOnCompleteListener(this@FreelancerOTPActivity) { task ->
                                                if (task.isSuccessful) {
                                                    Log.d("CHECK", "Successfully Registered")
                                                    // Open home activity or proceed further
                                                } else {
                                                    Log.e("FirebaseDBError", "Failed to insert data", task.exception)
                                                    Toast.makeText(this@FreelancerOTPActivity, "Failed to register user data", Toast.LENGTH_SHORT).show()
                                                }
                                            }
                                        }
                                        else{
                                            task.exception?.let { exception ->
                                                Log.e("FirebaseAuthError", "Failed to create user", exception)
                                            }
                                        }


                                    }
                            }
                        }

                        override fun onFailure(call: Call<Int>, t: Throwable) {
                            TODO("Not yet implemented")
                        }

                    })
                    Log.i(ContentValues.TAG, "The response is " + response.message());
                    Log.i(ContentValues.TAG, "The response is " + response.body());

                    val users = response.body()
                    if(users != null){
                        Toast.makeText(this@FreelancerOTPActivity, "Register", Toast.LENGTH_SHORT).show()
                    }else {
                        Toast.makeText(this@FreelancerOTPActivity, "User ID is null", Toast.LENGTH_SHORT).show()
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
}
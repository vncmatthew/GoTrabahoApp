package com.example.gotrabahomobile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import com.example.gotrabahomobile.Model.Freelancer
import com.example.gotrabahomobile.Model.User
import com.example.gotrabahomobile.Remote.UserRemote.UserInstance
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FreelancerProfilePageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_freelancer_profile_page)



        val tvCPass = findViewById<EditText>(R.id.newPasswordEditText)
        val tvPass = findViewById<EditText>(R.id.oldPasswordEditText)

        val cancelButton: Button = findViewById(R.id.buttonFreelancerEditProfileCancel)
        cancelButton.setOnClickListener {
            finish()
        }


        val saveProfileButton: Button = findViewById(R.id.buttonFreelancerEditProfileSave)
        saveProfileButton.setOnClickListener{

            val password = tvCPass.text?.toString()
            val OldPassword = tvPass.text?.toString()
            val userId = intent.getIntExtra("userId",0)
            val updateUser = User(
                userId = userId,
            )
            updateUser(updateUser, password!!,OldPassword!!)

        }
    }

    private fun updateUser(updateUser: User, password: String, oldpassword:String){
        val userId = intent.getIntExtra("userId",0)
        val email = intent.getStringExtra("email")
        val call = UserInstance.retrofitBuilder
        call.patchUser( updateUser).enqueue(object: Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    val user = response.body()
                    if (user != null) {
                        Log.d("Successf", "User updated successfully: $user")
                    } else {
                        Log.d("Successf", "User updated successfully, but response body is null")
                    }

                    val call = UserInstance.retrofitBuilder
                    val userF: FirebaseUser? = FirebaseAuth.getInstance().currentUser
                    val credential: AuthCredential = EmailAuthProvider.getCredential(email!!, oldpassword)
                    userF?.reauthenticate(credential)?.addOnCompleteListener { task ->
                        userF?.updatePassword(password)?.addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Log.d("Successful", "Password Successfully Changed in Firebase")
                            } else {
                                // Failed to update password
                            }
                        }
                    }
                    call.changePassword(email!!, password).enqueue(object: Callback<ResponseBody>{
                        override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                            if(response.isSuccessful)
                            {
                                FirebaseAuth.getInstance().signOut();
                                val intent = Intent(this@FreelancerProfilePageActivity, LoginActivity::class.java)
                                intent.putExtra("userId", userId)
                                startActivity(intent)
                                Toast.makeText(
                                    applicationContext,
                                    "Successfully Changed Password",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                            TODO("Not yet implemented")
                        }

                    })
                } else {
                    Log.d("Tag", "Response is not successful. Code: ${response.code()}, Message: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })

    }
}
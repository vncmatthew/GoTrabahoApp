package com.example.gotrabahomobile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import com.example.gotrabahomobile.Model.User
import com.example.gotrabahomobile.Remote.UserRemote.UserInstance
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import okhttp3.ResponseBody
import org.w3c.dom.Text
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CustomerProfilePageActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_profile_page)

        val cancelButton: Button = findViewById(R.id.buttonEditCustomerProfileCancel)
        cancelButton.setOnClickListener{
            finish()
        }
        val btnSaveProfile = findViewById<Button>(R.id.buttonEditCustomerProfileSave)
        val tvCPass = findViewById<EditText>(R.id.editTextProfileCustomerConfirmNewPassword)
        val tvPass = findViewById<EditText>(R.id.editTextProfileCustomerNewPassword)


        btnSaveProfile.setOnClickListener {

            val pass = tvCPass.text?.toString()
            val oldpass = tvPass.text?.toString()

            val userId = intent.getIntExtra("userId",0)
            val updateUser = User(
                userId = userId,

            )
            updateUser(updateUser, pass!!, oldpass!!)

        }

    }



    private fun updateUser(updateUser: User, password: String, oldpassword: String){
        val userId = intent.getIntExtra("userId",0)
        val email = intent.getStringExtra("email")
        val call = UserInstance.retrofitBuilder
        call.patchUser(userId, updateUser).enqueue(object: Callback<User>{
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

                    if (email != null && oldpassword != null) {
                        val credential: AuthCredential = EmailAuthProvider.getCredential(email, oldpassword)
                        userF?.reauthenticate(credential)?.addOnCompleteListener { reauthTask ->
                            if (reauthTask.isSuccessful) {
                                userF?.updatePassword(password)?.addOnCompleteListener { updateTask ->
                                    if (updateTask.isSuccessful) {
                                        Log.d("FirebaseAuth", "Password Successfully Changed in Firebase")
                                    } else {
                                        Log.e("FirebaseAuth", "Failed to update password", updateTask.exception)
                                    }
                                }
                            } else {
                                Log.e("FirebaseAuth", "Reauthentication failed", reauthTask.exception)
                            }
                        }
                    } else {
                        Log.e("FirebaseAuth", "Email or old password is null")
                    }
                    call.changePassword(email!!, password).enqueue(object: Callback<ResponseBody>{
                        override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                            if(response.isSuccessful)
                            {
                                FirebaseAuth.getInstance().signOut();
                                val intent = Intent(this@CustomerProfilePageActivity, LoginActivity::class.java)
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
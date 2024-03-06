package com.example.gotrabahomobile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.gotrabahomobile.DTO.PaymentDTO
import com.example.gotrabahomobile.Model.User
import com.example.gotrabahomobile.Remote.PaymentRemote.PaymentInstance
import com.example.gotrabahomobile.Remote.UserRemote.UserInstance
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PaymentActivity : AppCompatActivity() {
    private lateinit var input_email:EditText
    private lateinit var btn_pay: Button
    private lateinit var email: String
    var customerEmail: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        val userID = intent.getIntExtra("userID", 0)
        getEmail(userID)

        btn_pay = findViewById(R.id.buttonPayWithMaya)
        btn_pay.setOnClickListener {
            paymentBooking()

        }
    }

    private fun paymentBooking() {
        val selectedDateTime = intent.getStringExtra("selectedDateTime")
        val userID = intent.getIntExtra("userID", 0)
        val customerID = intent.getIntExtra("customerID", 0)
        val customerName = intent.getStringExtra("customerName")
        val emailBook = findViewById<EditText>(R.id.editTextEmailForInvoice)
        val invoiceLinkText = findViewById<TextView>(R.id.textViewInvoiceLink)

        val Paymentservice = PaymentInstance.retrofitBuilder


        val paymentInfo = PaymentDTO(email = emailBook.toString())

        Paymentservice.paymentBook(paymentInfo.toString()).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    val invoiceResponse = response.body()
                    //                    val intent = Intent(this@PaymentActivity, ConfirmActivity::class.java)
//                    intent.putExtra("BUTTON_STATE","payment_success")
//                    intent.putExtra("userID", userID)
//                    intent.putExtra("customerID", customerID)
//                    intent.putExtra("customerName", customerName)
//                    intent.putExtra("selectedDateTime", selectedDateTime)
//                    startActivity(intent)
                    val invoiceLink = response.body()?.toString()
//                    // Display the invoice link to the user
                    invoiceLinkText.text = invoiceLink
                } else {
                    // Handle error
                    Toast.makeText(this@PaymentActivity, "Failed to generate invoice.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                // Handle failure
                Toast.makeText(this@PaymentActivity, "Network error.", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun getEmail(userId: Int) {
        val call = UserInstance.retrofitBuilder

        call.getUser(userId).enqueue(object: Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) { val data = response.body()
                if (response.isSuccessful) {
                        email = data?.email.toString()
                    }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                Log.d("Failed to get email", "${t}" )
            }

        })

    }
}

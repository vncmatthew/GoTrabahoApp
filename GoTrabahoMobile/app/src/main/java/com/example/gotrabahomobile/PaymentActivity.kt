package com.example.gotrabahomobile

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
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
    private lateinit var btn_pay: Button
    private lateinit var userEmail: String
    private lateinit var textInvoiceLink: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        textInvoiceLink = findViewById(R.id.textViewInvoiceLink)
        btn_pay = findViewById(R.id.buttonPayWithMaya)
        userEmail = intent.getStringExtra("email").toString()
        //display user email
        Log.d("PaymentActivity", "Bundle: $userEmail")

        val backButton: ImageButton = findViewById(R.id.back_buttonNavbar)
        backButton.setOnClickListener{
            finish()
        }


        btn_pay.setOnClickListener {
            paymentBooking()
        }

    }



    private fun paymentBooking() {

        val Paymentservice = PaymentInstance.retrofitBuilder
        val paymentInfo = PaymentDTO(email = userEmail)

        Paymentservice.paymentBook(paymentInfo, 2).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    val invoiceResponse = response.body()

                    setupInvoiceLink(textInvoiceLink, invoiceResponse?.string())
                    Toast.makeText(this@PaymentActivity, "Invoice URL has been generated, please click the 'Invoice Link' to copy it to your clipboard", Toast.LENGTH_LONG).show()
                    Log.d("Invoice URL", invoiceResponse?.string() ?: "Example invoice link")
                    Log.d("Invoice URL", invoiceResponse?.toString() ?: "Example invoice link")
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

    private fun setupInvoiceLink(textView: TextView, url: String?) {
        val spannableString = SpannableString("Invoice Link")
        val clickableSpan: ClickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {

                val clipboard =
                    widget.context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

                val clip = ClipData.newPlainText("Invoice Link", url)

                clipboard.setPrimaryClip(clip)
                Log.d("Invoice URL", "Invoice URL has been copied to clipboard")
                Toast.makeText(this@PaymentActivity, "Text copied to clipboard", Toast.LENGTH_SHORT).show()
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.color = Color.BLUE // Set the text color
                ds.isUnderlineText = true

            }
        }

        spannableString.setSpan(clickableSpan, 0, spannableString.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        textInvoiceLink.text = spannableString
        textInvoiceLink.movementMethod = LinkMovementMethod.getInstance()
    }
}

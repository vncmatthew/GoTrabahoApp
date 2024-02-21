package com.example.gotrabahomobile

import android.Manifest
import android.content.ContentValues
import android.content.CursorLoader
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.gotrabahomobile.Model.Freelancer
import com.example.gotrabahomobile.Remote.FreelancerRemote.FreelancerInstance
import com.example.gotrabahomobile.Remote.UserRemote.UserInstance
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.IOException

class FreelancerJobVerificationActivity : AppCompatActivity() {

    private var selectedImageUri: Uri? = null
    private lateinit var ImgViewProof: ImageView
    private lateinit var btnProof: Button
    private lateinit var ImgViewCertificate: ImageView
    private lateinit var btnCertificate: Button
    var ImagePathProof: String? = null
    var ImagePathCertificate: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_freelancer_job_verification)

        ImgViewCertificate = findViewById(R.id.imageViewCertificate)
        ImgViewProof = findViewById(R.id.imageViewProofOfWork)
        btnProof = findViewById(R.id.buttonUploadProofOfWork)
        btnCertificate = findViewById(R.id.buttonUploadCertificate)

        btnProof.setOnClickListener {
            openImagePicker()
        }
        btnCertificate.setOnClickListener {
            openImagePicker()
        }


    }

    private fun openImagePicker() {
        Intent(Intent.ACTION_PICK).also {
            it.type = "image/*"
            val mimeTypes = arrayOf("image/jpeg", "image/png")
            it.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
            startActivityForResult(it, FreelancerIdentityVerificationActivity.REQUEST_CODE_IMAGE)
        }
    }

    private fun getRealPathFromURI(contentUri: Uri?): String? {
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        val loader =
            CursorLoader(applicationContext, contentUri, proj, null, null, null)
        val cursor: Cursor? = loader.loadInBackground()
        cursor?.let {
            val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            if (cursor.moveToFirst()) {
                val result = cursor.getString(columnIndex)
                cursor.close()
                return result
            }
        }
        return null
    }

    private fun getEmail() {
        //val email = intent.getStringExtra("email")
        val email = "hello@gmail.com"
        val service = UserInstance.retrofitBuilder

        if (email != null) {
            service.getEmail(email).enqueue(object : Callback<Int> {
                @RequiresApi(Build.VERSION_CODES.O)
                override fun onResponse(call: Call<Int>, response: Response<Int>) {
                    if (response.isSuccessful) {

                        val userId = response.body()
                        if(userId != null){
                            val service = FreelancerInstance.retrofitBuilder

                            val idType = if (selectedItem == "ID TYPE") {
                                null
                            } else {
                                when (selectedItem) {
                                    "Driver's License" ->  1
                                    "SSS" ->  2
                                    "Passport" ->  3
                                    "National Id" ->  4
                                    else -> null
                                }
                            }
                            val nameProof = email + "Proof.jpg"
                            val nameCertificate = email + "Certificate.jpg"
                            val fileProof = File(ImagePathProof)
                            val fileCertificate = File(ImagePathCertificate)
                            val dateSent = LocalDate.now()

                            val requestBodyP = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), fileProof)
                            val imagePartCertificate = MultipartBody.Part.createFormData("imageFile", nameCertificate, requestBodyP)

                            val requestBodyC = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), fileCertificate)
                            val imagePartProof = MultipartBody.Part.createFormData("imageFile", nameProof, requestBodyC)
                            val governmentId = name
                            val verificationStatus = false
                            val totalIncome = 0

                            registerCertificate(13, nameCertificate, dateSent)
                            Log.d("TEST", "${call.toString()}")

                            registerProof(13, nameProof, dateSent)

                            service.insertCertificate(imagePartCertificate)
                                .enqueue(object : Callback<Freelancer> {
                                    override fun onResponse(call: Call<Freelancer>, response: Response<Freelancer>) {

                                        if (response.isSuccessful) {

                                        } else {
                                            // Handle the error response
                                            val errorBody = response.errorBody()?.string()
                                            Log.d("Upload", "Error response: $errorBody")
                                        }
                                    }

                                    override fun onFailure(call: Call<Freelancer>, t: Throwable) {
                                        // Handle the failure
                                        Log.d("Upload", "File and attributes upload failed: ${t.message}")
                                        if (t is IOException) {
                                            Log.d("Upload", "Error response: ${t.localizedMessage}")
                                        }
                                    }
                                })

                            service.insertProof(imagePartProof)
                                .enqueue(object : Callback<Freelancer> {
                                    override fun onResponse(call: Call<Freelancer>, response: Response<Freelancer>) {

                                        if (response.isSuccessful) {

                                        } else {
                                            // Handle the error response
                                            val errorBody = response.errorBody()?.string()
                                            Log.d("Upload", "Error response: $errorBody")
                                        }
                                    }

                                    override fun onFailure(call: Call<Freelancer>, t: Throwable) {
                                        // Handle the failure
                                        Log.d("Upload", "File and attributes upload failed: ${t.message}")
                                        if (t is IOException) {
                                            Log.d("Upload", "Error response: ${t.localizedMessage}")
                                        }
                                    }
                                })

                        }
                        Log.d("Resort", "$userId")

                        val intent = Intent(this@FreelancerJobVerificationActivity, ApplicationConfirmationActivity::class.java)
                        intent.putExtra("userId", userId)
                        startActivity(intent)
                        return
                    } else {
                        Log.d("MainActivity", "Failed to connect: " + response.code())

                    }
                }

                override fun onFailure(call: Call<Int>, t: Throwable) {
                    Log.d ("Freelancer Identity Verification", "Failed to Retrieve UserId: ")
                }
            })
        }



    }


    private fun registerProof(freelancerId: Int, proofName: String?, dateSent: LocalDate?) {

        val proofInput = proofOfExperience(
            freelancerId = freelancerId,
            proofName = proofName,
            dateSent = dateSent)

        val freelancerService = FreelancerInstance.retrofitBuilder
        freelancerService.insertProof(proofInput).enqueue(object : Callback<Freelancer> {
            override fun onResponse(call: Call<Freelancer>, response: Response<Freelancer>) {
                Log.i(ContentValues.TAG, "The response is " + response.message());
                Log.i(ContentValues.TAG, "The response is " + response.body());

                if (response.isSuccessful) {
                    val proof = response.body()
                    if(proof != null){
                        Toast.makeText(this@FreelancerJobVerificationActivity, "Register", Toast.LENGTH_SHORT).show()
                    }else {
                        Toast.makeText(this@FreelancerJobVerificationActivity, "Freelancer ID is null", Toast.LENGTH_SHORT).show()
                    }
                }
                else {
                    // Handle the error response
                    Log.d("MainActivity", "Response code: ${response.message()}")

                }
            }
            override fun onFailure(call: Call<Freelancer>, t: Throwable) {
                // Handle network or other exceptions
                Log.d("MainActivity", "Exception: ", t)
            }
        })
    }

    private fun registerCertificate(freelancerId: Int, certificateName: String?, dateSent: LocalDate?) {

        val certificateInput = FreelancerTesdaCertificate(
            freelancerId = freelancerId,
            certificateName = certificateName,
            dateSent = dateSent)

        val freelancerService = FreelancerInstance.retrofitBuilder
        freelancerService.insertCertificate(certificateInput).enqueue(object : Callback<Freelancer> {
            override fun onResponse(call: Call<Freelancer>, response: Response<Freelancer>) {
                Log.i(ContentValues.TAG, "The response is " + response.message());
                Log.i(ContentValues.TAG, "The response is " + response.body());

                if (response.isSuccessful) {
                    val certificate = response.body()
                    if(certificate != null){
                        Toast.makeText(this@FreelancerJobVerificationActivity, "Register", Toast.LENGTH_SHORT).show()
                    }else {
                        Toast.makeText(this@FreelancerJobVerificationActivity, "Freelancer ID is null", Toast.LENGTH_SHORT).show()
                    }
                }
                else {
                    // Handle the error response
                    Log.d("MainActivity", "Response code: ${response.message()}")

                }
            }
            override fun onFailure(call: Call<Freelancer>, t: Throwable) {
                // Handle network or other exceptions
                Log.d("MainActivity", "Exception: ", t)
            }
        })
    }


}
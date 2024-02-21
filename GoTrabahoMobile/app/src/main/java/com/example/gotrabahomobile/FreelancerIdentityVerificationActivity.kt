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


class FreelancerIdentityVerificationActivity : AppCompatActivity() {
    var selectedItem: String? = null
    private var selectedImageUri: Uri? = null
    private lateinit var ImgViewID: ImageView
    private lateinit var btnGovernment: Button
    private val MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1
    var ImagePath: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_freelancer_identity_verification)

        ImgViewID = findViewById(R.id.imageViewGovernment)
        btnGovernment = findViewById(R.id.government_button)
        btnGovernment.setOnClickListener {
            openImagePicker()
        }
        val idType = findViewById<Spinner>(R.id.dropdownIDType)
        val buttonUpload = findViewById<Button>(R.id.buttonApplicationPageSignUp)
        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.idType,
            android.R.layout.simple_spinner_dropdown_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        idType.adapter = adapter

        idType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectedItem = parent?.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                selectedItem = null
            }

        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {

            // Request the permission
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE
            )
        }
        buttonUpload.setOnClickListener{

            getEmail()
        }

        }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission was granted, you can access the file now
                } else {
                    // Permission was denied, handle the denial
                }
                return
            }
            // ... (handle other permission requests)
        }
    }


    companion object {
        const val REQUEST_CODE_IMAGE = 101
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                REQUEST_CODE_IMAGE -> {
                    selectedImageUri = data?.data
                    ImgViewID.setImageURI(selectedImageUri)
                    ImagePath = getRealPathFromURI(data?.data)
                }
            }
        }
    }
    private fun getEmail() {
        //val email = intent.getStringExtra("email")
          val email = "hello@gmail.com"
        val service = UserInstance.retrofitBuilder

        if (email != null) {
            service.getEmail(email).enqueue(object : Callback<Int> {
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
                            val name = email + "GVT.jpg"
                            val file = File(ImagePath)
                            val requestBody = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file)
                            val imagePart = MultipartBody.Part.createFormData("imageFile", name, requestBody)

                            val governmentId = name
                            val verificationStatus = false
                            val totalIncome = 0

                            registerFreelancer(13, idType, governmentId, verificationStatus, totalIncome)
                            Log.d("TEST", "${call.toString()}")
                            service.insertGovernment(imagePart)
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

                        val intent = Intent(this@FreelancerIdentityVerificationActivity, FreelancerJobVerificationActivity::class.java)
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

    private fun openImagePicker() {
        Intent(Intent.ACTION_PICK).also {
            it.type = "image/*"
            val mimeTypes = arrayOf("image/jpeg", "image/png")
            it.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
            startActivityForResult(it, REQUEST_CODE_IMAGE)
        }
    }


    private fun registerFreelancer(userId: Int, idType: Int?, governmentId: String,  verificationStatus: Boolean, totalIncome: Int?) {

        val freelancerInput = Freelancer(
            userId = userId,
            idType = idType,
            governmentId = governmentId,
            verificationStatus = verificationStatus,
            totalIncome = totalIncome)

        val freelancerService = FreelancerInstance.retrofitBuilder
        freelancerService.insertFreelancer(freelancerInput).enqueue(object : Callback<Freelancer> {
            override fun onResponse(call: Call<Freelancer>, response: Response<Freelancer>) {
                Log.i(ContentValues.TAG, "The response is " + response.message());
                Log.i(ContentValues.TAG, "The response is " + response.body());

                if (response.isSuccessful) {
                    val freelancer = response.body()
                    if(freelancer != null){
                        Toast.makeText(this@FreelancerIdentityVerificationActivity, "Register", Toast.LENGTH_SHORT).show()
                    }else {
                        Toast.makeText(this@FreelancerIdentityVerificationActivity, "Freelancer ID is null", Toast.LENGTH_SHORT).show()
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
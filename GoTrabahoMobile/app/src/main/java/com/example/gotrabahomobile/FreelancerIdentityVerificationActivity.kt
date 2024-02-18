package com.example.gotrabahomobile

import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.Spinner
import com.bumptech.glide.Glide
import com.example.gotrabahomobile.DTO.FreelancerDTO
import com.example.gotrabahomobile.Model.Freelancer
import com.example.gotrabahomobile.Remote.FreelancerRemote.FreelancerInstance
import com.example.gotrabahomobile.Remote.UserRemote.UserInstance
import com.google.gson.Gson
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody

class FreelancerIdentityVerificationActivity : AppCompatActivity() {

    var selectedItem: String? = null
    private var selectedImageUri: Uri? = null
    private lateinit var ImgViewID: ImageView
    private lateinit var btnGovernment: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_freelancer_identity_verification)

        ImgViewID = findViewById(R.id.imageViewGovernment)
        btnGovernment = findViewById(R.id.government_button)
        btnGovernment.setOnClickListener {
            openImagePicker()
        }
        val idType = findViewById<Spinner>(R.id.dropdownIDType)
        val buttonUpload = findViewById<Button>(R.id.buttonApplicationPage2Continue)
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

        buttonUpload.setOnClickListener{
            getEmail()
        }

    }

    companion object {
        const val REQUEST_CODE_IMAGE = 101
    }
/*    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            val imageView = findViewById<ImageView>(R.id.imageViewGovernment)
            val selectedImageUri: Uri? = data.data
            Log.d("FUCK", "Where $selectedImageUri")
            loadImage(imageView, selectedImageUri)
        }
    }   */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_CODE_IMAGE -> {
                    selectedImageUri = data?.data
                    ImgViewID.setImageURI(selectedImageUri)
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
                            val parcelFileDescriptor = contentResolver.openFileDescriptor(
                                selectedImageUri!!, "r", null
                            ) ?: return

                            val tempFile = File.createTempFile("upload", ".jpg", cacheDir)
                            val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)
                            val outputStream = FileOutputStream(tempFile)
                            try {
                                inputStream.copyTo(outputStream)
                            } catch (e: IOException) {
                                e.printStackTrace()
                                return
                            } finally {
                                inputStream.close()
                                outputStream.close()
                            }

                            val requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), tempFile)

                            val imagePart = MultipartBody.Part.createFormData("image", tempFile.name, requestFile)



                            /*val file = File("D:\\Chrome Downloads\\image.png")
                            val requestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
                            val imagePart = MultipartBody.Part.createFormData("image", file.name, requestBody)*/

                             */
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
                            val freelancerDTO = FreelancerDTO(
                                freelancerId = null,
                                idType = idType,
                                userId = userId,
                                governmentId = null,
                                verificationStatus = false,
                                totalIncome =  0
                            )
                            val gson = Gson()
                            val freelancerDTOJson = gson.toJson(freelancerDTO)
                            val freelancerDTORequestBody = RequestBody.create(
                                "application/json".toMediaTypeOrNull(),
                                freelancerDTOJson
                            )
                            Log.d("TEST", "${call.toString()}")
                            val call = service.createFreelancer(userId, imagePart, freelancerDTORequestBody)
                            call.enqueue(object : Callback<Freelancer> {
                                override fun onResponse(call: Call<Freelancer>, response: Response<Freelancer>) {
                                    if (response.isSuccessful) {
                                        // Handle the successful response
                                    } else {
                                        // Handle the error response
                                        val errorBody = response.errorBody()?.string()
                                        Log.d("Upload", "Error response: $errorBody")
                                    }
                                }

                                override fun onFailure(call: Call<Freelancer>, t: Throwable) {
                                    // Handle the failure
                                    Log.d("Upload", "File and attributes upload failed: ${t.message}")
                                }
                            })


                        }
                        Log.d("Resort", "$userId")

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
    private val PICK_IMAGE_REQUEST = 1

    private fun loadImage(imageView: ImageView, imageUri: Uri?) {
        if (imageUri != null) {
            Glide.with(this)
                .load(imageUri)
                .into(imageView)
        } else {
            Log.d("FreelancerIdentityVerification", "Image URI is null")

        }
    }

    private fun openImagePicker() {
        Intent(Intent.ACTION_PICK).also {
            it.type = "image/*"
            val mimeTypes = arrayOf("image/jpeg", "image/png")
            it.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
            startActivityForResult(it, REQUEST_CODE_IMAGE)
        }
    }




    private fun ContentResolver.getFileName(selectedImageUri: Uri): String {
        var name = ""
        val returnCursor = this.query(selectedImageUri,null,null,null,null)
        if (returnCursor!=null){
            val nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            returnCursor.moveToFirst()
            name = returnCursor.getString(nameIndex)
            returnCursor.close()
        }

        return name
    }



}









package com.example.gotrabahomobile

import android.content.ContentValues
import android.content.CursorLoader
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
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
import androidx.annotation.RequiresApi
import com.example.gotrabahomobile.Model.Freelancer
import com.example.gotrabahomobile.Model.FreelancerTesdaCertificate
import com.example.gotrabahomobile.Model.Services
import com.example.gotrabahomobile.Model.proofOfExperience
import com.example.gotrabahomobile.Remote.FreelancerRemote.FreelancerInstance
import com.example.gotrabahomobile.Remote.ServicesRemote.ServicesInstance
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.IOException
import java.time.LocalDate

class FreelancerNewServiceJobVerificationActivity : AppCompatActivity() {


    private var selectedImageUriCertificate: Uri? = null
    private var selectedImageUriProof: Uri? = null
    var selectedService: String? = null
    private lateinit var ImgViewProof: ImageView
    private lateinit var btnProof: Button
    private lateinit var ImgViewCertificate: ImageView
    private lateinit var btnCertificate: Button
    private lateinit var btnSignUp: Button
    var ImagePathProof: String? = null
    var ImagePathCertificate: String? = null
    private val MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1

    companion object {
        const val REQUEST_CODE_IMAGE_PROOF =  1001 // Unique request code for proof image selection
        const val REQUEST_CODE_IMAGE_CERTIFICATE =  1002 // Unique request code for certificate image selection
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_freelancer_new_service_job_verification)


        val back: ImageView = findViewById(R.id.imageViewBackButton)
        ImgViewCertificate = findViewById(R.id.imageViewNewCertificate)
        ImgViewProof = findViewById(R.id.imageViewNewProofOfWork)
        btnProof = findViewById(R.id.buttonNewServiceUploadProofOfWork)
        btnCertificate = findViewById(R.id.buttonNewServiceUploadCertificate)
        btnSignUp = findViewById(R.id.buttonNewServiceNext)

        val serviceTypeName = findViewById<Spinner>(R.id.dropdownNewServiceIDType)

        val serviceTypesArray = resources.getStringArray(R.array.serviceTypes)
        val serviceTypesList = serviceTypesArray.toMutableList()

        val adapter = ArrayAdapter(this@FreelancerNewServiceJobVerificationActivity,
            android.R.layout.simple_spinner_item, serviceTypesList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        serviceTypeName.adapter = adapter
        serviceTypeName.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                // Store the selected item in the variable
                selectedService = parent.getItemAtPosition(position) as? String
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                Toast.makeText(this@FreelancerNewServiceJobVerificationActivity, "Please Select a Service", Toast.LENGTH_SHORT).show()
            }
        }
        btnProof.setOnClickListener {
            openImagePickerForProof()
        }
        btnCertificate.setOnClickListener {
            openImagePickerForCertificate()
        }

        btnSignUp.setOnClickListener {
            getEmail()
        }

/*        nextButton.setOnClickListener{

            val intent = Intent(this, FreelancerAddNewServiceActivity::class.java)
            intent.putExtra("freelancerId", freelancerId)
            intent.putExtra("userId", userId)
            intent.putExtra("freelancerId", freelancerId)
            intent.putExtra("firstName", firstName)
            intent.putExtra("lastName", lastName)
            intent.putExtra("fullName", fullName)
            intent.putExtra("email", email)
            startActivity(intent)
        }*/

        back.setOnClickListener{
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                FreelancerJobVerificationActivity.REQUEST_CODE_IMAGE_PROOF -> {
                    selectedImageUriProof = data?.data
                    ImgViewProof.setImageURI(selectedImageUriProof)
                    ImagePathProof = getRealPathFromURI(data?.data)
                }
                FreelancerJobVerificationActivity.REQUEST_CODE_IMAGE_CERTIFICATE -> {
                    selectedImageUriCertificate = data?.data
                    ImgViewCertificate.setImageURI(selectedImageUriCertificate)
                    ImagePathCertificate = getRealPathFromURI(data?.data)
                }
            }
        }
    }

    private fun openImagePickerForProof() {
        Intent(Intent.ACTION_PICK).also {
            it.type = "image/*"
            val mimeTypes = arrayOf("image/jpeg", "image/png")
            it.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
            startActivityForResult(it, FreelancerJobVerificationActivity.REQUEST_CODE_IMAGE_PROOF)
        }
    }

    private fun openImagePickerForCertificate() {
        Intent(Intent.ACTION_PICK).also {
            it.type = "image/*"
            val mimeTypes = arrayOf("image/jpeg", "image/png")
            it.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
            startActivityForResult(it, FreelancerJobVerificationActivity.REQUEST_CODE_IMAGE_CERTIFICATE)
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
        val freelancerId = intent.getIntExtra("freelancerId", 0)
        val userId = intent.getIntExtra("userId", 0) ?: 0
        val firstName = intent.getStringExtra("firstName")
        val lastName = intent.getStringExtra("lastName")
        val email = intent.getStringExtra("email")
        val fullName = intent.getStringExtra("fullName")

        val service = FreelancerInstance.retrofitBuilder
        service.getFreelancerId(userId).enqueue(object : Callback<Freelancer> {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onResponse(call: Call<Freelancer>, response: Response<Freelancer>) {
                if (response.isSuccessful) {

                    val data = response.body()
                    val freelancerId = data?.freelancerId

                    Log.d("Freelancer", "$freelancerId}")
                    if (freelancerId != null) {
                        val service = FreelancerInstance.retrofitBuilder

                        val nameProof = email + "Proof.jpg"
                        val nameCertificate = email + "Certificate.jpg"
                        val fileProof = File(ImagePathProof)
                        val fileCertificate = File(ImagePathCertificate)
                        val dateSent = LocalDate.now().toString()

                        val requestBodyP =
                            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), fileProof)
                        val imagePartCertificate = MultipartBody.Part.createFormData(
                            "imageFile",
                            nameCertificate,
                            requestBodyP
                        )

                        val requestBodyC = RequestBody.create(
                            "multipart/form-data".toMediaTypeOrNull(),
                            fileCertificate
                        )
                        val imagePartProof =
                            MultipartBody.Part.createFormData("imageFile", nameProof, requestBodyC)

                        registerCertificate(freelancerId, nameCertificate, dateSent)
                        Log.d("TEST", "${call.toString()}")

                        registerProof(freelancerId, nameProof, dateSent)
                        registerService(
                            freelancerId,
                            selectedService,
                            0,
                            null,
                            null
                        )
                        service.insertCertificateImage(imagePartCertificate)
                            .enqueue(object : Callback<FreelancerTesdaCertificate> {
                                override fun onResponse(
                                    call: Call<FreelancerTesdaCertificate>,
                                    response: Response<FreelancerTesdaCertificate>
                                ) {

                                    if (response.isSuccessful) {

                                    } else {
                                        // Handle the error response
                                        val errorBody = response.errorBody()?.string()
                                        Log.d("Upload", "Error response: $errorBody")
                                    }
                                }

                                override fun onFailure(
                                    call: Call<FreelancerTesdaCertificate>,
                                    t: Throwable
                                ) {
                                    // Handle the failure
                                    Log.d(
                                        "Upload",
                                        "File and attributes upload failed: ${t.message}"
                                    )
                                    if (t is IOException) {
                                        Log.d("Upload", "Error response: ${t.localizedMessage}")
                                    }
                                }
                            })

                        service.insertProofImage(imagePartProof)
                            .enqueue(object : Callback<proofOfExperience> {
                                override fun onResponse(
                                    call: Call<proofOfExperience>,
                                    response: Response<proofOfExperience>
                                ) {

                                    if (response.isSuccessful) {

                                    } else {
                                        // Handle the error response
                                        val errorBody = response.errorBody()?.string()
                                        Log.d("Upload", "Error response: $errorBody")
                                    }
                                }

                                override fun onFailure(
                                    call: Call<proofOfExperience>,
                                    t: Throwable
                                ) {
                                    // Handle the failure
                                    Log.d(
                                        "Upload",
                                        "File and attributes upload failed: ${t.message}"
                                    )
                                    if (t is IOException) {
                                        Log.d("Upload", "Error response: ${t.localizedMessage}")
                                    }
                                }
                            })
                        Log.d("Resort", "$userId")
                        finish()
                        return
                    }


                } else {
                    Log.d("MainActivity", "Failed to connect: " + response.code())

                }
            }

            override fun onFailure(call: Call<Freelancer>, t: Throwable) {
                Log.d("Freelancer Identity Verification", "Failed to Retrieve UserId: ")
            }
        })


    }


    private fun registerProof(freelancerId: Int, proofName: String?, dateSent: String?) {

        val proofInput = proofOfExperience(
            freelancerId = freelancerId,
            proofName = proofName,
            dateSent = dateSent
        )

        val freelancerService = FreelancerInstance.retrofitBuilder
        freelancerService.insertProof(proofInput).enqueue(object : Callback<proofOfExperience> {
            override fun onResponse(
                call: Call<proofOfExperience>,
                response: Response<proofOfExperience>
            ) {
                Log.i(ContentValues.TAG, "The response is " + response.message());
                Log.i(ContentValues.TAG, "The response is " + response.body());

                if (response.isSuccessful) {
                    val proof = response.body()
                    if (proof != null) {
                        Toast.makeText(
                            this@FreelancerNewServiceJobVerificationActivity,
                            "Proof Upload",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            this@FreelancerNewServiceJobVerificationActivity,
                            "Proof Upload is null",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    // Handle the error response
                    Log.d("MainActivity", "Response code: ${response.message()}")

                }
            }

            override fun onFailure(call: Call<proofOfExperience>, t: Throwable) {
                // Handle network or other exceptions
                Log.d("MainActivity", "Exception: ", t)
            }
        })
    }

    private fun registerCertificate(freelancerId: Int, certificateName: String?, dateSent: String?) {

        val certificateInput = FreelancerTesdaCertificate(
            freelancerId = freelancerId,
            certificateName = certificateName,
            dateSent = dateSent)

        val freelancerService = FreelancerInstance.retrofitBuilder
        freelancerService.insertCertificate(certificateInput).enqueue(object : Callback<FreelancerTesdaCertificate> {
            override fun onResponse(call: Call<FreelancerTesdaCertificate>, response: Response<FreelancerTesdaCertificate>) {
                Log.i(ContentValues.TAG, "The response is " + response.message());
                Log.i(ContentValues.TAG, "The response is " + response.body());

                if (response.isSuccessful) {
                    val certificate = response.body()
                    if(certificate != null){
                        Toast.makeText(this@FreelancerNewServiceJobVerificationActivity, "Certificate Upload", Toast.LENGTH_SHORT).show()
                    }else {
                        Toast.makeText(this@FreelancerNewServiceJobVerificationActivity, "Certificate Input Null", Toast.LENGTH_SHORT).show()
                    }
                }
                else {
                    // Handle the error response
                    Log.d("MainActivity", "Response code: ${response.message()}")

                }
            }
            override fun onFailure(call: Call<FreelancerTesdaCertificate>, t: Throwable) {
                // Handle network or other exceptions
                Log.d("MainActivity", "Exception: ", t)
            }
        })
    }

    private fun registerService(freelancerId: Int, serviceTypeName: String?, status: Int?, location: String?, rating: Float?) {

        val serviceInput = Services(
            freelancerId = freelancerId,
            name = "Register your Service",
            description = "Description:",
            priceEstimate = 0.0,
            serviceTypeName = serviceTypeName,
            status = status,
            showService = false,
            location = location,
            rating = rating)

        val ServiceService = ServicesInstance.retrofitBuilder
        ServiceService.insertService(serviceInput).enqueue(object : Callback<Services> {
            override fun onResponse(call: Call<Services>, response: Response<Services>) {
                Log.i(ContentValues.TAG, "The response is " + response.message());
                Log.i(ContentValues.TAG, "The response is " + response.body());

                if (response.isSuccessful) {
                    val proof = response.body()
                    if(proof != null){
                        Toast.makeText(this@FreelancerNewServiceJobVerificationActivity, "Service Upload", Toast.LENGTH_SHORT).show()
                    }else {
                        Toast.makeText(this@FreelancerNewServiceJobVerificationActivity, "Service Upload is null", Toast.LENGTH_SHORT).show()
                    }
                }
                else {
                    // Handle the error response
                    Log.d("MainActivity", "Response code: ${response.message()}")

                }
            }
            override fun onFailure(call: Call<Services>, t: Throwable) {
                // Handle network or other exceptions
                Log.d("MainActivity", "Exception: ", t)
            }
        })
    }

}
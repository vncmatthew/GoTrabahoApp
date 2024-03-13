package com.example.gotrabahomobile

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gotrabahomobile.DTO.ServicesDTO
import com.example.gotrabahomobile.Helper.ServicesListAdapter
import com.example.gotrabahomobile.Model.Services

class FreelancerServicesListActivity : AppCompatActivity() {

    private lateinit var servicesList: List<ServicesDTO>
    private lateinit var rvAdapter: ServicesListAdapter
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_freelancer_services_list)

        val editService: Button = findViewById(R.id.buttonEditService)
        val deleteService: Button = findViewById(R.id.buttonDeleteService)


        val backButton: ImageButton = findViewById(R.id.back_buttonNavbar)
        backButton.setOnClickListener{
            finish()
        }

        editService.setOnClickListener{
            val intent = Intent(this, FreelancerEditServiceActivity::class.java)
            startActivity(intent)
        }

        deleteService.setOnClickListener{
            showConfirmDeleteDialog()
        }


        val servicesListRecyclerView = findViewById<RecyclerView>(R.id.servicesListRecycler)



        servicesListRecyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        rvAdapter = ServicesListAdapter(servicesList)


    }

    private fun showConfirmDeleteDialog() {
        val deleteView = LayoutInflater.from(this).inflate(R.layout.dialog_delete_service, null)

        val buttonNo = deleteView.findViewById<Button>(R.id.buttonDeleteServiceNo)
        val buttonYes = deleteView.findViewById<Button>(R.id.buttonDeleteServiceYes)

        val alertDialog = AlertDialog.Builder(this)
            .setView(deleteView)
            .create()

        buttonNo.setOnClickListener{
            alertDialog.dismiss()
        }

        buttonYes.setOnClickListener {
            Toast.makeText(this, "Service deleted", Toast.LENGTH_SHORT).show()
            alertDialog.dismiss()
        }

        alertDialog.show()
    }
}
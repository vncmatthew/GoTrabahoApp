package com.example.gotrabahomobile.Helper


import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.gotrabahomobile.AddSubserviceActivity
import com.example.gotrabahomobile.DTO.UserDetails
import com.example.gotrabahomobile.FreelancerEditServiceActivity
import com.example.gotrabahomobile.Model.Services
import com.example.gotrabahomobile.Remote.ServicesRemote.ServicesInstance
import com.example.gotrabahomobile.databinding.ListItemServicesListEditDeleteBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FreelancerServiceListAdapter(private val servicesList: List<Services>, private val context: Context, private val UserDetails: UserDetails) : RecyclerView.Adapter<FreelancerServiceListAdapter.ServiceListViewHolder>() {

    private var statusOn = false
    inner class ServiceListViewHolder(val binding: ListItemServicesListEditDeleteBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceListViewHolder {
        return ServiceListViewHolder(
            ListItemServicesListEditDeleteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int {
        return servicesList.size
    }
    fun changeStatusTrue(serviceId: Int, freelancerId: Int){
        val call = ServicesInstance.retrofitBuilder
        val services = Services(
            serviceId = serviceId,
            freelancerId = freelancerId,
            showService = true
        )
        call.patchServices(services).enqueue(object: Callback<Services>{
            override fun onResponse(call: Call<Services>, response: Response<Services>) {
                if(response.isSuccessful){
                    Log.d("Changed Bookings", "True")
                }
            }

            override fun onFailure(call: Call<Services>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }

    fun changeStatusFalse(serviceId: Int, freelancerId: Int){


        val call = ServicesInstance.retrofitBuilder
        val services = Services(
            serviceId = serviceId,
            freelancerId = freelancerId,
            showService = false
        )
        call.patchServices(services).enqueue(object: Callback<Services>{
            override fun onResponse(call: Call<Services>, response: Response<Services>) {
                if(response.isSuccessful){
                    Log.d("Changed Bookings", "False")
                }
            }

            override fun onFailure(call: Call<Services>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }

    override fun onBindViewHolder(holder: ServiceListViewHolder, position: Int) {
        val services = servicesList[position]

        holder.binding.apply {
            editInfoServiceName.text = "${services.name}"
            editInfoDescription.text = "${services.description}"
//            editInfoSubServices.text = "${services.sub}
            editInfoPrice.text = "₱ ${services.priceEstimate}"

            holder.binding.buttonAddSubService.setOnClickListener {
                if(statusOn){
                    Toast.makeText(context, "Please turn Status to Inactive first before adding a sub-service", Toast.LENGTH_SHORT).show()
                }else {
                    val intent = Intent(context, AddSubserviceActivity::class.java)
                    intent.putExtra("serviceId", services.serviceId)
                    intent.putExtra("serviceTypeName", services.serviceTypeName)
                    intent.putExtra("status", services.status)
                    intent.putExtra("rating", services.rating)
                    intent.putExtra("freelancerId", services.freelancerId)
                    intent.putExtra("userId", UserDetails.userId)
                    intent.putExtra("firstName", UserDetails.firstName)
                    intent.putExtra("lastName", UserDetails.lastName)
                    intent.putExtra("fullName", UserDetails.fullName)
                    intent.putExtra("email", UserDetails.email)
                    intent.putExtra("showService", services.showService)
                    context.startActivity(intent)
                }
            }

            holder.binding.buttonEditService.setOnClickListener {
                if(statusOn){
                    Toast.makeText(context, "Please turn Status to Inactive first before editing", Toast.LENGTH_SHORT).show()
                }else{
                    val intent = Intent(context, FreelancerEditServiceActivity::class.java)
                    intent.putExtra("serviceId", services.serviceId)
                    intent.putExtra("serviceTypeName", services.serviceTypeName)
                    intent.putExtra("status", services.status)
                    intent.putExtra("rating", services.rating)
                    intent.putExtra("freelancerId", services.freelancerId)
                    intent.putExtra("userId", UserDetails.userId)
                    intent.putExtra("firstName", UserDetails.firstName)
                    intent.putExtra("lastName", UserDetails.lastName)
                    intent.putExtra("fullName", UserDetails.fullName)
                    intent.putExtra("email", UserDetails.email)
                    intent.putExtra("showService", services.showService)
                    context.startActivity(intent)
                }
            }

            holder.binding.buttonDeleteService.setOnClickListener {
                if(statusOn){
                    Toast.makeText(context, "Please turn Status to Inactive first before deleting", Toast.LENGTH_SHORT).show()
                }else {
                    val service = ServicesInstance.retrofitBuilder
                    service.deleteService(services.serviceId!!)
                        .enqueue(object : Callback<Services> {
                            override fun onResponse(
                                call: Call<Services>,
                                response: Response<Services>
                            ) {
                                if (response.isSuccessful) {
                                    Toast.makeText(context, "Successfully Deleted The Service", Toast.LENGTH_SHORT).show()
                                } else {
                                    Toast.makeText(context, "Cannot delete service, you have existing bookings with this service", Toast.LENGTH_SHORT).show()
                                }
                            }

                            override fun onFailure(call: Call<Services>, t: Throwable) {
                                TODO("Not yet implemented")
                            }

                        })
                }
            }

            holder.binding.ActiveStatusSwitch.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    changeStatusTrue(services.serviceId!!, services.freelancerId!!)
                    statusOn = true
                    switchLabel.text = "Active"

                } else {
                    changeStatusFalse(services.serviceId!!, services.freelancerId!!)
                    statusOn = false
                    switchLabel.text = "Inactive"
                }


            }

        }


    }
}
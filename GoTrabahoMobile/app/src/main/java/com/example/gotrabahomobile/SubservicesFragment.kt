package com.example.gotrabahomobile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.example.gotrabahomobile.Model.UserFirebase
import com.example.gotrabahomobile.Remote.ServicesRemote.ServicesInstance
import com.example.gotrabahomobile.databinding.FragmentCustomerHomeBinding
import com.example.gotrabahomobile.databinding.FragmentSubservicesBinding
import com.example.gotrabahomobile.fragments.CustomerHomeFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SubservicesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SubservicesFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    var selectedService: String? = null
    var userList = ArrayList<UserFirebase>()
    private var _binding: FragmentSubservicesBinding? = null
    private lateinit var viewMap: Map<String, Int>

    private val binding get() = _binding!!
    private var email: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        email = arguments?.getString("email")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSubservicesBinding.inflate(inflater, container, false)

        val longitude = arguments?.getDouble("longitude", 0.0) ?: 0
        val latitude = arguments?.getDouble("latitude", 0.0) ?: 0
        val userId = arguments?.getInt("userId", 0) ?: 0
        val buttonMapView: Button = _binding!!.buttonMapView
        buttonMapView.setOnClickListener{
            val intent = Intent(requireContext(), FreelancerListMapViewActivity::class.java)
            intent.putExtra("longitude", longitude)
            intent.putExtra("latitude", latitude)
            intent.putExtra("sqlId", userId)
            startActivity(intent)
        }

        return _binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val plumbing = view.findViewById<CardView>(R.id.plumbingSubservice)
        val electrical = view.findViewById<CardView>(R.id.electricalSubservice)
        val acRepair = view.findViewById<CardView>(R.id.acRepairSubservice)
        val refRepair = view.findViewById<CardView>(R.id.refRepairSubservice)
        val carpentry = view.findViewById<CardView>(R.id.carpentrySubservice)

        viewMap = mapOf(
            "Plumbing" to R.id.averageRatePlumbing,
            "Electrical" to R.id.averageRateElectrical,
            "AC Repair" to R.id.averageRateAcRepair,
            "Ref Repair" to R.id.averageRateRefRepair,
            "Carpentry" to R.id.averageRateCarpentry
        )

        // Loop through the map and find views
        for ((serviceType, viewId) in viewMap) {
            val textView = view.findViewById<TextView>(viewId)
            // Now you can work with each TextView
            setupTextView(textView, serviceType)
        }
        plumbing.setOnClickListener {
            val userId = arguments?.getInt("userId", 0) ?: 0

            val bundle = Bundle().apply {
                putInt("userId", userId)
                putString("serviceTypeName", "Plumbing")
            }

            val homeFragment = CustomerHomeFragment().apply {
                arguments = bundle
            }

            parentFragmentManager.beginTransaction()
                .replace(R.id.mainContainer, homeFragment)
                .addToBackStack(null)
                .commit()
        }

        electrical.setOnClickListener {
            val userId = arguments?.getInt("userId", 0) ?: 0

            val bundle = Bundle().apply {
                putInt("userId", userId)
                putString("serviceTypeName", "Electrical")
            }

            val homeFragment = CustomerHomeFragment().apply {
                arguments = bundle
            }

            parentFragmentManager.beginTransaction()
                .replace(R.id.mainContainer, homeFragment)
                .addToBackStack(null)
                .commit()
        }

        acRepair.setOnClickListener {
            val userId = arguments?.getInt("userId", 0) ?: 0

            val bundle = Bundle().apply {
                putInt("userId", userId)
                putString("serviceTypeName", "AC Repair")
            }

            val homeFragment = CustomerHomeFragment().apply {
                arguments = bundle
            }

            parentFragmentManager.beginTransaction()
                .replace(R.id.mainContainer, homeFragment)
                .addToBackStack(null)
                .commit()
        }

        refRepair.setOnClickListener {
            val userId = arguments?.getInt("userId", 0) ?: 0

            val bundle = Bundle().apply {
                putInt("userId", userId)
                putString("serviceTypeName", "Ref Repair")
            }

            val homeFragment = CustomerHomeFragment().apply {
                arguments = bundle
            }

            parentFragmentManager.beginTransaction()
                .replace(R.id.mainContainer, homeFragment)
                .addToBackStack(null)
                .commit()
        }

        carpentry.setOnClickListener {
            val userId = arguments?.getInt("userId", 0) ?: 0

            val bundle = Bundle().apply {
                putInt("userId", userId)
                putString("serviceTypeName", "Carpentry")
            }


            val homeFragment = CustomerHomeFragment().apply {
                arguments = bundle
            }

            parentFragmentManager.beginTransaction()
                .replace(R.id.mainContainer, homeFragment)
                .addToBackStack(null)
                .commit()
        }
    }

    private fun setupTextView(textView: TextView?, serviceType: String) {
        textView?.let { tv ->

            tv.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.black))
            tv.text = "Loading..."

            computeAverageAmount(serviceType) { averageAmount ->
                if (averageAmount != null) {
                    tv.text = "₱$averageAmount"
                } else {
                    tv.text = "N/A"
                }
            }
        }
    }

    private fun computeAverageAmount(serviceTypeName: String?, callback: (Double?) -> Unit) {
        val call = ServicesInstance.retrofitBuilder
        call.getAverageAmountPerService(serviceTypeName).enqueue(object : Callback<Double> {
            override fun onResponse(call: Call<Double>, response: Response<Double>) {
                if (response.isSuccessful) {
                    val averageAmount = response.body()
                    callback(averageAmount)
                } else {
                    callback(null)
                }
            }

            override fun onFailure(call: Call<Double>, t: Throwable) {
                callback(null)
            }
        })
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SubservicesFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SubservicesFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
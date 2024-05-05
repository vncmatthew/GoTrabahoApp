package com.example.gotrabahomobile


import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.gotrabahomobile.DTO.FreelancerLocations

import com.example.gotrabahomobile.Remote.ServicesRemote.ServicesInstance
import org.osmdroid.config.Configuration.*
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class FreelancerListMapViewActivity : AppCompatActivity() {

    private val REQUEST_PERMISSIONS_REQUEST_CODE = 1
    private lateinit var map : MapView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_freelancer_list_map_view)

        //handle permissions first, before map is created. not depicted here

        //load/initialize the osmdroid configuration, this can be done
        // This won't work unless you have imported this: org.osmdroid.config.Configuration.*
        getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this))
        //setting this before the layout is inflated is a good idea
        //it 'should' ensure that the map has a writable location for the map cache, even without permissions
        //if no tiles are displayed, you can try overriding the cache path using Configuration.getInstance().setCachePath
        //see also StorageUtils
        //note, the load method also sets the HTTP User Agent to your application's package name, if you abuse osm's
        //tile servers will get you banned based on this string.

        //inflate and create the map
        setContentView(R.layout.activity_freelancer_list_map_view)

        val backButton: ImageButton = findViewById(R.id.back_buttonNavbar)
        backButton.setOnClickListener{
            finish()
        }


        map = findViewById<MapView>(R.id.mapview)
        map.setTileSource(TileSourceFactory.MAPNIK)

        val mapController = map.controller
        mapController.setZoom(20.20)
        val longitude = intent.getDoubleExtra("longitude", 0.0)
        val latitude = intent.getDoubleExtra("latitude", 0.0)
        val startPoint = GeoPoint(latitude, longitude);

        mapController.setCenter(startPoint);
        addCustomerPin()
        addFreelancerPins()


    }

    override fun onResume() {
        super.onResume()
        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));
        map.onResume() //needed for compass, my location overlays, v6.0.0 and up
    }

    override fun onPause() {
        super.onPause()
        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Configuration.getInstance().save(this, prefs);
        map.onPause()  //needed for compass, my location overlays, v6.0.0 and up
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        val permissionsToRequest = ArrayList<String>()
        var i = 0
        while (i < grantResults.size) {
            permissionsToRequest.add(permissions[i])
            i++
        }
        if (permissionsToRequest.size > 0) {
            ActivityCompat.requestPermissions(
                this,
                permissionsToRequest.toTypedArray(),
                REQUEST_PERMISSIONS_REQUEST_CODE)
        }
    }

    private fun addCustomerPin() {
        val longitude = intent.getDoubleExtra("longitude", 0.0)
        val latitude = intent.getDoubleExtra("latitude", 0.0)
        val startPoint = GeoPoint(latitude, longitude);

        val startMarker = Marker(map)
        startMarker.setPosition(startPoint)
        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER)
        startMarker.setIcon(getResources().getDrawable(R.drawable.customer_pin))
        map.getOverlays().add(startMarker)

    }

    private fun addFreelancerPins() {
        val call = ServicesInstance.retrofitBuilder.getServiceLocations()
        call.enqueue(object : Callback<List<FreelancerLocations>> {
            override fun onResponse(
                call: Call<List<FreelancerLocations>>,
                response: Response<List<FreelancerLocations>>
            ) {
                val userId = intent.getStringExtra("userId")
                if (response.isSuccessful) {
                    response.body()?.let { freelancerLocations ->
                        for (freelancer in freelancerLocations) {
                            Log.i("Check", "onResponse: ${freelancer.latitude}")
                            Log.i("Check", "onLong: ${freelancer.longitude}")
                            val geoPoint = freelancer.latitude?.let { freelancer.longitude?.let { it1 ->
                                GeoPoint(it,
                                    it1
                                )
                            } }

                            // Create a marker for each freelancer location
                            val marker = Marker(map)
                            marker.position = geoPoint
                            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                            marker.setTitle("Service Name: " + freelancer.name)
                            Log.d("Freelancer", "${freelancer.name}")
                            Log.d("Freelancer", "${freelancer.description}")
                            marker.setSnippet("Service Description: " + freelancer.description)
                            marker.setIcon(ContextCompat.getDrawable(this@FreelancerListMapViewActivity, R.drawable.location_pin))

                            marker.setOnMarkerClickListener{marker, map->
                                val intent = Intent(this@FreelancerListMapViewActivity, FreelancerDetailsActivity::class.java)
                                intent.putExtra("sqlId", userId)
                                intent.putExtra("serviceName",freelancer.name)
                                intent.putExtra("description",freelancer.description)
                                intent.putExtra("location",freelancer.location)
                                intent.putExtra("price",freelancer.priceEstimate)
                                intent.putExtra("rating",freelancer.rating)
                                startActivity(intent)
                                true
                            }
                            // Add the marker to the map
                            map.overlays.add(marker)
                        }
                        map.invalidate() // Refresh the map to show the new markers
                    }
                }
            }

            override fun onFailure(call: Call<List<FreelancerLocations>>, t: Throwable) {
                // Handle failure
                Toast.makeText(this@FreelancerListMapViewActivity, "Failed to load locations", Toast.LENGTH_SHORT).show()
            }
        })
    }


  /*  private fun addMarker(){

        val service = ServicesInstance.retrofitBuilder

        service.getServiceLocations().enqueue(object : Callback<List<FreelancerLocations>>{
            override fun onResponse(
                call: Call<List<FreelancerLocations>>,
                response: Response<List<FreelancerLocations>>
            ) {
                if(response.isSuccessful){
                    response.body().let{
                        if (it != null) {
                            for(freelancer in it){
                                Log.i("Check", "onResponse: ${freelancer.latitude}")
                                Log.i("Check", "onResponse: ${freelancer.longitude}")
                            }
                        }
                    }
                }
            }

            override fun onFailure(call: Call<List<FreelancerLocations>>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }*/


    /*private fun requestPermissionsIfNecessary(String[] permissions) {
            val permissionsToRequest = ArrayList<String>();
        permissions.forEach { permission ->
        if (ContextCompat.checkSelfPermission(this, permission)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            permissionsToRequest.add(permission);
        }
    }
        if (permissionsToRequest.size() > 0) {
            ActivityCompat.requestPermissions(
                    this,
                    permissionsToRequest.toArray(new String[0]),
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }*/
}
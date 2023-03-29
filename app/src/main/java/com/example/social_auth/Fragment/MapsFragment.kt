package com.example.social_auth.Fragment

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.social_auth.R
import com.facebook.FacebookSdk.getApplicationContext
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import java.util.*


class MapsFragment : Fragment() , OnMapReadyCallback{
    private lateinit var btnstory: Button
    private lateinit var btnreel: Button
    private lateinit var btnhelp: Button
    private lateinit var googleMap: GoogleMap
    private lateinit  var circle :Circle
    private val LOCATION_PERMISSION_REQ_CODE = 1000;
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0
    private lateinit var locationManager: LocationManager

    private val latitudes = listOf(22.763178764746748,22.768650824959106,22.774296400914015,22.775798489144332,
        22.762852907462914,22.758607726011007,22.764455598412933)
    private val longitudes = listOf(75.88535081595182, 75.88535316288471, 75.88998399674892,75.88416695594789,
        75.87989922612904,75.88331669569014,75.89127078652382)
    private val callback = OnMapReadyCallback { googleMap ->


    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        getCurrentLocationn()


        val view = inflater.inflate(R.layout.fragment_maps, container, false)
        btnstory = view.findViewById(R.id.btnstory)
        btnreel = view.findViewById(R.id.btnreel)
        btnhelp = view.findViewById(R.id.btnhelp)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment

        // Set the OnMapReadyCallback listener
        mapFragment.getMapAsync(this)
       return view
    }

    private fun getCurrentLocationn() {
        if (ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            // request permission
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQ_CODE
            );

            return
        }
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                // getting the last known or current location
                latitude = location.latitude
                longitude = location.longitude
                println("latitude==11"+latitude+"latitude==22"+longitude)
            }.addOnFailureListener {
                Toast.makeText(requireActivity(), "Failed on getting current location", Toast.LENGTH_SHORT).show()
            }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
        mapFragment?.getMapAsync {
            this.googleMap = googleMap

            // Request the user's location
            locationManager = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
            if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), 1)
                return@getMapAsync
            }
            val location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
            if (location != null) {
                val currentLatLng = LatLng(location.latitude, location.longitude)
                googleMap.addMarker(MarkerOptions().position(currentLatLng).title("Current location"))
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f))
            }
            val center = LatLng(location!!.latitude, location!!.longitude)
            println("center"+center)
            val radius = 1000 // 1km in meters
           circle=googleMap.addCircle(CircleOptions()
                .center(center)
                .radius(radius.toDouble())
                .strokeWidth(2f)
                .strokeColor(Color.RED)
                .fillColor(Color.argb(70, 255, 0, 0))
            )
        }
        }


    @SuppressLint("ResourceType")
    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        googleMap.uiSettings.setAllGesturesEnabled(true)
        googleMap.uiSettings.isCompassEnabled = true
        googleMap.uiSettings.isMyLocationButtonEnabled = true
        googleMap.setPadding(0, 0, 0, 96)

        btnstory.setOnClickListener({
            for (i in latitudes.indices) {
                val latLng = LatLng(latitudes[i], longitudes[i])
                println("latLng"+latLng)

                if(markerisInside(latitudes[i],longitudes[i]))
                {
                  map.addMarker(MarkerOptions().position(latLng).icon(BitmapFromVector(getApplicationContext(), R.drawable.ic_baseline_fmd_good_24)))
                }
                else
                {
                    map.addMarker(MarkerOptions().position(latLng))
                }
                // map.addMarker(MarkerOptions().position(latLng))

                val zoomLevel = 15f
                val cameraPosition = CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel)
                map.moveCamera(cameraPosition)

            }

        })




        // on below line we are adding click listener for our marker
        map.setOnMarkerClickListener { marker: Marker ->

            // on below line we are displaying a toast message on clicking on marker



            var geocoder:Geocoder


            var addresses:List<Address>
            geocoder =  Geocoder(requireContext(), Locale.getDefault());

            addresses =
                geocoder.getFromLocation(marker.position.latitude, marker.position.longitude, 1) as List<Address>; // Here 1 represent max location result to returned, by documents it recommended 1 to 5

            var address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            var city = addresses.get(0).getLocality();
            var state = addresses.get(0).getAdminArea();
            var country = addresses.get(0).getCountryName();
            var postalCode = addresses.get(0).getPostalCode();
            var knownName = addresses.get(0).getFeatureName();


            val distance = FloatArray(2)

            Location.distanceBetween(
                marker.position.latitude, marker.position.longitude,
                circle.getCenter().latitude, circle.getCenter().longitude, distance
            )

            if (distance[0] > circle.getRadius()) {
                Toast.makeText(requireContext(), "Outside", Toast.LENGTH_LONG).show()


            } else {
                Toast.makeText(requireContext(), "Inside", Toast.LENGTH_LONG).show()
            }


            Toast.makeText(context, "Clicked location is " + address, Toast.LENGTH_SHORT).show()

            false
        }




//         for adding custom marker

//        map.setOnMapClickListener(object :GoogleMap.OnMapClickListener {
//            override fun onMapClick(latlng :LatLng) {
////                map.clear();
//                map.animateCamera(CameraUpdateFactory.newLatLng(latlng));
//                val location = LatLng(latlng.latitude,latlng.longitude)
//                map.addMarker(MarkerOptions().position(location))
//
//            }
//        })

//        val mapView = childFragmentManager.findFragmentById(R.id.map)!!.requireView()
////        mapView.findViewById<ViewGroup>(Integer.parseInt("map"))?.addView(btnstory)
//        btnstory.setOnClickListener {
//            Toast.makeText(view?.context,"Clicked Story",Toast.LENGTH_LONG).show()
//        }
        btnreel.setOnClickListener({
            Toast.makeText(view?.context,"Clicked Reels",Toast.LENGTH_LONG).show()

        })
        btnhelp.setOnClickListener({
            Toast.makeText(view?.context,"Clicked Help",Toast.LENGTH_LONG).show()

        })



    }



    fun markerisInside( latitude:Double,longitude:Double):Boolean{
        val distance = FloatArray(2)

        Location.distanceBetween(
            latitude, longitude,
            circle.getCenter().latitude, circle.getCenter().longitude, distance
        )

        if (distance[0] > circle.getRadius()) {
           // Toast.makeText(requireContext(), "Outside", Toast.LENGTH_LONG).show()
            return  false


        } else {
            //Toast.makeText(requireContext(), "Inside", Toast.LENGTH_LONG).show()
            return  true;
        }

    }

    private fun BitmapFromVector(context: Context, vectorResId: Int): BitmapDescriptor? {
        // below line is use to generate a drawable.
        val vectorDrawable = ContextCompat.getDrawable(context, vectorResId)

        // below line is use to set bounds to our vector drawable.


        vectorDrawable!!.setBounds(
            0,
            0,
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight
        )
        // below line is use to create a bitmap for our
        // drawable which we have added.
        val bitmap = Bitmap.createBitmap(
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )

        // below line is use to add bitmap in our canvas.
        val canvas = Canvas(bitmap)

        // below line is use to draw our
        // vector drawable in canvas.
        vectorDrawable.draw(canvas)

        // after generating our bitmap we are returning our bitmap.
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }


}






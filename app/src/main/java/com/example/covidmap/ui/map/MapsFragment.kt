package com.example.covidmap.ui.map

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Camera
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.covidmap.COVIDMapApplication.Companion.ApplicationContext
import com.example.covidmap.R
import com.example.covidmap.databinding.FragmentMapsBinding
import com.example.covidmap.model.Center
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.security.Permission

class MapsFragment : Fragment() {

    private val viewModel: MapsViewModel by viewModels()

    private var currentLocation = LatLng(127.toDouble(), 127.toDouble())
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private val REQUEST_CODE = 1000
    private val permissions = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )
    private var map: GoogleMap? = null

    @SuppressLint("MissingPermission")
    private val callback = OnMapReadyCallback { googleMap ->
        var latLng = LatLng(37.33,126.59)
        map = googleMap

        if (!viewModel.centerList.value.isNullOrEmpty()) {
            for (center in viewModel.centerList.value!!) {
                val color = when(center.centerType){
                    "지역" -> BitmapDescriptorFactory.HUE_BLUE
                    "중앙/권역" -> BitmapDescriptorFactory.HUE_RED
                    else -> BitmapDescriptorFactory.HUE_YELLOW
                }
                googleMap.addMarker(
                    MarkerOptions().position(
                        LatLng(
                            center.lat.toDouble(),
                            center.lng.toDouble()
                        )
                    ).title(center.centerName)
                        .icon(BitmapDescriptorFactory.defaultMarker(color))
                )
            }
        }

        googleMap.apply {
            uiSettings.isZoomControlsEnabled=true
            uiSettings.isZoomGesturesEnabled=true
            if (ContextCompat.checkSelfPermission(
                    ApplicationContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED) isMyLocationEnabled=true

            setOnMarkerClickListener {
                Toast.makeText(context,it.title,Toast.LENGTH_SHORT).show()
                false
            }
            moveCamera(
                CameraUpdateFactory.newLatLngZoom(
                    currentLocation,10F
                )
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentMapsBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
//        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkPermission()
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?

        viewModel.centerList.observe(viewLifecycleOwner, {
            Log.d("callback in : ","centerList size : ${it.size}")

            if (!it.isNullOrEmpty()) {
                Log.d("callback inn : ","centerList size : ${it.size}")
                Log.d("centerList : ","${it[0].centerName}")

                mapFragment?.getMapAsync(callback)
            }
        })

        CoroutineScope(Dispatchers.IO).launch {
            viewModel.loadSavedCenterList()
        }

        mapFragment?.getMapAsync(callback)
    }



    private fun checkPermission(){
        when {
            ContextCompat.checkSelfPermission(
                ApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                // You can use the API that requires the permission.
                fusedLocationProviderClient =  LocationServices.getFusedLocationProviderClient(requireActivity())
                val task = fusedLocationProviderClient.lastLocation
                task.addOnSuccessListener { location ->
                    location?.let {
                        currentLocation = LatLng(location.latitude,location.longitude)
                        map?.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation,10F))
                        map?.isMyLocationEnabled=true
                    }
                }
                Toast.makeText(context, "Permissions Granted",Toast.LENGTH_SHORT).show()
            }
            shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) -> {
            // In an educational UI, explain to the user why your app requires this
            // permission for a specific feature to behave as expected. In this UI,
            // include a "cancel" or "no thanks" button that allows the user to
            // continue using your app without granting the permission.
//            showInContextUI(...)
            Toast.makeText(activity,"권한요청을 승인해야 사용 가능합니다",Toast.LENGTH_SHORT).show()
        }
            else -> {
                // You can directly ask for the permission.
                ActivityCompat.requestPermissions(this.requireActivity(),
                    permissions,
                    REQUEST_CODE)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Toast.makeText(activity,"권한요청 성공",Toast.LENGTH_SHORT).show()
    }
}
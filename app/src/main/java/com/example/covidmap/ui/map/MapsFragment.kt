package com.example.covidmap.ui.map

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.covidmap.COVIDMapApplication.Companion.getApplicationContext
import com.example.covidmap.R
import com.example.covidmap.databinding.FragmentMapsBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MapsFragment : Fragment() {

    private val viewModel: MapsViewModel by viewModels()

    private var currentLocation = LatLng(37.33, 127.26)
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private val REQUEST_CODE = 1000
    private val permissions = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )
    private var map: GoogleMap? = null

    @SuppressLint("MissingPermission")
    private val callback = OnMapReadyCallback { googleMap ->
        map = googleMap

        if (!viewModel.centerList.value.isNullOrEmpty()) {
            for (center in viewModel.centerList.value!!) {
                val color = when (center.centerType) {
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
            uiSettings.isZoomControlsEnabled = true
            uiSettings.isZoomGesturesEnabled = true
            if (ContextCompat.checkSelfPermission(
                    getApplicationContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) isMyLocationEnabled = true

            setOnMarkerClickListener {
                Toast.makeText(context, it.title, Toast.LENGTH_SHORT).show()
                false
            }
            moveCamera(
                CameraUpdateFactory.newLatLngZoom(
                    currentLocation, 10F
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
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        checkPermission()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?

        viewModel.centerList.observe(viewLifecycleOwner, {
            if (!it.isNullOrEmpty()) {
                mapFragment?.getMapAsync(callback)
            }
        })

        CoroutineScope(Dispatchers.IO).launch {
            viewModel.loadSavedCenterList()
        }

        mapFragment?.getMapAsync(callback)
    }

    private fun checkPermission() {
        when {
            ContextCompat.checkSelfPermission(
                getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                fusedLocationProviderClient =
                    LocationServices.getFusedLocationProviderClient(requireActivity())
                val task = fusedLocationProviderClient.lastLocation
                task.addOnSuccessListener { location ->
                    location?.let {
                        currentLocation = LatLng(location.latitude, location.longitude)
                        map?.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 10F))
                        map?.isMyLocationEnabled = true
                    }
                }
                Toast.makeText(context, "Permissions Granted", Toast.LENGTH_SHORT).show()
            }
            shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) -> {
                Toast.makeText(activity, "위치권한을 승인해주세요", Toast.LENGTH_SHORT).show()
            }
            else -> {
                // You can directly ask for the permission.
                ActivityCompat.requestPermissions(
                    this.requireActivity(),
                    permissions,
                    REQUEST_CODE
                )
            }
        }
    }
}
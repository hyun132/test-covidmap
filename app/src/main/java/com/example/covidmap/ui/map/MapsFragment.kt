package com.example.covidmap.ui.map

import android.Manifest
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.covidmap.R
import com.example.covidmap.databinding.FragmentMapsBinding
import com.example.covidmap.model.Center
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MapsFragment : Fragment() {

    private val viewModel: MapsViewModel by viewModels()
//    var centerList: List<Center> = listOf()

    private val REQUEST_CODE = 1000
    private val permissions = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    private val callback = OnMapReadyCallback { googleMap ->
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */

//        Log.d("callback : ","centerList size : ${centerList.size}")

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

        googleMap.uiSettings.isZoomControlsEnabled=true
        googleMap.uiSettings.isZoomGesturesEnabled=true
        googleMap.setOnMarkerClickListener {
            Toast.makeText(context,it.title,Toast.LENGTH_SHORT).show()
            false
        }
        googleMap.moveCamera(
            CameraUpdateFactory.newLatLngZoom(
                LatLng(
                    37.5,
                    127.5
                ),15F
            )
        )
        //여기에 room에서 불러온 리스트들 마커찍어서 보야줘야함.
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?

        viewModel.centerList.observe(viewLifecycleOwner, {

            Log.d("callback in : ","centerList size : ${it.size}")
            if (!it.isNullOrEmpty()) {

                Log.d("callback inn : ","centerList size : ${it.size}")
                Log.d("centerList : ","${it[0].centerName}")
//                centerList = it
                mapFragment?.getMapAsync(callback)

            }



        })
        CoroutineScope(Dispatchers.IO).launch {
            viewModel.loadSavedCenterList()
        }

        mapFragment?.getMapAsync(callback)

    }



//    fun checkPermission(){
//        when {
//            ContextCompat.checkSelfPermission(
//                ApplicationContext(),
//                Manifest.permission.ACCESS_FINE_LOCATION
//            ) == PackageManager.PERMISSION_GRANTED -> {
//                // You can use the API that requires the permission.
//                performAction(...)
//            }
//            shouldShowRequestPermissionRationale(...) -> {
//            // In an educational UI, explain to the user why your app requires this
//            // permission for a specific feature to behave as expected. In this UI,
//            // include a "cancel" or "no thanks" button that allows the user to
//            // continue using your app without granting the permission.
////            showInContextUI(...)
//            Toast.makeText(activity,"권한요청을 승인해야 사용 가능합니다",Toast.LENGTH_SHORT).show()
//        }
//            else -> {
//                // You can directly ask for the permission.
//                ActivityCompat.requestPermissions(ApplicationContext(),
//                    permissions,
//                    REQUEST_CODE)
//            }
//        }
//    }

//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        Toast.makeText(activity,"권한요청 성공",Toast.LENGTH_SHORT).show()
//    }
}
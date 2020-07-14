package com.example.weatherapp

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.PermissionChecker.PERMISSION_GRANTED
import androidx.core.content.PermissionChecker.checkSelfPermission
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.adapter.WeatherDayAdapter
import com.example.weatherapp.util.Constant.API_KEY
import com.example.weatherapp.util.Constant.METRIC
import com.example.weatherapp.util.Util
import com.example.weatherapp.viewmodel.CityWeatherViewModel
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import kotlinx.android.synthetic.main.city_weather_fragment.view.*
import java.util.*


class CityWeatherFragment : Fragment(R.layout.city_weather_fragment), OnMapReadyCallback,
    ActivityCompat.OnRequestPermissionsResultCallback {

    private val REQUEST_CODE = 1

    private lateinit var viewModel: CityWeatherViewModel

    private lateinit var map: GoogleMap

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    @SuppressLint("MissingPermission")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(CityWeatherViewModel::class.java)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        val mapFragment =
            childFragmentManager.findFragmentById(R.id.google_map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)

        locationEnabledControl()
        initAutoComplete(view)
        getLastLocation()
    }

    override fun onStart() {
        super.onStart()
        Log.d("START", "START")
        if (!checkPermissions()) {
            requestPermissions()
        } else {
            getLastLocation()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CODE) {
            when {
                grantResults.isEmpty() -> Log.i("TAG", "User interaction was cancelled.")
                (grantResults[0] == PERMISSION_GRANTED) -> {
                    getLastLocation()
                    enableMyLocation()
                }
                else -> {
                }
            }
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        }
    }

    private fun initAutoComplete(view: View) {
        if (!Places.isInitialized()) {
            Places.initialize(requireContext(), getString(R.string.google_map_api_key), Locale.US);
        }

        val autocompleteFragment =
            childFragmentManager.findFragmentById(R.id.auto_complete_fragment) as AutocompleteSupportFragment

        val fields = listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG)
        autocompleteFragment.setPlaceFields(fields)

        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                Log.i("PLACE", "Place: ${place.name}, ${place.id}, ${place.latLng}")
                viewModel.getWeather(
                    place.latLng?.latitude.toString(),
                    place.latLng?.longitude.toString(),
                    METRIC,
                    API_KEY
                )
                map.addMarker(
                    MarkerOptions().position(
                        LatLng(
                            place.latLng!!.latitude,
                            place.latLng!!.longitude
                        )
                    )
                )
                map.moveCamera(
                    CameraUpdateFactory.newLatLng(
                        LatLng(
                            place.latLng!!.latitude,
                            place.latLng!!.longitude
                        )
                    )
                )


                viewModel.weatherData.observe(viewLifecycleOwner, Observer { response ->
                    response.let {
                        Log.d("TAG", response.toString())
                        view.recyclerView.adapter =
                            response.daily?.let { daily -> WeatherDayAdapter(daily) }
                    }
                })

            }

            override fun onError(staus: Status) {
                Toast.makeText(context, staus.statusMessage, Toast.LENGTH_SHORT).show()
            }

        })
    }


    private fun enableMyLocation() {
        if (!::map.isInitialized) return
        if (checkSelfPermission(requireContext(), ACCESS_FINE_LOCATION)
            == PERMISSION_GRANTED
        ) {
            map.isMyLocationEnabled = true
        } else {
            Util.requestPermission(
                activity as AppCompatActivity,
                REQUEST_CODE,
                ACCESS_FINE_LOCATION
            )
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        fusedLocationClient.lastLocation
            .addOnCompleteListener { taskLocation ->
                if (taskLocation.isSuccessful && taskLocation.result != null) {
                    val location = taskLocation.result

                    viewModel.getWeather(
                        location!!.latitude.toString(), location!!.longitude.toString(), METRIC,
                        API_KEY
                    )

                    viewModel.weatherData.observe(viewLifecycleOwner, Observer { response ->
                        response.let {
                            Log.d("veri", response.toString())
                            view?.recyclerView?.adapter =
                                response.daily?.let { daily -> WeatherDayAdapter(daily) }
                        }
                    })

                }
            }
    }

    private fun locationEnabledControl() {
        val lm =
            requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        var gps_enabled = false
        var network_enabled = false

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
        } catch (ex: Exception) {
        }

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        } catch (ex: Exception) {
        }
        if (!gps_enabled && !network_enabled) {
            AlertDialog.Builder(requireContext()).setMessage("Do you want to enable location?")
                .setPositiveButton("Yes") { it, it2 ->
                    requireContext().startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                    getLastLocation()
                }.setNegativeButton("Cancel", null).show()
        }

    }

    private fun checkPermissions() =
        checkSelfPermission(
            requireContext(),
            ACCESS_COARSE_LOCATION
        ) == PERMISSION_GRANTED

    private fun startLocationPermissionRequest() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(ACCESS_COARSE_LOCATION),
            REQUEST_CODE
        )
    }

    private fun requestPermissions() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                requireActivity(),
                ACCESS_COARSE_LOCATION
            )
        ) {
            startLocationPermissionRequest()

        } else {
            startLocationPermissionRequest()
        }
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        map = googleMap ?: return
        enableMyLocation()
    }


}